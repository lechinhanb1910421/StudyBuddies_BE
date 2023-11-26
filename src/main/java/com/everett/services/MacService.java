package com.everett.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.everett.daos.MacInfoDAO;
import com.everett.daos.MacRecordDAO;
import com.everett.daos.UserDAO;
import com.everett.exceptions.checkedExceptions.BusinessException;
import com.everett.exceptions.checkedExceptions.UserNotFoundException;
import com.everett.models.Avatar;
import com.everett.models.MacInfo;
import com.everett.models.MacRecord;
import com.everett.models.User;
import com.everett.models.type.MacRecordStatus;
import com.everett.models.type.UserRoleType;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

@Stateless
public class MacService {
    private static final Logger logger = LogManager.getLogger(MacService.class);
    private static final String MAC_REPORT_FILE_NAME = "MAC_REPORT_%s.csv";
    private static final String STACK_ID_TEMPLATE = "MAC_CREATION_%s_%d";
    private static final int PASSWORD_LENGTH = 10;
    private static final int PASSWORD_CHAR_LOWER_LIMIT = 97;
    private static final int PASSWORD_CHAR_UPPER_LIMIT = 122;
    private static final String DEFAULT_ACCOUNT_STATUS = "active";
    private static final UserRoleType DEFAULT_USER_ROLE = UserRoleType.STUDENT;
    private static final String STATUS_MESSAGE_SUCCESS = "User with email: [%s] was successfully added";
    private static final String STATUS_MESSAGE_FAIL_READ_CSV_ROW = "Fail to read csv row [%s]";
    private static final String STATUS_MESSAGE_FAIL_PERSIST_DB = "Fail to persist record [%s] to database";
    private static final String STATUS_MESSAGE_FAIL_KEYCLOAK = "Fail to create user in keycloak";
    private static final String STATUS_MESSAGE_FAIL_USER_EXISTED = "User with email: [%s] existed in database";
    private static final int CSV_STUDENT_CODE_INDEX = 0;
    private static final int CSV_FAMILY_NAME_INDEX = 1;
    private static final int CSV_GIVEN_NAME_INDEX = 2;
    private static final int CSV_EMAIL_INDEX = 3;
    private static final String CSV_REPORT_HEADER = "Email,Temp password,Status,Message\n";
    private static final String CSV_REPORT_ROW_TEMPLATE = "%s,%s,%s,%s\n";

    @Inject
    @ConfigProperty(name = "mac_export_folder")
    private String macExportFolder;

    @Inject
    @ConfigProperty(name = "default_ava_url")
    private String defaultAvaUrl;

    @Inject
    MultiThreadService multiThreadService;

    @Inject
    MacInfoDAO macInfoDAO;

    @Inject
    MacRecordDAO macRecordDAO;

    @Inject
    UserDAO userDAO;

    @Inject
    private KeycloakAdminService kcAdminService;

    public String exportMacResultFile(String stackId) throws IOException {
        List<MacRecord> records = macRecordDAO.getRecordsByStackId(stackId);
        String reportFileName = buildMacReportName(stackId);
        File file = new File(macExportFolder + "/" + reportFileName);
        if (file.length() == 0) {
            logger.info("CANNOT FIND FILE FOR STACK ID [" + stackId + "]");
            logger.info("BUILDING NEW REPORT FILE FOR STACK ID: [" + stackId + "]");
            try (FileWriter myWriter = new FileWriter(file.getAbsolutePath())) {
                myWriter.write(CSV_REPORT_HEADER);
                for (MacRecord record : records) {
                    myWriter.write(
                            String.format(CSV_REPORT_ROW_TEMPLATE, record.getUserEmail(), record.getUserTempPass(),
                                    record.getStatus(), record.getStatusMessage()));
                }
            }
        }
        byte[] encoded = Files.readAllBytes(Paths.get(macExportFolder + "/" + reportFileName));
        return new String(encoded, "UTF-8");
    }

    public String buildMacReportName(String stackId) {
        return String.format(MAC_REPORT_FILE_NAME, stackId);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String importUserAccount(MultipartFormDataInput formDataInput, String triggerUserId, String triggerUserEmail)
            throws BusinessException {
        logger.info("START MAC JOB TO IMPORT USER");
        InputPart file = getFile(formDataInput);
        String stackId = generateStackId(triggerUserId);
        Timestamp createdTime = Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        saveMacCommandInfo(file, triggerUserEmail, stackId, createdTime);

        try {
            try (InputStream inputStream = file.getBody(InputStream.class, null)) {
                CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream));
                String[] nextRecord = new String[200];
                try {
                    // remove first line
                    nextRecord = csvReader.readNext();
                    while ((nextRecord = csvReader.readNext()) != null) {
                        multiThreadService.submitTask(createMacTask(nextRecord, createdTime, stackId));
                    }
                } catch (CsvValidationException | IOException ex) {
                    logger.info("FAILED TO READ RECORD CSV ROW");
                    saveFailMacRecord(MacRecordStatus.FAIL_READING_CSV_ROW, createdTime, stackId, nextRecord, null);
                }
            }
        } catch (IOException ioEx) {
            throw new BusinessException("An error occurred when read file");
        }
        return stackId;
    }

    private Runnable createMacTask(String[] nextRecord, Timestamp createdTime, String stackId) {
        return new Runnable() {
            public void run() {
                String userEmail = nextRecord[CSV_EMAIL_INDEX];
                try {
                    userDAO.getUserByEmail(userEmail);
                    saveFailMacRecord(MacRecordStatus.FAIL_USER_EXISTED, createdTime, stackId, nextRecord, userEmail);
                    return;
                } catch (UserNotFoundException e) {
                    User user = new User(nextRecord[CSV_STUDENT_CODE_INDEX], nextRecord[CSV_GIVEN_NAME_INDEX],
                            nextRecord[CSV_FAMILY_NAME_INDEX], nextRecord[CSV_EMAIL_INDEX], createdTime,
                            DEFAULT_ACCOUNT_STATUS, DEFAULT_USER_ROLE);
                    Avatar avatar = new Avatar(defaultAvaUrl);
                    user.setAvatar(avatar);
                    try {
                        userDAO.persistUser(user);
                    } catch (Exception ex) {
                        saveFailMacRecord(MacRecordStatus.FAIL_PERSIST_DB, createdTime, stackId, nextRecord, userEmail);
                        logger.info("FAIL PERSIST USER TO DATABASE");
                        return;
                    }
                    String userTempPass = generateRandomTempPassword();
                    try {
                        // call Keycloak admin client to add user to keycloak
                        kcAdminService.createKeycloakUser(user, userTempPass);
                    } catch (Exception ex) {
                        saveFailMacRecord(MacRecordStatus.FAIL_KEYCLOAK, createdTime, stackId, nextRecord, userEmail);
                        logger.info("FAIL TO CREATE USER IN KEYCLOAK");
                        return;
                    }
                    saveSuccesMacRecord(createdTime, nextRecord[CSV_EMAIL_INDEX], stackId, userTempPass);
                }

            }
        };
    }

    private void saveSuccesMacRecord(Timestamp createdAt, String userEmail, String stackId, String tempPass) {
        MacRecord macRecord = MacRecord.builder()
                .createdAt(createdAt)
                .status(MacRecordStatus.SUCCESS)
                .statusMessage(String.format(STATUS_MESSAGE_SUCCESS, userEmail))
                .stackId(stackId)
                .userEmail(userEmail)
                .userTempPass(tempPass)
                .build();

        macRecordDAO.persistMacRecord(macRecord);
        logger.info("USER WITH EMAIL: [" + userEmail + "] WAS SUCCESSFULLY ADDED");
    }

    private void saveFailMacRecord(MacRecordStatus macRecordStatus, Timestamp createdAt, String stackId,
            String[] record, String email) {
        MacRecord macRecord = MacRecord.builder()
                .createdAt(createdAt)
                .status(macRecordStatus)
                .statusMessage(buildFailStatusMessage(macRecordStatus, record, email))
                .stackId(stackId)
                .userEmail(email)
                .userTempPass("")
                .build();

        macRecordDAO.persistMacRecord(macRecord);

        logger.info("FAILED TO ADD RECORD [" + record.toString() + "] TO DATABASE");
    }

    private String buildFailStatusMessage(MacRecordStatus status, String[] record, String userEmail) {
        switch (status) {
            case FAIL_PERSIST_DB:
                return String.format(STATUS_MESSAGE_FAIL_PERSIST_DB, record.toString());
            case FAIL_USER_EXISTED:
                return String.format(STATUS_MESSAGE_FAIL_USER_EXISTED, userEmail);
            case FAIL_KEYCLOAK:
                return STATUS_MESSAGE_FAIL_KEYCLOAK;
            case FAIL_READING_CSV_ROW:
                return String.format(STATUS_MESSAGE_FAIL_READ_CSV_ROW, record.toString());
            default:
                return "";
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private void saveMacCommandInfo(InputPart file, String triggerUserEmail, String stackId, Timestamp createdAt) {
        String fileName = getFileName(file);
        logger.info("PERSISTING MAC INFO WITH FILE NAME [" + fileName + "] AND STACK ID [" + stackId + "] TO DATABASE");

        MacInfo macInfo = MacInfo.builder()
                .createdAt(createdAt)
                .triggerUserEmail(triggerUserEmail)
                .stackId(stackId)
                .fileName(fileName)
                .fileFullData(getFileFullData(file))
                .build();
        macInfoDAO.persistMacInfo(macInfo);
    }

    private String getFileName(InputPart file) {
        MultivaluedMap<String, String> headers = file.getHeaders();
        String[] contentDispositionHeader = headers.getFirst("Content-Disposition").split(";");
        for (String name : contentDispositionHeader) {
            if ((name.trim().startsWith("filename"))) {
                String[] tmp = name.split("=");
                return tmp[1].trim().replaceAll("\"", "");
            }
        }
        return "";
    }

    private String getFileFullData(InputPart file) {
        try {
            try (InputStream inputStream = file.getBody(InputStream.class, null)) {
                return new BufferedReader(
                        new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                        .lines()
                        .collect(Collectors.joining("\n"));
            }
        } catch (IOException e) {
            logger.info("AN ERROR OCCURED WHEN READING FILE");
            return "ERROR WHEN READING FULL DATA";
        }
    }

    private InputPart getFile(MultipartFormDataInput formInput) throws BusinessException {
        List<InputPart> inputFiles = Optional.ofNullable(formInput)
                .map(MultipartFormDataInput::getFormDataMap)
                .map(entry -> entry.get("file"))
                .orElseThrow(() -> new BusinessException("Cannot get 'file' part"));
        if (inputFiles.size() > 1) {
            throw new BusinessException("Multiple files upload are not allowed");
        }
        return inputFiles.get(0);
    }

    private String generateStackId(String triggerUserId) {
        return String.format(STACK_ID_TEMPLATE, triggerUserId.substring(1), System.currentTimeMillis());
    }

    private String generateRandomTempPassword() {
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int randomLimitedInt = PASSWORD_CHAR_LOWER_LIMIT +
                    (int) (random.nextFloat() * (PASSWORD_CHAR_UPPER_LIMIT - PASSWORD_CHAR_LOWER_LIMIT + 1));
            boolean isUpperCase = random.nextFloat() > 0.5;
            buffer.append((char) (isUpperCase ? randomLimitedInt - 32 : randomLimitedInt));
        }
        return buffer.toString();

    }

}

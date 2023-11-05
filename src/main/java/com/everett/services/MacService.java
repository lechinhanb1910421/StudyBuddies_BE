package com.everett.services;

import java.io.BufferedReader;
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
import com.everett.models.Avatar;
import com.everett.models.MacInfo;
import com.everett.models.MacRecord;
import com.everett.models.User;
import com.everett.models.type.MacRecordStatus;
import com.everett.models.type.UserRoleType;
import com.opencsv.CSVReader;

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
    private static final String STATUS_MESSAGE_SUCCESS = "USER WITH EMAIL: [%s] WAS SUCCESSFULLY ADDED";
    private static final String STATUS_MESSAGE_FAIL_DB = "FAILED TO PERSIST RECORD [%s] TO DATABASE";
    private static final String STATUS_MESSAGE_FAIL_KEYCLOAK = "FAILED TO CREATE USER IN KEYCLOAK";
    private static final int CSV_STUDENT_CODE_INDEX = 0;
    private static final int CSV_FAMILY_NAME_INDEX = 1;
    private static final int CSV_GIVEN_NAME_INDEX = 2;
    private static final int CSV_EMAIL_INDEX = 3;

    @Inject
    @ConfigProperty(name = "mac_export_folder")
    private String macExportFolder;

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

    @Inject
    @ConfigProperty(name = "default_ava_url")
    private String defaultAvaUrl;

    public String exportMacResultFile(String stackId) throws IOException {
        // File file = File.createTempFile(buildMacReportName(stackId), ".csv");
        // try (FileWriter myWriter = new FileWriter(file.getAbsolutePath())) {
        // myWriter.write("Files in Java might be tricky, but it is fun enough!");
        // }
        // File file = new File(macExportFolder + "/User_Account_K45.csv");

        byte[] encoded = Files.readAllBytes(Paths.get(macExportFolder + "/User_Account_K45.csv"));
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
                    }
                    multiThreadService.submitTask(createMacTask(nextRecord, createdTime, stackId));
                } catch (Exception ex) {
                    logger.info("FAILED TO PERSIST USER TO DATABASE");
                    saveFailMacRecord(MacRecordStatus.FAIL_DB, createdTime, stackId, nextRecord);
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
                User user = new User(nextRecord[CSV_STUDENT_CODE_INDEX], nextRecord[CSV_GIVEN_NAME_INDEX],
                        nextRecord[CSV_FAMILY_NAME_INDEX], nextRecord[CSV_EMAIL_INDEX], createdTime,
                        DEFAULT_ACCOUNT_STATUS, DEFAULT_USER_ROLE);
                Avatar avatar = new Avatar(defaultAvaUrl);
                user.setAvatar(avatar);
                userDAO.persistUser(user);
                // call Keycloak admin client to add user to keycloak
                String userTempPass = generateRandomTempPassword();
                try {
                    kcAdminService.createKeycloakUser(user, userTempPass);
                } catch (Exception ex) {
                    saveFailMacRecord(MacRecordStatus.FAIL_KEYCLOAK, createdTime, stackId, null);
                    logger.info("FAIL TO CREATE USER IN KEYCLOAK");
                }
                saveSuccesMacRecord(createdTime, nextRecord[CSV_EMAIL_INDEX], stackId, userTempPass);
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
            String[] record) {
        if (MacRecordStatus.FAIL_DB.equals(macRecordStatus)) {
            MacRecord macRecord = MacRecord.builder()
                    .createdAt(createdAt)
                    .status(MacRecordStatus.FAIL_DB)
                    .statusMessage(String.format(STATUS_MESSAGE_FAIL_DB, record.toString()))
                    .stackId(stackId)
                    .userEmail("UNKNOWN")
                    .userTempPass("")
                    .build();

            macRecordDAO.persistMacRecord(macRecord);
        } else if (MacRecordStatus.FAIL_KEYCLOAK.equals(macRecordStatus)) {
            MacRecord macRecord = MacRecord.builder()
                    .createdAt(createdAt)
                    .status(MacRecordStatus.FAIL_KEYCLOAK)
                    .statusMessage(STATUS_MESSAGE_FAIL_KEYCLOAK)
                    .stackId(stackId)
                    .userEmail("UNKNOWN")
                    .userTempPass("")
                    .build();

            macRecordDAO.persistMacRecord(macRecord);
        }
        logger.info("FAILED TO ADD RECORD [" + record.toString() + "] TO DATABASE");
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

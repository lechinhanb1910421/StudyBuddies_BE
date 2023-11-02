package com.everett.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.everett.exceptions.checkedExceptions.BusinessException;
import com.everett.models.User;
import com.everett.models.type.UserRoleType;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

@Stateless
public class MacService {
    private static final Logger logger = LogManager.getLogger(MacService.class);
    private static final String STACK_ID_TEMPLATE = "MAC_CREATION_%s_%d";
    private static final int PASSWORD_LENGTH = 10;
    private static final int PASSWORD_CHAR_LOWER_LIMIT = 97;
    private static final int PASSWORD_CHAR_UPPER_LIMIT = 122;
    private static final String DEFAULT_ACCOUNT_STATUS = "active";
    private static final UserRoleType DEFAULT_USER_ROLE = UserRoleType.STUDENT;

    @Inject
    MultiThreadService multiThreadService;

    public void importUserAccount(MultipartFormDataInput formDataInput, String triggerUserId) throws BusinessException {
        InputPart file = getFile(formDataInput);
        String stackId = generateStackId(triggerUserId);
        Timestamp createdTime = Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS));

        try {
            try (InputStream inputStream = file.getBody(InputStream.class, null)) {
                CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream));
                String[] nextRecord;
                try {
                    while ((nextRecord = csvReader.readNext()) != null) {
                        User user = new User(nextRecord[0], nextRecord[2], nextRecord[1], nextRecord[3], createdTime,
                                DEFAULT_ACCOUNT_STATUS, DEFAULT_USER_ROLE);
                        String userPassword = generateRandomTempPassword();
                        logger.info("GENERATE STACK ID: [" + stackId +
                                "] FOR USER:[" + user + "] WITH PASSWORD: [" + userPassword + "]");
                    }
                } catch (CsvValidationException e) {
                }
            }
        } catch (IOException ioEx) {
            throw new BusinessException("An error occurred when read file");
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

CREATE TABLE PUBLIC.Mac_info (
    macInfoId BIGSERIAL NOT NULL,
    createdAt TIMESTAMP WITH TIME ZONE,
    triggerUserEmail VARCHAR(80),
    stackId VARCHAR(50),
    fileName VARCHAR(100),
    fileFullData TEXT,
    PRIMARY KEY (macInfoId)
);

CREATE TABLE PUBLIC.Mac_record (
    macRecId BIGSERIAL NOT NULL,
    createdAt TIMESTAMP WITH TIME ZONE,
    status VARCHAR(50),     
    statusMessage TEXT,
    stackId VARCHAR(50),
    userEmail VARCHAR(80),
    userTempPass VARCHAR(11),
    PRIMARY KEY (macRecId)
);

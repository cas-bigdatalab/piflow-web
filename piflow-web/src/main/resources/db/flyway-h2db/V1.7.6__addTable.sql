-- ----------------------------
-- Add column
-- ----------------------------
CREATE TABLE IF NOT EXISTS `FLOW_STOPS_PUBLISHING` (
    `ID` VARCHAR(40) PRIMARY KEY NOT NULL,
    `CRT_DTTM` DATETIME NOT NULL COMMENT 'Create date time',
    `CRT_USER` VARCHAR(255) NOT NULL COMMENT 'Create user',
    `ENABLE_FLAG` BIT(1) NOT NULL COMMENT 'Enable flag',
    `LAST_UPDATE_DTTM` DATETIME NOT NULL COMMENT 'Last update date time',
    `LAST_UPDATE_USER` VARCHAR(255) NOT NULL COMMENT 'Last update user',
    `VERSION` BIGINT(20) DEFAULT NULL COMMENT 'Version',
    `PUBLISHING_ID` VARCHAR(255) DEFAULT NULL COMMENT 'publishing id',
    `STOPS_ID` VARCHAR(255) DEFAULT NULL COMMENT 'publishing stops id',
    `NAME` VARCHAR(255) DEFAULT NULL COMMENT 'publishing name'
);
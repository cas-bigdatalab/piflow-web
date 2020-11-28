CREATE TABLE IF NOT EXISTS `SPARK_JAR` (
  `ID` VARCHAR(40) NOT NULL,
  `CRT_DTTM` DATETIME(0) NOT NULL COMMENT 'Create date time',
  `CRT_USER` VARCHAR(255) NOT NULL COMMENT 'Create user',
  `ENABLE_FLAG` BIT(1) NOT NULL COMMENT 'Enable flag',
  `LAST_UPDATE_DTTM` DATETIME(0) NOT NULL COMMENT 'Last update date time',
  `LAST_UPDATE_USER` VARCHAR(255) NOT NULL COMMENT 'Last update user',
  `VERSION` BIGINT(20) NULL DEFAULT NULL COMMENT 'Version',
  `JAR_NAME` VARCHAR(1000) COMMENT 'jar name',
  `JAR_URL` VARCHAR(1000) COMMENT 'jar url',
  `MOUNT_ID` VARCHAR(1000) COMMENT 'jar mount id',
  `STATUS` VARCHAR(255) COMMENT 'Spark jar status',
  PRIMARY KEY (ID)) ENGINE=INNODB;

ALTER TABLE `flow` MODIFY COLUMN `description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'description';
ALTER TABLE `flow_process` MODIFY COLUMN `description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'description';
ALTER TABLE `flow_process_group` MODIFY COLUMN `description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'description';
ALTER TABLE `flow_process_stop` MODIFY COLUMN `description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL;
ALTER TABLE `flow_process_stop_property` MODIFY COLUMN `custom_value` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL;
ALTER TABLE `flow_process_stop_property` MODIFY COLUMN `description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'description';
ALTER TABLE `flow_process_stop_property` MODIFY COLUMN `display_name` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL;
ALTER TABLE `flow_process_stop_property` MODIFY COLUMN `name` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL;
ALTER TABLE `flow_stops` MODIFY COLUMN `description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL;
ALTER TABLE `flow_stops_property` MODIFY COLUMN `allowable_values` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL;
ALTER TABLE `flow_stops_property` MODIFY COLUMN `custom_value` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'custom value';
ALTER TABLE `flow_stops_property` MODIFY COLUMN `description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'description';
ALTER TABLE `flow_stops_property` MODIFY COLUMN `display_name` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL;
ALTER TABLE `flow_stops_template` MODIFY COLUMN `description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL;
ALTER TABLE `flow_stops_property_template` ADD COLUMN `language` varchar(255) NULL COMMENT 'language';
ALTER TABLE `flow_stops_template` ADD COLUMN `visualization_type` varchar(255) NULL COMMENT 'visualization type';

DELETE FROM sys_init_records WHERE id='8a80d89774dd81080174dd890bbb0002';
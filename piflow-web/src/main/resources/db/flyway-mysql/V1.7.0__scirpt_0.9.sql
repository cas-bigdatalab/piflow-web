CREATE TABLE IF NOT EXISTS `test_data` (
  `id` VARCHAR(40) NOT NULL,
  `crt_dttm` DATETIME(0) NOT NULL COMMENT 'Create date time',
  `crt_user` VARCHAR(255) NOT NULL COMMENT 'Create user',
  `enable_flag` BIT(1) NOT NULL COMMENT 'Enable flag',
  `last_update_dttm` DATETIME(0) NOT NULL COMMENT 'Last update date time',
  `last_update_user` VARCHAR(255) NOT NULL COMMENT 'Last update user',
  `version` BIGINT(20) NULL DEFAULT NULL COMMENT 'Version',
  `name` VARCHAR(1000) COMMENT 'name',
  `description` Text COMMENT 'description',
  PRIMARY KEY (ID)) ENGINE=INNODB;
  
  CREATE TABLE IF NOT EXISTS `test_data_schema` (
  `id` VARCHAR(40) NOT NULL,
  `crt_dttm` DATETIME(0) NOT NULL COMMENT 'Create date time',
  `crt_user` VARCHAR(255) NOT NULL COMMENT 'Create user',
  `enable_flag` BIT(1) NOT NULL COMMENT 'Enable flag',
  `last_update_dttm` DATETIME(0) NOT NULL COMMENT 'Last update date time',
  `last_update_user` VARCHAR(255) NOT NULL COMMENT 'Last update user',
  `version` BIGINT(20) NULL DEFAULT NULL COMMENT 'Version',
  `fk_test_data_id` VARCHAR(40) COMMENT 'fk test_data id',
  `field_name` VARCHAR(1000) COMMENT 'field_name',
  `field_type` VARCHAR(1000) COMMENT 'field_type',
  `field_description` Text COMMENT 'description',
  `field_soft` BIGINT(20) COMMENT 'soft',
  PRIMARY KEY (ID)) ENGINE=INNODB;
  ALTER TABLE `test_data_schema` ADD CONSTRAINT `FK22rp96r4290eons0000000001` FOREIGN KEY (`fk_test_data_id`) REFERENCES `test_data` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

  CREATE TABLE IF NOT EXISTS `test_data_schema_values` (
  `id` VARCHAR(40) NOT NULL,
  `crt_dttm` DATETIME(0) NOT NULL COMMENT 'Create date time',
  `crt_user` VARCHAR(255) NOT NULL COMMENT 'Create user',
  `enable_flag` BIT(1) NOT NULL COMMENT 'Enable flag',
  `last_update_dttm` DATETIME(0) NOT NULL COMMENT 'Last update date time',
  `last_update_user` VARCHAR(255) NOT NULL COMMENT 'Last update user',
  `version` BIGINT(20) NULL DEFAULT NULL COMMENT 'Version',
  `fk_test_data_id` VARCHAR(40) COMMENT 'fk test_data id',
  `fk_test_data_schema_id` VARCHAR(40) COMMENT 'fk test_data_schema id',
  `field_value` VARCHAR(1000) COMMENT 'field type',
  `data_row` bigint(20) COMMENT 'data row',
  PRIMARY KEY (ID)) ENGINE=INNODB;
  ALTER TABLE `test_data_schema_values` ADD CONSTRAINT `FK33rp96r4290eons0000000001` FOREIGN KEY (`fk_test_data_id`) REFERENCES `test_data` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
  ALTER TABLE `test_data_schema_values` ADD CONSTRAINT `FK33rp96r4290eons0000000002` FOREIGN KEY (`fk_test_data_schema_id`) REFERENCES `test_data_schema` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
  
  CREATE TABLE IF NOT EXISTS `FLOW_STOPS_TEMPLATE_MANAGE` (
  `ID` VARCHAR(40) PRIMARY KEY NOT NULL,
  `CRT_DTTM` DATETIME NOT NULL COMMENT 'Create date time',
  `CRT_USER` VARCHAR(255) NOT NULL COMMENT 'Create user',
  `ENABLE_FLAG` BIT NOT NULL COMMENT 'Enable flag',
  `LAST_UPDATE_DTTM` DATETIME NOT NULL COMMENT 'Last update date time',
  `LAST_UPDATE_USER` VARCHAR(255) NOT NULL COMMENT 'Last update user',
  `VERSION` BIGINT NULL DEFAULT NULL COMMENT 'Version',
  `BUNDLE` VARCHAR(255) COMMENT 'bundle',
  `STOPS_GROUPS` VARCHAR(255) COMMENT 'groups name',
  `IS_SHOW` BIT COMMENT 'is show'
);

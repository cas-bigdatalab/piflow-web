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
CREATE TABLE IF NOT EXISTS `spark_jar` (
  `id` VARCHAR(40) NOT NULL,
  `crt_dttm` DATETIME(0) NOT NULL COMMENT 'Create date time',
  `crt_user` VARCHAR(255) NOT NULL COMMENT 'Create user',
  `enable_flag` BIT(1) NOT NULL COMMENT 'Enable flag',
  `last_update_dttm` DATETIME(0) NOT NULL COMMENT 'Last update date time',
  `last_update_user` VARCHAR(255) NOT NULL COMMENT 'Last update user',
  `version` BIGINT(20) NULL DEFAULT NULL COMMENT 'Version',
  `jar_name` VARCHAR(1000) COMMENT 'jar name',
  `jar_url` VARCHAR(1000) COMMENT 'jar url',
  `mount_id` VARCHAR(1000) COMMENT 'jar mount id',
  `status` VARCHAR(255) COMMENT 'Spark jar status',
  primary key (id)) engine=InnoDB;
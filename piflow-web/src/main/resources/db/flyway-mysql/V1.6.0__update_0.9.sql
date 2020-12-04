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

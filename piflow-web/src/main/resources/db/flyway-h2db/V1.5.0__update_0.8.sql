-- rename table
ALTER  TABLE flow_sotps_groups RENAME TO flow_stops_groups;

-- create table
CREATE TABLE IF NOT EXISTS `group_schedule` (
  `id` VARCHAR(40) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `crt_dttm` DATETIME(0) NOT NULL COMMENT 'Create date time',
  `crt_user` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'Create user',
  `enable_flag` BIT(1) NOT NULL COMMENT 'Enable flag',
  `last_update_dttm` DATETIME(0) NOT NULL COMMENT 'Last update date time',
  `last_update_user` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'Last update user',
  `version` BIGINT(20) NULL DEFAULT NULL COMMENT 'Version',
  `cron_expression` VARCHAR ( 255 ) COMMENT 'cron expression',
  `plan_end_time` DATETIME COMMENT 'plan end time',
  `plan_start_time` DATETIME COMMENT 'plan start time',
  `schedule_id` VARCHAR ( 255 ) COMMENT 'service schedule id',
  `schedule_process_template_id` VARCHAR ( 255 ) COMMENT 'Template ID for generating Process',
  `schedule_run_template_id` VARCHAR ( 255 ) COMMENT 'Start template ID',
  `status` VARCHAR ( 255 ) COMMENT 'schedule task status',
  `type` VARCHAR ( 255 ) COMMENT 'schedule content Flow or FlowGroup',
  PRIMARY KEY ( id )) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS `stops_hub` (
  `id` VARCHAR(40) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `crt_dttm` DATETIME(0) NOT NULL COMMENT 'Create date time',
  `crt_user` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'Create user',
  `enable_flag` BIT(1) NOT NULL COMMENT 'Enable flag',
  `last_update_dttm` DATETIME(0) NOT NULL COMMENT 'Last update date time',
  `last_update_user` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'Last update user',
  `version` BIGINT(20) NULL DEFAULT NULL COMMENT 'Version',
  `jar_name` VARCHAR(1000) COMMENT 'jar name',
  `jar_url` VARCHAR(1000) COMMENT 'jar url',
  `mount_id` VARCHAR(1000) COMMENT 'jar mount id',
  `status` VARCHAR(255) COMMENT 'StopsHue status',
  primary key (id)) engine=InnoDB;

-- add column
ALTER TABLE `flow_process` ADD COLUMN `fk_group_schedule_id` VARCHAR(40);
ALTER TABLE `flow_stops_property` ADD COLUMN `example` TEXT(0) COMMENT 'property example';
ALTER TABLE flow_stops_property_template ADD COLUMN `example` TEXT(0) COMMENT 'property example';

-- add foreign key
ALTER TABLE `flow_process` ADD CONSTRAINT FK8sqeh2bcr2pylbf4b7owvokly FOREIGN KEY (fk_group_schedule_id) REFERENCES group_schedule (id);

-- update data
UPDATE `sys_schedule` SET `job_class`='cn.cnic.schedule.RunningProcessSync' WHERE id='ff8081816eaa8a5d016eaa8a77e40000';
UPDATE `sys_schedule` SET `job_class`='cn.cnic.schedule.RunningProcessGroupSync' WHERE id='ff8081816eaa9317016eaa932dd50000';
UPDATE `sys_menu` SET `menu_name`='GroupExample',menu_description='GroupExample' WHERE id='0641076d5ae840c09d2be6wmenu00008';


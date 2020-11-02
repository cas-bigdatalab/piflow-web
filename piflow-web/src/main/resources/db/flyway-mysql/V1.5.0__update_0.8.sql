
-- create table
CREATE TABLE IF NOT EXISTS `group_schedule` (
  `id` VARCHAR(40) NOT NULL,
  `crt_dttm` DATETIME(0) NOT NULL COMMENT 'Create date time',
  `crt_user` VARCHAR(255) NOT NULL COMMENT 'Create user',
  `enable_flag` BIT(1) NOT NULL COMMENT 'Enable flag',
  `last_update_dttm` DATETIME(0) NOT NULL COMMENT 'Last update date time',
  `last_update_user` VARCHAR(255) NOT NULL COMMENT 'Last update user',
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
  `status` VARCHAR(255) COMMENT 'StopsHue status',
  primary key (id)) engine=InnoDB;

-- rename table
ALTER  TABLE flow_sotps_groups RENAME TO flow_stops_groups;

-- add column
ALTER TABLE `flow_process` ADD COLUMN `fk_group_schedule_id` VARCHAR(40);
ALTER TABLE `flow_stops_property` ADD COLUMN `example` TEXT(0) COMMENT 'property example';
ALTER TABLE flow_stops_property_template ADD COLUMN `example` TEXT(0) COMMENT 'property example';

-- add foreign key
ALTER TABLE `flow_process` ADD CONSTRAINT `FK8sqeh2bcr2pylbf4b7owvokly` FOREIGN KEY (`fk_group_schedule_id`) REFERENCES `group_schedule` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- sys_schedule
INSERT INTO `sys_schedule`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `cron_expression`, `job_class`, `job_name`, `status`, `last_run_result`) VALUES ('b494d4fecea148709a0d81cbb39e7f54', '2020-08-14 18:58:22', 'admin', b'1', '2020-09-28 22:04:59', 'admin', 17, '0/59 * * * * ?', 'cn.cnic.schedule.RunningGroupScheduleSync', 'RunningGroupScheduleSync', 'RUNNING', 'SUCCEED');

INSERT INTO `sys_init_records`(`id`, `init_date`, `is_succeed`) VALUES ('8a80d89774dd81080174dd890bbb0002', '2021-01-01 00:00:00', b'1');


-- update data
UPDATE `sys_schedule` SET `job_class`='cn.cnic.schedule.RunningProcessSync' WHERE id='ff8081816eaa8a5d016eaa8a77e40000';
UPDATE `sys_schedule` SET `job_class`='cn.cnic.schedule.RunningProcessGroupSync' WHERE id='ff8081816eaa9317016eaa932dd50000';
UPDATE `data_source_property` SET `name` = 'nodes' WHERE `id` = '5f297c6968ca4ba884479b1a43b82198';
UPDATE `data_source_property` SET `name` = 'type' WHERE `id` = '8d1b0acee4be46788ca552b2040b90fc';
UPDATE `data_source_property` SET `name` = 'port' WHERE `id` = '968c061007fa4216b9a0b05d8d68bf2c';
UPDATE `data_source_property` SET `name` = 'index' WHERE `id` = 'f386627bb0734ef9a59c0316d4bd7ab7';



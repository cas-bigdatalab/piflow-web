
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

-- mx_node_image
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da01725040task0001', '2020-05-26 00:00:01', 'admin', b'1', '2020-05-26 00:00:01', 'admin', 0, 'task8.png', 'img/task/task8.png', 'TASK', '/img/task/task8.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da01725040task0002', '2020-05-26 00:00:02', 'admin', b'1', '2020-05-26 00:00:02', 'admin', 0, 'task7.png', 'img/task/task7.png', 'TASK', '/img/task/task7.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da01725040task0003', '2020-05-26 00:00:03', 'admin', b'1', '2020-05-26 00:00:03', 'admin', 0, 'task6.png', 'img/task/task6.png', 'TASK', '/img/task/task6.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da01725040task0004', '2020-05-26 00:00:04', 'admin', b'1', '2020-05-26 00:00:04', 'admin', 0, 'task5.png', 'img/task/task5.png', 'TASK', '/img/task/task5.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da01725040task0005', '2020-05-26 00:00:05', 'admin', b'1', '2020-05-26 00:00:05', 'admin', 0, 'task4.png', 'img/task/task4.png', 'TASK', '/img/task/task4.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da01725040task0006', '2020-05-26 00:00:06', 'admin', b'1', '2020-05-26 00:00:06', 'admin', 0, 'task3.png', 'img/task/task3.png', 'TASK', '/img/task/task3.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da01725040task0007', '2020-05-26 00:00:07', 'admin', b'1', '2020-05-26 00:00:07', 'admin', 0, 'task2.png', 'img/task/task2.png', 'TASK', '/img/task/task2.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da01725040task0008', '2020-05-26 00:00:08', 'admin', b'1', '2020-05-26 00:00:08', 'admin', 0, 'task1.png', 'img/task/task1.png', 'TASK', '/img/task/task1.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da01725040task0009', '2020-05-26 00:00:09', 'admin', b'1', '2020-05-26 00:00:09', 'admin', 0, 'task.png', 'img/task/task.png', 'TASK', '/img/task/task.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da0172504group0001', '2020-05-26 00:00:09', 'admin', b'1', '2020-05-26 00:00:09', 'admin', 0, 'group.png', 'img/group/group.png', 'GROUP', '/img/group/group.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da0172504group0002', '2020-05-26 00:00:08', 'admin', b'1', '2020-05-26 00:00:08', 'admin', 0, 'group1.png', 'img/group/group1.png', 'GROUP', '/img/group/group1.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da0172504group0003', '2020-05-26 00:00:07', 'admin', b'1', '2020-05-26 00:00:07', 'admin', 0, 'group2.png', 'img/group/group2.png', 'GROUP', '/img/group/group2.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da0172504group0004', '2020-05-26 00:00:06', 'admin', b'1', '2020-05-26 00:00:06', 'admin', 0, 'group3.png', 'img/group/group3.png', 'GROUP', '/img/group/group3.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da0172504group0005', '2020-05-26 00:00:05', 'admin', b'1', '2020-05-26 00:00:05', 'admin', 0, 'group4.png', 'img/group/group4.png', 'GROUP', '/img/group/group4.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da0172504group0006', '2020-05-26 00:00:04', 'admin', b'1', '2020-05-26 00:00:04', 'admin', 0, 'group5.png', 'img/group/group5.png', 'GROUP', '/img/group/group5.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da0172504group0007', '2020-05-26 00:00:03', 'admin', b'1', '2020-05-26 00:00:03', 'admin', 0, 'group6.png', 'img/group/group6.png', 'GROUP', '/img/group/group6.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da0172504group0008', '2020-05-26 00:00:02', 'admin', b'1', '2020-05-26 00:00:02', 'admin', 0, 'group7.png', 'img/group/group7.png', 'GROUP', '/img/group/group7.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da0172504group0009', '2020-05-26 00:00:01', 'admin', b'1', '2020-05-26 00:00:01', 'admin', 0, 'group8.png', 'img/group/group8.png', 'GROUP', '/img/group/group8.png');


-- sys_schedule
INSERT INTO `sys_schedule`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `cron_expression`, `job_class`, `job_name`, `status`, `last_run_result`) VALUES ('b494d4fecea148709a0d81cbb39e7f54', '2020-08-14 18:58:22', 'admin', b'1', '2020-09-28 22:04:59', 'admin', 17, '0/59 * * * * ?', 'cn.cnic.schedule.RunningGroupScheduleSync', 'RunningGroupScheduleSync', 'RUNNING', 'SUCCEED');


-- update data
UPDATE `sys_schedule` SET `job_class`='cn.cnic.schedule.RunningProcessSync' WHERE id='ff8081816eaa8a5d016eaa8a77e40000';
UPDATE `sys_schedule` SET `job_class`='cn.cnic.schedule.RunningProcessGroupSync' WHERE id='ff8081816eaa9317016eaa932dd50000';
UPDATE `upgrade_0_7_to_0_8`.`data_source_property` SET `name` = 'nodes' WHERE `id` = '5f297c6968ca4ba884479b1a43b82198';
UPDATE `upgrade_0_7_to_0_8`.`data_source_property` SET `name` = 'type' WHERE `id` = '8d1b0acee4be46788ca552b2040b90fc';
UPDATE `upgrade_0_7_to_0_8`.`data_source_property` SET `name` = 'port' WHERE `id` = '968c061007fa4216b9a0b05d8d68bf2c';
UPDATE `upgrade_0_7_to_0_8`.`data_source_property` SET `name` = 'index' WHERE `id` = 'f386627bb0734ef9a59c0316d4bd7ab7';






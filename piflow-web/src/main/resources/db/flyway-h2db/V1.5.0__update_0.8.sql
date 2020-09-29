
-- create table
CREATE TABLE IF NOT EXISTS `GROUP_SCHEDULE` (
  `ID` VARCHAR(40) NOT NULL,
  `CRT_DTTM` DATETIME(0) NOT NULL COMMENT 'Create date time',
  `CRT_USER` VARCHAR(255) NOT NULL COMMENT 'Create user',
  `ENABLE_FLAG` BIT(1) NOT NULL COMMENT 'Enable flag',
  `LAST_UPDATE_DTTM` DATETIME(0) NOT NULL COMMENT 'Last update date time',
  `LAST_UPDATE_USER` VARCHAR(255) NOT NULL COMMENT 'Last update user',
  `VERSION` BIGINT(20) NULL DEFAULT NULL COMMENT 'Version',
  `CRON_EXPRESSION` VARCHAR ( 255 ) COMMENT 'cron expression',
  `PLAN_END_TIME` DATETIME COMMENT 'plan end time',
  `PLAN_START_TIME` DATETIME COMMENT 'plan start time',
  `SCHEDULE_ID` VARCHAR ( 255 ) COMMENT 'service schedule id',
  `SCHEDULE_PROCESS_TEMPLATE_ID` VARCHAR ( 255 ) COMMENT 'Template ID for generating Process',
  `SCHEDULE_RUN_TEMPLATE_ID` VARCHAR ( 255 ) COMMENT 'Start template ID',
  `STATUS` VARCHAR ( 255 ) COMMENT 'schedule task status',
  `TYPE` VARCHAR ( 255 ) COMMENT 'schedule content Flow or FlowGroup',
  PRIMARY KEY ( ID )) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS `STOPS_HUB` (
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
  `STATUS` VARCHAR(255) COMMENT 'StopsHue status',
  primary key (ID)) engine=InnoDB;

-- rename table
ALTER  TABLE flow_sotps_groups RENAME TO flow_stops_groups;

-- add column
ALTER TABLE `FLOW_PROCESS` ADD COLUMN `FK_GROUP_SCHEDULE_ID` VARCHAR(40);
ALTER TABLE `FLOW_STOPS_PROPERTY` ADD COLUMN `EXAMPLE` TEXT(0) COMMENT 'property example';
ALTER TABLE FLOW_STOPS_PROPERTY_TEMPLATE ADD COLUMN `EXAMPLE` TEXT(0) COMMENT 'property example';

-- add foreign key
ALTER TABLE `FLOW_PROCESS` ADD CONSTRAINT `FK8sqeh2bcr2pylbf4b7owvokly` FOREIGN KEY (`FK_GROUP_SCHEDULE_ID`) REFERENCES `GROUP_SCHEDULE` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- mx_node_image
INSERT INTO `MX_NODE_IMAGE`(`ID`, `CRT_DTTM`, `CRT_USER`, `ENABLE_FLAG`, `LAST_UPDATE_DTTM`, `LAST_UPDATE_USER`, `VERSION`, `IMAGE_NAME`, `IMAGE_PATH`, `IMAGE_TYPE`, `IMAGE_URL`) VALUES ('ff808181725005da01725040task0001', parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'task8.png', 'img/task/task8.png', 'task', '/img/task/task8.png');
INSERT INTO `MX_NODE_IMAGE`(`ID`, `CRT_DTTM`, `CRT_USER`, `ENABLE_FLAG`, `LAST_UPDATE_DTTM`, `LAST_UPDATE_USER`, `VERSION`, `IMAGE_NAME`, `IMAGE_PATH`, `IMAGE_TYPE`, `IMAGE_URL`) VALUES ('ff808181725005da01725040task0002', parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'task7.png', 'img/task/task7.png', 'task', '/img/task/task7.png');
INSERT INTO `MX_NODE_IMAGE`(`ID`, `CRT_DTTM`, `CRT_USER`, `ENABLE_FLAG`, `LAST_UPDATE_DTTM`, `LAST_UPDATE_USER`, `VERSION`, `IMAGE_NAME`, `IMAGE_PATH`, `IMAGE_TYPE`, `IMAGE_URL`) VALUES ('ff808181725005da01725040task0003', parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'task6.png', 'img/task/task6.png', 'task', '/img/task/task6.png');
INSERT INTO `MX_NODE_IMAGE`(`ID`, `CRT_DTTM`, `CRT_USER`, `ENABLE_FLAG`, `LAST_UPDATE_DTTM`, `LAST_UPDATE_USER`, `VERSION`, `IMAGE_NAME`, `IMAGE_PATH`, `IMAGE_TYPE`, `IMAGE_URL`) VALUES ('ff808181725005da01725040task0004', parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'task5.png', 'img/task/task5.png', 'task', '/img/task/task5.png');
INSERT INTO `MX_NODE_IMAGE`(`ID`, `CRT_DTTM`, `CRT_USER`, `ENABLE_FLAG`, `LAST_UPDATE_DTTM`, `LAST_UPDATE_USER`, `VERSION`, `IMAGE_NAME`, `IMAGE_PATH`, `IMAGE_TYPE`, `IMAGE_URL`) VALUES ('ff808181725005da01725040task0005', parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'task4.png', 'img/task/task4.png', 'task', '/img/task/task4.png');
INSERT INTO `MX_NODE_IMAGE`(`ID`, `CRT_DTTM`, `CRT_USER`, `ENABLE_FLAG`, `LAST_UPDATE_DTTM`, `LAST_UPDATE_USER`, `VERSION`, `IMAGE_NAME`, `IMAGE_PATH`, `IMAGE_TYPE`, `IMAGE_URL`) VALUES ('ff808181725005da01725040task0006', parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'task3.png', 'img/task/task3.png', 'task', '/img/task/task3.png');
INSERT INTO `MX_NODE_IMAGE`(`ID`, `CRT_DTTM`, `CRT_USER`, `ENABLE_FLAG`, `LAST_UPDATE_DTTM`, `LAST_UPDATE_USER`, `VERSION`, `IMAGE_NAME`, `IMAGE_PATH`, `IMAGE_TYPE`, `IMAGE_URL`) VALUES ('ff808181725005da01725040task0007', parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'task2.png', 'img/task/task2.png', 'task', '/img/task/task2.png');
INSERT INTO `MX_NODE_IMAGE`(`ID`, `CRT_DTTM`, `CRT_USER`, `ENABLE_FLAG`, `LAST_UPDATE_DTTM`, `LAST_UPDATE_USER`, `VERSION`, `IMAGE_NAME`, `IMAGE_PATH`, `IMAGE_TYPE`, `IMAGE_URL`) VALUES ('ff808181725005da01725040task0008', parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'task1.png', 'img/task/task1.png', 'task', '/img/task/task1.png');
INSERT INTO `MX_NODE_IMAGE`(`ID`, `CRT_DTTM`, `CRT_USER`, `ENABLE_FLAG`, `LAST_UPDATE_DTTM`, `LAST_UPDATE_USER`, `VERSION`, `IMAGE_NAME`, `IMAGE_PATH`, `IMAGE_TYPE`, `IMAGE_URL`) VALUES ('ff808181725005da01725040task0009', parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'task.png', 'img/task/task.png', 'task', '/img/task/task.png');
INSERT INTO `MX_NODE_IMAGE`(`ID`, `CRT_DTTM`, `CRT_USER`, `ENABLE_FLAG`, `LAST_UPDATE_DTTM`, `LAST_UPDATE_USER`, `VERSION`, `IMAGE_NAME`, `IMAGE_PATH`, `IMAGE_TYPE`, `IMAGE_URL`) VALUES ('ff808181725005da0172504group0001', parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'group.png', 'img/group/group.png', 'group', '/img/group/group.png');
INSERT INTO `MX_NODE_IMAGE`(`ID`, `CRT_DTTM`, `CRT_USER`, `ENABLE_FLAG`, `LAST_UPDATE_DTTM`, `LAST_UPDATE_USER`, `VERSION`, `IMAGE_NAME`, `IMAGE_PATH`, `IMAGE_TYPE`, `IMAGE_URL`) VALUES ('ff808181725005da0172504group0002', parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'group1.png', 'img/group/group1.png', 'group', '/img/group/group1.png');
INSERT INTO `MX_NODE_IMAGE`(`ID`, `CRT_DTTM`, `CRT_USER`, `ENABLE_FLAG`, `LAST_UPDATE_DTTM`, `LAST_UPDATE_USER`, `VERSION`, `IMAGE_NAME`, `IMAGE_PATH`, `IMAGE_TYPE`, `IMAGE_URL`) VALUES ('ff808181725005da0172504group0003', parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'group2.png', 'img/group/group2.png', 'group', '/img/group/group2.png');
INSERT INTO `MX_NODE_IMAGE`(`ID`, `CRT_DTTM`, `CRT_USER`, `ENABLE_FLAG`, `LAST_UPDATE_DTTM`, `LAST_UPDATE_USER`, `VERSION`, `IMAGE_NAME`, `IMAGE_PATH`, `IMAGE_TYPE`, `IMAGE_URL`) VALUES ('ff808181725005da0172504group0004', parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'group3.png', 'img/group/group3.png', 'group', '/img/group/group3.png');
INSERT INTO `MX_NODE_IMAGE`(`ID`, `CRT_DTTM`, `CRT_USER`, `ENABLE_FLAG`, `LAST_UPDATE_DTTM`, `LAST_UPDATE_USER`, `VERSION`, `IMAGE_NAME`, `IMAGE_PATH`, `IMAGE_TYPE`, `IMAGE_URL`) VALUES ('ff808181725005da0172504group0005', parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'group4.png', 'img/group/group4.png', 'group', '/img/group/group4.png');
INSERT INTO `MX_NODE_IMAGE`(`ID`, `CRT_DTTM`, `CRT_USER`, `ENABLE_FLAG`, `LAST_UPDATE_DTTM`, `LAST_UPDATE_USER`, `VERSION`, `IMAGE_NAME`, `IMAGE_PATH`, `IMAGE_TYPE`, `IMAGE_URL`) VALUES ('ff808181725005da0172504group0006', parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'group5.png', 'img/group/group5.png', 'group', '/img/group/group5.png');
INSERT INTO `MX_NODE_IMAGE`(`ID`, `CRT_DTTM`, `CRT_USER`, `ENABLE_FLAG`, `LAST_UPDATE_DTTM`, `LAST_UPDATE_USER`, `VERSION`, `IMAGE_NAME`, `IMAGE_PATH`, `IMAGE_TYPE`, `IMAGE_URL`) VALUES ('ff808181725005da0172504group0007', parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'group6.png', 'img/group/group6.png', 'group', '/img/group/group6.png');
INSERT INTO `MX_NODE_IMAGE`(`ID`, `CRT_DTTM`, `CRT_USER`, `ENABLE_FLAG`, `LAST_UPDATE_DTTM`, `LAST_UPDATE_USER`, `VERSION`, `IMAGE_NAME`, `IMAGE_PATH`, `IMAGE_TYPE`, `IMAGE_URL`) VALUES ('ff808181725005da0172504group0008', parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'group7.png', 'img/group/group7.png', 'group', '/img/group/group7.png');
INSERT INTO `MX_NODE_IMAGE`(`ID`, `CRT_DTTM`, `CRT_USER`, `ENABLE_FLAG`, `LAST_UPDATE_DTTM`, `LAST_UPDATE_USER`, `VERSION`, `IMAGE_NAME`, `IMAGE_PATH`, `IMAGE_TYPE`, `IMAGE_URL`) VALUES ('ff808181725005da0172504group0009', parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'group8.png', 'img/group/group8.png', 'group', '/img/group/group8.png');


-- sys_schedule
INSERT INTO `SYS_SCHEDULE`(`ID`, `CRT_DTTM`, `CRT_USER`, `ENABLE_FLAG`, `LAST_UPDATE_DTTM`, `LAST_UPDATE_USER`, `VERSION`, `CRON_EXPRESSION`, `JOB_CLASS`, `JOB_NAME`, `STATUS`, `LAST_RUN_RESULT`) VALUES ('b494d4fecea148709a0d81cbb39e7f54', '2020-08-14 18:58:22', 'admin', b'1', '2020-09-28 22:04:59', 'admin', 17, '0/59 * * * * ?', 'cn.cnic.schedule.runninggroupschedulesync', 'runninggroupschedulesync', 'running', 'succeed');


-- update data
UPDATE `SYS_SCHEDULE` SET `JOB_CLASS`='cn.cnic.schedule.RunningProcessSync' WHERE ID='ff8081816eaa8a5d016eaa8a77e40000';
UPDATE `SYS_SCHEDULE` SET `JOB_CLASS`='cn.cnic.schedule.RunningProcessGroupSync' WHERE ID='ff8081816eaa9317016eaa932dd50000';
UPDATE `UPGRADE_0_7_TO_0_8`.`DATA_SOURCE_PROPERTY` SET `NAME` = 'nodes' WHERE `ID` = '5f297c6968ca4ba884479b1a43b82198';
UPDATE `UPGRADE_0_7_TO_0_8`.`DATA_SOURCE_PROPERTY` SET `NAME` = 'type' WHERE `ID` = '8d1b0acee4be46788ca552b2040b90fc';
UPDATE `UPGRADE_0_7_TO_0_8`.`DATA_SOURCE_PROPERTY` SET `NAME` = 'port' WHERE `ID` = '968c061007fa4216b9a0b05d8d68bf2c';
UPDATE `UPGRADE_0_7_TO_0_8`.`DATA_SOURCE_PROPERTY` SET `NAME` = 'index' WHERE `ID` = 'f386627bb0734ef9a59c0316d4bd7ab7';







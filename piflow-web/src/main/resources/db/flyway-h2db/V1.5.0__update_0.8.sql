
-- create table
CREATE TABLE IF NOT EXISTS `GROUP_SCHEDULE` (
  `ID` VARCHAR(40) PRIMARY KEY NOT NULL,
  `CRT_DTTM` DATETIME NOT NULL COMMENT 'Create date time',
  `CRT_USER` VARCHAR(255) NOT NULL COMMENT 'Create user',
  `ENABLE_FLAG` BIT NOT NULL COMMENT 'Enable flag',
  `LAST_UPDATE_DTTM` DATETIME NOT NULL COMMENT 'Last update date time',
  `LAST_UPDATE_USER` VARCHAR(255) NOT NULL COMMENT 'Last update user',
  `VERSION` BIGINT COMMENT 'Version',
  `CRON_EXPRESSION` VARCHAR ( 255 ) COMMENT 'cron expression',
  `PLAN_END_TIME` DATETIME COMMENT 'plan end time',
  `PLAN_START_TIME` DATETIME COMMENT 'plan start time',
  `SCHEDULE_ID` VARCHAR ( 255 ) COMMENT 'service schedule id',
  `SCHEDULE_PROCESS_TEMPLATE_ID` VARCHAR ( 255 ) COMMENT 'Template ID for generating Process',
  `SCHEDULE_RUN_TEMPLATE_ID` VARCHAR ( 255 ) COMMENT 'Start template ID',
  `STATUS` VARCHAR ( 255 ) COMMENT 'schedule task status',
  `TYPE` VARCHAR ( 255 ) COMMENT 'schedule content Flow or FlowGroup'
);

CREATE TABLE IF NOT EXISTS `STOPS_HUB` (
  `ID` VARCHAR(40) PRIMARY KEY NOT NULL,
  `CRT_DTTM` DATETIME NOT NULL COMMENT 'Create date time',
  `CRT_USER` VARCHAR(255) NOT NULL COMMENT 'Create user',
  `ENABLE_FLAG` BIT NOT NULL COMMENT 'Enable flag',
  `LAST_UPDATE_DTTM` DATETIME NOT NULL COMMENT 'Last update date time',
  `LAST_UPDATE_USER` VARCHAR(255) NOT NULL COMMENT 'Last update user',
  `VERSION` BIGINT COMMENT 'Version',
  `JAR_NAME` VARCHAR(1000) COMMENT 'jar name',
  `JAR_URL` VARCHAR(1000) COMMENT 'jar url',
  `MOUNT_ID` VARCHAR(1000) COMMENT 'jar mount id',
  `STATUS` VARCHAR(255) COMMENT 'StopsHue status'
);

-- rename table
ALTER  TABLE FLOW_SOTPS_GROUPS RENAME TO FLOW_STOPS_GROUPS;

-- add column
ALTER TABLE `FLOW_PROCESS` ADD COLUMN `FK_GROUP_SCHEDULE_ID` VARCHAR(40);
ALTER TABLE `FLOW_STOPS_PROPERTY` ADD COLUMN `EXAMPLE` TEXT(0) COMMENT 'property example';
ALTER TABLE FLOW_STOPS_PROPERTY_TEMPLATE ADD COLUMN `EXAMPLE` TEXT(0) COMMENT 'property example';

-- add foreign key
ALTER TABLE `FLOW_PROCESS` ADD CONSTRAINT `FK8sqeh2bcr2pylbf4b7owvokly` FOREIGN KEY (`FK_GROUP_SCHEDULE_ID`) REFERENCES `GROUP_SCHEDULE` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- sys_schedule
INSERT INTO `SYS_SCHEDULE`(`ID`, `CRT_DTTM`, `CRT_USER`, `ENABLE_FLAG`, `LAST_UPDATE_DTTM`, `LAST_UPDATE_USER`, `VERSION`, `CRON_EXPRESSION`, `JOB_CLASS`, `JOB_NAME`, `STATUS`, `LAST_RUN_RESULT`) VALUES ('b494d4fecea148709a0d81cbb39e7f54', parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 17, '0/59 * * * * ?', 'cn.cnic.schedule.RunningGroupScheduleSync', 'RunningGroupScheduleSync', 'RUNNING', 'SUCCEED');

-- update data
UPDATE `SYS_SCHEDULE` SET `JOB_CLASS`='cn.cnic.schedule.RunningProcessSync' WHERE ID='ff8081816eaa8a5d016eaa8a77e40000';
UPDATE `SYS_SCHEDULE` SET `JOB_CLASS`='cn.cnic.schedule.RunningProcessGroupSync' WHERE ID='ff8081816eaa9317016eaa932dd50000';
UPDATE `DATA_SOURCE_PROPERTY` SET `NAME` = 'nodes' WHERE `ID` = '5f297c6968ca4ba884479b1a43b82198';
UPDATE `DATA_SOURCE_PROPERTY` SET `NAME` = 'type' WHERE `ID` = '8d1b0acee4be46788ca552b2040b90fc';
UPDATE `DATA_SOURCE_PROPERTY` SET `NAME` = 'port' WHERE `ID` = '968c061007fa4216b9a0b05d8d68bf2c';
UPDATE `DATA_SOURCE_PROPERTY` SET `NAME` = 'index' WHERE `ID` = 'f386627bb0734ef9a59c0316d4bd7ab7';







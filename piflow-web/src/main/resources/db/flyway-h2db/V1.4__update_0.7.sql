-- ----------------------------
-- Drop Table
-- ----------------------------
DROP TABLE IF EXISTS `FLOW_INFO`;
DROP TABLE IF EXISTS `FLOW_PROJECT`;
DROP TABLE IF EXISTS `SCHEDULE`;

-- ----------------------------
-- Create Table
-- ----------------------------
CREATE TABLE IF NOT EXISTS `SYS_INIT_RECORDS` (
   ID VARCHAR(40) PRIMARY KEY NOT NULL,
   INIT_DATE DATETIME NOT NULL,
   IS_SUCCEED BIT NOT NULL,
);

CREATE TABLE IF NOT EXISTS mx_node_image (
   ID VARCHAR(40) PRIMARY KEY NOT NULL,
   CRT_DTTM DATETIME NOT NULL,
   CRT_USER VARCHAR(255) NOT NULL,
   ENABLE_FLAG BIT NOT NULL,
   LAST_UPDATE_DTTM DATETIME NOT NULL,
   LAST_UPDATE_USER VARCHAR(255) NOT NULL,
   VERSION BIGINT,
   IMAGE_NAME VARCHAR(255),
   IMAGE_PATH VARCHAR(255),
   IMAGE_TYPE VARCHAR(255),
   IMAGE_URL VARCHAR(255),
);

-- ----------------------------
-- Add column
-- ----------------------------
ALTER TABLE `SYS_SCHEDULE` ADD COLUMN LAST_RUN_RESULT VARCHAR(255) COMMENT 'task last run result';
ALTER TABLE `FLOW_GROUP` ADD COLUMN FK_FLOW_GROUP_ID VARCHAR(40);
ALTER TABLE `FLOW_PROCESS_GROUP` ADD COLUMN FK_FLOW_PROCESS_GROUP_ID VARCHAR(40);
ALTER TABLE `FLOW_GROUP_TEMPLATE` ADD COLUMN TEMPLATE_TYPE VARCHAR(255) COMMENT 'template type';
ALTER TABLE `FLOW_PROCESS_GROUP` ADD COLUMN PAGE_ID VARCHAR(255);
ALTER TABLE `FLOW_TEMPLATE` ADD COLUMN SOURCE_FLOW_NAME VARCHAR(255) COMMENT 'source flow name';
ALTER TABLE `FLOW_TEMPLATE` ADD COLUMN TEMPLATE_TYPE VARCHAR(255) COMMENT 'template type';
ALTER TABLE `FLOW_TEMPLATE` ADD COLUMN URL VARCHAR(255);
ALTER TABLE `MX_GRAPH_MODEL` ADD COLUMN FK_PROCESS_ID VARCHAR(40);
ALTER TABLE `MX_GRAPH_MODEL` ADD COLUMN FK_PROCESS_GROUP_ID VARCHAR(40);
ALTER TABLE `FLOW_STOPS_PROPERTY` ADD COLUMN IS_OLD_DATA BIT(1) COMMENT 'Has it been updated';
ALTER TABLE `FLOW_PROCESS_STOP` ADD COLUMN bundel VARCHAR(255) COMMENT 'bundle';
ALTER TABLE `FLOW_STOPS` ADD COLUMN bundel VARCHAR(255) COMMENT 'bundle';
ALTER TABLE `FLOW_STOPS_TEMPLATE` ADD COLUMN bundel VARCHAR(255) COMMENT 'bundle';
ALTER TABLE `STOPS_TEMPLATE` ADD COLUMN bundel VARCHAR(255) COMMENT 'bundle';

-- ----------------------------
-- Add Foreign key
-- ----------------------------
ALTER TABLE `FLOW_GROUP` ADD CONSTRAINT FKe1i6t5gnt6ys4yqkt5uumr20w FOREIGN KEY (FK_FLOW_GROUP_ID) REFERENCES FLOW_GROUP (ID);
ALTER TABLE `FLOW_PROCESS_GROUP` ADD CONSTRAINT FKpxf1rth8fs0pld3jlvigkaf2y FOREIGN KEY (FK_FLOW_PROCESS_GROUP_ID) REFERENCES FLOW_PROCESS_GROUP (ID);
ALTER TABLE `MX_GRAPH_MODEL` ADD CONSTRAINT FKkw0r9m7r3jm9scab8caoxnnxc FOREIGN KEY (FK_PROCESS_ID) REFERENCES FLOW_PROCESS (ID);
ALTER TABLE `MX_GRAPH_MODEL` ADD CONSTRAINT FKnugg3p8uvupfu3mso2iax2g8t FOREIGN KEY (FK_PROCESS_GROUP_ID) REFERENCES FLOW_PROCESS_GROUP (ID);

-- ----------------------------
-- Update Data
-- ----------------------------
UPDATE SYS_SCHEDULE SS SET SS.LAST_RUN_RESULT='SUCCEED';
UPDATE `SYS_MENU` SS SET SS.ENABLE_FLAG=0;
UPDATE `flow_stops_property` fsp SET fsp.is_old_data=0;

-- ----------------------------
-- Insert Data
-- ----------------------------
INSERT INTO SYS_MENU(ID, CRT_DTTM, CRT_USER, ENABLE_FLAG, LAST_UPDATE_DTTM, LAST_UPDATE_USER, VERSION, MENU_DESCRIPTION, MENU_JURISDICTION, MENU_NAME, MENU_PARENT, MENU_URL, MENU_SORT) VALUES ('0641076d5ae840c09d2be6wmenu00001', parsedatetime('01-01-2020 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'system', 1, parsedatetime('01-01-2020 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'system', 0, 'Flows', 'USER', 'Flow', NULL, '/piflow-web/web/flowList', 100001);
INSERT INTO SYS_MENU(ID, CRT_DTTM, CRT_USER, ENABLE_FLAG, LAST_UPDATE_DTTM, LAST_UPDATE_USER, VERSION, MENU_DESCRIPTION, MENU_JURISDICTION, MENU_NAME, MENU_PARENT, MENU_URL, MENU_SORT) VALUES ('0641076d5ae840c09d2be6wmenu00002', parsedatetime('01-01-2020 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'system', 1, parsedatetime('01-01-2020 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'system', 0, 'FlowGroup', 'USER', 'Group', NULL, '/piflow-web/web/flowGroupList', 100001);
INSERT INTO SYS_MENU(ID, CRT_DTTM, CRT_USER, ENABLE_FLAG, LAST_UPDATE_DTTM, LAST_UPDATE_USER, VERSION, MENU_DESCRIPTION, MENU_JURISDICTION, MENU_NAME, MENU_PARENT, MENU_URL, MENU_SORT) VALUES ('0641076d5ae840c09d2be6wmenu00003', parsedatetime('01-01-2020 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'system', 1, parsedatetime('01-01-2020 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'system', 0, 'Process', 'USER', 'Process', NULL, '/piflow-web/web/processAndProcessGroup', 100002);
INSERT INTO SYS_MENU(ID, CRT_DTTM, CRT_USER, ENABLE_FLAG, LAST_UPDATE_DTTM, LAST_UPDATE_USER, VERSION, MENU_DESCRIPTION, MENU_JURISDICTION, MENU_NAME, MENU_PARENT, MENU_URL, MENU_SORT) VALUES ('0641076d5ae840c09d2be6wmenu00004', parsedatetime('01-01-2020 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'system', 1, parsedatetime('01-01-2020 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'system', 0, 'FlowGroupTemplate', 'USER', 'Template', NULL, '/piflow-web/web/flowTemplateList', 100003);
INSERT INTO SYS_MENU(ID, CRT_DTTM, CRT_USER, ENABLE_FLAG, LAST_UPDATE_DTTM, LAST_UPDATE_USER, VERSION, MENU_DESCRIPTION, MENU_JURISDICTION, MENU_NAME, MENU_PARENT, MENU_URL, MENU_SORT) VALUES ('0641076d5ae840c09d2be6wmenu00005', parsedatetime('01-01-2020 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'system', 1, parsedatetime('01-01-2020 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'system', 0, 'DataSource', 'USER', 'DataSource', NULL, '/piflow-web/web/dataSources', 400001);
INSERT INTO SYS_MENU(ID, CRT_DTTM, CRT_USER, ENABLE_FLAG, LAST_UPDATE_DTTM, LAST_UPDATE_USER, VERSION, MENU_DESCRIPTION, MENU_JURISDICTION, MENU_NAME, MENU_PARENT, MENU_URL, MENU_SORT) VALUES ('0641076d5ae840c09d2be6wmenu00007', parsedatetime('01-01-2020 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'system', 1, parsedatetime('01-01-2020 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'system', 0, 'Example1', 'USER', 'FlowExample', 'Example', '/piflow-web/mxGraph/drawingBoard?drawingBoardType=TASK&load=0641076d5ae840c09d2be5b71fw00001', 500002);
INSERT INTO SYS_MENU(ID, CRT_DTTM, CRT_USER, ENABLE_FLAG, LAST_UPDATE_DTTM, LAST_UPDATE_USER, VERSION, MENU_DESCRIPTION, MENU_JURISDICTION, MENU_NAME, MENU_PARENT, MENU_URL, MENU_SORT) VALUES ('0641076d5ae840c09d2be6wmenu00008', parsedatetime('01-01-2020 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'system', 1, parsedatetime('01-01-2020 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'system', 0, 'Example2', 'USER', 'GroupExample2', 'Example', '/piflow-web/mxGraph/drawingBoard?drawingBoardType=GROUP&load=ff808181725050fe017250group10002', 500003);
INSERT INTO SYS_MENU(ID, CRT_DTTM, CRT_USER, ENABLE_FLAG, LAST_UPDATE_DTTM, LAST_UPDATE_USER, VERSION, MENU_DESCRIPTION, MENU_JURISDICTION, MENU_NAME, MENU_PARENT, MENU_URL, MENU_SORT) VALUES ('0641076d5ae840c09d2be6wmenu00009', parsedatetime('01-01-2020 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'system', 1, parsedatetime('01-01-2020 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'system', 0, 'Schedule', 'ADMIN', 'Schedule', 'Admin', '/piflow-web/web/sysScheduleList', 900001);

INSERT INTO MX_NODE_IMAGE(ID, CRT_DTTM, CRT_USER, ENABLE_FLAG, LAST_UPDATE_DTTM, LAST_UPDATE_USER, VERSION, IMAGE_NAME, IMAGE_PATH, IMAGE_TYPE, IMAGE_URL) VALUES ('ff808181725005da01725040task0001', parsedatetime('26-05-2020 00:00:01.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('26-05-2020 00:00:01.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'task8.png', 'img/task/task8.png', 'TASK', '/img/task/task8.png');
INSERT INTO MX_NODE_IMAGE(ID, CRT_DTTM, CRT_USER, ENABLE_FLAG, LAST_UPDATE_DTTM, LAST_UPDATE_USER, VERSION, IMAGE_NAME, IMAGE_PATH, IMAGE_TYPE, IMAGE_URL) VALUES ('ff808181725005da01725040task0002', parsedatetime('26-05-2020 00:00:02.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('26-05-2020 00:00:02.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'task7.png', 'img/task/task7.png', 'TASK', '/img/task/task7.png');
INSERT INTO MX_NODE_IMAGE(ID, CRT_DTTM, CRT_USER, ENABLE_FLAG, LAST_UPDATE_DTTM, LAST_UPDATE_USER, VERSION, IMAGE_NAME, IMAGE_PATH, IMAGE_TYPE, IMAGE_URL) VALUES ('ff808181725005da01725040task0003', parsedatetime('26-05-2020 00:00:03.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('26-05-2020 00:00:03.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'task6.png', 'img/task/task6.png', 'TASK', '/img/task/task6.png');
INSERT INTO MX_NODE_IMAGE(ID, CRT_DTTM, CRT_USER, ENABLE_FLAG, LAST_UPDATE_DTTM, LAST_UPDATE_USER, VERSION, IMAGE_NAME, IMAGE_PATH, IMAGE_TYPE, IMAGE_URL) VALUES ('ff808181725005da01725040task0004', parsedatetime('26-05-2020 00:00:04.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('26-05-2020 00:00:04.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'task5.png', 'img/task/task5.png', 'TASK', '/img/task/task5.png');
INSERT INTO MX_NODE_IMAGE(ID, CRT_DTTM, CRT_USER, ENABLE_FLAG, LAST_UPDATE_DTTM, LAST_UPDATE_USER, VERSION, IMAGE_NAME, IMAGE_PATH, IMAGE_TYPE, IMAGE_URL) VALUES ('ff808181725005da01725040task0005', parsedatetime('26-05-2020 00:00:05.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('26-05-2020 00:00:05.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'task4.png', 'img/task/task4.png', 'TASK', '/img/task/task4.png');
INSERT INTO MX_NODE_IMAGE(ID, CRT_DTTM, CRT_USER, ENABLE_FLAG, LAST_UPDATE_DTTM, LAST_UPDATE_USER, VERSION, IMAGE_NAME, IMAGE_PATH, IMAGE_TYPE, IMAGE_URL) VALUES ('ff808181725005da01725040task0006', parsedatetime('26-05-2020 00:00:06.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('26-05-2020 00:00:06.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'task3.png', 'img/task/task3.png', 'TASK', '/img/task/task3.png');
INSERT INTO MX_NODE_IMAGE(ID, CRT_DTTM, CRT_USER, ENABLE_FLAG, LAST_UPDATE_DTTM, LAST_UPDATE_USER, VERSION, IMAGE_NAME, IMAGE_PATH, IMAGE_TYPE, IMAGE_URL) VALUES ('ff808181725005da01725040task0007', parsedatetime('26-05-2020 00:00:07.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('26-05-2020 00:00:07.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'task2.png', 'img/task/task2.png', 'TASK', '/img/task/task2.png');
INSERT INTO MX_NODE_IMAGE(ID, CRT_DTTM, CRT_USER, ENABLE_FLAG, LAST_UPDATE_DTTM, LAST_UPDATE_USER, VERSION, IMAGE_NAME, IMAGE_PATH, IMAGE_TYPE, IMAGE_URL) VALUES ('ff808181725005da01725040task0008', parsedatetime('26-05-2020 00:00:08.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('26-05-2020 00:00:08.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'task1.png', 'img/task/task1.png', 'TASK', '/img/task/task1.png');
INSERT INTO MX_NODE_IMAGE(ID, CRT_DTTM, CRT_USER, ENABLE_FLAG, LAST_UPDATE_DTTM, LAST_UPDATE_USER, VERSION, IMAGE_NAME, IMAGE_PATH, IMAGE_TYPE, IMAGE_URL) VALUES ('ff808181725005da01725040task0009', parsedatetime('26-05-2020 00:00:09.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('26-05-2020 00:00:09.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'task.png', 'img/task/task.png', 'TASK', '/img/task/task.png');

INSERT INTO MX_NODE_IMAGE(ID, CRT_DTTM, CRT_USER, ENABLE_FLAG, LAST_UPDATE_DTTM, LAST_UPDATE_USER, VERSION, IMAGE_NAME, IMAGE_PATH, IMAGE_TYPE, IMAGE_URL) VALUES ('ff808181725005da0172504group0009', parsedatetime('26-05-2020 00:00:01.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('26-05-2020 00:00:01.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'group8.png', 'img/group/group8.png', 'GROUP', '/img/group/group8.png');
INSERT INTO MX_NODE_IMAGE(ID, CRT_DTTM, CRT_USER, ENABLE_FLAG, LAST_UPDATE_DTTM, LAST_UPDATE_USER, VERSION, IMAGE_NAME, IMAGE_PATH, IMAGE_TYPE, IMAGE_URL) VALUES ('ff808181725005da0172504group0008', parsedatetime('26-05-2020 00:00:02.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('26-05-2020 00:00:02.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'group7.png', 'img/group/group7.png', 'GROUP', '/img/group/group7.png');
INSERT INTO MX_NODE_IMAGE(ID, CRT_DTTM, CRT_USER, ENABLE_FLAG, LAST_UPDATE_DTTM, LAST_UPDATE_USER, VERSION, IMAGE_NAME, IMAGE_PATH, IMAGE_TYPE, IMAGE_URL) VALUES ('ff808181725005da0172504group0007', parsedatetime('26-05-2020 00:00:03.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('26-05-2020 00:00:03.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'group6.png', 'img/group/group6.png', 'GROUP', '/img/group/group6.png');
INSERT INTO MX_NODE_IMAGE(ID, CRT_DTTM, CRT_USER, ENABLE_FLAG, LAST_UPDATE_DTTM, LAST_UPDATE_USER, VERSION, IMAGE_NAME, IMAGE_PATH, IMAGE_TYPE, IMAGE_URL) VALUES ('ff808181725005da0172504group0006', parsedatetime('26-05-2020 00:00:04.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('26-05-2020 00:00:04.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'group5.png', 'img/group/group5.png', 'GROUP', '/img/group/group5.png');
INSERT INTO MX_NODE_IMAGE(ID, CRT_DTTM, CRT_USER, ENABLE_FLAG, LAST_UPDATE_DTTM, LAST_UPDATE_USER, VERSION, IMAGE_NAME, IMAGE_PATH, IMAGE_TYPE, IMAGE_URL) VALUES ('ff808181725005da0172504group0005', parsedatetime('26-05-2020 00:00:05.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('26-05-2020 00:00:05.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'group4.png', 'img/group/group4.png', 'GROUP', '/img/group/group4.png');
INSERT INTO MX_NODE_IMAGE(ID, CRT_DTTM, CRT_USER, ENABLE_FLAG, LAST_UPDATE_DTTM, LAST_UPDATE_USER, VERSION, IMAGE_NAME, IMAGE_PATH, IMAGE_TYPE, IMAGE_URL) VALUES ('ff808181725005da0172504group0004', parsedatetime('26-05-2020 00:00:06.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('26-05-2020 00:00:06.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'group3.png', 'img/group/group3.png', 'GROUP', '/img/group/group3.png');
INSERT INTO MX_NODE_IMAGE(ID, CRT_DTTM, CRT_USER, ENABLE_FLAG, LAST_UPDATE_DTTM, LAST_UPDATE_USER, VERSION, IMAGE_NAME, IMAGE_PATH, IMAGE_TYPE, IMAGE_URL) VALUES ('ff808181725005da0172504group0003', parsedatetime('26-05-2020 00:00:07.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('26-05-2020 00:00:07.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'group2.png', 'img/group/group2.png', 'GROUP', '/img/group/group2.png');
INSERT INTO MX_NODE_IMAGE(ID, CRT_DTTM, CRT_USER, ENABLE_FLAG, LAST_UPDATE_DTTM, LAST_UPDATE_USER, VERSION, IMAGE_NAME, IMAGE_PATH, IMAGE_TYPE, IMAGE_URL) VALUES ('ff808181725005da0172504group0002', parsedatetime('26-05-2020 00:00:08.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('26-05-2020 00:00:08.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'group1.png', 'img/group/group1.png', 'GROUP', '/img/group/group1.png');
INSERT INTO MX_NODE_IMAGE(ID, CRT_DTTM, CRT_USER, ENABLE_FLAG, LAST_UPDATE_DTTM, LAST_UPDATE_USER, VERSION, IMAGE_NAME, IMAGE_PATH, IMAGE_TYPE, IMAGE_URL) VALUES ('ff808181725005da0172504group0001', parsedatetime('26-05-2020 00:00:09.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 1, parsedatetime('26-05-2020 00:00:09.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'admin', 0, 'group.png', 'img/group/group.png', 'GROUP', '/img/group/group.png');




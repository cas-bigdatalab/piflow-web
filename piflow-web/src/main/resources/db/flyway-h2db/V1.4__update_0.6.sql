-- ----------------------------
-- add table column
-- ----------------------------
ALTER TABLE SYS_SCHEDULE ADD COLUMN LAST_RUN_RESULT VARCHAR(255) COMMENT 'task last run result';
ALTER TABLE FLOW_GROUP ADD COLUMN FK_FLOW_GROUP_ID VARCHAR(40);
ALTER TABLE FLOW_PROCESS_GROUP ADD COLUMN PAGE_ID VARCHAR(255);
ALTER TABLE FLOW_PROCESS_GROUP ADD COLUMN FK_FLOW_PROCESS_GROUP_ID VARCHAR(40);

-- ----------------------------
-- add foreign key
-- ----------------------------
ALTER TABLE FLOW_GROUP ADD CONSTRAINT FKe1i6t5gnt6ys4yqkt5uumr20w FOREIGN KEY (FK_FLOW_GROUP_ID) REFERENCES FLOW_GROUP (ID);
ALTER TABLE FLOW_PROCESS_GROUP ADD CONSTRAINT FKpxf1rth8fs0pld3jlvigkaf2y FOREIGN KEY (FK_FLOW_PROCESS_GROUP_ID) REFERENCES FLOW_PROCESS_GROUP (ID);

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `SYS_MENU`(`ID`, `CRT_DTTM`, `CRT_USER`, `ENABLE_FLAG`, `LAST_UPDATE_DTTM`, `LAST_UPDATE_USER`, `VERSION`, `MENU_DESCRIPTION`, `MENU_JURISDICTION`, `MENU_NAME`, `MENU_PARENT`, `MENU_URL`, `MENU_SORT`) VALUES ('0641076d5ae840c09d2be5bmenu00017', parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'system', 1, parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'system', 0, 'schedule', 'ADMIN', 'schedule', 'Admin', '/piflow-web/web/sysScheduleList', 900001);

-- ----------------------------
-- Records of sys_schedule
-- ----------------------------
UPDATE SYS_SCHEDULE SS SET SS.LAST_RUN_RESULT='SUCCEED';


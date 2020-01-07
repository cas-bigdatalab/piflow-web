-- add table column
ALTER TABLE SYS_SCHEDULE ADD COLUMN LAST_RUN_RESULT VARCHAR(255) COMMENT 'task last run result';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `SYS_MENU`(`ID`, `CRT_DTTM`, `CRT_USER`, `ENABLE_FLAG`, `LAST_UPDATE_DTTM`, `LAST_UPDATE_USER`, `VERSION`, `MENU_DESCRIPTION`, `MENU_JURISDICTION`, `MENU_NAME`, `MENU_PARENT`, `MENU_URL`, `MENU_SORT`) VALUES ('0641076d5ae840c09d2be5bmenu00017', parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'system', 1, parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'system', 0, 'Test1', 'ADMIN', 'schedule', 'Admin', '/piflow-web/web/sysScheduleList', 900001);
INSERT INTO `SYS_MENU`(`ID`, `CRT_DTTM`, `CRT_USER`, `ENABLE_FLAG`, `LAST_UPDATE_DTTM`, `LAST_UPDATE_USER`, `VERSION`, `MENU_DESCRIPTION`, `MENU_JURISDICTION`, `MENU_NAME`, `MENU_PARENT`, `MENU_URL`, `MENU_SORT`) VALUES ('0641076d5ae840c09d2be5bmenu00018', parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'system', 1, parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'system', 0, 'Test2', 'ADMIN', 'Test2', 'Admin', '/piflow-web/web/sysScheduleList', 900002);

-- ----------------------------
-- Records of sys_schedule
-- ----------------------------
UPDATE SYS_SCHEDULE SS SET SS.LAST_RUN_RESULT='SUCCEED';
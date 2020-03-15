-- add table column
ALTER TABLE sys_schedule ADD COLUMN last_run_result VARCHAR(255) COMMENT 'task last run result';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be5bmenu00017', parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'system', 1, parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'system', 0, 'Test1', 'ADMIN', 'schedule', 'Admin', '/piflow-web/web/sysScheduleList', 900001);

-- ----------------------------
-- Records of sys_schedule
-- ----------------------------
UPDATE sys_schedule SS SET SS.last_run_result='SUCCEED';
-- add table column
ALTER TABLE sys_schedule ADD COLUMN last_run_result varchar(255) COMMENT 'task last run result';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be5bmenu00017', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'Test1', 'ADMIN', 'schedule', 'Admin', '/piflow-web/web/sysScheduleList', 900001);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be5bmenu00018', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'Test2', 'ADMIN', 'Test2', 'Admin', '/piflow-web/web/sysScheduleList', 900002);

-- ----------------------------
-- Records of sys_schedule
-- ----------------------------
UPDATE sys_schedule ss SET ss.last_run_result="SUCCEED";
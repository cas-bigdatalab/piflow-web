-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO sys_user(id, crt_dttm, crt_user, enable_flag, last_update_dttm, last_update_user, version, age, name, password, sex, username) VALUES ('bef148e608004bd8a72e658fed2f9c9f', parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'system', 1, parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'system', 0, NULL, 'admin', '$2a$10$.PvkSt3Dxz7wopF.q.jNIecayq/b7BPB5ozELXFHxAJljN1hWbbmS', NULL, 'admin');

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO sys_role(id, role, fk_sys_user_id) VALUES (1, 'ADMIN', 'bef148e608004bd8a72e658fed2f9c9f');
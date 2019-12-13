-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO SYS_USER(ID, CRT_DTTM, CRT_USER, ENABLE_FLAG, LAST_UPDATE_DTTM, LAST_UPDATE_USER, VERSION, AGE, NAME, PASSWORD, SEX, USERNAME) VALUES ('bef148e608004bd8a72e658fed2f9c9f', parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'system', 1, parsedatetime('2021-01-01 00:00:00.00', 'dd-MM-yyyy hh:mm:ss.SS'), 'system', 0, NULL, 'admin', '$2a$10$.PvkSt3Dxz7wopF.q.jNIecayq/b7BPB5ozELXFHxAJljN1hWbbmS', NULL, 'admin');

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO SYS_ROLE(ID, ROLE, FK_SYS_USER_ID) VALUES (1, 'ADMIN', 'bef148e608004bd8a72e658fed2f9c9f');
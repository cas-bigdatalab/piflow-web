-- ----------------------------
-- Delete and reload
-- ----------------------------
DELETE FROM sys_role WHERE fk_sys_user_id = '0641076d5ae840c09d2be5b71su00001';
DELETE FROM sys_user WHERE id = '0641076d5ae840c09d2be5b71su00001';
-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `age`, `name`, `password`, `sex`, `username`) VALUES ('0641076d5ae840c09d2be5b71su00001', NOW(), 'system', b'1', NOW(), 'system', 0, NULL, 'admin', '$2a$10$.PvkSt3Dxz7wopF.q.jNIecayq/b7BPB5ozELXFHxAJljN1hWbbmS', NULL, 'admin');
-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role`(`id`, `role`, `fk_sys_user_id`) VALUES (0, 'ADMIN', '0641076d5ae840c09d2be5b71su00001');
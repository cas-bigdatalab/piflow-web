-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log`
(
    `id`               int(11) NOT NULL AUTO_INCREMENT,
    `username`         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
    `last_login_ip`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '管理员地址',
    `type`             int(11) NULL DEFAULT NULL COMMENT '操作分类',
    `action`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作动作',
    `status`           tinyint(1) NULL DEFAULT NULL COMMENT '操作状态',
    `result`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作结果，或者成功消息，或者失败消息',
    `comment`          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '补充信息',
    `crt_dttm`         datetime NULL DEFAULT NULL COMMENT '创建时间',
    `last_update_dttm` datetime NULL DEFAULT NULL COMMENT '更新时间',
    `enable_flag`      bit(1) NULL DEFAULT b'0' COMMENT '逻辑删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 95 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;


ALTER TABLE `sys_user` ADD COLUMN `status` tinyint(3) NULL COMMENT 'user status';
ALTER TABLE `sys_user` ADD COLUMN `last_login_ip` varchar(255) NULL COMMENT 'last login ip';


UPDATE `sys_user` SET `status`=0 WHERE `id`='bef148e608004bd8a72e658fed2f9c9f';


ALTER TABLE `mx_cell` MODIFY COLUMN `mx_value` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'mx_value';
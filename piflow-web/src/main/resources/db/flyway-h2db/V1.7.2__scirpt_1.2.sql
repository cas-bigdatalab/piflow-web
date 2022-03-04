
CREATE TABLE IF NOT EXISTS "SYS_OPERATION_LOG" (
    "ID"               INTEGER(11) PRIMARY KEY AUTO_INCREMENT,
    "USERNAME"         VARCHAR(45) NULL DEFAULT NULL COMMENT '用户名',
    "LAST_LOGIN_IP"    VARCHAR(45) NULL DEFAULT NULL COMMENT '管理员地址',
    "TYPE"             INTEGER(11) NULL DEFAULT NULL COMMENT '操作分类',
    "ACTION"           VARCHAR(45) NULL DEFAULT NULL COMMENT '操作动作',
    "STATUS"           TINYINT NULL DEFAULT NULL COMMENT '操作状态',
    "RESULT"           VARCHAR(127) NULL DEFAULT NULL COMMENT '操作结果，或者成功消息，或者失败消息',
    "COMMENT"          VARCHAR(255) NULL DEFAULT NULL COMMENT '补充信息',
    "CRT_DTTM"         DATETIME NULL DEFAULT NULL COMMENT '创建时间',
    "LAST_UPDATE_DTTM" DATETIME NULL DEFAULT NULL COMMENT '更新时间',
    "ENABLE_FLAG"      BIT NULL DEFAULT '0' COMMENT '逻辑删除'
);

ALTER TABLE `SYS_USER` ADD COLUMN "STATUS" TINYINT NULL DEFAULT NULL COMMENT 'STATUS';
ALTER TABLE `SYS_USER` ADD COLUMN "LAST_LOGIN_IP" VARCHAR(63) NULL DEFAULT NULL COMMENT 'LAST_LOGIN_IP';


UPDATE `SYS_USER` SET `STATUS` = 0 WHERE `ID` = 'bef148e608004bd8a72e658fed2f9c9f';

ALTER TABLE `MX_CELL` MODIFY COLUMN `MX_VALUE` TEXT COMMENT 'MX_VALUE';

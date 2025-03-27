-- ----------------------------
-- Add column
-- ----------------------------
CREATE TABLE IF NOT EXISTS `user_share_platform_identification` (
    `username` VARCHAR(40) NOT NULL COMMENT '系统用户名',
    `share_platform_identification` VARCHAR(255) NOT NULL COMMENT '用户对应台站名',
    `share_platform_user` VARCHAR(255) NOT NULL COMMENT '用户对应台站管理员用户',

    PRIMARY KEY (`username`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
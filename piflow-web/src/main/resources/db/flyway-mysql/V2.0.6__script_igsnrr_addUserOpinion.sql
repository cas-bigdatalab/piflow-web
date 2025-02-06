-- ----------------------------
-- Add user_opinion
-- ----------------------------
CREATE TABLE IF NOT EXISTS `user_opinion` (
    `id` VARCHAR(40) NOT NULL PRIMARY KEY COMMENT '主键ID',
    `name` VARCHAR(255) DEFAULT NULL COMMENT '标题',
    `description` text DEFAULT NULL COMMENT '描述',
    `crt_dttm` datetime    NOT NULL COMMENT '创建时间',
    `crt_user` varchar(255) NOT NULL COMMENT '创建人',
    `company` varchar(255) DEFAULT '' COMMENT '创建人所属台站',
    `organization` varchar(255) DEFAULT '' COMMENT '单位名称',
    `enable_flag` bit(1) NOT NULL COMMENT '逻辑删除，0-不可用 1-可用',
    `state` tinyint(1) NOT NULL COMMENT '状态 0-待解决 1-已解决',
    `last_update_dttm` datetime NOT NULL COMMENT '最后更新时间',
    `last_update_user` varchar(255) NOT NULL COMMENT '最后更新人',
    `bak` varchar(255) NULL DEFAULT NULL COMMENT '备注'
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT='用户意见反馈表';
-----------------------------------
--Table structure for error_log_mapping
-----------------------------------

CREATE TABLE IF NOT EXISTS `error_log_mapping`
(
    `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `name` varchar(255) NULL DEFAULT NULL COMMENT '名称',
    `origin_abstract` varchar(1024) NULL DEFAULT NULL COMMENT '原始报错摘要',
    `explain_zh` varchar(512) NULL DEFAULT NULL COMMENT '中文解释映射',
    `regex_pattern` varchar(512) NULL DEFAULT NULL COMMENT '关键词匹配，正则表达式',
    `crt_dttm` datetime NOT NULL COMMENT '创建时间',
    `crt_user` varchar(255) NOT NULL COMMENT '创建人',
    `enable_flag` bit(1) NOT NULL COMMENT '逻辑删除，0-不可用 1-可用',
    `last_update_dttm` datetime NOT NULL COMMENT '最后更新时间',
    `last_update_user` varchar(255) NOT NULL COMMENT '最后更新人',
    `version` bigint(20) NULL DEFAULT NULL COMMENT 'version',
    `bak1` varchar(255) NULL DEFAULT NULL COMMENT '保留字段1',
    `bak2` varchar(255) NULL DEFAULT NULL COMMENT '保留字段2',
    `bak3` varchar(255) NULL DEFAULT NULL COMMENT '保留字段3'
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT='用户错误日志映射表';
-----------------------------------
--alter Table structure for sys_user
-----------------------------------

ALTER TABLE sys_user ADD phone_number varchar(40) DEFAULT "" NOT NULL COMMENT '联系电话';
ALTER TABLE sys_user ADD email varchar(40) DEFAULT "" NOT NULL COMMENT '联系邮箱';


-----------------------------------
--alter Table structure for data_product
-----------------------------------

ALTER TABLE data_product ADD is_share bit(1) DEFAULT 0 NOT NULL COMMENT '是否发布到共享服务';
ALTER TABLE data_product ADD doi_id varchar(100) DEFAULT "" NOT NULL COMMENT 'DOI标识';
ALTER TABLE data_product ADD cstr_id varchar(100) DEFAULT "" NOT NULL COMMENT 'CSTR标识';
ALTER TABLE data_product ADD subject_type_id varchar(40) DEFAULT "" NOT NULL COMMENT '学科分类id';
ALTER TABLE data_product ADD time_range varchar(100) DEFAULT "" NOT NULL COMMENT '时间范围';
ALTER TABLE data_product ADD spacial_range varchar(100) DEFAULT "" NOT NULL COMMENT '空间范围';
ALTER TABLE data_product ADD dataset_size varchar(100) DEFAULT "" NOT NULL COMMENT '存储量' after dataset_url;
ALTER TABLE data_product ADD dataset_type tinyint(1) DEFAULT 0 NOT NULL COMMENT '数据集类型 0-电子表格 1-数据库';
ALTER TABLE data_product ADD associate_id bigint(20) NULL DEFAULT NUll COMMENT '关联的数据产品id，多个用英文分号隔开';

-----------------------------------
--alter Table structure for flow_publishing
-----------------------------------

ALTER TABLE flow_publishing ADD flow_sort int DEFAULT 0 NOT NULL COMMENT '排序';

-----------------------------------
--Table structure for ecosystem_type
-----------------------------------

CREATE TABLE IF NOT EXISTS `ecosystem_type`
(
    `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `name` varchar(255) NULL DEFAULT NULL COMMENT '名称',
    `description` text NULL DEFAULT NULL COMMENT '简介',
    `crt_dttm` datetime    NOT NULL COMMENT '创建时间',
    `crt_user` varchar(255) NOT NULL COMMENT '创建人',
    `enable_flag` bit(1) NOT NULL COMMENT '逻辑删除，0-不可用 1-可用',
    `last_update_dttm` datetime NOT NULL COMMENT '最后更新时间',
    `last_update_user` varchar(255) NOT NULL COMMENT '最后更新人',
    `version` bigint(20) NULL DEFAULT NULL COMMENT 'version',
    `bak1` varchar(255) NULL DEFAULT NULL COMMENT '保留字段1',
    `bak2` varchar(255) NULL DEFAULT NULL COMMENT '保留字段2',
    `bak3` varchar(255) NULL DEFAULT NULL COMMENT '保留字段3'
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT='生态系统类型';

INSERT INTO `ecosystem_type` (`id`, `name`, `description`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version` ) VALUES
    ('1', '森林', '森林', NOW(), 'admin', b'1', NOW(), 'admin', 0 );
INSERT INTO `ecosystem_type` (`id`, `name`, `description`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version` ) VALUES
    ('2', '草地', '草地', NOW(), 'admin', b'1', NOW(), 'admin', 0 );
INSERT INTO `ecosystem_type` (`id`, `name`, `description`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version` ) VALUES
    ('3', '荒漠', '荒漠', NOW(), 'admin', b'1', NOW(), 'admin', 0 );
INSERT INTO `ecosystem_type` (`id`, `name`, `description`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version` ) VALUES
    ('4', '农田', '农田', NOW(), 'admin', b'1', NOW(), 'admin', 0 );
INSERT INTO `ecosystem_type` (`id`, `name`, `description`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version` ) VALUES
    ('5', '水体', '水体', NOW(), 'admin', b'1', NOW(), 'admin', 0 );



-----------------------------------
--Table structure for ecosystem_type_associate
-----------------------------------

CREATE TABLE IF NOT EXISTS `ecosystem_type_associate`
(
    `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `ecosystem_type_id` bigint(20) NOT NULL COMMENT '生态系统类型ID',
    `ecosystem_type_name`varchar(255) NOT NULL COMMENT '生态系统类型名称',
    `associate_id` varchar(50) NOT NULL COMMENT '关联ID',
    `associate_type` tinyint(1) NOT NULL COMMENT '关联类型 0-流水线 1-数据产品 2-用户',
    `bak1` varchar(255) NULL DEFAULT NULL COMMENT '保留字段1',
    `bak2` varchar(255) NULL DEFAULT NULL COMMENT '保留字段2',
    `bak3` varchar(255) NULL DEFAULT NULL COMMENT '保留字段3'
    PRIMARY KEY (`id`) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT='生态系统类型_关联表';
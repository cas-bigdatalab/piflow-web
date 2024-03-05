-----------------------------------
--Table structure for data_product_type
-----------------------------------

CREATE TABLE IF NOT EXISTS `data_product_type`
(
    `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `parent_id` bigint(20) NOT NULL COMMENT '父级ID',
    `level` tinyint(1) NOT NULL COMMENT '级别',
    `name` varchar(255) NULL DEFAULT NULL COMMENT '名称',
    `description` varchar(512) NULL DEFAULT NULL COMMENT '简介，不少于50字',
    `crt_dttm` datetime    NOT NULL COMMENT '创建时间',
    `crt_user` varchar(255) NOT NULL COMMENT '创建人',
    `enable_flag` bit(1) NOT NULL COMMENT '逻辑删除，0-不可用 1-可用',
    `last_update_dttm` datetime NOT NULL COMMENT '最后更新时间',
    `last_update_user` varchar(255) NOT NULL COMMENT '最后更新人',
    `version` bigint(20) NULL DEFAULT NULL COMMENT 'version',
    `bak1` varchar(255) NULL DEFAULT NULL COMMENT '保留字段1',
    `bak2` varchar(255) NULL DEFAULT NULL COMMENT '保留字段2',
    `bak3` varchar(255) NULL DEFAULT NULL COMMENT '保留字段3'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT='数据产品类型表';

-----------------------------------
--Table structure for product_type_associate
-----------------------------------

CREATE TABLE IF NOT EXISTS `product_type_associate`
(
    `id` bigint(20) NOT NULL COMMENT '主键ID',
    `product_type_id` bigint(20) NOT NULL COMMENT '数据产品类型ID',
    `product_type_name` varchar(255) NOT NULL COMMENT '数据产品名称',
    `associate_id` varchar(50) NOT NULL COMMENT '关联ID',
    `associate_type` tinyint(1) NOT NULL COMMENT '关联类型 0-流水线 1-数据产品 2-用户',
    `state` tinyint(1) NOT NULL COMMENT '状态 0-已删除 1-感兴趣 2-不感兴趣 3-可用',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT='数据产品类型_关联表';

-----------------------------------
--Table structure for data_product
-----------------------------------

CREATE TABLE IF NOT EXISTS `data_product`
(
    `id` bigint(20) NOT NULL COMMENT '主键ID',
    `process_id` varchar(40) NOT NULL COMMENT '关联的进程ID',
    `property_id` bigint(20) NOT NULL COMMENT '关联的发布参数ID',
    `property_name` varchar(255) NOT NULL COMMENT '关联的发布参数名称',
    `dataset_url` varchar(255) NULL DEFAULT NULL COMMENT '资源地址，即为输出参数的custumValue值',
    `name` varchar(255) NULL DEFAULT NULL COMMENT '数据产品名称，不少于10个字。建议命名规范：时间-区域-产品名称，例如：2002-2016年中国典型陆地生态系统土壤含水量观测数据集',
    `description` varchar(512) NULL DEFAULT NULL COMMENT '简介，不少于50字',
    `permission` tinyint(1) NOT NULL COMMENT '权限 0-公开 1-授权',
    `keyword` varchar(50) NULL DEFAULT NULL COMMENT '关键词，多个用英文分号隔开',
    `sdPublisher` varchar(255) NULL DEFAULT NULL COMMENT '发布者姓名',
    `email` varchar(50) NULL DEFAULT NULL COMMENT '发布者邮箱',
    `state` tinyint(1) NOT NULL COMMENT '数据产品状态 0-已删除 1-生成中 2-生成失败 3-待发布 4-待审核 5-已发布 6-拒绝发布 7-已下架',
    `opinion` varchar(255) NULL DEFAULT NULL COMMENT '发布审核意见',
    `down_reason` varchar(255) NULL DEFAULT NULL COMMENT '下架原因',
    `crt_dttm` datetime    NOT NULL COMMENT '创建时间',
    `crt_user` varchar(255) NOT NULL COMMENT '创建人',
    `enable_flag` bit(1) NOT NULL COMMENT '逻辑删除，0-不可用 1-可用',
    `last_update_dttm` datetime NOT NULL COMMENT '最后更新时间',
    `last_update_user` varchar(255) NOT NULL COMMENT '最后更新人',
    `version` bigint(20) NULL DEFAULT NULL COMMENT '版本',
    `bak1` varchar(255) NULL DEFAULT NULL COMMENT '保留字段1',
    `bak2` varchar(255) NULL DEFAULT NULL COMMENT '保留字段2',
    `bak3` varchar(255) NULL DEFAULT NULL COMMENT '保留字段3',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT='数据产品表';

-----------------------------------
--Table structure for product_user
-----------------------------------

CREATE TABLE IF NOT EXISTS `product_user`
(
    `id` bigint(20) NOT NULL COMMENT '主键ID',
    `product_id` bigint(20) NOT NULL COMMENT '数据产品ID',
    `product_name` varchar(255) NULL DEFAULT NULL COMMENT '数据产品名称',
    `user_id` varchar(40) NOT NULL COMMENT '申请用户ID',
    `user_name` varchar(255) NULL DEFAULT NULL COMMENT '申请用户名称',
    `user_email` varchar(50) NULL DEFAULT NULL COMMENT '申请用户邮箱',
    `reason` varchar(255) NULL DEFAULT NULL COMMENT '申请理由',
    `opinion` varchar(255) NULL DEFAULT NULL COMMENT '审核意见',
    `state` tinyint(1) NOT NULL COMMENT '状态 0-失效 1-待审核 2-审核通过 3-审核拒绝',
    `last_update_dttm` datetime NOT NULL COMMENT '最后更新时间',
    `last_update_user` varchar(255) NOT NULL COMMENT '最后更新人',
    `bak1` varchar(255) NULL DEFAULT NULL COMMENT '保留字段1',
    `bak2` varchar(255) NULL DEFAULT NULL COMMENT '保留字段2',
    `bak3` varchar(255) NULL DEFAULT NULL COMMENT '保留字段3',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT='数据产品_用户关联表';

-----------------------------------
--Table structure for file_info
-----------------------------------

CREATE TABLE IF NOT EXISTS `file`
(
    `id` bigint(20) NOT NULL COMMENT '主键ID',
    `file_name` varchar(255) NOT NULL COMMENT '文件名称',
    `file_type` varchar(128) NOT NULL COMMENT '文件类型',
    `file_path` varchar(255) NOT NULL COMMENT '文件存储位置',
    `associate_type` tinyint(1) NOT NULL COMMENT '关联类型 0-数据产品类型  1-数据产品 2-数据产品封面 3-流水线发布参数 4-流水线',
    `associate_id` varchar(50) NOT NULL COMMENT '关联ID',
    `crt_dttm` datetime    NOT NULL COMMENT '创建时间',
    `crt_user` varchar(255) NOT NULL COMMENT '创建人',
    `enable_flag` bit(1) NOT NULL COMMENT '逻辑删除，0-不可用 1-可用',
    `last_update_dttm` datetime NOT NULL COMMENT '最后更新时间',
    `last_update_user` varchar(255) NOT NULL COMMENT '最后更新人',
    `version` bigint(20) NULL DEFAULT NULL COMMENT '版本',
    `bak1` varchar(255) NULL DEFAULT NULL COMMENT '保留字段1',
    `bak2` varchar(255) NULL DEFAULT NULL COMMENT '保留字段2',
    `bak3` varchar(255) NULL DEFAULT NULL COMMENT '保留字段3',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT='文件表';

-----------------------------------
--Table structure for role
-----------------------------------

CREATE TABLE IF NOT EXISTS `role`
(
    `id` bigint(20) NOT NULL COMMENT '主键ID',
    `parent_id` bigint(20) NOT NULL COMMENT '所属父级角色ID',
    `code` varchar(255) NOT NULL COMMENT '角色唯一编码',
    `name` varchar(255) NOT NULL COMMENT '角色名称',
    `description` varchar(255) NOT NULL COMMENT '角色描述',
    `crt_dttm` datetime    NOT NULL COMMENT '创建时间',
    `crt_user` varchar(255) NOT NULL COMMENT '创建人',
    `enable_flag` bit(1) NOT NULL COMMENT '逻辑删除，0-不可用 1-可用',
    `last_update_dttm` datetime NOT NULL COMMENT '最后更新时间',
    `last_update_user` varchar(255) NOT NULL COMMENT '最后更新人',
    `version` bigint(20) NULL DEFAULT NULL COMMENT '版本',
    `bak1` varchar(255) NULL DEFAULT NULL COMMENT '保留字段1',
    `bak2` varchar(255) NULL DEFAULT NULL COMMENT '保留字段2',
    `bak3` varchar(255) NULL DEFAULT NULL COMMENT '保留字段3',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT='角色表';

-----------------------------------
--Table structure for user_role
-----------------------------------

CREATE TABLE IF NOT EXISTS `user_role`
(
    `id` bigint(20) NOT NULL COMMENT '主键ID',
    `user_id` varchar(40) NOT NULL COMMENT '用户ID',
    `username` varchar(255) NOT NULL COMMENT '用户名',
    `role_id` bigint(20) NOT NULL COMMENT '角色ID',
    `role_code` varchar(255) NOT NULL COMMENT '角色唯一编码',
    `crt_dttm` datetime    NOT NULL COMMENT '创建时间',
    `crt_user` varchar(255) NOT NULL COMMENT '创建人',
    `enable_flag` bit(1) NOT NULL COMMENT '逻辑删除，0-不可用 1-可用',
    `last_update_dttm` datetime NOT NULL COMMENT '最后更新时间',
    `last_update_user` varchar(255) NOT NULL COMMENT '最后更新人',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT='用户_角色关联表';

-----------------------------------
--Table structure for role
-----------------------------------

CREATE TABLE IF NOT EXISTS `flow_publishing`
(
    `id` bigint(20) NOT NULL COMMENT '主键ID',
    `name` varchar(255) NOT NULL COMMENT '参数的发布名称',
    `flow_id` varchar(40) NOT NULL COMMENT '关联的流水线ID',
    `product_type_id` bigint(20) NOT NULL COMMENT '所属数据产品类型ID',
    `product_type_name` varchar(255) NOT NULL COMMENT '所属数据产品类型名称',
    `product_type_description` varchar(512) NOT NULL COMMENT '所属数据产品类型简介，不少于50字',
    `description` varchar(512) NOT NULL COMMENT '简介，不少于50字',
    `crt_dttm` datetime    NOT NULL COMMENT '创建时间',
    `crt_user` varchar(255) NOT NULL COMMENT '创建人',
    `enable_flag` bit(1) NOT NULL COMMENT '逻辑删除，0-不可用 1-可用',
    `last_update_dttm` datetime NOT NULL COMMENT '最后更新时间',
    `last_update_user` varchar(255) NOT NULL COMMENT '最后更新人',
    `version` bigint(20) NULL DEFAULT NULL COMMENT '版本',
    `bak1` varchar(255) NULL DEFAULT NULL COMMENT '保留字段1',
    `bak2` varchar(255) NULL DEFAULT NULL COMMENT '保留字段2',
    `bak3` varchar(255) NULL DEFAULT NULL COMMENT '保留字段3',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT='流水线发布表';

-----------------------------------
--Table structure for flow_stops_publishing_property
-----------------------------------

CREATE TABLE IF NOT EXISTS `flow_stops_publishing_property`
(
    `id` bigint(20) NOT NULL COMMENT '主键ID',
    `publishing_id` bigint(20) NOT NULL COMMENT '关联的流水线发布ID',
    `stop_id` varchar(40) NOT NULL COMMENT '关联的组件ID',
    `stop_name` varchar(255) DEFAULT NULL COMMENT '参数名称',
    `stop_bundle` varchar(255) DEFAULT NULL COMMENT '参数bundle',
    `property_id` varchar(40) NOT NULL COMMENT '关联的组件参数ID',
    `property_name` varchar(255) DEFAULT NULL COMMENT '参数名称',
    `name` varchar(255) NOT NULL COMMENT '参数的发布名称',
    `type` tinyint(1) NOT NULL COMMENT '参数的发布类型 0-输入 1-其他 2-输出',
    `allowable_values` varchar(255) NULL DEFAULT NULL COMMENT '关联的组件参数ID',
    `custom_value` varchar(255) NULL DEFAULT NULL COMMENT '参数真实的配置值',
    `description` text NULL DEFAULT NULL COMMENT '参数描述',
    `property_sort` bigint(20) NULL DEFAULT NULL COMMENT '参数顺序',
    `example` varchar(512) NULL DEFAULT NULL COMMENT '参数样例',
    `crt_dttm` datetime    NOT NULL COMMENT '创建时间',
    `crt_user` varchar(255) NOT NULL COMMENT '创建人',
    `enable_flag` bit(1) NOT NULL COMMENT '逻辑删除，0-不可用 1-可用',
    `last_update_dttm` datetime NOT NULL COMMENT '最后更新时间',
    `last_update_user` varchar(255) NOT NULL COMMENT '最后更新人',
    `version` bigint(20) NULL DEFAULT NULL COMMENT '版本',
    `bak1` varchar(255) NOT NULL DEFAULT '1' COMMENT '保留字段1',
    `bak2` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '' COMMENT '保留字段2',
    `bak3` varchar(255) NULL DEFAULT NULL COMMENT '保留字段3',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT='流水线发布组件属性表';

-----------------------------------
--init some data to product type
-----------------------------------

insert into `data_product_type` (`id`, `parent_id`, `level`, `name`, `description`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version` ) values ('1','0', '1', '生态系统要素观测数据产品', '生态系统要素数据产品', NOW(), 'admin', b'1', NOW(), 'admin', 0 );
insert into `data_product_type` (`id`, `parent_id`, `level`, `name`, `description`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version` ) values ('2','0', '1', '生态系统过程与功能数据产品', '生态系统服务功能数据产品', NOW(), 'admin', b'1', NOW(), 'admin', 0 );
insert into `data_product_type` (`id`, `parent_id`, `level`, `name`, `description`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version` ) values ('3','0', '1', '生态系统质量数据产品', '生态系统质量数据产品', NOW(), 'admin', b'1', NOW(), 'admin', 0 );
insert into `data_product_type` (`id`, `parent_id`, `level`, `name`, `description`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version` ) values ('4','1', '2', '大气环境要素观测数据产品', '参照气象行业标准《地面气象观测资料质量控制》，对自动气象观测站观测的逐时地面气象要素（气温、降水量、湿度、风速、大气压等）和辐射要素数据（L0级数据）进行质量控制、数据插补和统计分析等处理，生成日尺度、月尺度和年尺度的大气环境要素数据产品（L1-L3级数据）。', NOW(), 'admin', b'1', NOW(), 'admin', 0 );
insert into `data_product_type` (`id`, `parent_id`, `level`, `name`, `description`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version` ) values ('5','1', '2', '生物要素观测数据产品', '对生物量、植物种类组成、凋落物、物候期等观测数据进行质量控制和数据处理，生成质控后的样方调查数据（L1级数据）和样地尺度的统计数据（L3级数据）。', NOW(), 'admin', b'1', NOW(), 'admin', 0 );
insert into `data_product_type` (`id`, `parent_id`, `level`, `name`, `description`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version` ) values ('6','1', '2', '土壤要素观测数据产品', '土壤湿度、温度、质地、含水量、养分、酸碱度和侵蚀等要素的测量结果和分析信息', NOW(), 'admin', b'1', NOW(), 'admin', 0 );
insert into `data_product_type` (`id`, `parent_id`, `level`, `name`, `description`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version` ) values ('7','1', '2', '水环境要素观测数据产品', '提供水文、水化学等要素的测量结果和分析信息', NOW(), 'admin', b'1', NOW(), 'admin', 0 );
insert into `data_product_type` (`id`, `parent_id`, `level`, `name`, `description`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version` ) values ('8','2', '2', '生产力和固碳功能数据产品', '利用样地尺度的生物量、土壤有机质等统计数据（L3级数据），计算生成植被碳密度、土壤表层碳密度和剖面土壤有机碳密度数据产品（L4级数据）。', NOW(), 'admin', b'1', NOW(), 'admin', 0 );
insert into `data_product_type` (`id`, `parent_id`, `level`, `name`, `description`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version` ) values ('9','2', '2', '水源涵养功能数据产品', '利用样地尺度的土壤含水量统计数据（L3级数据），计算生成0-20cm、0-90cm、剖面分层土壤蓄水量数据产品（L4级数据）。', NOW(), 'admin', b'1', NOW(), 'admin', 0 );
insert into `data_product_type` (`id`, `parent_id`, `level`, `name`, `description`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version` ) values ('10','2', '2', '生物多样性功能数据产品', '利用经过质量控制后的森林乔木每木调查数据（L1级数据），分物种统计样地尺度的物种数据，计算物种丰富度、Shannon-Wiener指数、Simpson指数、Pielou指数（L4级数据）。', NOW(), 'admin', b'1', NOW(), 'admin', 0 );
insert into `data_product_type` (`id`, `parent_id`, `level`, `name`, `description`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version` ) values ('11','2', '2', '土壤保持功能数据产品', '土壤保持功能', NOW(), 'admin', b'1', NOW(), 'admin', 0 );
insert into `data_product_type` (`id`, `parent_id`, `level`, `name`, `description`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version` ) values ('12','2', '2', '防风固沙功能数据产品', '防风固沙功能', NOW(), 'admin', b'1', NOW(), 'admin', 0 );
insert into `data_product_type` (`id`, `parent_id`, `level`, `name`, `description`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version` ) values ('13','4', '3', '地面气象要素数据产品', '气温、最高气温、最低气温、相对湿度、大气压、10min平均风速、降水量', NOW(), 'admin', b'1', NOW(), 'admin', 0 );
insert into `data_product_type` (`id`, `parent_id`, `level`, `name`, `description`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version` ) values ('14','4', '3', '辐射数据产品', '总辐射曝辐量、总辐射辐射度、净辐射曝辐量、净辐射辐射度、光量子通量密度', NOW(), 'admin', b'1', NOW(), 'admin', 0 );
insert into `data_product_type` (`id`, `parent_id`, `level`, `name`, `description`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version` ) values ('15','5', '3', '植物群落种类组成与物质生产数据产品', '生物量、物种组成、凋落物（森林、草地、水体）', NOW(), 'admin', b'1', NOW(), 'admin', 0 );
insert into `data_product_type` (`id`, `parent_id`, `level`, `name`, `description`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version` ) values ('16','6', '3', '土壤化学性质数据产品', '土壤有机质（表面和剖面）', NOW(), 'admin', b'1', NOW(), 'admin', 0 );
insert into `data_product_type` (`id`, `parent_id`, `level`, `name`, `description`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version` ) values ('17','7', '3', '水文要素数据产品', '土壤含水量（TDR、中子仪）', NOW(), 'admin', b'1', NOW(), 'admin', 0 );
insert into `data_product_type` (`id`, `parent_id`, `level`, `name`, `description`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version` ) values ('18','7', '3', '水化学要素数据产品', '水质（待定）', NOW(), 'admin', b'1', NOW(), 'admin', 0 );
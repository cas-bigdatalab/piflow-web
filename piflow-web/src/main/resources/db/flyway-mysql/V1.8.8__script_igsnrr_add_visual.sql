-- ----------------------------
-- Table structure for vis_dataBase_info
-- ----------------------------

CREATE TABLE  IF NOT EXISTS `vis_dataBase_info` (
                                     `id` int NOT NULL AUTO_INCREMENT COMMENT 'id号',
                                     `db_name` varchar(255) NOT NULL COMMENT '数据库名称',
                                     `description` varchar(900) DEFAULT NULL COMMENT '数据库描述',
                                     `driver_class` varchar(255) NOT NULL COMMENT '数据库驱动',
                                     `url` varchar(255) NOT NULL COMMENT '数据库地址',
                                     `user_name` varchar(255) NOT NULL COMMENT '用户名',
                                     `password` varchar(255) NOT NULL COMMENT '密码',
                                     `create_time` varchar(255) NOT NULL COMMENT '创建时间',
                                     `update_time` varchar(255) NOT NULL COMMENT '更新时间',
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='数据库信息';


-- ----------------------------
-- Table structure for vis_excel_name_asso
-- ----------------------------

CREATE TABLE  IF NOT EXISTS `vis_excel_name_asso` (
                                       `id` int NOT NULL AUTO_INCREMENT COMMENT 'id号',
                                       `excel_name` varchar(255) NOT NULL COMMENT 'excel文件名',
                                       `asso_name` varchar(255) NOT NULL COMMENT '关联表名',
                                       `create_time` varchar(255) NOT NULL COMMENT '创建时间',
                                       `update_time` varchar(255) NOT NULL COMMENT '更新时间',
                                       `reserve1` varchar(255) DEFAULT NULL COMMENT '备用字段1',
                                       `reserve2` varchar(255) DEFAULT NULL COMMENT '备用字段1',
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='excel表名关联信息';

-- ----------------------------
-- Table structure for vis_graph_conf
-- ----------------------------

CREATE TABLE  IF NOT EXISTS `vis_graph_conf` (
                                  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id号',
                                  `name` varchar(255) NOT NULL COMMENT '图表配置名称',
                                  `description` varchar(900) DEFAULT NULL COMMENT '图表配置描述',
                                  `graph_template_id` int NOT NULL COMMENT '图表模板id',
                                  `config_info` varchar(3000) NOT NULL COMMENT '具体配置信息',
                                  `create_time` varchar(255) NOT NULL COMMENT '创建时间',
                                  `update_time` varchar(255) NOT NULL COMMENT '更新时间',
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='图表配置表';

-- ----------------------------
-- Table structure for vis_graph_template
-- ----------------------------

CREATE TABLE  IF NOT EXISTS `vis_graph_template` (
                                      `id` int NOT NULL AUTO_INCREMENT COMMENT 'id号',
                                      `data_base_id` int NOT NULL COMMENT '数据库id',
                                      `type` varchar(255) NOT NULL COMMENT '数据类型（mysql、excel）',
                                      `name` varchar(255) NOT NULL COMMENT '模板名称',
                                      `excel_asso_id` int DEFAULT NULL COMMENT 'excel表名关联id',
                                      `table_name` varchar(255) NOT NULL COMMENT '表名称',
                                      `description` varchar(900) DEFAULT NULL COMMENT '模板描述',
                                      `sheet_name` varchar(255) DEFAULT NULL COMMENT 'sheet页名称',
                                      `create_time` varchar(255) NOT NULL COMMENT '创建时间',
                                      `update_time` varchar(255) NOT NULL COMMENT '更新时间',
                                      `reserve1` varchar(255) DEFAULT NULL COMMENT '备用字段1',
                                      `reserve2` varchar(255) DEFAULT NULL COMMENT '备用字段1',
                                      PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='图表模板信息';

-- ----------------------------
-- Table structure for vis_product_template_graph_asso
-- ----------------------------

CREATE TABLE  IF NOT EXISTS `vis_product_template_graph_asso` (
                                                   `id` int NOT NULL AUTO_INCREMENT COMMENT 'id号',
                                                   `product_id` bigint NOT NULL COMMENT '数据产品id',
                                                   `graph_template_id` int NOT NULL COMMENT 'vis_graph_templete表id',
                                                   `graph_conf_id` int NOT NULL COMMENT 'vis_graph_conf表id',
                                                   `owner` varchar(255) NOT NULL COMMENT '所有者，哪个用户创建的',
                                                   `type` int NOT NULL COMMENT '数据源类型，0-excel, 1-MySQL',
                                                   `path` varchar(255) NOT NULL COMMENT 'excel路径+文件名，MySQL类型为空',
                                                   `create_time` varchar(255) NOT NULL COMMENT '创建时间',
                                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='数据产品关联表';
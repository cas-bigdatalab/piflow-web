
DROP TABLE IF EXISTS `sys_menu`;
DROP TABLE IF EXISTS `schedule`;
DROP TABLE IF EXISTS `data_source_property`;
DROP TABLE IF EXISTS `data_source`;
DROP TABLE IF EXISTS `process_stops_customized_property`;
DROP TABLE IF EXISTS `flow_stops_customized_property`;
DROP TABLE IF EXISTS `flow_process_group_path`;
DROP TABLE IF EXISTS `flow_process_group`;
DROP TABLE IF EXISTS `flow_group_path`;
DROP TABLE IF EXISTS `flow_group`;
DROP TABLE IF EXISTS `flow_project`;
DROP TABLE IF EXISTS `flow_group_template`;
-- ----------------------------
-- Table structure for menu
-- ----------------------------
CREATE TABLE `sys_menu`  (
  `id` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `crt_dttm` datetime NOT NULL,
  `crt_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `enable_flag` bit(1) NOT NULL,
  `last_update_dttm` datetime NOT NULL,
  `last_update_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `version` bigint(20) NULL DEFAULT NULL,
  `menu_description` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'description',
  `menu_jurisdiction` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'task status',
  `menu_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'menu name',
  `menu_parent` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'task status',
  `menu_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'menu url',
  `menu_sort` int(11) NULL DEFAULT NULL COMMENT 'menu sort',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for schedule
-- ----------------------------
CREATE TABLE `schedule`  (
  `id` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `crt_dttm` datetime NOT NULL,
  `crt_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `enable_flag` bit(1) NOT NULL,
  `last_update_dttm` datetime NOT NULL,
  `last_update_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `version` bigint(20) NULL DEFAULT NULL,
  `class_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `cron` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `description` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'description',
  `method_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `task_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `task_state` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'task status',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for data_source
-- ----------------------------
CREATE TABLE `data_source`  (
  `id` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `crt_dttm` datetime NOT NULL,
  `crt_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `enable_flag` bit(1) NOT NULL,
  `last_update_dttm` datetime NOT NULL,
  `last_update_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `version` bigint(20) NULL DEFAULT NULL,
  `data_source_description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'dataSourceDescription',
  `data_source_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'dataSourceName',
  `data_source_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'dataSourceType',
  `is_template` bit(1) NULL DEFAULT NULL COMMENT 'isTemplate',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for data_source_property
-- ----------------------------
CREATE TABLE `data_source_property`  (
  `id` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `crt_dttm` datetime NOT NULL,
  `crt_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `enable_flag` bit(1) NOT NULL,
  `last_update_dttm` datetime NOT NULL,
  `last_update_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `version` bigint(20) NULL DEFAULT NULL,
  `description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'description',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'name',
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'value',
  `fk_data_source_id` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKmu1sbq6pael97442xi5bwdmu0`(`fk_data_source_id`) USING BTREE,
  CONSTRAINT `FKmu1sbq6pael97442xi5bwdmu0` FOREIGN KEY (`fk_data_source_id`) REFERENCES `data_source` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for flow_project
-- ----------------------------
CREATE TABLE `flow_project` (
  `id` varchar(40) NOT NULL,
  `crt_dttm` datetime NOT NULL,
  `crt_user` varchar(255) NOT NULL,
  `enable_flag` bit(1) NOT NULL,
  `last_update_dttm` datetime NOT NULL,
  `last_update_user` varchar(255) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `description` text COMMENT 'description',
  `is_example` bit(1) DEFAULT NULL COMMENT 'isExample',
  `name` varchar(255) DEFAULT NULL COMMENT 'flow name',
  `uuid` varchar(255) DEFAULT NULL COMMENT 'flow uuid',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for flow_group
-- ----------------------------
CREATE TABLE `flow_group` (
  `id` varchar(40) NOT NULL,
  `crt_dttm` datetime NOT NULL,
  `crt_user` varchar(255) NOT NULL,
  `enable_flag` bit(1) NOT NULL,
  `last_update_dttm` datetime NOT NULL,
  `last_update_user` varchar(255) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `description` text COMMENT 'description',
  `is_example` bit(1) DEFAULT NULL COMMENT 'isExample',
  `name` varchar(255) DEFAULT NULL COMMENT 'flow name',
  `uuid` varchar(255) DEFAULT NULL COMMENT 'flow uuid',
  `fk_flow_project_id` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKd0sfu1rtmctw8qess69c1qhaw` (`fk_flow_project_id`),
  CONSTRAINT `FKd0sfu1rtmctw8qess69c1qhaw` FOREIGN KEY (`fk_flow_project_id`) REFERENCES `flow_project` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for flow_group_path
-- ----------------------------
CREATE TABLE `flow_group_path` (
  `id` varchar(40) NOT NULL,
  `crt_dttm` datetime NOT NULL,
  `crt_user` varchar(255) NOT NULL,
  `enable_flag` bit(1) NOT NULL,
  `last_update_dttm` datetime NOT NULL,
  `last_update_user` varchar(255) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `filter_condition` varchar(255) DEFAULT NULL,
  `line_from` varchar(255) DEFAULT NULL COMMENT 'line from',
  `line_inport` varchar(255) DEFAULT NULL COMMENT 'line in port',
  `line_outport` varchar(255) DEFAULT NULL COMMENT 'line out port',
  `page_id` varchar(255) DEFAULT NULL,
  `line_to` varchar(255) DEFAULT NULL COMMENT 'line to',
  `fk_flow_group_id` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKluqls1o7ynyiinor8ttdc6wdd` (`fk_flow_group_id`),
  CONSTRAINT `FKluqls1o7ynyiinor8ttdc6wdd` FOREIGN KEY (`fk_flow_group_id`) REFERENCES `flow_group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for flow_process_group
-- ----------------------------
CREATE TABLE `flow_process_group` (
  `id` varchar(40) NOT NULL,
  `crt_dttm` datetime NOT NULL,
  `crt_user` varchar(255) NOT NULL,
  `enable_flag` bit(1) NOT NULL,
  `last_update_dttm` datetime NOT NULL,
  `last_update_user` varchar(255) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `app_id` varchar(255) DEFAULT NULL COMMENT 'The id returned when calling runProcess',
  `description` varchar(1024) DEFAULT NULL COMMENT 'description',
  `end_time` datetime DEFAULT NULL COMMENT 'End time of the process',
  `flow_id` varchar(255) DEFAULT NULL COMMENT 'flowId',
  `name` varchar(255) DEFAULT NULL COMMENT 'Process name',
  `parent_process_id` varchar(255) DEFAULT NULL COMMENT 'third parentProcessId',
  `process_id` varchar(255) DEFAULT NULL COMMENT 'third processId',
  `progress` varchar(255) DEFAULT NULL COMMENT 'Process progress',
  `run_mode_type` varchar(255) DEFAULT NULL COMMENT 'Process RunModeType',
  `start_time` datetime DEFAULT NULL COMMENT 'Process startup time',
  `state` varchar(255) DEFAULT NULL COMMENT 'Process status',
  `view_xml` text COMMENT 'Process view xml string',
  `process_parent_type` varchar(255) DEFAULT NULL COMMENT 'Process parent type',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for flow_process_group_path
-- ----------------------------
CREATE TABLE `flow_process_group_path` (
  `id` varchar(40) NOT NULL,
  `crt_dttm` datetime NOT NULL,
  `crt_user` varchar(255) NOT NULL,
  `enable_flag` bit(1) NOT NULL,
  `last_update_dttm` datetime NOT NULL,
  `last_update_user` varchar(255) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `line_from` varchar(255) DEFAULT NULL,
  `line_inport` varchar(255) DEFAULT NULL,
  `line_outport` varchar(255) DEFAULT NULL,
  `page_id` varchar(255) DEFAULT NULL,
  `line_to` varchar(255) DEFAULT NULL,
  `fk_flow_process_group_id` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKirc18h7dj5ti11wlnifjwiyyh` (`fk_flow_process_group_id`),
  CONSTRAINT `FKirc18h7dj5ti11wlnifjwiyyh` FOREIGN KEY (`fk_flow_process_group_id`) REFERENCES `flow_process_group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for flow_stops_customized_property
-- ----------------------------
CREATE TABLE `flow_stops_customized_property` (
  `id` varchar(40) NOT NULL,
  `crt_dttm` datetime NOT NULL,
  `crt_user` varchar(255) NOT NULL,
  `enable_flag` bit(1) NOT NULL,
  `last_update_dttm` datetime NOT NULL,
  `last_update_user` varchar(255) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `custom_value` text COMMENT 'custom value',
  `description` text COMMENT 'description',
  `name` varchar(255) DEFAULT NULL COMMENT 'name',
  `fk_stops_id` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK92yilfng8y5k7psuevts911c7` (`fk_stops_id`),
  CONSTRAINT `FK92yilfng8y5k7psuevts911c7` FOREIGN KEY (`fk_stops_id`) REFERENCES `flow_stops` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for process_stops_customized_property
-- ----------------------------
CREATE TABLE `process_stops_customized_property` (
  `id` varchar(40) NOT NULL,
  `crt_dttm` datetime NOT NULL,
  `crt_user` varchar(255) NOT NULL,
  `enable_flag` bit(1) NOT NULL,
  `last_update_dttm` datetime NOT NULL,
  `last_update_user` varchar(255) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `custom_value` text COMMENT 'custom value',
  `description` text COMMENT 'description',
  `name` varchar(255) DEFAULT NULL COMMENT 'name',
  `fk_flow_process_stop_id` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1ql9h2ueevqxg1xjnt06repqv` (`fk_flow_process_stop_id`),
  CONSTRAINT `FK1ql9h2ueevqxg1xjnt06repqv` FOREIGN KEY (`fk_flow_process_stop_id`) REFERENCES `flow_process_stop` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for flow_group_template
-- ----------------------------
CREATE TABLE `flow_group_template` (
   `id` varchar(40) NOT NULL,
   `crt_dttm` datetime NOT NULL,
   `crt_user` varchar(255) NOT NULL,
   `enable_flag` bit(1) NOT NULL,
   `last_update_dttm` datetime NOT NULL,
   `last_update_user` varchar(255) NOT NULL,
   `version` bigint(20) DEFAULT NULL,
   `description` varchar(1024) DEFAULT NULL COMMENT 'description',
   `flow_group_name` varchar(255) DEFAULT NULL,
   `name` varchar(255) DEFAULT NULL,
   `path` varchar(255) DEFAULT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `sys_schedule` (
   `id` varchar(40) NOT NULL,
   `crt_dttm` datetime NOT NULL,
   `crt_user` varchar(255) NOT NULL,
   `enable_flag` bit(1) NOT NULL,
   `last_update_dttm` datetime NOT NULL,
   `last_update_user` varchar(255) NOT NULL,
   `version` bigint(20) DEFAULT NULL,
   `cron_expression` varchar(255) DEFAULT NULL COMMENT 'cron',
   `job_class` varchar(255) DEFAULT NULL COMMENT 'job class',
   `job_name` varchar(255) DEFAULT NULL COMMENT 'job name',
   `status` varchar(255) DEFAULT NULL COMMENT 'task status',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- add table column
ALTER TABLE flow_group ADD COLUMN page_id varchar(255);
ALTER TABLE flow ADD COLUMN page_id varchar(255);
ALTER TABLE flow ADD COLUMN fk_flow_group_id varchar(40);
ALTER TABLE flow ADD COLUMN fk_flow_project_id varchar(40);
ALTER TABLE flow_path ADD COLUMN filter_condition varchar(255);
ALTER TABLE flow_stops ADD COLUMN is_customized bit;
ALTER TABLE flow_stops ADD COLUMN fk_data_source_id varchar(40);
alter table flow_stops_property add column is_locked bit;
ALTER TABLE flow_stops_property ADD COLUMN property_sort bigint(20) COMMENT 'property sort';
ALTER TABLE flow_stops_template ADD COLUMN is_customized bit;
ALTER TABLE flow_stops_property_template ADD COLUMN property_sort bigint(20) COMMENT 'property sort';
ALTER TABLE flow_process ADD COLUMN page_id varchar(255);
ALTER TABLE flow_process ADD COLUMN fk_flow_process_group_id varchar(40);
ALTER TABLE flow_process ADD COLUMN run_mode_type varchar(255) COMMENT 'Process RunModeType';
ALTER TABLE flow_process ADD COLUMN process_parent_type varchar(255) COMMENT 'Process parent type';
ALTER TABLE mx_graph_model ADD COLUMN fk_flow_group_id varchar(40);
ALTER TABLE mx_graph_model ADD COLUMN fk_flow_project_id varchar(40);

-- add foreign key
ALTER TABLE flow ADD CONSTRAINT FK3e62cxjsjbtola3f8hcp1my1o foreign key (fk_flow_group_id) references flow_group (id);
ALTER TABLE flow ADD CONSTRAINT FKg80q2a1d63pkfbayl55cuydcj foreign key (fk_flow_project_id) references flow_project (id);
ALTER TABLE flow_stops ADD CONSTRAINT FKr5de1px70o0uj1hlob7ilc90c foreign key (fk_data_source_id) references data_source (id);
ALTER TABLE flow_process ADD CONSTRAINT FKgvr5o5p4n6dbfx010h7wbjgku foreign key (fk_flow_process_group_id) references flow_process_group (id);
ALTER TABLE mx_graph_model ADD CONSTRAINT FKbwxper47v5e1ii4wjrcrhi63e foreign key (fk_flow_group_id) references flow_group (id);
ALTER TABLE mx_graph_model ADD CONSTRAINT FK37dhc2jlml6pyvv1srdihtiky foreign key (fk_flow_project_id) references flow_project (id);

-- update table column
ALTER TABLE flow_stops_property_template MODIFY COLUMN allowable_values text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL;
ALTER TABLE flow_stops_property_template MODIFY COLUMN default_value text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL;
ALTER TABLE flow_stops_property_template MODIFY COLUMN description text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT 'Defaults';
ALTER TABLE flow_stops_property_template MODIFY COLUMN display_name text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT 'description';


-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be5bmenu00001', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'Flows', 'USER', 'Flow', 'Flow', '/piflow-web/web/flowList', 100001);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be5bmenu00002', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'Processes', 'USER', 'Process', 'Flow', '/piflow-web/web/processList', 100002);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be5bmenu00003', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'Templates', 'USER', 'Template', 'Flow', '/piflow-web/web/template', 100003);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be5bmenu00004', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'FlowGroup', 'USER', 'FlowGroup', 'Group', '/piflow-web/web/flowGroupList', 200001);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be5bmenu00005', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'FlowGroupProcess', 'USER', 'FlowGroupProcess', 'Group', '/piflow-web/web/processGroupList', 200002);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be5bmenu00006', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'FlowProcess', 'USER', 'FlowProcess', 'Group', '/piflow-web/web/groupTypeProcessList', 200003);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be5bmenu00007', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'FlowGroupTemplate', 'USER', 'FlowGroupTemplate', 'Group', '/piflow-web/web/flowGroupTemplateList', 200004);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be5bmenu00008', '2019-08-15 10:23:20', 'system', b'0', '2019-08-15 10:23:36', 'system', 0, 'Project', 'USER', 'Project', 'Project', '/piflow-web/web/instructionalVideo', 300001);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be5bmenu00009', '2019-08-15 10:23:20', 'system', b'0', '2019-08-15 10:23:36', 'system', 0, 'ProjectProcess', 'USER', 'ProjectProcess', 'Project', '/piflow-web/web/instructionalVideo', 300002);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be5bmenu00010', '2019-08-15 10:23:20', 'system', b'0', '2019-08-15 10:23:36', 'system', 0, 'GroupProcess', 'USER', 'GroupProcess', 'Project', '/piflow-web/web/instructionalVideo', 300003);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be5bmenu00011', '2019-08-15 10:23:20', 'system', b'0', '2019-08-15 10:23:36', 'system', 0, 'FlowProcess', 'USER', 'FlowProcess', 'Project', '/piflow-web/web/instructionalVideo', 300004);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be5bmenu00012', '2019-08-15 10:23:20', 'system', b'0', '2019-08-15 10:23:36', 'system', 0, 'ProjectTemplate', 'USER', 'ProjectTemplate', 'Project', '/piflow-web/web/instructionalVideo', 300005);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be5bmenu00013', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'DataSource', 'USER', 'DataSource', '', '/piflow-web/web/dataSources', 400001);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be5bmenu00014', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'Instruction', 'USER', 'Instruction', 'Example', '/piflow-web/web/instructionalVideo', 500001);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be5bmenu00015', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'Example1', 'USER', 'Example1', 'Example', '/piflow-web/grapheditor/home?load=0c4fdee973824a999e1569770677c020', 500002);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be5bmenu00016', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'Example2', 'USER', 'Example2', 'Example', '/piflow-web/grapheditor/home?load=c9c77d24b65942fb9665fbdbe8710236', 500003);
-- INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be5bmenu00017', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'Test1', 'ADMIN', 'Test1', 'Admin', '/piflow-web/web/instructionalVideo', 900001);
-- INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be5bmenu00018', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'Test2', 'ADMIN', 'Test2', 'Admin', '/piflow-web/web/instructionalVideo', 900002);
-- ----------------------------
-- Records of data_source
-- ----------------------------
INSERT INTO `data_source`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `data_source_description`, `data_source_name`, `data_source_type`, `is_template`) VALUES ('05ce8a4f0ef942098b5bd5610ff8f181', '2019-11-21 00:00:01', 'system', b'1', '2019-11-21 00:00:01', 'Admin', 0, NULL, 'FTP', 'FTP', b'1');
INSERT INTO `data_source`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `data_source_description`, `data_source_name`, `data_source_type`, `is_template`) VALUES ('342a4d45f8f2468194d88ee50ae4cab9', '2019-11-21 00:00:02', 'system', b'1', '2019-11-21 00:00:02', 'Admin', 0, NULL, 'Redis', 'Redis', b'1');
INSERT INTO `data_source`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `data_source_description`, `data_source_name`, `data_source_type`, `is_template`) VALUES ('4776ca565a6542259c17962869538f0c', '2019-11-21 00:00:03', 'system', b'1', '2019-11-21 00:00:03', 'Admin', 0, NULL, 'MongoDB', 'MongoDB', b'1');
INSERT INTO `data_source`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `data_source_description`, `data_source_name`, `data_source_type`, `is_template`) VALUES ('49aeba77472f43bba6245f22723619bf', '2019-11-21 00:00:04', 'system', b'1', '2019-11-21 00:00:04', 'Admin', 0, NULL, 'ElasticSearch', 'ElasticSearch', b'1');
INSERT INTO `data_source`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `data_source_description`, `data_source_name`, `data_source_type`, `is_template`) VALUES ('abe3113ce02d422aba70e9588ebdfff3', '2019-11-21 00:00:05', 'system', b'1', '2019-11-21 00:00:05', 'Admin', 0, NULL, 'JDBC', 'JDBC', b'1');
-- ----------------------------
-- Records of data_source_property
-- ----------------------------
INSERT INTO `data_source_property`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `description`, `name`, `value`, `fk_data_source_id`) VALUES ('2f2e9d19240a458791282c6eee633f62', '2019-11-21 00:00:01', 'Admin', b'1', '2019-11-21 00:00:01', 'system', 0, NULL, 'url', NULL, '05ce8a4f0ef942098b5bd5610ff8f181');
INSERT INTO `data_source_property`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `description`, `name`, `value`, `fk_data_source_id`) VALUES ('45b3fd29b8fd45be8c87498c1d764406', '2019-11-21 00:00:02', 'Admin', b'1', '2019-11-21 00:00:02', 'system', 0, NULL, 'port', NULL, '05ce8a4f0ef942098b5bd5610ff8f181');
INSERT INTO `data_source_property`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `description`, `name`, `value`, `fk_data_source_id`) VALUES ('363fa8f831d5410db31e1c15bf74b229', '2019-11-21 00:00:03', 'Admin', b'1', '2019-11-21 00:00:03', 'system', 0, NULL, 'username', NULL, '05ce8a4f0ef942098b5bd5610ff8f181');
INSERT INTO `data_source_property`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `description`, `name`, `value`, `fk_data_source_id`) VALUES ('c72ecaebaaad4ef08a5279f6713f107f', '2019-11-21 00:00:04', 'Admin', b'1', '2019-11-21 00:00:04', 'system', 0, NULL, 'password', NULL, '05ce8a4f0ef942098b5bd5610ff8f181');
INSERT INTO `data_source_property`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `description`, `name`, `value`, `fk_data_source_id`) VALUES ('18eff61306a349718dce9c0c6988c8b8', '2019-11-21 00:00:05', 'Admin', b'1', '2019-11-21 00:00:05', 'system', 0, NULL, 'host', NULL, '342a4d45f8f2468194d88ee50ae4cab9');
INSERT INTO `data_source_property`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `description`, `name`, `value`, `fk_data_source_id`) VALUES ('8516d28f43884252886e908126778ef0', '2019-11-21 00:00:06', 'Admin', b'1', '2019-11-21 00:00:06', 'system', 0, NULL, 'port', NULL, '342a4d45f8f2468194d88ee50ae4cab9');
INSERT INTO `data_source_property`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `description`, `name`, `value`, `fk_data_source_id`) VALUES ('5b88aff65a674814a983b955df2404bf', '2019-11-21 00:00:07', 'Admin', b'1', '2019-11-21 00:00:07', 'system', 0, NULL, 'password', NULL, '342a4d45f8f2468194d88ee50ae4cab9');
INSERT INTO `data_source_property`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `description`, `name`, `value`, `fk_data_source_id`) VALUES ('d28fe75ea4464261b15b9294b82bcb52', '2019-11-21 00:00:08', 'Admin', b'1', '2019-11-21 00:00:08', 'system', 0, NULL, 'address', NULL, '4776ca565a6542259c17962869538f0c');
INSERT INTO `data_source_property`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `description`, `name`, `value`, `fk_data_source_id`) VALUES ('e68116bc9c6c4ebcbfa7e8fb322349f7', '2019-11-21 00:00:09', 'Admin', b'1', '2019-11-21 00:00:09', 'system', 0, NULL, 'database', NULL, '4776ca565a6542259c17962869538f0c');
INSERT INTO `data_source_property`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `description`, `name`, `value`, `fk_data_source_id`) VALUES ('5f297c6968ca4ba884479b1a43b82198', '2019-11-21 00:00:10', 'Admin', b'1', '2019-11-21 00:00:10', 'system', 0, NULL, 'es_nodes', NULL, '49aeba77472f43bba6245f22723619bf');
INSERT INTO `data_source_property`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `description`, `name`, `value`, `fk_data_source_id`) VALUES ('8d1b0acee4be46788ca552b2040b90fc', '2019-11-21 00:00:11', 'Admin', b'1', '2019-11-21 00:00:11', 'system', 0, NULL, 'es_type', NULL, '49aeba77472f43bba6245f22723619bf');
INSERT INTO `data_source_property`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `description`, `name`, `value`, `fk_data_source_id`) VALUES ('968c061007fa4216b9a0b05d8d68bf2c', '2019-11-21 00:00:12', 'Admin', b'1', '2019-11-21 00:00:12', 'system', 0, NULL, 'es_port', NULL, '49aeba77472f43bba6245f22723619bf');
INSERT INTO `data_source_property`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `description`, `name`, `value`, `fk_data_source_id`) VALUES ('f386627bb0734ef9a59c0316d4bd7ab7', '2019-11-21 00:00:13', 'Admin', b'1', '2019-11-21 00:00:13', 'system', 0, NULL, 'es_index', NULL, '49aeba77472f43bba6245f22723619bf');
INSERT INTO `data_source_property`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `description`, `name`, `value`, `fk_data_source_id`) VALUES ('2cf157b6cb05464794cf40d3d9156409', '2019-11-21 00:00:14', 'Admin', b'1', '2019-11-21 00:00:14', 'system', 0, NULL, 'url', NULL, 'abe3113ce02d422aba70e9588ebdfff3');
INSERT INTO `data_source_property`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `description`, `name`, `value`, `fk_data_source_id`) VALUES ('b6463d34ad234a38badfa82d324dcc78', '2019-11-21 00:00:15', 'Admin', b'1', '2019-11-21 00:00:15', 'system', 0, NULL, 'driver', NULL, 'abe3113ce02d422aba70e9588ebdfff3');
INSERT INTO `data_source_property`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `description`, `name`, `value`, `fk_data_source_id`) VALUES ('947c4d47518c4863b8651cf3d604f4f1', '2019-11-21 00:00:16', 'Admin', b'1', '2019-11-21 00:00:16', 'system', 0, NULL, 'user', NULL, 'abe3113ce02d422aba70e9588ebdfff3');
INSERT INTO `data_source_property`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `description`, `name`, `value`, `fk_data_source_id`) VALUES ('e85bc1643a5e429da693caf461e8f5cd', '2019-11-21 00:00:17', 'Admin', b'1', '2019-11-21 00:00:17', 'system', 0, NULL, 'password', NULL, 'abe3113ce02d422aba70e9588ebdfff3');
-- ----------------------------
-- Records of sys_schedule
-- ----------------------------
INSERT INTO `sys_schedule`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `cron_expression`, `job_class`, `job_name`, `status`) VALUES ('ff8081816eaa8a5d016eaa8a77e40000', '2019-11-27 09:47:12', 'system', b'1', '2019-11-27 09:47:12', 'system', 0, '0/5 * * * * ?', 'com.nature.schedule.RunningProcessSync', 'RunningProcessSync', 'RUNNING');
INSERT INTO `sys_schedule`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `cron_expression`, `job_class`, `job_name`, `status`) VALUES ('ff8081816eaa9317016eaa932dd50000', '2019-11-27 09:56:43', 'system', b'1', '2019-11-27 09:56:43', 'system', 0, '0/5 * * * * ?', 'com.nature.schedule.RunningProcessGroupSync', 'RunningProcessGroupSync', 'RUNNING');
-- ----------------------------
-- update Example1
-- ----------------------------
update `flow_stops_property` SET custom_value = replace(custom_value,'10.0.86.89','10.0.86.191') WHERE id in('1a98eb8872864f29862807e87af14aa9','ded789f2a7c94fb7b90f9e9762cdc211','e35b30dd1aba478d84cb26f3da3a8476','fb679cb8ccd149018a50c7d9a2986d02');
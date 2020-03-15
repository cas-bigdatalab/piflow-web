-- ----------------------------
-- Table structure for menu
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sys_menu`  (
    `id` VARCHAR(40) PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    `crt_dttm` DATETIME NOT NULL COMMENT 'Create date time',
     `crt_user` VARCHAR(255) NOT NULL COMMENT 'Create user',
    `enable_flag` BIT NOT NULL COMMENT 'Enable flag',
    `last_update_dttm` DATETIME NOT NULL COMMENT 'Last update date time',
    `last_update_user` VARCHAR(255) NOT NULL COMMENT 'Last update user',
    `version` BIGINT COMMENT 'Version',
    `menu_description` VARCHAR(1024) COMMENT 'Menu description',
    `menu_jurisdiction` VARCHAR(255) COMMENT 'Menu jurisdiction',
    `menu_name` VARCHAR(255) COMMENT 'Menu name',
    `menu_parent` VARCHAR(255) COMMENT 'Menu parent',
    `menu_url` VARCHAR(255) COMMENT 'Menu url',
    `menu_sort` INTEGER COMMENT 'Menu sort'
);

-- ----------------------------
-- Table structure for data_source
-- ----------------------------
CREATE TABLE IF NOT EXISTS `data_source`  (
    `id` VARCHAR(40) PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    `crt_dttm` DATETIME NOT NULL COMMENT 'Create date time',
     `crt_user` VARCHAR(255) NOT NULL COMMENT 'Create user',
    `enable_flag` BIT NOT NULL COMMENT 'Enable flag',
    `last_update_dttm` DATETIME NOT NULL COMMENT 'Last update date time',
    `last_update_user` VARCHAR(255) NOT NULL COMMENT 'Last update user',
    `version` BIGINT COMMENT 'Version',
    `data_source_description` TEXT(0) NULL COMMENT 'dataSourceDescription',
    `data_source_name` VARCHAR(255) COMMENT 'dataSourceName',
    `data_source_type` VARCHAR(255) COMMENT 'dataSourceType',
    `is_template` BIT COMMENT 'isTemplate'
);

-- ----------------------------
-- Table structure for data_source_property
-- ----------------------------
CREATE TABLE IF NOT EXISTS `data_source_property`  (
    `id` VARCHAR(40) PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    `crt_dttm` DATETIME NOT NULL COMMENT 'Create date time',
     `crt_user` VARCHAR(255) NOT NULL COMMENT 'Create user',
    `enable_flag` BIT NOT NULL COMMENT 'Enable flag',
    `last_update_dttm` DATETIME NOT NULL COMMENT 'Last update date time',
    `last_update_user` VARCHAR(255) NOT NULL COMMENT 'Last update user',
    `version` BIGINT COMMENT 'Version',
    `DESCRIPTION` TEXT(0) NULL COMMENT 'description',
    `NAME` VARCHAR(255) COMMENT 'name',
    `VALUE` VARCHAR(255) COMMENT 'value',
    `fk_data_source_id` VARCHAR(40) COMMENT 'Foreign key fk_data_source_id'
);
ALTER TABLE `data_source_property` ADD CONSTRAINT `FKmu1sbq6pael97442xi5bwdmu0` FOREIGN KEY (`fk_data_source_id`) REFERENCES `data_source` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- ----------------------------
-- Table structure for flow_group
-- ----------------------------
CREATE TABLE IF NOT EXISTS `flow_group` (
    `id` VARCHAR(40) PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    `crt_dttm` DATETIME NOT NULL COMMENT 'Create date time',
     `crt_user` VARCHAR(255) NOT NULL COMMENT 'Create user',
    `enable_flag` BIT NOT NULL COMMENT 'Enable flag',
    `last_update_dttm` DATETIME NOT NULL COMMENT 'Last update date time',
    `last_update_user` VARCHAR(255) NOT NULL COMMENT 'Last update user',
    `version` BIGINT COMMENT 'Version',
    `description` TEXT(0) COMMENT 'description',
    `is_example` BIT COMMENT 'isExample',
    `name` VARCHAR(255) COMMENT 'flow name',
    `uuid` VARCHAR(255) COMMENT 'flow uuid'
);

-- ----------------------------
-- Table structure for flow_group_path
-- ----------------------------
CREATE TABLE IF NOT EXISTS `flow_group_path` (
    `id` VARCHAR(40) PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    `crt_dttm` DATETIME NOT NULL COMMENT 'Create date time',
     `crt_user` VARCHAR(255) NOT NULL COMMENT 'Create user',
    `enable_flag` BIT NOT NULL COMMENT 'Enable flag',
    `last_update_dttm` DATETIME NOT NULL COMMENT 'Last update date time',
    `last_update_user` VARCHAR(255) NOT NULL COMMENT 'Last update user',
    `version` BIGINT COMMENT 'Version',
    `filter_condition` VARCHAR(255) COMMENT 'filter_condition',
    `line_from` VARCHAR(255) COMMENT 'line from',
    `line_inport` VARCHAR(255) COMMENT 'line in port',
    `line_outport` VARCHAR(255) COMMENT 'line out port',
    `page_id` VARCHAR(255) DEFAULT NULL COMMENT 'page_id',
    `line_to` VARCHAR(255) COMMENT 'line to',
    `fk_flow_group_id` VARCHAR(40) COMMENT 'Foreign key fk_flow_group_id'
);
ALTER TABLE `flow_group_path` ADD CONSTRAINT `FKluqls1o7ynyiinor8ttdc6wdd` FOREIGN KEY (`fk_flow_group_id`) REFERENCES `flow_group` (`id`);

-- ----------------------------
-- Table structure for flow_process_group
-- ----------------------------
CREATE TABLE IF NOT EXISTS `flow_process_group` (
    `id` VARCHAR(40) PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    `crt_dttm` DATETIME NOT NULL COMMENT 'Create date time',
     `crt_user` VARCHAR(255) NOT NULL COMMENT 'Create user',
    `enable_flag` BIT NOT NULL COMMENT 'Enable flag',
    `last_update_dttm` DATETIME NOT NULL COMMENT 'Last update date time',
    `last_update_user` VARCHAR(255) NOT NULL COMMENT 'Last update user',
    `version` BIGINT COMMENT 'Version',
    `app_id` VARCHAR(255) COMMENT 'The id returned when calling runProcess',
    `description` VARCHAR(1024) COMMENT 'description',
    `end_time` DATETIME COMMENT 'End time of the process',
    `flow_id` VARCHAR(255) COMMENT 'flowId',
    `name` VARCHAR(255) COMMENT 'Process name',
    `parent_process_id` VARCHAR(255) COMMENT 'Third parentProcessId',
    `process_id` VARCHAR(255) COMMENT 'Third processId',
    `progress` VARCHAR(255) COMMENT 'Process progress',
    `run_mode_type` VARCHAR(255) COMMENT 'Process RunModeType',
    `start_time` datetime COMMENT 'Process startup time',
    `state` VARCHAR(255) COMMENT 'Process status',
    `view_xml` TEXT(0) COMMENT 'Process view xml string',
    `process_parent_type` VARCHAR(255) COMMENT 'Process parent type'
);

-- ----------------------------
-- Table structure for flow_process_group_path
-- ----------------------------
CREATE TABLE IF NOT EXISTS `flow_process_group_path` (
    `id` VARCHAR(40) PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    `crt_dttm` DATETIME NOT NULL COMMENT 'Create date time',
     `crt_user` VARCHAR(255) NOT NULL COMMENT 'Create user',
    `enable_flag` BIT NOT NULL COMMENT 'Enable flag',
    `last_update_dttm` DATETIME NOT NULL COMMENT 'Last update date time',
    `last_update_user` VARCHAR(255) NOT NULL COMMENT 'Last update user',
    `version` BIGINT COMMENT 'Version',
    `line_from` VARCHAR(255) DEFAULT NULL COMMENT 'line_from',
    `line_inport` VARCHAR(255) DEFAULT NULL COMMENT 'line_inport',
    `line_outport` VARCHAR(255) DEFAULT NULL COMMENT 'line_outport',
    `page_id` VARCHAR(255) DEFAULT NULL COMMENT 'page_id',
    `line_to` VARCHAR(255) DEFAULT NULL COMMENT 'line_to',
    `fk_flow_process_group_id` VARCHAR(40) DEFAULT NULL COMMENT 'Foreign key fk_flow_process_group_id'
);
ALTER TABLE `flow_process_group_path` ADD CONSTRAINT `FKirc18h7dj5ti11wlnifjwiyyh` FOREIGN KEY (`fk_flow_process_group_id`) REFERENCES `flow_process_group` (`id`);

-- ----------------------------
-- Table structure for flow_stops_customized_property
-- ----------------------------
CREATE TABLE IF NOT EXISTS `flow_stops_customized_property` (
    `id` VARCHAR(40) PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    `crt_dttm` DATETIME NOT NULL COMMENT 'Create date time',
     `crt_user` VARCHAR(255) NOT NULL COMMENT 'Create user',
    `enable_flag` BIT NOT NULL COMMENT 'Enable flag',
    `last_update_dttm` DATETIME NOT NULL COMMENT 'Last update date time',
    `last_update_user` VARCHAR(255) NOT NULL COMMENT 'Last update user',
    `version` BIGINT COMMENT 'Version',
    `custom_value` TEXT(0) COMMENT 'custom value',
    `description` TEXT(0) COMMENT 'description',
    `name` VARCHAR(255) COMMENT 'name',
    `fk_stops_id` VARCHAR(40) DEFAULT NULL COMMENT 'Foreign key fk_stops_id'
);
ALTER TABLE `flow_stops_customized_property` ADD CONSTRAINT `FK92yilfng8y5k7psuevts911c7` FOREIGN KEY (`fk_stops_id`) REFERENCES `flow_stops` (`id`);

-- ----------------------------
-- Table structure for process_stops_customized_property
-- ----------------------------
CREATE TABLE IF NOT EXISTS `process_stops_customized_property` (
    `id` VARCHAR(40) PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    `crt_dttm` DATETIME NOT NULL COMMENT 'Create date time',
     `crt_user` VARCHAR(255) NOT NULL COMMENT 'Create user',
    `enable_flag` BIT NOT NULL COMMENT 'Enable flag',
    `last_update_dttm` DATETIME NOT NULL COMMENT 'Last update date time',
    `last_update_user` VARCHAR(255) NOT NULL COMMENT 'Last update user',
    `version` BIGINT COMMENT 'Version',
    `custom_value` TEXT(0) COMMENT 'custom value',
    `description` TEXT(0) COMMENT 'description',
    `name` VARCHAR(255) COMMENT 'name',
    `fk_flow_process_stop_id` VARCHAR(40) DEFAULT NULL COMMENT 'Foreign key fk_flow_process_stop_id'
);
ALTER TABLE `process_stops_customized_property` ADD CONSTRAINT `FK1ql9h2ueevqxg1xjnt06repqv` FOREIGN KEY (`fk_flow_process_stop_id`) REFERENCES `flow_process_stop` (`id`);
-- ----------------------------
-- Table structure for flow_group_template
-- ----------------------------
CREATE TABLE IF NOT EXISTS `flow_group_template` (
    `id` VARCHAR(40) PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    `crt_dttm` DATETIME NOT NULL COMMENT 'Create date time',
     `crt_user` VARCHAR(255) NOT NULL COMMENT 'Create user',
    `enable_flag` BIT NOT NULL COMMENT 'Enable flag',
    `last_update_dttm` DATETIME NOT NULL COMMENT 'Last update date time',
    `last_update_user` VARCHAR(255) NOT NULL COMMENT 'Last update user',
    `version` BIGINT COMMENT 'Version',
    `description` VARCHAR(1024) COMMENT 'description',
    `flow_group_name` VARCHAR(255) DEFAULT NULL COMMENT 'flow_group_name',
    `name` VARCHAR(255) DEFAULT NULL COMMENT 'name',
    `path` VARCHAR(255) DEFAULT NULL COMMENT 'path'
);

CREATE TABLE IF NOT EXISTS `sys_schedule` (
    `id` VARCHAR(40) PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    `crt_dttm` DATETIME NOT NULL COMMENT 'Create date time',
     `crt_user` VARCHAR(255) NOT NULL COMMENT 'Create user',
    `enable_flag` BIT NOT NULL COMMENT 'Enable flag',
    `last_update_dttm` DATETIME NOT NULL COMMENT 'Last update date time',
    `last_update_user` VARCHAR(255) NOT NULL COMMENT 'Last update user',
    `version` BIGINT COMMENT 'Version',
    `cron_expression` VARCHAR(255) COMMENT 'Cron expression',
    `job_class` VARCHAR(255) COMMENT 'job class',
    `job_name` VARCHAR(255) COMMENT 'job name',
    `status` VARCHAR(255) COMMENT 'task status'
);

-- add table column
ALTER TABLE flow_group ADD COLUMN page_id VARCHAR(255) COMMENT 'page_id';
ALTER TABLE flow ADD COLUMN page_id VARCHAR(255) COMMENT 'page_id';
ALTER TABLE flow ADD COLUMN fk_flow_group_id VARCHAR(40) COMMENT 'fk_flow_group_id';
ALTER TABLE flow_path ADD COLUMN filter_condition VARCHAR(255) COMMENT 'filter_condition';
ALTER TABLE flow_stops ADD COLUMN is_customized BIT COMMENT 'is_customized';
ALTER TABLE flow_stops ADD COLUMN fk_data_source_id VARCHAR(40) COMMENT 'fk_data_source_id';
alter table flow_stops_property ADD COLUMN is_locked BIT COMMENT 'is_locked';
ALTER TABLE flow_stops_property ADD COLUMN property_sort BIGINT(20) COMMENT 'property sort';
ALTER TABLE flow_stops_template ADD COLUMN is_customized BIT COMMENT 'is_customized';
ALTER TABLE flow_stops_property_template ADD COLUMN property_sort BIGINT(20) COMMENT 'property sort';
ALTER TABLE flow_process ADD COLUMN page_id VARCHAR(255) COMMENT 'page_id';
ALTER TABLE flow_process ADD COLUMN fk_flow_process_group_id VARCHAR(40) COMMENT 'fk_flow_process_group_id';
ALTER TABLE flow_process ADD COLUMN run_mode_type VARCHAR(255) COMMENT 'Process RunModeType';
ALTER TABLE flow_process ADD COLUMN process_parent_type VARCHAR(255) COMMENT 'Process parent type';
ALTER TABLE mx_graph_model ADD COLUMN fk_flow_group_id VARCHAR(40) COMMENT 'fk_flow_group_id';

-- ADD FOREIGN KEY
ALTER TABLE flow ADD CONSTRAINT FK3e62cxjsjbtola3f8hcp1my1o FOREIGN KEY (fk_flow_group_id) REFERENCES flow_group (id);
ALTER TABLE flow_stops ADD CONSTRAINT FKr5de1px70o0uj1hlob7ilc90c FOREIGN KEY (fk_data_source_id) REFERENCES data_source (id);
ALTER TABLE flow_process ADD CONSTRAINT FKgvr5o5p4n6dbfx010h7wbjgku FOREIGN KEY (fk_flow_process_group_id) REFERENCES flow_process_group (id);
ALTER TABLE mx_graph_model ADD CONSTRAINT FKbwxper47v5e1ii4wjrcrhi63e FOREIGN KEY (fk_flow_group_id) REFERENCES flow_group (id);

-- update table column
ALTER TABLE flow_stops_property_template MODIFY COLUMN allowable_values TEXT(0) COMMENT 'Allowable_values';
ALTER TABLE flow_stops_property_template MODIFY COLUMN default_value TEXT(0) COMMENT 'Default_value';
ALTER TABLE flow_stops_property_template MODIFY COLUMN description TEXT(0) COMMENT 'Defaults';
ALTER TABLE flow_stops_property_template MODIFY COLUMN display_name TEXT(0) COMMENT 'Description';


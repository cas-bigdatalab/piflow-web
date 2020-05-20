-- ----------------------------
-- Delete Foreign key
-- ----------------------------
ALTER TABLE `flow` DROP FOREIGN KEY `FKg80q2a1d63pkfbayl55cuydcj`;
ALTER TABLE `flow_group` DROP FOREIGN KEY `FKd0sfu1rtmctw8qess69c1qhaw`;
ALTER TABLE `mx_graph_model` DROP FOREIGN KEY `FK37dhc2jlml6pyvv1srdihtiky`;

-- ----------------------------
-- Delete field
-- ----------------------------
ALTER TABLE `flow` DROP COLUMN `fk_flow_project_id`;
ALTER TABLE `flow_group` DROP COLUMN `fk_flow_project_id`;
ALTER TABLE `mx_graph_model` DROP COLUMN `fk_flow_project_id`;

-- ----------------------------
-- Drop Table
-- ----------------------------
DROP TABLE IF EXISTS `flow_info`;
DROP TABLE IF EXISTS `flow_project`;
DROP TABLE IF EXISTS `schedule`;

-- ----------------------------
-- Create Table
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sys_init_records` (
   id varchar(40) NOT NULL,
   init_date datetime NOT NULL,
   is_succeed bit NOT NULL,
   PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS mx_node_image (
   id varchar(40) NOT NULL,
   crt_dttm datetime NOT NULL,
   crt_user varchar(255) NOT NULL,
   enable_flag bit NOT NULL,
   last_update_dttm datetime NOT NULL,
   last_update_user varchar(255) NOT NULL,
   version bigint,
   image_name varchar(255),
   image_path varchar(255),
   image_type varchar(255),
   image_url varchar(255),
   primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Add column
-- ----------------------------
ALTER TABLE `sys_schedule` ADD COLUMN last_run_result varchar(255) COMMENT 'task last run result';
ALTER TABLE `flow_group` ADD COLUMN fk_flow_group_id varchar(40);
ALTER TABLE `flow_process_group` ADD COLUMN fk_flow_process_group_id varchar(40);
ALTER TABLE `flow_group_template` ADD COLUMN template_type varchar(255) COMMENT 'template type';
ALTER TABLE `flow_process_group` ADD COLUMN page_id varchar(255);
ALTER TABLE `flow_template` ADD COLUMN source_flow_name varchar(255) COMMENT 'source flow name';
ALTER TABLE `flow_template` ADD COLUMN template_type varchar(255) COMMENT 'template type';
ALTER TABLE `flow_template` ADD COLUMN url varchar(255);
ALTER TABLE `mx_graph_model` ADD COLUMN fk_process_id varchar(40);
ALTER TABLE `mx_graph_model` ADD COLUMN fk_process_group_id varchar(40);
ALTER TABLE `flow_stops_property` ADD COLUMN is_old_data bit(1) COMMENT 'Has it been updated';

-- ----------------------------
-- Add Foreign key
-- ----------------------------
ALTER TABLE `flow_group` ADD CONSTRAINT FKe1i6t5gnt6ys4yqkt5uumr20w FOREIGN KEY (fk_flow_group_id) REFERENCES flow_group (id);
ALTER TABLE `flow_process_group` ADD CONSTRAINT FKpxf1rth8fs0pld3jlvigkaf2y FOREIGN KEY (fk_flow_process_group_id) REFERENCES flow_process_group (id);
ALTER TABLE `mx_graph_model` ADD CONSTRAINT FKkw0r9m7r3jm9scab8caoxnnxc FOREIGN KEY (fk_process_id) REFERENCES flow_process (id);
ALTER TABLE `mx_graph_model` ADD CONSTRAINT FKnugg3p8uvupfu3mso2iax2g8t FOREIGN KEY (fk_process_group_id) REFERENCES flow_process_group (id);

-- ----------------------------
-- Update Data
-- ----------------------------
UPDATE sys_schedule ss SET ss.last_run_result="SUCCEED";
UPDATE `sys_menu` ss SET ss.enable_flag=0;

-- ----------------------------
-- Insert Data
-- ----------------------------
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be6wmenu00001', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'Flows', 'USER', 'Flow', NULL, '/piflow-web/web/flowList', 100001);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be6wmenu00002', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'FlowGroup', 'USER', 'Group', NULL, '/piflow-web/web/flowGroupList', 100001);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be6wmenu00003', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'Process', 'USER', 'Process', NULL, '/piflow-web/web/processAndProcessGroup', 100002);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be6wmenu00004', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'FlowGroupTemplate', 'USER', 'Template', NULL, '/piflow-web/web/flowTemplateList', 100003);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be6wmenu00005', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'DataSource', 'USER', 'DataSource', NULL, '/piflow-web/web/dataSources', 400001);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be6wmenu00006', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'Instruction', 'USER', 'Instruction', 'Example', '/piflow-web/web/instructionalVideo', 500001);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be6wmenu00007', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'Example1', 'USER', 'Example1', 'Example', '/piflow-web/mxGraph/drawingBoard?drawingBoardType=TASK&load=0641076d5ae840c09d2be5b71fw00001', 500002);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be6wmenu00008', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'Example2', 'USER', 'Example2', 'Example', '/piflow-web/mxGraph/drawingBoard?drawingBoardType=GROUP&load=0641076d5ae840c09d2be5b71fw00002', 500003);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be6wmenu00009', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'Schedule', 'ADMIN', 'Schedule', 'Admin', '/piflow-web/web/sysScheduleList', 900001);

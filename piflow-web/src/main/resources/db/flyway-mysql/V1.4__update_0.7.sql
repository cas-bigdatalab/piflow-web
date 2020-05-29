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
UPDATE `flow_stops_property` fsp SET fsp.is_old_data=0;

-- ----------------------------
-- Insert Data
-- ----------------------------
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be6wmenu00001', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'Flows', 'USER', 'Flow', NULL, '/piflow-web/web/flowList', 100001);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be6wmenu00002', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'FlowGroup', 'USER', 'Group', NULL, '/piflow-web/web/flowGroupList', 100001);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be6wmenu00003', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'Process', 'USER', 'Process', NULL, '/piflow-web/web/processAndProcessGroup', 100002);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be6wmenu00004', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'FlowGroupTemplate', 'USER', 'Template', NULL, '/piflow-web/web/flowTemplateList', 100003);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be6wmenu00005', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'DataSource', 'USER', 'DataSource', NULL, '/piflow-web/web/dataSources', 400001);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be6wmenu00007', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'Example1', 'USER', 'FlowExample', 'Example', '/piflow-web/mxGraph/drawingBoard?drawingBoardType=TASK&load=0641076d5ae840c09d2be5b71fw00001', 500002);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be6wmenu00008', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'Example2', 'USER', 'GroupExample', 'Example', '/piflow-web/mxGraph/drawingBoard?drawingBoardType=GROUP&load=ff808181725050fe017250group10002', 500003);
INSERT INTO `sys_menu`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `menu_description`, `menu_jurisdiction`, `menu_name`, `menu_parent`, `menu_url`, `menu_sort`) VALUES ('0641076d5ae840c09d2be6wmenu00009', '2019-08-15 10:23:20', 'system', b'1', '2019-08-15 10:23:36', 'system', 0, 'Schedule', 'ADMIN', 'Schedule', 'Admin', '/piflow-web/web/sysScheduleList', 900001);

INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da01725040task0001', '2020-05-26 00:00:01', 'admin', b'1', '2020-05-26 00:00:01', 'admin', 0, 'task8.png', 'img/task/task8.png', 'TASK', '/img/task/task8.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da01725040task0002', '2020-05-26 00:00:02', 'admin', b'1', '2020-05-26 00:00:02', 'admin', 0, 'task7.png', 'img/task/task7.png', 'TASK', '/img/task/task7.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da01725040task0003', '2020-05-26 00:00:03', 'admin', b'1', '2020-05-26 00:00:03', 'admin', 0, 'task6.png', 'img/task/task6.png', 'TASK', '/img/task/task6.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da01725040task0004', '2020-05-26 00:00:04', 'admin', b'1', '2020-05-26 00:00:04', 'admin', 0, 'task5.png', 'img/task/task5.png', 'TASK', '/img/task/task5.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da01725040task0005', '2020-05-26 00:00:05', 'admin', b'1', '2020-05-26 00:00:05', 'admin', 0, 'task4.png', 'img/task/task4.png', 'TASK', '/img/task/task4.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da01725040task0006', '2020-05-26 00:00:06', 'admin', b'1', '2020-05-26 00:00:06', 'admin', 0, 'task3.png', 'img/task/task3.png', 'TASK', '/img/task/task3.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da01725040task0007', '2020-05-26 00:00:07', 'admin', b'1', '2020-05-26 00:00:07', 'admin', 0, 'task2.png', 'img/task/task2.png', 'TASK', '/img/task/task2.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da01725040task0008', '2020-05-26 00:00:08', 'admin', b'1', '2020-05-26 00:00:08', 'admin', 0, 'task1.png', 'img/task/task1.png', 'TASK', '/img/task/task1.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da01725040task0009', '2020-05-26 00:00:09', 'admin', b'1', '2020-05-26 00:00:09', 'admin', 0, 'task.png', 'img/task/task.png', 'TASK', '/img/task/task.png');

INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da0172504group0009', '2020-05-26 00:00:01', 'admin', b'1', '2020-05-26 00:00:01', 'admin', 0, 'group8.png', 'img/group/group8.png', 'GROUP', '/img/group/group8.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da0172504group0008', '2020-05-26 00:00:02', 'admin', b'1', '2020-05-26 00:00:02', 'admin', 0, 'group7.png', 'img/group/group7.png', 'GROUP', '/img/group/group7.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da0172504group0007', '2020-05-26 00:00:03', 'admin', b'1', '2020-05-26 00:00:03', 'admin', 0, 'group6.png', 'img/group/group6.png', 'GROUP', '/img/group/group6.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da0172504group0006', '2020-05-26 00:00:04', 'admin', b'1', '2020-05-26 00:00:04', 'admin', 0, 'group5.png', 'img/group/group5.png', 'GROUP', '/img/group/group5.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da0172504group0005', '2020-05-26 00:00:05', 'admin', b'1', '2020-05-26 00:00:05', 'admin', 0, 'group4.png', 'img/group/group4.png', 'GROUP', '/img/group/group4.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da0172504group0004', '2020-05-26 00:00:06', 'admin', b'1', '2020-05-26 00:00:06', 'admin', 0, 'group3.png', 'img/group/group3.png', 'GROUP', '/img/group/group3.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da0172504group0003', '2020-05-26 00:00:07', 'admin', b'1', '2020-05-26 00:00:07', 'admin', 0, 'group2.png', 'img/group/group2.png', 'GROUP', '/img/group/group2.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da0172504group0002', '2020-05-26 00:00:08', 'admin', b'1', '2020-05-26 00:00:08', 'admin', 0, 'group1.png', 'img/group/group1.png', 'GROUP', '/img/group/group1.png');
INSERT INTO `mx_node_image`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `image_name`, `image_path`, `image_type`, `image_url`) VALUES ('ff808181725005da0172504group0001', '2020-05-26 00:00:09', 'admin', b'1', '2020-05-26 00:00:09', 'admin', 0, 'group.png', 'img/group/group.png', 'GROUP', '/img/group/group.png');



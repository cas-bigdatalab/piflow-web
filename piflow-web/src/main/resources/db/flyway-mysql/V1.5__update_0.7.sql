-- ----------------------------
-- Update sys_menu
-- ----------------------------
UPDATE `sys_menu` SET `menu_url`='/piflow-web/mxGraph/drawingBoard?drawingBoardType=TASK&load=0c4fdee973824a999e1569770677c020' WHERE ID = '0641076d5ae840c09d2be5bmenu00015';
UPDATE `sys_menu` SET `menu_url`='/piflow-web/mxGraph/drawingBoard?drawingBoardType=TASK&load=c9c77d24b65942fb9665fbdbe8710236' WHERE ID = '0641076d5ae840c09d2be5bmenu00016';
-- ----------------------------
-- Delete sys_menu
-- ----------------------------
DELETE from `sys_menu` WHERE ID = '0641076d5ae840c09d2be5bmenu00018';

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
-- Add column
-- ----------------------------
ALTER TABLE `flow_group` ADD COLUMN fk_flow_group_id varchar(40);
ALTER TABLE `flow_process_group` ADD COLUMN fk_flow_process_group_id varchar(40);

-- ----------------------------
-- Add Foreign key
-- ----------------------------
ALTER TABLE `flow_group` ADD CONSTRAINT FKe1i6t5gnt6ys4yqkt5uumr20w FOREIGN KEY (fk_flow_group_id) REFERENCES flow_group (id);
ALTER TABLE `flow_process_group` ADD CONSTRAINT FKpxf1rth8fs0pld3jlvigkaf2y FOREIGN KEY (fk_flow_process_group_id) REFERENCES flow_process_group (id)
-- ----------------------------
-- Records of sys_menu
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
-- ----------------------------
-- Add column
-- ----------------------------
ALTER TABLE `stops_hub` ADD COLUMN `bundles` Text NULL DEFAULT NULL COMMENT 'bundle';
ALTER TABLE `stops_hub` ADD COLUMN `is_publishing` bit(1) NULL DEFAULT 0 COMMENT 'Is Publishing';
ALTER TABLE `sys_user` ADD COLUMN `developer_access_key` VARCHAR(255) NULL DEFAULT NULL COMMENT 'developer access key';
-- ----------------------------
-- Modify column
-- ----------------------------
ALTER TABLE `flow_stops_property` MODIFY COLUMN `example` TEXT CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'property example';
ALTER TABLE `flow_stops_property_template` MODIFY COLUMN `example` TEXT CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'property example';
-- ----------------------------
-- Add column
-- ----------------------------
ALTER TABLE `data_source` ADD COLUMN `is_available` bit(1) NULL COMMENT 'is available' AFTER `is_template`;
ALTER TABLE `data_source` ADD COLUMN `image_url` varchar(255) NULL COMMENT 'stop image url' AFTER `is_available`;
ALTER TABLE `flow_stops_template` ADD COLUMN `image_url` varchar(255) NULL COMMENT 'stop image url' AFTER `is_data_source`;
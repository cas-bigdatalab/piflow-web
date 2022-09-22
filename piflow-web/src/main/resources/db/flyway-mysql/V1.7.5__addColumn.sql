-- ----------------------------
-- Add column
-- ----------------------------
ALTER TABLE `flow_stops` ADD COLUMN `is_disabled` bit(1) NULL AFTER `is_data_source`;
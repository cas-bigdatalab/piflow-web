-- ----------------------------
-- Add column
-- ----------------------------
ALTER TABLE `FLOW_STOPS` ADD COLUMN `IS_DISABLED` BIT(1) NULL AFTER `IS_DATA_SOURCE`;
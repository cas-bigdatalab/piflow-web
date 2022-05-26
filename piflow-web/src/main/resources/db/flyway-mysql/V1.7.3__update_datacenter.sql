-- ----------------------------
-- Add column
-- ----------------------------
ALTER TABLE `flow_stops_template` ADD COLUMN `is_data_source` bit(1) NULL AFTER `visualization_type`;
ALTER TABLE `flow_stops_template` ADD INDEX(`bundel`);
ALTER TABLE `flow_stops` ADD COLUMN `is_data_source` bit(1) NULL AFTER `fk_data_source_id`;
ALTER TABLE `flow_process_stop` ADD COLUMN `is_data_source` bit(1) NULL AFTER `fk_flow_process_id`;
ALTER TABLE `data_source` ADD COLUMN `stops_template_bundle` varchar(255) NULL AFTER `is_template`;

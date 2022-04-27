-- ----------------------------
-- Table flow_stops_template,Add column
-- ----------------------------
ALTER TABLE `flow_stops_template`
ADD COLUMN `is_data_source` bit(1) NULL AFTER `visualization_type`;


-- ----------------------------
-- Table flow_stops,Add column
-- ----------------------------
ALTER TABLE `flow_stops`
ADD COLUMN `is_data_source` bit(1) NULL AFTER `fk_data_source_id`;



-- ----------------------------
-- Table flow_process_stop,Add column
-- ----------------------------
ALTER TABLE `flow_process_stop`
ADD COLUMN `is_data_source` bit(1) NULL AFTER `fk_flow_process_id`;
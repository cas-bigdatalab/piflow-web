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



-- ----------------------------
-- Table data_source,Add foreign key and column
-- ----------------------------
ALTER TABLE `flow_stops_template`ADD INDEX(`bundel`);

ALTER TABLE `data_source`
ADD COLUMN `stops_template_bundle` varchar(255) NULL AFTER `is_template`,
ADD CONSTRAINT `FKqwv1iytgkhhgnjdvhqbsknas6` FOREIGN KEY (`stops_template_bundle`) REFERENCES `flow_stops_template` (`bundel`) ON DELETE RESTRICT ON UPDATE RESTRICT;

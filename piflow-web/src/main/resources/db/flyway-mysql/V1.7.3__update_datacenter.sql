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


-- ----------------------------
-- Table data_source,Add data of data_source_type = STOP
-- ----------------------------
INSERT INTO `data_source`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `data_source_description`, `data_source_name`, `data_source_type`, `is_template`) VALUES ('a9aa6416c43d11ec95bdc8000a005a9b', NOW(), 'system', b'1', NOW(), 'Admin', 0, NULL, 'STOP', 'STOP', b'1');

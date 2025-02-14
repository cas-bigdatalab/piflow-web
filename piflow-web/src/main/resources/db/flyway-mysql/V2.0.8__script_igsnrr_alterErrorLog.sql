-- ----------------------------
-- alter error_log_mapping
-- ----------------------------

ALTER TABLE `error_log_mapping` ADD COLUMN flow_publish_id VARCHAR(512) NOT NULL COMMENT '关联的发布流水线id，多个用逗号隔开';
ALTER TABLE `error_log_mapping` ADD COLUMN flow_publish_name text NOT NULL COMMENT '关联的发布流水线名称，多个用逗号隔开';

ALTER TABLE `error_log_mapping` ADD INDEX index_flow_publish_id (`flow_publish_id`);
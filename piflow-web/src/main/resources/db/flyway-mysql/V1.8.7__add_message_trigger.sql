

-- ----------------------------
-- Table structure for message_task_trigger_instance
-- ----------------------------
DROP TABLE IF EXISTS `message_task_trigger_instance`;
CREATE TABLE `message_task_trigger_instance` (
                                                 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '数据库主键ID',
                                                 `type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                                 `trigger_instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内部生成的唯一触发实例ID，用于日志追踪，UUID',
                                                 `message_source_id` bigint NOT NULL COMMENT '关联的消息源ID',
                                                 `message_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '关联的消息的MessageId (来自IMessage)',
                                                 `protocol_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息协议类型 (来自IMessage)',
                                                 `target_workflow_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '目标工作流的ID或名称',
                                                 `target_workflow_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                                 `process_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '如果成功触发Piflow，则记录Piflow的Job ID',
                                                 `trigger_time` datetime NOT NULL COMMENT '触发时间',
                                                 `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '任务状态：RUNNING, SUCCESS, FAILED, RETRYING 等',
                                                 `duration_millis` bigint DEFAULT NULL COMMENT '触发耗时（毫秒）',
                                                 `error_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '错误信息，如果触发失败',
                                                 `raw_message_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '原始消息内容（部分或全部），用于回溯',
                                                 `execution_list` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '执行记录',
                                                 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                                 `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                                 PRIMARY KEY (`id`),
                                                 UNIQUE KEY `uk_trigger_instance_id` (`trigger_instance_id`)
) ENGINE=InnoDB AUTO_INCREMENT=351 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务触发实例表';

SET FOREIGN_KEY_CHECKS = 1;



-- ----------------------------
-- Table structure for message_trigger_task_definition
-- ----------------------------
DROP TABLE IF EXISTS `message_trigger_task_definition`;
CREATE TABLE `message_trigger_task_definition` (
                                                   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                   `name` varchar(255) NOT NULL COMMENT '消息源名称，例如：订单系统RabbitMQ，用户行为Kafka',
                                                   `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '调度类型\n',
                                                   `protocol` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息协议类型 (例如: RABBITMQ, KAFKA)',
                                                   `properties` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '配置组名',
                                                   `filter_rule_json` text COMMENT '消息过滤规则的JSON配置 (例如: {"expression": "..."})',
                                                   `advanced_config` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '高级配置参数',
                                                   `filter_rule_type` varchar(50) DEFAULT NULL COMMENT '消息过滤规则类型 (例如: AVIATOR)',
                                                   `target_workflow_id` varchar(255) NOT NULL COMMENT '消息触发后要调用的目标工作流ID',
                                                   `target_workflow_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '工作流名称',
                                                   `concurrency_limit` bigint DEFAULT NULL COMMENT '定义的并发执行上限',
                                                   `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'PAUSED' COMMENT '消息源状态 (ENABLED:启用, PAUSED:暂停, DISABLED:禁用)',
                                                   `creator_id` varchar(40) NOT NULL COMMENT '创建者id',
                                                   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
                                                   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后更新时间',
                                                   PRIMARY KEY (`id`),
                                                   KEY `idx_type` (`protocol`),
                                                   KEY `idx_status` (`status`),
                                                   KEY `idx_target_workflow_id` (`target_workflow_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='消息触发任务定义表';

SET FOREIGN_KEY_CHECKS = 1;

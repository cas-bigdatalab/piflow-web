-----------------------------------
--Table structure for file_schedule
-----------------------------------

CREATE TABLE IF NOT EXISTS `file_schedule`
(
    `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `name` varchar(255) NULL DEFAULT NULL COMMENT '名称',
    `description` text NULL DEFAULT NULL COMMENT '简介',
    `file_dict` varchar(255) NOT NULL DEFAULT "" COMMENT '文件所在目录',
    `file_prefix` varchar(100) NULL DEFAULT NULL COMMENT '文件名前缀匹配',
    `file_suffix` varchar(100) NULL DEFAULT NULL COMMENT '文件名后缀匹配',
    `associate_id` varchar(50) NULL DEFAULT NULL COMMENT '关联id',
    `associate_type` varchar(50) NOT NULL DEFAULT "" COMMENT '关联类型：FLOW-流水线',
    `file_path` text NULL DEFAULT NULL COMMENT '触发调度的文件路径（相对，多个用逗号隔开）',
    `file_size` int(4) DEFAULT NULL COMMENT '文件大小',
    `file_last_modify_time` datetime NULL DEFAULT NULL COMMENT '文件最后修改时间（最新触发时间）',
    `crt_dttm` datetime NOT NULL COMMENT '创建时间',
    `crt_user` varchar(255) NOT NULL COMMENT '创建人',
    `enable_flag` bit(1) NOT NULL COMMENT '逻辑删除，0-不可用 1-可用',
    `last_update_dttm` datetime NOT NULL COMMENT '最后更新时间',
    `last_update_user` varchar(255) NOT NULL COMMENT '最后更新人',
    `version` bigint(20) NULL DEFAULT NULL COMMENT 'version',
    `state` tinyint(1) NULL DEFAULT NULL COMMENT '状态：0-初始化（INIT） 1-正在运行（RUNNING） 2-暂停（STOP）',
    `bak1` varchar(255) NULL DEFAULT NULL COMMENT '保留字段1',
    `bak2` varchar(255) NULL DEFAULT NULL COMMENT '保留字段2',
    `bak3` varchar(255) NULL DEFAULT NULL COMMENT '保留字段3'
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT='文件触发记录表';

-----------------------------------
--Table structure for file_schedule_origin
-----------------------------------

CREATE TABLE IF NOT EXISTS `file_schedule_origin`
(
    `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `schedule_id` bigint(20) NOT NULL COMMENT '关联文件触发记录ID',
    `origin_file_record` text NULL DEFAULT NULL COMMENT '原始文件列表记录'
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT='文件触发记录原始文件列表';

-----------------------------------
--add file_schedule sync
-----------------------------------

INSERT INTO `sys_schedule`(`id`, `crt_dttm`, `crt_user`, `enable_flag`, `last_update_dttm`, `last_update_user`, `version`, `cron_expression`, `job_class`, `job_name`, `status`, `last_run_result`) VALUES ('9ddf3384d2314ff4b3c858892b06e600', '2024-05-15 09:47:12', 'system', b'1', '2024-05-15 09:47:12', 'system', 0, '0 0/5 * * * ?', 'cn.cnic.schedule.FileScheduleSync', 'FileScheduleSync', 'RUNNING', 'SUCCEED');

-- ----------------------------
-- Alter Table file_schedule and file_schedule_origin
-- ----------------------------

ALTER TABLE `file_schedule` ADD COLUMN `stop_id` varchar(50) NULL DEFAULT NULL COMMENT '关联组件id' AFTER `associate_type`;
ALTER TABLE `file_schedule` ADD COLUMN `stop_name` varchar(255) NULL DEFAULT NULL COMMENT '关联组件名称' AFTER `stop_id`;
ALTER TABLE `file_schedule` ADD COLUMN `property_id` varchar(50) NULL DEFAULT NULL COMMENT '关联组件的参数id' AFTER `stop_name`;
ALTER TABLE `file_schedule` ADD COLUMN `property_name` text NULL DEFAULT NULL COMMENT '关联组件的参数名称' AFTER `property_id`;
ALTER TABLE `file_schedule` ADD COLUMN `trigger_mode` tinyint(1) NULL DEFAULT 0 COMMENT '触发模式 0-并行  1-串行' AFTER `file_last_modify_time`;
ALTER TABLE `file_schedule` ADD COLUMN `serial_rule` tinyint(1) NULL DEFAULT NULL COMMENT '串行规则 0-文件最后更新时间  1-文件名' AFTER `trigger_mode`;
ALTER TABLE `file_schedule` ADD COLUMN `regex` varchar(255) NULL DEFAULT NULL COMMENT '串行文件名时的判断排序的正则表达式' AFTER `serial_rule`;
ALTER TABLE `file_schedule` ADD COLUMN `serial_order` tinyint(1) NULL DEFAULT NULL COMMENT '串行顺序 0-升序 1-降序' AFTER `regex`;
ALTER TABLE `file_schedule` ADD COLUMN `process_id` varchar(512) NULL DEFAULT NULL COMMENT '最近运行的进程' AFTER `serial_order`;
ALTER TABLE `file_schedule_origin` ADD COLUMN `pending_file_record` text NULL DEFAULT NULL COMMENT '待处理的文件' AFTER `origin_file_record`;

-----------------------------------
--alter flow_process
-----------------------------------

ALTER TABLE `flow_process` ADD COLUMN `trigger_mode` tinyint(1) NULL DEFAULT 0 COMMENT '触发模式 0-手动触发、1-定时调度、2.文件触发调度';
ALTER TABLE `flow_process` ADD COLUMN `schedule_id` varchar(40) NULL COMMENT '调度id 包括定时调度和文件触发调度';
ALTER TABLE `flow_process` ADD COLUMN `schedule_name` varchar(255) NULL COMMENT '调度名称';
ALTER TABLE `flow_process` ADD COLUMN `trigger_file` text NULL COMMENT '调度文件名';












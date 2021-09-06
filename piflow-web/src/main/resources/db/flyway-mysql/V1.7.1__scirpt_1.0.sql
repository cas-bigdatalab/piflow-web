CREATE TABLE IF NOT EXISTS `note_book` (
  `id` VARCHAR(40) NOT NULL,
  `crt_dttm` DATETIME(0) NOT NULL COMMENT 'Create date time',
  `crt_user` VARCHAR(255) NOT NULL COMMENT 'Create user',
  `enable_flag` BIT(1) NOT NULL COMMENT 'Enable flag',
  `last_update_dttm` DATETIME(0) NOT NULL COMMENT 'Last update date time',
  `last_update_user` VARCHAR(255) NOT NULL COMMENT 'Last update user',
  `version` BIGINT(20) NULL DEFAULT NULL COMMENT 'Version',
  `name` VARCHAR(1000) COMMENT 'name',
  `description` TEXT COMMENT 'description',
  `sessions_id` VARCHAR(255) COMMENT '`sessions id',
  `code_type` VARCHAR(255) NOT NULL COMMENT 'code type',
  PRIMARY KEY (ID)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS `code_snippet` (
  `id` VARCHAR(40) NOT NULL,
  `crt_dttm` DATETIME(0) NOT NULL COMMENT 'Create date time',
  `crt_user` VARCHAR(255) NOT NULL COMMENT 'Create user',
  `enable_flag` BIT(1) NOT NULL COMMENT 'Enable flag',
  `last_update_dttm` DATETIME(0) NOT NULL COMMENT 'Last update date time',
  `last_update_user` VARCHAR(255) NOT NULL COMMENT 'Last update user',
  `version` BIGINT(20) NULL DEFAULT NULL COMMENT 'Version',
  `fk_note_book_id` VARCHAR(40) COMMENT 'fk node_book id',
  `execute_id` VARCHAR(40) COMMENT 'execute code id',
  `code_content` TEXT COMMENT 'code content',
  `code_snippet_sort` BIGINT COMMENT 'soft'
) ENGINE=INNODB;
ALTER TABLE `code_snippet` ADD CONSTRAINT `FK22rp96r4290eons0000000002` FOREIGN KEY (`fk_note_book_id`) REFERENCES `note_book` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE TABLE IF NOT EXISTS `flow_global_params` (
  `id` VARCHAR(40) PRIMARY KEY NOT NULL,
  `crt_dttm` DATETIME NOT NULL COMMENT 'Create date time',
  `crt_user` VARCHAR(255) NOT NULL COMMENT 'Create user',
  `enable_flag` BIT NOT NULL COMMENT 'Enable flag',
  `last_update_dttm` DATETIME NOT NULL COMMENT 'Last update date time',
  `last_update_user` VARCHAR(255) NOT NULL COMMENT 'Last update user',
  `version` BIGINT NULL DEFAULT NULL COMMENT 'Version',
  `name` VARCHAR(1000) COMMENT 'name',
  `type` VARCHAR(255) COMMENT 'type',
  `content` TEXT COMMENT 'content'
);

CREATE TABLE IF NOT EXISTS `association_global_params_flow` (
    `global_params_id` VARCHAR(40) NOT NULL COMMENT 'FLOW_GLOBAL_PARAMS primary key id',
    `flow_id` VARCHAR(40) COMMENT 'FLOW primary key id',
    `process_id` VARCHAR(40) COMMENT 'process primary key id'
);
ALTER TABLE `association_global_params_flow` ADD CONSTRAINT `FK22rp96r4290eons0000000003` FOREIGN KEY (`global_params_id`) REFERENCES `flow_global_params` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `association_global_params_flow` ADD CONSTRAINT `FK22rp96r4290eons0000000004` FOREIGN KEY (`flow_id`) REFERENCES `flow` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `association_global_params_flow` ADD CONSTRAINT `FK22rp96r4290eons0000000005` FOREIGN KEY (`process_id`) REFERENCES `flow_process` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `FLOW_STOPS_TEMPLATE_MANAGE` RENAME TO `flow_stops_template_manage`;


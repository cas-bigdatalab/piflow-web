-- ----------------------------
-- Add column
-- ----------------------------
CREATE TABLE IF NOT EXISTS `flow_stops_publishing` (
    `id` VARCHAR(40) NOT NULL,
    `crt_dttm` DATETIME NOT NULL COMMENT 'Create date time',
    `crt_user` VARCHAR(255) NOT NULL COMMENT 'Create user',
    `enable_flag` BIT(1) NOT NULL COMMENT 'Enable flag',
    `last_update_dttm` DATETIME NOT NULL COMMENT 'Last update date time',
    `last_update_user` VARCHAR(255) NOT NULL COMMENT 'Last update user',
    `version` BIGINT(20) DEFAULT NULL COMMENT 'Version',
    `publishing_id` VARCHAR(255) DEFAULT NULL COMMENT 'publishing id',
    `stops_id` VARCHAR(255) DEFAULT NULL COMMENT 'publishing stops id',
    `name` VARCHAR(255) DEFAULT NULL COMMENT 'publishing name',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
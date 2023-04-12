-----------------------------------
--Table structure for stops_hub_file_record
-----------------------------------

CREATE TABLE IF NOT EXISTS `stops_hub_file_record`
(
    `id`                 varchar(40) NOT NULL COMMENT 'UUID',
    `file_name`          varchar(255) NULL DEFAULT NULL COMMENT 'file name',
    `file_path`          varchar(255) NULL DEFAULT NULL COMMENT 'file path',
    `stops_hub_id`       varchar(255) NULL DEFAULT NULL COMMENT 'stops_hub id',
    `docker_images_name` varchar(255) NULL DEFAULT NULL COMMENT 'docker image name',
    `crt_dttm`           datetime    NOT NULL COMMENT 'creat time',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci COMMENT='all file info of zip mount';


ALTER TABLE `stops_hub` ADD COLUMN `type` varchar(40) NULL COMMENT 'component type';
ALTER TABLE `stops_hub` ADD COLUMN `language_version` varchar(40) NULL COMMENT 'the language version that the component depends on';

ALTER TABLE `flow_stops_template` ADD COLUMN `component_type` varchar(40) NULL COMMENT 'component type';
ALTER TABLE `flow_stops_template` ADD COLUMN `docker_images_name` varchar(255) NULL COMMENT 'docker image name';
ALTER TABLE `flow_stops_template` ADD COLUMN `stops_hub_id` varchar(40) NULL COMMENT 'stops_hub id';

-- ----------------------------
-- create Table base_image_info
-- ----------------------------

CREATE TABLE `base_image_info`
(
    `id`                      int  NOT NULL AUTO_INCREMENT COMMENT '基础镜像自增id',
    `base_image_name`         varchar(255)  NOT NULL COMMENT '基础镜像名称',
    `base_image_version`      varchar(40)  DEFAULT NULL COMMENT '基础镜像python版本',
    `base_image_description`  text COMMENT 'description',
    `harbor_user`             varchar(40)  DEFAULT NULL COMMENT 'harbor仓库用户名',
    `harbor_password`         varchar(40)  DEFAULT NULL COMMENT 'harbor密码',
    `crt_dttm`                datetime     NOT NULL COMMENT '上传时间',
    `crt_user`                varchar(255) NOT NULL COMMENT '上传用户',
    PRIMARY KEY (`id`),
    UNIQUE KEY `unique_base_image_info` (`base_image_name`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT='基础镜像信息表';

-- ----------------------------
-- Table flow_stops
-- ----------------------------
ALTER TABLE `stops_hub` ADD COLUMN `description` VARCHAR(255) DEFAULT NULL AFTER `jar_url`;
ALTER TABLE `stops_hub` ADD COLUMN `image` VARCHAR(255) DEFAULT NULL COMMENT '算法镜像' AFTER `language_version`;
ALTER TABLE `stops_hub` ADD COLUMN `base_image` varchar(255) DEFAULT NULL COMMENT '算子基础镜像名称' AFTER `image`;
ALTER TABLE `stops_hub` ADD COLUMN `base_image_description` VARCHAR(255) DEFAULT NULL COMMENT '算子基础镜像描述' AFTER `base_image`;

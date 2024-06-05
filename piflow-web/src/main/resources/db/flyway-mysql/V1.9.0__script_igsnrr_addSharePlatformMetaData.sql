CREATE TABLE IF NOT EXISTS `share_platform_metadata`
(
    `id`                 BIGINT(20)   NOT NULL COMMENT '主键id',
    `metadata_file_path` VARCHAR(255) NOT NULL COMMENT '元数据文件路径',
    `icon_path`          VARCHAR(255) NOT NULL COMMENT '图标地址(本地)',
    `documentation_path` VARCHAR(255) NOT NULL COMMENT '文档地址(相对)',
    `review_status`      TINYINT      NOT NULL DEFAULT 0 COMMENT '状态 0-无效 1-已编辑,尚未提交 2-已提交,审核中 3-已提交,审核不通过,需修改 4-已发布',
    `product_url`        VARCHAR(255)          DEFAULT NULL COMMENT '发布的url,未发布时为空',
    `crt_dttm`           DATETIME     NOT NULL COMMENT '创建时间',
    `last_updated_dttm`  DATETIME     NOT NULL COMMENT '最后更新时间',
    PRIMARY KEY (`id`)

) ENGINE = InnoDB
  AUTO_INCREMENT = 26
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='共享服务平台元数据信息表';

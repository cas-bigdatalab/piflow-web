-- ----------------------------
-- alter data_product
-- ----------------------------

ALTER TABLE `data_product` ADD COLUMN dataset_year VARCHAR(40) NULL DEFAULT NULL COMMENT '年份' after `state`;
ALTER TABLE `data_product` ADD COLUMN dataset_line varchar(100) NULL DEFAULT "0" COMMENT '数据条数' after `dataset_size`;
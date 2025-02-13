-- ----------------------------
-- alter data_product
-- ----------------------------

ALTER TABLE `data_product` ADD COLUMN year VARCHAR(40) DEFAULT NULL COMMENT '年份' after `state`;
ALTER TABLE data_product ADD dataset_line varchar(100) NULL DEFAULT "0" COMMENT '数据条数' after `dataset_size`;
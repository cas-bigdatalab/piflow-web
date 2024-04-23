-----------------------------------
--alter Table structure for data_product
-----------------------------------

ALTER TABLE data_product MODIFY COLUMN associate_id varchar(256) NULL DEFAULT NUll COMMENT '关联的数据产品id，多个用英文分号隔开';
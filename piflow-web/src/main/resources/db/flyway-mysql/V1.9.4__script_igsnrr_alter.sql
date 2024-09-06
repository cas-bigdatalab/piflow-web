ALTER TABLE data_product CHANGE COLUMN bak1 company VARCHAR(255) DEFAULT NULL COMMENT '创建人所属单位';
ALTER TABLE flow_process_group ADD COLUMN company VARCHAR(255) DEFAULT  "" COMMENT '创建人所属单位';
ALTER TABLE flow_process ADD COLUMN company VARCHAR(255) DEFAULT  "" COMMENT '创建人所属单位';
ALTER TABLE vis_dataBase_info ADD COLUMN company VARCHAR(255) DEFAULT  "" COMMENT '创建人所属单位';
ALTER TABLE vis_graph_conf ADD COLUMN company VARCHAR(255) DEFAULT  "" COMMENT '创建人所属单位';
ALTER TABLE vis_graph_template ADD COLUMN company VARCHAR(255) DEFAULT  "" COMMENT '创建人所属单位';
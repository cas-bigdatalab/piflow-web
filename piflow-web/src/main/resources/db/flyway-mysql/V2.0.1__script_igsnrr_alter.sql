ALTER TABLE `sys_user` ADD COLUMN organization VARCHAR(255) DEFAULT  "" COMMENT '单位名称';
ALTER TABLE data_product CHANGE COLUMN bak2 organization VARCHAR(255) DEFAULT NULL COMMENT '单位名称';
ALTER TABLE flow_process_group ADD COLUMN organization VARCHAR(255) DEFAULT  "" COMMENT '单位名称';
ALTER TABLE flow_process ADD COLUMN organization VARCHAR(255) DEFAULT  "" COMMENT '单位名称';
ALTER TABLE vis_dataBase_info ADD COLUMN organization VARCHAR(255) DEFAULT  "" COMMENT '单位名称';
ALTER TABLE vis_graph_conf ADD COLUMN organization VARCHAR(255) DEFAULT  "" COMMENT '单位名称';
ALTER TABLE vis_graph_template ADD COLUMN organization VARCHAR(255) DEFAULT  "" COMMENT '单位名称';
-----------------------------------
--alter Table structure for flow_stops_publishing_property
-----------------------------------

ALTER TABLE user_role MODIFY COLUMN user_id varchar(40) NOT NULL COMMENT '用户ID';
INSERT INTO `role` (id,parent_id,code,name,description,crt_dttm,crt_user,enable_flag,last_update_dttm,last_update_user,version,bak1,bak2,bak3) VALUES
    (1,0,'ADMIN','管理员','管理员','2024-02-27 17:54:49','admin',1,'2024-02-27 17:54:49','admin',0,NULL,NULL,NULL);
INSERT INTO user_role (id,user_id,username,role_id,role_code,crt_dttm,crt_user,enable_flag,last_update_dttm,last_update_user) VALUES
    (1,'bef148e608004bd8a72e658fed2f9c9f','admin',1,'ADMIN','2024-02-27 17:54:49','admin',1,'2024-02-27 17:54:49','admin');
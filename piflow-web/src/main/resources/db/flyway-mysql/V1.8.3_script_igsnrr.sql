-----------------------------------
--alter Table structure for flow_stops_publishing_property
-----------------------------------

alter table `user_role` add `username` varchar(255) NOT NULL COMMENT '用户名' after `user_id`;
alter table `user_role` add `role_code` varchar(255) NOT NULL COMMENT '角色唯一编码' after `role_id`;
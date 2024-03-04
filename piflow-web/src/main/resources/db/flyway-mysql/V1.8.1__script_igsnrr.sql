-----------------------------------
--alter Table structure for flow_stops_publishing_property
-----------------------------------

alter table `flow_stops_publishing_property` add `stop_bundle` varchar(255) NULL DEFAULT NULL COMMENT '参数bundle' after `stop_id`;
alter table `flow_stops_publishing_property` add `property_name` varchar(255) NULL DEFAULT NULL COMMENT '参数名称' after `property_id`
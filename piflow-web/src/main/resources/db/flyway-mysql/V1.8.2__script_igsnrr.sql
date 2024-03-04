-----------------------------------
--alter Table structure for flow_stops_publishing_property
-----------------------------------

ALTER TABLE flow_stops_publishing_property MODIFY COLUMN description text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '参数描述';
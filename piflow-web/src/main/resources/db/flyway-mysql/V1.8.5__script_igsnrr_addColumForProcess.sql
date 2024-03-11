-----------------------------------
--alter Table structure for flow_process_stop
-----------------------------------

ALTER TABLE flow_process_stop ADD flow_stop_id varchar(40) DEFAULT "" NOT NULL COMMENT '关联flow stop id';
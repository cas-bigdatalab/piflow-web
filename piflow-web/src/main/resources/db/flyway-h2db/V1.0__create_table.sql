CREATE TABLE IF NOT EXISTS "flow"(
    "id" VARCHAR(40) PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    "crt_dttm" DATETIME NOT NULL COMMENT 'Create date time',
    "crt_user" VARCHAR(255) NOT NULL COMMENT 'Create user',
    "enable_flag" BIT NOT NULL COMMENT 'Enable flag',
    "last_update_dttm" DATETIME NOT NULL COMMENT 'Last update date time',
    "last_update_user" VARCHAR(255) NOT NULL COMMENT 'Last update user',
    "version" BIGINT COMMENT 'Version',
    "description" TEXT(0) COMMENT 'Eescription',
    "driver_memory" VARCHAR(255) COMMENT 'DriverMemory',
    "executor_cores" VARCHAR(255) COMMENT 'ExecutorCores',
    "executor_memory" VARCHAR(255) COMMENT 'ExecutorMemory',
    "executor_number" VARCHAR(255) COMMENT 'ExecutorNumber',
    "is_example" BIT COMMENT 'Is example',
    "name" VARCHAR(255) COMMENT 'Flow name',
    "uuid" VARCHAR(255) COMMENT 'Flow uuid'
);

CREATE TABLE IF NOT EXISTS "flow_path"(
    "id" VARCHAR(40) PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    "crt_dttm" DATETIME NOT NULL COMMENT 'Create date time',
    "crt_user" VARCHAR(255) NOT NULL COMMENT 'Create user',
    "enable_flag" BIT NOT NULL COMMENT 'Enable flag',
    "last_update_dttm" DATETIME NOT NULL COMMENT 'Last update date time',
    "last_update_user" VARCHAR(255) NOT NULL COMMENT 'Last update user',
    "version" BIGINT COMMENT 'Version',
    "line_from" VARCHAR(255) COMMENT 'Line from',
    "line_inport" VARCHAR(255) COMMENT 'Line in port',
    "line_outport" VARCHAR(255) COMMENT 'Line out port',
    "page_id" VARCHAR(255) COMMENT 'Page id',
    "line_to" VARCHAR(255) COMMENT 'Line to',
    "fk_flow_id" VARCHAR(40) COMMENT 'Foreign key flow id'
);
ALTER TABLE `flow_path` ADD CONSTRAINT `FK33rp96r4290eonsirbwrp8h0f` FOREIGN KEY (`fk_flow_id`) REFERENCES `flow` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE TABLE IF NOT EXISTS "flow_process"(
    "id" VARCHAR(40) PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    "crt_dttm" DATETIME NOT NULL COMMENT 'Create date time',
    "crt_user" VARCHAR(255) NOT NULL COMMENT 'Create user',
    "enable_flag" BIT NOT NULL COMMENT 'Enable flag',
    "last_update_dttm" DATETIME NOT NULL COMMENT 'Last update date time',
    "last_update_user" VARCHAR(255) NOT NULL COMMENT 'Last update user',
    "version" BIGINT COMMENT 'Version',
    "app_id" VARCHAR(255) COMMENT 'The id returned when calling runProcess',
    "description" VARCHAR(1024) COMMENT 'Description',
    "driver_memory" VARCHAR(255) COMMENT 'Driver memory',
    "end_time" DATETIME COMMENT 'End time of the process',
    "executor_cores" VARCHAR(255) COMMENT 'Executor_cores',
    "executor_memory" VARCHAR(255) COMMENT 'Executor memory',
    "executor_number" VARCHAR(255) COMMENT 'Executor number',
    "flow_id" VARCHAR(255) COMMENT 'Flow id',
    "name" VARCHAR(255) COMMENT 'Process name',
    "parent_process_id" VARCHAR(255) COMMENT 'Third parentProcessId',
    "process_id" VARCHAR(255) COMMENT 'Third processId',
    "progress" VARCHAR(255) COMMENT 'Process progress',
    "start_time" DATETIME COMMENT 'Process startup time',
    "state" VARCHAR(255) COMMENT 'Process status',
    "view_xml" TEXT COMMENT 'Process view xml string',
);

CREATE TABLE IF NOT EXISTS "flow_process_path"(
    "id" VARCHAR(40) PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    "crt_dttm" DATETIME NOT NULL COMMENT 'Create date time',
    "crt_user" VARCHAR(255) NOT NULL COMMENT 'Create user',
    "enable_flag" BIT NOT NULL COMMENT 'Enable flag',
    "last_update_dttm" DATETIME NOT NULL COMMENT 'Last update date time',
    "last_update_user" VARCHAR(255) NOT NULL COMMENT 'Last update user',
    "version" BIGINT COMMENT 'Version',
    "line_from" VARCHAR(255) COMMENT 'Line from',
    "line_inport" VARCHAR(255) COMMENT 'Line in port',
    "line_outport" VARCHAR(255) COMMENT 'Line out port',
    "page_id" VARCHAR(255) COMMENT 'Page id',
    "line_to" VARCHAR(255) COMMENT 'Line to',
    "fk_flow_process_id" VARCHAR(40) COMMENT 'Foreign key flow process id'
);
ALTER TABLE `flow_process_path` ADD CONSTRAINT `FKad4n0sl8j977awtec5beyrphy` FOREIGN KEY (`fk_flow_process_id`) REFERENCES `flow_process` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE TABLE IF NOT EXISTS "FLOW_PROCESS_STOP"(
    "id" VARCHAR(40) PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    "crt_dttm" DATETIME NOT NULL COMMENT 'Create date time',
    "crt_user" VARCHAR(255) NOT NULL COMMENT 'Create user',
    "enable_flag" BIT NOT NULL COMMENT 'Enable flag',
    "last_update_dttm" DATETIME NOT NULL COMMENT 'Last update date time',
    "last_update_user" VARCHAR(255) NOT NULL COMMENT 'Last update user',
    "version" BIGINT COMMENT 'Version',
    "bundel" VARCHAR(255) COMMENT 'Bundel',
    "description" VARCHAR(255) COMMENT 'Description',
    "end_time" DATETIME COMMENT 'End time',
    "groups" VARCHAR(255) COMMENT 'Groups',
    "in_port_type" VARCHAR(255) COMMENT 'In port type',
    "inports" VARCHAR(255) COMMENT 'In ports',
    "name" VARCHAR(255) COMMENT 'Name',
    "out_port_type" VARCHAR(255) COMMENT 'Out port type',
    "outports" VARCHAR(255) COMMENT 'Out ports',
    "owner" VARCHAR(255) COMMENT 'Owner',
    "page_id" VARCHAR(255) COMMENT 'Page id',
    "start_time" DATETIME COMMENT 'Start time',
    "state" VARCHAR(255) COMMENT 'ProcessStop status',
    "fk_flow_process_id" VARCHAR(40) COMMENT 'Foreign key flow process id'
);
ALTER TABLE `flow_process_stop` ADD CONSTRAINT `FK6rvjgxm3smnh3jjjnxnqiwl1p` FOREIGN KEY (`fk_flow_process_id`) REFERENCES `flow_process` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE TABLE IF NOT EXISTS "flow_process_stop_property"(
    "id" VARCHAR(40) PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    "crt_dttm" DATETIME NOT NULL COMMENT 'Create date time',
    "crt_user" VARCHAR(255) NOT NULL COMMENT 'Create user',
    "enable_flag" BIT NOT NULL COMMENT 'Enable flag',
    "last_update_dttm" DATETIME NOT NULL COMMENT 'Last update date time',
    "last_update_user" VARCHAR(255) NOT NULL COMMENT 'Last update user',
    "version" BIGINT COMMENT 'Version',
    "allowable_values" VARCHAR(255) COMMENT 'Allowable values',
    "custom_value" VARCHAR(255) COMMENT 'Custom value',
    "description" VARCHAR(1024) COMMENT 'Description',
    "display_name" VARCHAR(255) COMMENT 'Display name',
    "name" VARCHAR(255) COMMENT 'Description',
    "property_required" BIT COMMENT 'Property required',
    "property_sensitive" BIT COMMENT 'Property sensitive',
    "fk_flow_process_stop_id" VARCHAR(40) COMMENT 'Foreign key flowProcessStop id'
);
ALTER TABLE `flow_process_stop_property` ADD CONSTRAINT `FK6pqbouerl5dg97la1yqygj5rp` FOREIGN KEY (`fk_flow_process_stop_id`) REFERENCES `flow_process_stop` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE TABLE IF NOT EXISTS "flow_sotps_groups"(
    "id" VARCHAR(40) PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    "crt_dttm" DATETIME NOT NULL COMMENT 'Create date time',
    "crt_user" VARCHAR(255) NOT NULL COMMENT 'Create user',
    "enable_flag" BIT NOT NULL COMMENT 'Enable flag',
    "last_update_dttm" DATETIME NOT NULL COMMENT 'Last update date time',
    "last_update_user" VARCHAR(255) NOT NULL COMMENT 'Last update user',
    "version" BIGINT COMMENT 'Version',
    "group_name" VARCHAR(255) COMMENT 'Group name'
);

CREATE TABLE IF NOT EXISTS "flow_stops"(
    "id" VARCHAR(40) PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    "crt_dttm" DATETIME NOT NULL COMMENT 'Create date time',
    "crt_user" VARCHAR(255) NOT NULL COMMENT 'Create user',
    "enable_flag" BIT NOT NULL COMMENT 'Enable flag',
    "last_update_dttm" DATETIME NOT NULL COMMENT 'Last update date time',
    "last_update_user" VARCHAR(255) NOT NULL COMMENT 'Last update user',
    "version" BIGINT COMMENT 'Version',
    "bundel" VARCHAR(255) COMMENT 'Bundel',
    "description" TEXT(0) COMMENT 'Description',
    "groups" VARCHAR(255) COMMENT 'Groups',
    "in_port_type" VARCHAR(255) COMMENT 'In port type',
    "inports" VARCHAR(255) COMMENT 'In ports',
    "is_checkpoint" BIT COMMENT 'Is checkpoint',
    "name" VARCHAR(255) COMMENT 'name',
    "out_port_type" VARCHAR(255) COMMENT 'Out port type',
    "outports" VARCHAR(255) COMMENT 'Out ports',
    "owner" VARCHAR(255) COMMENT 'Owner',
    "page_id" VARCHAR(255) COMMENT 'Page id',
    "start_time" DATETIME COMMENT 'Start time',
    "state" VARCHAR(255) COMMENT 'FlowStop state',
    "stop_time" DATETIME COMMENT 'Stop time',
    "fk_flow_id" VARCHAR(40) COMMENT 'Foreign key flow id'
);
ALTER TABLE `flow_stops` ADD CONSTRAINT `FK11mku3yphyjswbtwj9df79k44` FOREIGN KEY (`fk_flow_id`) REFERENCES `FLOW` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE TABLE IF NOT EXISTS "flow_stops_property"(
    "id" VARCHAR(40) PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    "crt_dttm" DATETIME NOT NULL COMMENT 'Create date time',
    "crt_user" VARCHAR(255) NOT NULL COMMENT 'Create user',
    "enable_flag" BIT NOT NULL COMMENT 'Enable flag',
    "last_update_dttm" DATETIME NOT NULL COMMENT 'Last update date time',
    "last_update_user" VARCHAR(255) NOT NULL COMMENT 'Last update user',
    "version" BIGINT COMMENT 'Version',
    "allowable_values" VARCHAR(255) COMMENT 'Allowable values',
    "custom_value" TEXT(0) COMMENT 'Custom value',
    "description" TEXT(0) COMMENT 'Description',
    "display_name" VARCHAR(255) COMMENT 'Display name',
    "is_select" BIT COMMENT 'Is select',
    "name" VARCHAR(255) COMMENT 'name',
    "property_required" BIT COMMENT 'Property required',
    "property_sensitive" BIT COMMENT 'Property sensitive',
    "fk_stops_id" VARCHAR(40) COMMENT 'Foreign key stops id'
);
ALTER TABLE `flow_stops_property` ADD CONSTRAINT `FKsjcg9klyumklhkpl8408v6uuq` FOREIGN KEY (`fk_stops_id`) REFERENCES `flow_stops` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE TABLE IF NOT EXISTS "flow_stops_template"(
    "id" VARCHAR(40) PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    "crt_dttm" DATETIME NOT NULL COMMENT 'Create date time',
    "crt_user" VARCHAR(255) NOT NULL COMMENT 'Create user',
    "enable_flag" BIT NOT NULL COMMENT 'Enable flag',
    "last_update_dttm" DATETIME NOT NULL COMMENT 'Last update date time',
    "last_update_user" VARCHAR(255) NOT NULL COMMENT 'Last update user',
    "version" BIGINT COMMENT 'Version',
    "bundel" VARCHAR(255) COMMENT 'Bundel',
    "description" TEXT(0) COMMENT 'description',
    "groups" VARCHAR(255) COMMENT 'Groups',
    "in_port_type" VARCHAR(255) COMMENT 'In port type',
    "inports" VARCHAR(255) COMMENT 'In ports',
    "name" VARCHAR(255) COMMENT 'name',
    "out_port_type" VARCHAR(255) COMMENT 'Out port type',
    "outports" VARCHAR(255) COMMENT 'Out ports',
    "owner" VARCHAR(255) COMMENT 'Owner',
    "STOP_GROUP" VARCHAR(255) COMMENT 'stopGroup'
);

CREATE TABLE IF NOT EXISTS "flow_stops_property_template"(
    "id" VARCHAR(40) PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    "crt_dttm" DATETIME NOT NULL COMMENT 'Create date time',
    "crt_user" VARCHAR(255) NOT NULL COMMENT 'Create user',
    "enable_flag" BIT NOT NULL COMMENT 'Enable flag',
    "last_update_dttm" DATETIME NOT NULL COMMENT 'Last update date time',
    "last_update_user" VARCHAR(255) NOT NULL COMMENT 'Last update user',
    "version" BIGINT COMMENT 'Version',
    "allowable_values" TEXT(0) COMMENT 'Default value',
    "default_value" TEXT(0) COMMENT 'Default value',
    "description" TEXT(0) COMMENT 'Description',
    "display_name" TEXT(0) COMMENT 'Display name',
    "name" VARCHAR(255) COMMENT 'Name',
    "property_required" BIT COMMENT 'Property required',
    "property_sensitive" BIT COMMENT 'Property sensitive',
    "fk_stops_id" VARCHAR(255) COMMENT 'Foreign key stops id'
);
ALTER TABLE `flow_stops_property_template` ADD CONSTRAINT `FKhtnjkpgjkx21r2qf4r3q3mjr9` FOREIGN KEY (`fk_stops_id`) REFERENCES `flow_stops_template` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE TABLE IF NOT EXISTS "flow_template"(
    "id" VARCHAR(40) PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    "crt_dttm" DATETIME NOT NULL COMMENT 'Create date time',
    "crt_user" VARCHAR(255) NOT NULL COMMENT 'Create user',
    "enable_flag" BIT NOT NULL COMMENT 'Enable flag',
    "last_update_dttm" DATETIME NOT NULL COMMENT 'Last update date time',
    "last_update_user" VARCHAR(255) NOT NULL COMMENT 'Last update user',
    "version" BIGINT COMMENT 'Version',
    "description" VARCHAR(1024) COMMENT 'description',
    "name" VARCHAR(255) COMMENT 'Name',
    "path" VARCHAR(255) COMMENT 'Path',
    "value" LONGTEXT COMMENT 'Value',
    "fk_flow_id" VARCHAR(40) COMMENT 'Foreign key flow id'
);
ALTER TABLE `flow_template` ADD CONSTRAINT `FKkcg573sjiknyhppuc0q62a0kj` FOREIGN KEY (`fk_flow_id`) REFERENCES `flow` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE TABLE IF NOT EXISTS "association_groups_stops_template"(
    "groups_id" VARCHAR(40) NOT NULL COMMENT 'Group primary key id',
    "stops_template_id" VARCHAR(40) NOT NULL COMMENT 'stops_template primary key id'
);
ALTER TABLE `association_groups_stops_template` ADD CONSTRAINT `FK5ceurc1karlogl9ppecmkcp7e` FOREIGN KEY (`groups_id`) REFERENCES `flow_sotps_groups` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `association_groups_stops_template` ADD CONSTRAINT `FKqwv1iytgkhhgnjdvhqbskncf4` FOREIGN KEY (`stops_template_id`) REFERENCES `flow_stops_template` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE TABLE IF NOT EXISTS "mx_graph_model"(
    "id" VARCHAR(40) PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    "crt_dttm" DATETIME NOT NULL COMMENT 'Create date time',
    "crt_user" VARCHAR(255) NOT NULL COMMENT 'Create user',
    "enable_flag" BIT NOT NULL COMMENT 'Enable flag',
    "last_update_dttm" DATETIME NOT NULL COMMENT 'Last update date time',
    "last_update_user" VARCHAR(255) NOT NULL COMMENT 'Last update user',
    "version" BIGINT COMMENT 'Version',
    "mx_arrows" VARCHAR(255) COMMENT 'mx_arrows',
    "mx_background" VARCHAR(255) COMMENT 'mx_background',
    "mx_connect" VARCHAR(255) COMMENT 'mx_connect',
    "mx_dx" VARCHAR(255) COMMENT 'mx_dx',
    "mx_dy" VARCHAR(255) COMMENT 'mx_dy',
    "mx_fold" VARCHAR(255) COMMENT 'mx_fold',
    "mx_grid" VARCHAR(255) COMMENT 'mx_grid',
    "mx_gridsize" VARCHAR(255) COMMENT 'mx_gridsize',
    "mx_guides" VARCHAR(255) COMMENT 'mx_guides',
    "mx_page" VARCHAR(255) COMMENT 'mx_page',
    "mx_pageheight" VARCHAR(255) COMMENT 'mx_pageheight',
    "mx_pagescale" VARCHAR(255) COMMENT 'mx_pagescale',
    "mx_pagewidth" VARCHAR(255) COMMENT 'mx_pagewidth',
    "mx_tooltips" VARCHAR(255) COMMENT 'mx_tooltips',
    "fk_flow_id" VARCHAR(40) COMMENT 'Foreign key flow id'
);
ALTER TABLE `mx_graph_model` ADD CONSTRAINT `FKktpy5kv5fgya1gn012g7395l9` FOREIGN KEY (`fk_flow_id`) REFERENCES `flow` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE TABLE IF NOT EXISTS "MX_CELL"(
    "id" VARCHAR(40) PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    "crt_dttm" DATETIME NOT NULL COMMENT 'Create date time',
    "crt_user" VARCHAR(255) NOT NULL COMMENT 'Create user',
    "enable_flag" BIT NOT NULL COMMENT 'Enable flag',
    "last_update_dttm" DATETIME NOT NULL COMMENT 'Last update date time',
    "last_update_user" VARCHAR(255) NOT NULL COMMENT 'Last update user',
    "version" BIGINT COMMENT 'Version',
    "mx_edge" VARCHAR(255) COMMENT 'mx_edge',
    "mx_pageid" VARCHAR(255) COMMENT 'mx_pageid',
    "mx_parent" VARCHAR(255) COMMENT 'mx_parent',
    "mx_source" VARCHAR(255) COMMENT 'mx_source',
    "mx_style" VARCHAR(255) COMMENT 'mx_style',
    "mx_target" VARCHAR(255) COMMENT 'mx_target',
    "mx_value" VARCHAR(255) COMMENT 'mx_value',
    "mx_vertex" VARCHAR(255) COMMENT 'mx_vertex',
    "fk_mx_graph_id" VARCHAR(40) COMMENT 'Foreign key fk_mx_graph_id'
);
ALTER TABLE `mx_cell` ADD CONSTRAINT `FK4s2gnt8t7e5ok1v7r3v99pji5` FOREIGN KEY (`fk_mx_graph_id`) REFERENCES `mx_graph_model` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE TABLE IF NOT EXISTS "mx_geometry"(
    "id" VARCHAR(40) PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    "crt_dttm" DATETIME NOT NULL COMMENT 'Create date time',
    "crt_user" VARCHAR(255) NOT NULL COMMENT 'Create user',
    "enable_flag" BIT NOT NULL COMMENT 'Enable flag',
    "last_update_dttm" DATETIME NOT NULL COMMENT 'Last update date time',
    "last_update_user" VARCHAR(255) NOT NULL COMMENT 'Last update user',
    "version" BIGINT COMMENT 'Version',
    "mx_as" VARCHAR(255) COMMENT 'mx_as',
    "mx_height" VARCHAR(255) COMMENT 'mx_height',
    "mx_relative" VARCHAR(255) COMMENT 'mx_relative',
    "mx_width" VARCHAR(255) COMMENT 'mx_width',
    "mx_x" VARCHAR(255) COMMENT 'mx_x',
    "mx_y" VARCHAR(255) COMMENT 'mx_y',
    "fk_mx_cell_id" VARCHAR(40) COMMENT 'Foreign key fk_mx_cell_id'
);
ALTER TABLE `mx_geometry` ADD CONSTRAINT `FK6elkg2vbxxjrun0qaqaajwgfu` FOREIGN KEY (`fk_mx_cell_id`) REFERENCES `mx_cell` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE TABLE IF NOT EXISTS "stops_template"(
    "id" VARCHAR(40) PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    "bundel" VARCHAR(255) COMMENT 'Bundel',
    "crt_dttm" DATETIME NOT NULL COMMENT 'Create date time',
    "crt_user" VARCHAR(255) COMMENT 'Create user',
    "description" VARCHAR(255) COMMENT 'Description',
    "enable_flag" BIT NOT NULL COMMENT 'Enable flag',
    "groups" VARCHAR(255) COMMENT 'Groups',
    "in_port_type" VARCHAR(255) COMMENT 'In port type',
    "inports" VARCHAR(255) COMMENT 'In ports',
    "is_checkpoint" BIT COMMENT 'Is checkpoint',
    "name" VARCHAR(255) COMMENT 'Name',
    "out_port_type" VARCHAR(255) COMMENT 'Out port type',
    "outports" VARCHAR(255) COMMENT 'Out ports',
    "owner" VARCHAR(255) COMMENT 'Owner',
    "page_id" VARCHAR(255) COMMENT 'Page id',
    "version" BIGINT COMMENT 'Version',
    "fk_template_id" VARCHAR(40) 'Foreign key fk_template_id'
);
ALTER TABLE `stops_template` ADD CONSTRAINT `FKn0wu7i6frf0xp2iypda50vlmh` FOREIGN KEY (`fk_template_id`) REFERENCES `flow_template` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE TABLE IF NOT EXISTS "property_template"(
    "id" VARCHAR(40) PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    "allowable_values" VARCHAR(255) COMMENT 'Allowable values',
    "crt_dttm" DATETIME NOT NULL COMMENT 'Create date time',
    "crt_user" VARCHAR(255) COMMENT 'Create user',
    "custom_value" VARCHAR(255) COMMENT 'Custom value',
    "description" VARCHAR(1024) COMMENT 'Description',
    "display_name" VARCHAR(255) COMMENT 'Display name',
    "enable_flag" BIT NOT NULL COMMENT 'Enable flag',
    "is_select" BIT COMMENT 'Is select',
    "name" VARCHAR(255) COMMENT 'Name',
    "property_required" BIT COMMENT 'Property required',
    "property_sensitive" BIT COMMENT 'Property sensitive',
    "version" BIGINT COMMENT 'Version',
    "fk_stops_id" VARCHAR(40) COMMENT 'Foreign key stops id'
);
ALTER TABLE `property_template` ADD CONSTRAINT `FK35p1h6w0dsmjc33eavnbuiys3` FOREIGN KEY (`fk_stops_id`) REFERENCES `stops_template` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE TABLE IF NOT EXISTS "sys_user"(
    "id" VARCHAR(40) PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    "crt_dttm" DATETIME NOT NULL COMMENT 'Create date time',
    "crt_user" VARCHAR(255) NOT NULL COMMENT 'Create user',
    "enable_flag" BIT NOT NULL COMMENT 'Enable flag',
    "last_update_dttm" DATETIME NOT NULL COMMENT 'Last update date time',
    "last_update_user" VARCHAR(255) NOT NULL COMMENT 'Last update user',
    "version" BIGINT COMMENT 'Version',
    "age" INTEGER COMMENT 'Age',
    "name" VARCHAR(255) COMMENT 'Name',
    "password" VARCHAR(255) COMMENT 'Password',
    "sex" VARCHAR(255) COMMENT 'Sex',
    "username" VARCHAR(255) COMMENT 'Username'
);
CREATE TABLE IF NOT EXISTS "sys_role"(
    "id" INTEGER PRIMARY KEY NOT NULL COMMENT 'Primary key id',
    "role" VARCHAR(255) COMMENT 'role',
    "fk_sys_user_id" VARCHAR(40) COMMENT 'Foreign key fk_sys_user_id'
);
ALTER TABLE `sys_role` ADD CONSTRAINT `FK48hlg5qgnejc4xropo99whsyt` FOREIGN KEY (`fk_sys_user_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE TABLE IF NOT EXISTS "statistics" (
    "id" VARCHAR(40) NOT NULL COMMENT 'Primary key id',
    "login_ip" VARCHAR(255) COMMENT 'login_ip',
    "login_time" DATETIME COMMENT 'login_time',
    "login_user" VARCHAR(255) COMMENT 'login_user'
);
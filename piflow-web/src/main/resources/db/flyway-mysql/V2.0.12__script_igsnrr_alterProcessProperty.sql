-- ----------------------------
-- alter flow_process_stop_property
-- ----------------------------

ALTER TABLE `flow_process_stop_property` ADD COLUMN type tinyint(11) DEFAULT  NULL COMMENT '类型 0file 1common 2output';

CREATE TABLE `password_reset_token` (
    id VARCHAR(40) PRIMARY KEY COMMENT 'UUID',
    user_id VARCHAR(40) NOT NULL COMMENT '关联用户ID',
    token VARCHAR(100) NOT NULL UNIQUE COMMENT '重置令牌',
    expiry_date DATETIME NOT NULL COMMENT '过期时间',
    used BOOLEAN DEFAULT FALSE COMMENT '是否使用过'
);

ALTER TABLE `flow_process` ADD COLUMN `contain_input` bit(1) DEFAULT NULL COMMENT '是否包含输入';
ALTER TABLE `flow_process_group` ADD COLUMN `contain_input` bit(1) DEFAULT NULL COMMENT '是否包含输入';
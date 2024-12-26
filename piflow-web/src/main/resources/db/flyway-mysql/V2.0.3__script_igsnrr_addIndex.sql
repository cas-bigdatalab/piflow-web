-- ----------------------------
-- alter data_product
-- ----------------------------

CREATE INDEX idx_state ON data_product (state);
CREATE INDEX idx_role ON sys_role (role);
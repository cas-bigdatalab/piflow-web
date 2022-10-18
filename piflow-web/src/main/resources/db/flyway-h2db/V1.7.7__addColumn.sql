-- ----------------------------
-- Add column
-- ----------------------------
ALTER TABLE `STOPS_HUB` ADD COLUMN `bundles` Text NULL DEFAULT NULL COMMENT 'bundle';
ALTER TABLE `STOPS_HUB` ADD COLUMN `is_publishing` bit(1) NULL DEFAULT 0 COMMENT 'Is Publishing';
ALTER TABLE `SYS_USER` ADD COLUMN `developer_access_key` VARCHAR(255) NULL DEFAULT NULL COMMENT 'developer access key';
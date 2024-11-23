ALTER TABLE flow_process CHANGE COLUMN start_time start_time_old DATETIME;
ALTER TABLE flow_process ADD COLUMN start_time VARCHAR(50);
UPDATE flow_process SET start_time = DATE_FORMAT(start_time_old, '%Y-%m-%d %H:%i:%s');
ALTER TABLE flow_process DROP COLUMN start_time_old;

ALTER TABLE flow_process CHANGE COLUMN end_time end_time_old DATETIME;
ALTER TABLE flow_process ADD COLUMN end_time VARCHAR(50);
UPDATE flow_process SET end_time = DATE_FORMAT(end_time_old, '%Y-%m-%d %H:%i:%s');
ALTER TABLE flow_process DROP COLUMN end_time_old;

ALTER TABLE flow_process_stop CHANGE COLUMN start_time start_time_old DATETIME;
ALTER TABLE flow_process_stop ADD COLUMN start_time VARCHAR(50);
UPDATE flow_process_stop SET start_time = DATE_FORMAT(start_time_old, '%Y-%m-%d %H:%i:%s');
ALTER TABLE flow_process_stop DROP COLUMN start_time_old;

ALTER TABLE flow_process_stop CHANGE COLUMN end_time end_time_old DATETIME;
ALTER TABLE flow_process_stop ADD COLUMN end_time VARCHAR(50);
UPDATE flow_process_stop SET end_time = DATE_FORMAT(end_time_old, '%Y-%m-%d %H:%i:%s');
ALTER TABLE flow_process_stop DROP COLUMN end_time_old;


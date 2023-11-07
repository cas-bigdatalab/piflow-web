-----------------------------------
--fix the cron expression of RunningGroupScheduleSync
-----------------------------------

UPDATE `sys_schedule` SET `cron_expression`='0 */1 * * * ?' WHERE id='b494d4fecea148709a0d81cbb39e7f54';

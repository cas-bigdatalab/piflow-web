package cn.cnic.base.util;

import cn.cnic.component.system.entity.SysSchedule;
import org.quartz.*;
import org.slf4j.Logger;

public class QuartzUtils {

    static Logger logger = LoggerUtil.getLogger();

    /**
     * Create a timed task(The default startup state after a scheduled task is created)
     *
     * @param scheduler   scheduler
     * @param sysSchedule Timing task information class
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static void createScheduleJob(Scheduler scheduler, SysSchedule sysSchedule) {
        try {
            //Get the execution class of the timed task (must be the absolute path name of the class)
            //The timing task class needs to be a concrete implementation of the job class. QuartzJobBean is an abstract class for jobs.
            Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(sysSchedule.getJobClass());
            // Build timed task information
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(sysSchedule.getJobName()).build();
            // Set the timing task execution mode
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(sysSchedule.getCronExpression());
            // Build trigger trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(sysSchedule.getJobName()).withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (ClassNotFoundException e) {
            logger.error("Timed task class path error: Please enter the absolute path of the class", e);
        } catch (SchedulerException e) {
            logger.error("Error creating scheduled task:", e);
        }
    }

    /**
     * Pause a scheduled task based on the task name
     *
     * @param scheduler scheduler
     * @param jobName   Scheduled task name
     * @throws SchedulerException
     */
    public static void pauseScheduleJob(Scheduler scheduler, String jobName) {
        JobKey jobKey = JobKey.jobKey(jobName);
        try {
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            logger.error("Error suspending scheduled task:", e);
        }
    }

    /**
     * Resume scheduled tasks based on task name
     *
     * @param scheduler scheduler
     * @param jobName   Scheduled task name
     * @throws SchedulerException
     */
    public static void resumeScheduleJob(Scheduler scheduler, String jobName) {
        JobKey jobKey = JobKey.jobKey(jobName);
        try {
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            logger.error("Error resuming scheduled task:", e);
        }
    }

    /**
     * Run a scheduled task immediately based on the task name
     *
     * @param scheduler scheduler
     * @param jobName   Scheduled task name
     * @throws SchedulerException
     */
    public static void runOnce(Scheduler scheduler, String jobName) {
        JobKey jobKey = JobKey.jobKey(jobName);
        try {
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            logger.error("Error running scheduled task：", e);
        }
    }

    /**
     * Update scheduled tasks
     *
     * @param scheduler   scheduler
     * @param sysSchedule Scheduled task name
     * @throws SchedulerException
     */
    public static void updateScheduleJob(Scheduler scheduler, SysSchedule sysSchedule) {
        try {
            //Get the trigger for the corresponding task
            TriggerKey triggerKey = TriggerKey.triggerKey(sysSchedule.getJobName());
            //Set the timing task execution mode
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(sysSchedule.getCronExpression());
            //Trigger trigger to rebuild the task
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            //Reset the corresponding job
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            logger.error("Update timing task error:", e);
        }
    }

    /**
     * Delete a scheduled task from the scheduler based on the scheduled task name
     *
     * @param scheduler scheduler
     * @param jobName   Scheduled task name
     * @throws SchedulerException
     */
    public static void deleteScheduleJob(Scheduler scheduler, String jobName) {
        JobKey jobKey = JobKey.jobKey(jobName);
        try {
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            logger.error("Error deleting scheduled task：", e);
        }
    }
}
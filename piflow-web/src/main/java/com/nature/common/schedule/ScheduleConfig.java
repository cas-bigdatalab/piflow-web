package com.nature.common.schedule;

import com.nature.base.util.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

@Configuration
@EnableAsync
public class ScheduleConfig {

    /**
     * Introduce the log, note that all are under the "org.slf4j" package
     */
    Logger logger = LoggerUtil.getLogger();

    @Resource
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    private ScheduledFuture<?> future;

    private String cron = "";


    public String getCron() {
        return cron;
    }

    public void stopCron() {
        if (future != null) {
            //取消定时任务
            future.cancel(true);
        }
    }

    public void setCron(String cron,ISchedule iSchedule) {
        this.cron = cron;
        stopCron();
        future = threadPoolTaskScheduler.schedule(new Runnable() {

            @Override
            public void run() {
                try {
                    iSchedule.taskSync();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                if ("".equals(cron) || cron == null) {
                    return null;
                }
                // 定时任务触发，可修改定时任务的执行周期
                CronTrigger trigger = new CronTrigger(cron);
                Date nextExecDate = trigger.nextExecutionTime(triggerContext);
                return nextExecDate;
            }
        });
    }
}
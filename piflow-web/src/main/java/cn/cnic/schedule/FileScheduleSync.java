package cn.cnic.schedule;

import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.component.schedule.service.IScheduleService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;


@Component
public class FileScheduleSync extends QuartzJobBean {

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();

    private final IScheduleService scheduleServiceImpl;

    @Autowired
    public FileScheduleSync(IScheduleService scheduleServiceImpl) {
        this.scheduleServiceImpl = scheduleServiceImpl;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {

        logger.warn("file schedule start!!");
        try {
            scheduleServiceImpl.run();
        } catch (Exception e) {
            logger.error("file schedule error,message:{}",e.getMessage());
        }
        logger.warn("file schedule run success!!");
    }
}
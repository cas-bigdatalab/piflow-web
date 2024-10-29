package cn.cnic.schedule;

import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.SpringContextUtil;
import cn.cnic.common.executor.ServicesExecutor;
import cn.cnic.component.process.domain.ProcessDomain;
import cn.cnic.third.market.service.IMarket;
import cn.cnic.third.service.IFlow;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;


@Component
public class FairManSync extends QuartzJobBean {

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();

    private final IMarket marketImpl;

    @Autowired
    public FairManSync(IMarket marketImpl) {
        this.marketImpl = marketImpl;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {

        logger.info("send statistics to fairman start!!");
        try {
            marketImpl.sendStatisticToFairMan();
        } catch (Exception e) {
            logger.info("send statistics to fairman error,message:{}",e.getMessage());
        }
        logger.info("send statistics to fairman finish!!");
    }
}
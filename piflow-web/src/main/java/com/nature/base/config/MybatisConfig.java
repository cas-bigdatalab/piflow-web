package com.nature.base.config;

import com.github.pagehelper.PageHelper;
import com.nature.base.util.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 分页插件配置
 *
 * @author Nature
 */
@Configuration
public class MybatisConfig {

    /**
     * 引入日志，注意都是"org.slf4j"包下
     */
    Logger logger = LoggerUtil.getLogger();

    @Bean
    public PageHelper pageHelper() {
        logger.debug("...pageHelper...");
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithoutCount", "true");
        p.setProperty("reasonable", "true");
        pageHelper.setProperties(p);
        return pageHelper;
    }
}
 
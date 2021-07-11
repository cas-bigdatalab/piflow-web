package cn.cnic.base.config;

import com.github.pagehelper.PageHelper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Paging plug-in configuration
 */
@Slf4j
@Configuration
public class MybatisConfig {

    @Bean
    public PageHelper pageHelper() {
        log.debug("...pageHelper...");
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithoutCount", "true");
        p.setProperty("reasonable", "true");
        pageHelper.setProperties(p);
        return pageHelper;
    }
}
 
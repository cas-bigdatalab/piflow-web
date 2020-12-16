package cn.cnic;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import cn.cnic.base.util.LoggerUtil;

@PropertySources({
        @PropertySource(value = "classpath:apiConfig.properties", encoding = "utf-8"),
        @PropertySource(value = "classpath:baseConfig.properties", encoding = "utf-8")
})
@MapperScan(basePackages = "cn.cnic.**.mapper.*.*")
@EnableTransactionManagement
@SpringBootApplication
public class Application {

    static Logger logger = LoggerUtil.getLogger();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        logger.warn("***************************************************************");
        logger.warn("***************** Spring Boot Startup Success *****************");
        logger.warn("***************************************************************");
    }
}

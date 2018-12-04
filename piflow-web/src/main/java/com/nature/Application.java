package com.nature;

import com.nature.base.util.LoggerUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan(basePackages = "com.nature.mapper.*.*")
@EnableTransactionManagement
@SpringBootApplication
public class Application {

    static Logger logger = LoggerUtil.getLogger();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        logger.warn("***************************************************************");
        logger.warn("******************  Spring Boot 启动成功  **************************");
        logger.warn("***************************************************************");
    }

}

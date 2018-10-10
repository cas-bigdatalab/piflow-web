package com.nature;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.nature.base.util.LoggerUtil;

@SpringBootApplication
@MapperScan(basePackages = "com.nature.mapper.*.*")
public class Application {

	static Logger logger = LoggerUtil.getLogger();

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		logger.warn("***************************************************************");
		logger.warn("******************  Spring Boot 启动成功  **************************");
		logger.warn("***************************************************************");
	}

}

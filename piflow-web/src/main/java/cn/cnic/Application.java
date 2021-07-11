package cn.cnic;

import cn.cnic.base.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@PropertySources({
        @PropertySource(value = "classpath:apiConfig.properties", encoding = "utf-8"),
        @PropertySource(value = "classpath:baseConfig.properties", encoding = "utf-8")
})
@MapperScan(basePackages = "cn.cnic.**.mapper.*.*")
@EnableTransactionManagement
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);
        SpringContextUtil.setApplicationContext(context);
        log.warn("***************************************************************");
        log.warn("***************** Spring Boot Startup Success *****************");
        log.warn("***************************************************************");
    }
    
    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> connector.setProperty("relaxedQueryChars", "|{}[]\\"));
        return factory;
    }
}

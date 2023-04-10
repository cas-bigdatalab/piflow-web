package cn.cnic;

import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.SpringContextUtil;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;


@PropertySources({
        @PropertySource(value = "classpath:apiConfig.properties", encoding = "utf-8"),
        @PropertySource(value = "classpath:baseConfig.properties", encoding = "utf-8"),
        @PropertySource(value = "classpath:messageConfig.properties", encoding = "utf-8"),
        @PropertySource(value = "classpath:docker.properties", encoding = "utf-8")
})
@MapperScan(basePackages = "cn.cnic.**.mapper.*.*")
@EnableTransactionManagement
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

	/**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private static Logger logger = LoggerUtil.getLogger();

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);
        SpringContextUtil.setApplicationContext(context);
        logger.warn("***************************************************************");
        logger.warn("***************** Spring Boot Startup Success *****************");
        logger.warn("***************************************************************");
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        SpringContextUtil.setApplicationContext(WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext));
    }

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> connector.setProperty("relaxedQueryChars", "|{}[]\\"));
        return factory;
    }
}

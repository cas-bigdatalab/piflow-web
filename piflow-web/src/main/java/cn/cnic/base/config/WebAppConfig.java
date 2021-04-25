package cn.cnic.base.config;

import cn.cnic.base.util.LoggerUtil;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import java.util.Arrays;

/**
 * @ClassName: WebAppConfig
 * @Description: TODO(Here is a sentence describing the function of this class.)
 */
@Configuration
public class WebAppConfig implements WebMvcConfigurer {
    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    Logger logger = LoggerUtil.getLogger();

    /**
     * Inject the first defined interceptor
     */
    @Autowired
    private ConfigInterceptor configInterceptor;


    //Method of accessing pictures and videos
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String storagePathHead = System.getProperty("user.dir");
        String imagesPath = ("file:" + storagePathHead + "/storage/image/");
        String videosPath = ("file:" + storagePathHead + "/storage/video/");
        String xmlPath = ("file:" + storagePathHead + "/storage/xml/");
        logger.info("imagesPath=" + imagesPath);
        logger.info("videosPath=" + videosPath);
        logger.info("xmlPath=" + xmlPath);
        registry.addResourceHandler("/images/**", "/videos/**", "/xml/**").addResourceLocations(imagesPath, videosPath, xmlPath);
        
        // Swagger2Config
        registry.addResourceHandler("/**")
        .addResourceLocations("classpath:/META-INF/resources/")
        .addResourceLocations("classpath:/resources/")
        .addResourceLocations("classpath:/static/")
        .addResourceLocations("classpath:/public/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(configInterceptor).excludePathPatterns(Arrays.asList("/components/**", "/js/**", "/css/**", "/img/**", "/img/*"));
    }

    /*
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

    }
    */

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "OPTIONS", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(false).maxAge(3600);
        WebMvcConfigurer.super.addCorsMappings(registry);
    }
}

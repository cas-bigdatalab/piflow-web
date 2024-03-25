package cn.cnic.base.config;


import cn.cnic.common.constant.SysParamsCache;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cn.cnic.base.utils.LoggerUtil;

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
    private Logger logger = LoggerUtil.getLogger();

    private final ConfigInterceptor configInterceptor;

    @Autowired
    public WebAppConfig(ConfigInterceptor configInterceptor) {
        this.configInterceptor = configInterceptor;
    }


    //Method of accessing pictures and videos
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String storagePathHead = System.getProperty("user.dir");
        SysParamsCache.setImagesPath(storagePathHead + "/../../storage/image/");
        SysParamsCache.setVideosPath(storagePathHead + "/../../storage/video/");
        SysParamsCache.setXmlPath(storagePathHead + "/../../storage/xml/");
        SysParamsCache.setCsvPath(storagePathHead + "/../../storage/csv/");
        SysParamsCache.setFilePath(storagePathHead + "/../../storage/files/");
        String imagesPath = ("file:" + SysParamsCache.IMAGES_PATH);
        String videosPath = ("file:" + SysParamsCache.VIDEOS_PATH);
        String xmlPath = ("file:" + SysParamsCache.XML_PATH);
        String filePath = ("file:" + SysParamsCache.FILE_PATH);
        logger.info("imagesPath=" + imagesPath);
        logger.info("videosPath=" + videosPath);
        logger.info("xmlPath=" + xmlPath);
        registry.addResourceHandler("/images/**", "/videos/**", "/xml/**", "/files/**").addResourceLocations(imagesPath, videosPath, xmlPath,filePath);
        
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

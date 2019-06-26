package com.nature.base.config;

import com.nature.base.util.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Administrator
 * @ClassName: WebAppConfig
 * @Description: TODO(Here is a sentence describing the function of this class.)
 * @date 2017-7-11
 */
@Configuration
public class WebAppConfig implements WebMvcConfigurer {
    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    Logger logger = LoggerUtil.getLogger();
    //Get the path of the image in the configuration file
    @Value("${syspara.imagesPath}")
    private String mImagesPath;
    //Get the path of the video in the configuration file
    @Value("${syspara.videosPath}")
    private String mVideosPath;

    //Method of accessing pictures and videos
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        logger.info("imagesPath=" + "file:" + mImagesPath);
        logger.info("videosPath=" + "file:" + mVideosPath);
        registry.addResourceHandler("/images/**", "/videos/**").addResourceLocations("file:" + mImagesPath, "file:" + mVideosPath);
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}

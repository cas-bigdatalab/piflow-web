package com.nature.base.config;

import com.nature.base.util.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Administrator
 * @ClassName: WebAppConfig
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2017年7月11日
 */
@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {
    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    Logger logger = LoggerUtil.getLogger();
    //获取配置文件中图片的路径
    @Value("${syspara.imagesPath}")
    private String mImagesPath;
    //获取配置文件中视频的路径
    @Value("${syspara.videosPath}")
    private String mVideosPath;

    //访问图片、视频方法
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        logger.info("imagesPath=" + "file:" + mImagesPath);
        logger.info("videosPath=" + "file:" + mVideosPath);
        registry.addResourceHandler("/images/**", "/videos/**").addResourceLocations("file:" + mImagesPath, "file:" + mVideosPath);
        super.addResourceHandlers(registry);
    }
}

package cn.cnic.base.config;

import cn.cnic.base.util.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.Arrays;

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

    /**
     * Inject the first defined interceptor
     */
    @Resource
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
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(configInterceptor).excludePathPatterns(Arrays.asList("/components/**", "/js/**", "/css/**", "/img/**", "/img/*"));
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // system
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/page/error/404").setViewName("errorPage");
        registry.addViewController("/page/bootPage/index").setViewName("bootPage");

        //index
        registry.addViewController("/page/dashboard").setViewName("dashboard");
        registry.addViewController("/page/flow/flow_List").setViewName("flow/flow_List");
        registry.addViewController("/page/flowGroup/flow_group_List").setViewName("flowGroup/flow_group_List");
        registry.addViewController("/page/processAndProcessGroup/process_processGroup_list").setViewName("processAndProcessGroup/process_processGroup_list");
        registry.addViewController("/page/admin/sys_schedule_List").setViewName("admin/sys_schedule_List");

        //macro
        registry.addViewController("/page/macro/fragmentMacro").setViewName("macro/fragmentMacro");
        registry.addViewController("/page/macro/graphHeadMacro").setViewName("macro/graphHeadMacro");
        registry.addViewController("/page/macro/publicUserHeadMacro").setViewName("macro/publicUserHeadMacro");
        registry.addViewController("/page/macro/headMacro").setViewName("macro/headMacro");

        // datasource
        registry.addViewController("/page/datasource/getDatasourceListPage").setViewName("dataSource/data_source_List");
        registry.addViewController("/page/datasource/getDataSourceInputPage").setViewName("dataSource/dataSourceInput");

        // flow
        registry.addViewController("/page/flow/drawingBoard").setViewName("flow/mxGraph/index");
        registry.addViewController("/page/flow/getFlowListImport").setViewName("flow/flow_list_import");
        registry.addViewController("/page/flow/inc/graphEditor_menus_task").setViewName("flow/inc/graphEditor_menus_task");
        registry.addViewController("/page/flow/inc/graphEditor_crumbs_task").setViewName("flow/inc/graphEditor_crumbs_task");
        registry.addViewController("/page/flow/inc/graphEditor_crumbs_task").setViewName("flow/inc/graphEditor_crumbs_task");
        registry.addViewController("/page/flow/inc/flow_info_inc").setViewName("flow/inc/flow_info_inc");
        registry.addViewController("/page/flow/inc/flow_path_inc").setViewName("flow/inc/flow_path_inc");
        registry.addViewController("/page/flow/inc/flow_property_inc").setViewName("flow/inc/flow_property_inc");

        //process
        registry.addViewController("/page/process/drawingBoard").setViewName("process/mxGraph/index");
        registry.addViewController("/page/process/inc/process_info_inc").setViewName("process/inc/process_info_inc");
        registry.addViewController("/page/process/inc/process_property_inc").setViewName("process/inc/process_property_inc");
        registry.addViewController("/page/process/inc/process_path_inc").setViewName("process/inc/process_path_inc");
        registry.addViewController("/page/process/getCheckpoint").setViewName("process/inc/process_checkpoint_inc");
        registry.addViewController("/page/process/getDebugDataHtml").setViewName("process/inc/debug_data_inc");
        registry.addViewController("/page/process/inc/process_crumbs_task").setViewName("process/inc/process_crumbs_task");
        registry.addViewController("/page/process/inc/process_operation_button").setViewName("process/inc/process_operation_button");

        //flow group
        registry.addViewController("/page/flowGroup/drawingBoard").setViewName("flowGroup/mxGraph/index");
        registry.addViewController("/page/flowGroup/inc/flowGroup_info_inc").setViewName("flowGroup/inc/flowGroup_info_inc");
        registry.addViewController("/page/flowGroup/inc/flowGroup_path_inc").setViewName("flowGroup/inc/flowGroup_path_inc");
        registry.addViewController("/page/flowGroup/inc/flowGroup_property_inc").setViewName("flowGroup/inc/flowGroup_property_inc");

        //process group
        registry.addViewController("/page/processGroup/drawingBoard").setViewName("processGroup/mxGraph/index");
        registry.addViewController("/page/processGroup/inc/process_operation_button_group").setViewName("processGroup/inc/process_operation_button_group");
        registry.addViewController("/page/processGroup/inc/process_crumbs_group").setViewName("processGroup/inc/process_crumbs_group");




    }
}

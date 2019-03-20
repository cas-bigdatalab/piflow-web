package com.nature.common.constant;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nature.base.util.LoggerUtil;

/**
 * 系统参数缓存类
 * <p>
 * 1、系统参数有几个不需要配置，系统参数表中无需相关参数配置 2、系统参数需要数据表中配置的必须和该类的属性名保持完全一致
 * 3、参数值如果是多个，则优先以;，如果;号不能处理，则,号次之
 * <p>
 * 注： 缓存类的静态属性必须使用时进行缓存值替换的测试，确保缓存使用正确
 * <p>
 * 设置常量规则
 * 1、默认设置的常量值
 * 2、spring从properties文件获取设置[static不能注入，需要public的非静态set方法处理]
 * 3、数据库读取设置
 *
 * @author LHG
 */
@Component
public class SysParamsCache {

    static Logger logger = LoggerUtil.getLogger();

    // 接口ip和端口
    public static String INTERFACE_URL_HEAD;

    @Value("${syspara.interfaceUrlHead}")
    public void setInterfaceUrlHead(String interfaceUrlHead) {
        INTERFACE_URL_HEAD = interfaceUrlHead;
    }

    // stops groups信息
    public static String INTERFACE_STOPS_GROUPS = "/stop/groups";
    // stops List信息(所有stops的bundle)
    public static String INTERFACE_STOPS_LIST = "/stop/list";
    // stop详细信息接口
    public static String INTERFACE_STOPS_INFO = "/stop/info";
    // 启动flow接口
    public static String INTERFACE_FLOW_START = "/flow/start";
    // 停止flow接口
    public static String INTERFACE_FLOW_STOP = "/flow/stop";
    // flow基本信息接口
    public static String INTERFACE_FLOW_INFO = "/flow/info";
    // flow日志接口
    public static String INTERFACE_FLOW_LOG = "/flow/log";
    // flow进度接口
    public static String INTERFACE_FLOW_PROGRESS = "/flow/progress";
    // flow checkpoints接口
    public static String INTERFACE_FLOW_CHECKPOINTS = "/flow/checkpoints";

    /**
     * stops groups信息(所有)地址
     */
    public static String STOP_GROUPS_URL() {
        return INTERFACE_URL_HEAD + INTERFACE_STOPS_GROUPS;
    }

    /**
     * stops List信息(所有stops的bundle)地址
     */
    public static String STOP_LIST_URL() {
        return INTERFACE_URL_HEAD + INTERFACE_STOPS_LIST;
    }

    /**
     * stop详细信息接口地址
     */
    public static String STOP_INFO_URL() {
        return INTERFACE_URL_HEAD + INTERFACE_STOPS_INFO;
    }

    /**
     * flow启动接口地址
     */
    public static String FLOW_START_URL() {
        return INTERFACE_URL_HEAD + INTERFACE_FLOW_START;
    }

    /**
     * flow停止接口地址
     */
    public static String FLOW_STOP_URL() {
        return INTERFACE_URL_HEAD + INTERFACE_FLOW_STOP;
    }

    /**
     * flow基本信息接口地址
     */
    public static String FLOW_INFO_URL() {
        return INTERFACE_URL_HEAD + INTERFACE_FLOW_INFO;
    }

    /**
     * flow日志接口地址
     */
    public static String FLOW_LOG_URL() {
        return INTERFACE_URL_HEAD + INTERFACE_FLOW_LOG;
    }

    /**
     * flow进度接口地址
     */
    public static String FLOW_PROGRESS_URL() {
        return INTERFACE_URL_HEAD + INTERFACE_FLOW_PROGRESS;
    }

    /**
     * flow进度接口地址
     */
    public static String FLOW_CHECKPOINTS_URL() {
        return INTERFACE_URL_HEAD + INTERFACE_FLOW_CHECKPOINTS;
    }

    // 图片路径(配置文件中 读取)
    public static String IMAGES_PATH;

    @Value("${syspara.imagesPath}")
    public void setImagesPath(String imagesPath) {
        IMAGES_PATH = imagesPath;
    }

    // xml文件路径(配置文件中 读取)
    public static String XML_PATH;

    @Value("${syspara.xmlPath}")
    public void setmlPath(String xmlPath) {
        XML_PATH = xmlPath;
    }
}

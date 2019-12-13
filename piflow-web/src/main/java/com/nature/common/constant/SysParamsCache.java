package com.nature.common.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * System parameter cache class
 * <p>
 * 1. There are several system parameters that do not need to be configured. The system parameter table does not need to be configured with related parameters. 2. The system parameters need to be configured in the data table and must be exactly the same as the attribute names of the class.
 * 3. If the parameter value is more than one, it will take precedence; if the number cannot be processed, then the number is
 * <p>
 * Note: The static properties of the cache class must be tested for cache value substitution, ensuring that the cache is used correctly.
 * <p>
 * Set constant rules
 * 1, the default set of constant values
 * 2, "spring" gets the settings from the "properties" file [static can not be injected, need "public" non-static "set" method]...
 * 3, database read settings
 *
 * @author LHG
 */
@Component
public class SysParamsCache {

    // Image path (read in configuration file)
    public static String IMAGES_PATH;

    public static void setImagesPath(String imagesPath) {
        IMAGES_PATH = imagesPath;
    }

    // Videos path (read in configuration file)
    public static String VIDEOS_PATH;

    public static void setVideosPath(String videosPath) {
        VIDEOS_PATH = videosPath;
    }

    // Xml file path (read in configuration file)
    public static String XML_PATH;

    public static void setXmlPath(String xmlPath) {
        XML_PATH = xmlPath;
    }

    // is load stops
    public static boolean IS_LOAD_STOP;

    @Value("${syspara.isLoadStop}")
    public void setIsLoadStop(String isLoadStop) {
        IS_LOAD_STOP = Boolean.valueOf(isLoadStop);
    }

    // Whether to load stops
    public static Boolean IS_IFRAME;

    @Value("${syspara.isIframe}")
    public void setIsIframe(String isIframe) {
        IS_IFRAME = Boolean.valueOf(isIframe);
    }

    // Interface ip and port
    public static String INTERFACE_URL_HEAD;

    @Value("${syspara.interfaceUrlHead}")
    public void setInterfaceUrlHead(String interfaceUrlHead) {
        INTERFACE_URL_HEAD = interfaceUrlHead;
    }


    // Stops group information
    public static String STOPS_GROUPS_URL;

    @Value("${api.stopsGroupsUrl}")
    public void setStopsGroupsUrl(String stopsGroupsUrl) {
        STOPS_GROUPS_URL = stopsGroupsUrl;
    }

    public static String getStopsGroupsUrl() {
        return INTERFACE_URL_HEAD + STOPS_GROUPS_URL;
    }

    // "stops List" information ("bundle" for all "stops")
    public static String STOPS_LIST_URL;

    @Value("${api.stopsListUrl}")
    public void setStopsListUrl(String stopsListUrl) {
        STOPS_LIST_URL = stopsListUrl;
    }

    public static String getStopsListUrl() {
        return INTERFACE_URL_HEAD + STOPS_LIST_URL;
    }

    // "stop" detail interface
    public static String STOPS_INFO_URL;

    @Value("${api.stopsInfoUrl}")
    public void setStopsInfoUrl(String stopsInfoUrl) {
        STOPS_INFO_URL = stopsInfoUrl;
    }

    public static String getStopsInfoUrl() {
        return INTERFACE_URL_HEAD + STOPS_INFO_URL;
    }

    // Start the "flow" interface
    public static String FLOW_START_URL;

    @Value("${api.flowStartUrl}")
    public void setFlowStartUrl(String flowStartUrl) {
        FLOW_START_URL = flowStartUrl;
    }

    public static String getFlowStartUrl() {
        return INTERFACE_URL_HEAD + FLOW_START_URL;
    }

    // Stop the "flow" interface
    public static String FLOW_STOP_URL;

    @Value("${api.flowStopUrl}")
    public void setFlowStopUrl(String flowStopUrl) {
        FLOW_STOP_URL = flowStopUrl;
    }

    public static String getFlowStopUrl() {
        return INTERFACE_URL_HEAD + FLOW_STOP_URL;
    }

    // "flow" basic information interface
    public static String FLOW_INFO_URL;

    @Value("${api.flowInfoUrl}")
    public void setFlowInfoUrl(String flowInfoUrl) {
        FLOW_INFO_URL = flowInfoUrl;
    }

    public static String getFlowInfoUrl() {
        return INTERFACE_URL_HEAD + FLOW_INFO_URL;
    }

    // "flow" log interface
    public static String FLOW_LOG_URL;

    @Value("${api.flowLogUrl}")
    public void setFlowLogUrl(String flowLogUrl) {
        FLOW_LOG_URL = flowLogUrl;
    }

    public static String getFlowLogUrl() {
        return INTERFACE_URL_HEAD + FLOW_LOG_URL;
    }

    // "flow" progress interface
    public static String FLOW_PROGRESS_URL;

    @Value("${api.flowProgressUrl}")
    public void setFlowProgressUrl(String flowProgressUrl) {
        FLOW_PROGRESS_URL = flowProgressUrl;
    }

    public static String getFlowProgressUrl() {
        return INTERFACE_URL_HEAD + FLOW_PROGRESS_URL;
    }

    // "flow checkpoints" interface
    public static String FLOW_CHECKPOINTS_URL;

    @Value("${api.flowCheckpointsUrl}")
    public void setFlowCheckpointsUrl(String flowCheckpointsUrl) {
        FLOW_CHECKPOINTS_URL = flowCheckpointsUrl;
    }

    public static String getFlowCheckpointsUrl() {
        return INTERFACE_URL_HEAD + FLOW_CHECKPOINTS_URL;
    }

    // "flow debug" interface
    public static String FLOW_DEBUG_DATA_URL;

    @Value("${api.flowDebugDataUrl}")
    public void setFlowDebugDataUrl(String flowDebugDataUrl) {
        FLOW_DEBUG_DATA_URL = flowDebugDataUrl;
    }

    public static String getFlowDebugDataUrl() {
        return INTERFACE_URL_HEAD + FLOW_DEBUG_DATA_URL;
    }

    // "flow group start"  interface
    public static String FLOW_GROUP_START_URL;

    @Value("${api.flowGroupStartUrl}")
    public void setFlowGroupStartUrl(String flowGroupStartUrl) {
        FLOW_GROUP_START_URL = flowGroupStartUrl;
    }

    public static String getFlowGroupStartUrl() {
        return INTERFACE_URL_HEAD + FLOW_GROUP_START_URL;
    }

    // "flow group stop"  interface
    public static String FLOW_GROUP_STOP_URL;

    @Value("${api.flowGroupStopUrl}")
    public void setFlowGroupStopUrl(String flowGroupStopUrl) {
        FLOW_GROUP_STOP_URL = flowGroupStopUrl;
    }

    public static String getFlowGroupStopUrl() {
        return INTERFACE_URL_HEAD + FLOW_GROUP_STOP_URL;
    }

    public static String FLOW_GROUP_INFO_URL;

    @Value("${api.flowGroupInfoUrl}")
    public void setFlowGroupInfoUrl(String flowGroupInfoUrl) {
        FLOW_GROUP_INFO_URL = flowGroupInfoUrl;
    }

    public static String getFlowGroupInfoUrl() {
        return INTERFACE_URL_HEAD + FLOW_GROUP_INFO_URL;
    }

    public static String FLOW_GROUP_PROGRESS_URL;

    @Value("${api.flowGroupProgressUrl}")
    public void setFlowGroupProgressUrl(String flowGroupProgressUrl) {
        FLOW_GROUP_PROGRESS_URL = flowGroupProgressUrl;
    }

    public static String getFlowGroupProgressUrl() {
        return INTERFACE_URL_HEAD + FLOW_GROUP_PROGRESS_URL;
    }

    public static String PROJECT_START_URL;

    @Value("${api.projectStartUrl}")
    public void setProjectStartUrl(String projectStartUrl) {
        PROJECT_START_URL = projectStartUrl;
    }

    public static String getProjectStartUrl() {
        return INTERFACE_URL_HEAD + PROJECT_START_URL;
    }

    public static String PROJECT_STOP_URL;

    @Value("${api.projectStopUrl}")
    public void setProjectStopUrl(String projectStopUrl) {
        PROJECT_STOP_URL = projectStopUrl;
    }

    public static String getProjectStopUrl() {
        return INTERFACE_URL_HEAD + PROJECT_STOP_URL;
    }

    public static String PROJECT_INFO_URL;

    @Value("${api.projectInfoUrl}")
    public void setProjectInfoUrl(String projectInfoUrl) {
        PROJECT_INFO_URL = projectInfoUrl;
    }

    public static String getProjectInfoUrl() {
        return INTERFACE_URL_HEAD + PROJECT_INFO_URL;
    }
}

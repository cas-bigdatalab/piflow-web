package cn.cnic.third.market.service.impl;

import cn.cnic.base.utils.HttpUtils;
import cn.cnic.base.utils.JsonUtils;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.common.constant.ApiConfig;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.process.domain.ProcessDomain;
import cn.cnic.component.stopsComponent.vo.PublishComponentVo;
import cn.cnic.third.market.service.IMarket;
import cn.cnic.third.vo.flow.ThirdProgressVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


@Component
public class MarketImpl implements IMarket {

	/**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();

    private final ProcessDomain processDomain;

    @Autowired
    public MarketImpl(ProcessDomain processDomain) {
        this.processDomain = processDomain;
    }

    /**
     * Publish Components
     *
     * @param bundle
     * @param category
     * @param description
     * @param logo
     * @param name
     * @param file
     * @return
     */
    @Override
    public Map<String, Object> publishComponents(String accessKey, String bundle, String category, String description, String logo, String name, File file) {
        if (StringUtils.isBlank(accessKey)) {
            return ReturnMapUtils.setFailedMsg(MessageConfig.PARAM_IS_NULL_MSG("accessKey"));
        }
        if (StringUtils.isBlank(bundle)) {
            return ReturnMapUtils.setFailedMsg(MessageConfig.PARAM_IS_NULL_MSG("bundle"));
        }
        if (StringUtils.isBlank(category)) {
            return ReturnMapUtils.setFailedMsg(MessageConfig.PARAM_IS_NULL_MSG("category"));
        }
        if (StringUtils.isBlank(description)) {
            return ReturnMapUtils.setFailedMsg(MessageConfig.PARAM_IS_NULL_MSG("description"));
        }
        if (StringUtils.isBlank(logo)) {
            return ReturnMapUtils.setFailedMsg(MessageConfig.PARAM_IS_NULL_MSG("logo"));
        }
        if (StringUtils.isBlank(name)) {
            return ReturnMapUtils.setFailedMsg(MessageConfig.PARAM_IS_NULL_MSG("name"));
        }
        if (null == file || !file.exists()) {
            return ReturnMapUtils.setFailedMsg(MessageConfig.PARAM_ERROR_MSG());
        }
        Map<String, String> headerParam = new HashMap<>();
        //accessKey: User unique ID
        headerParam.put("accessKey", accessKey);
        Map<String, String> param = new HashMap<>();
        // * file: Uploaded files
        // * name: Component Name
        // * logo: Component LOGO
        // * description: Component introduction
        // * category: Component Classification
        // * bundle: --
        // * software: 621f4987583197d50685102b(fixed)
        //File file = new File("/home/nature/Downloads/ExcelRead.jar");
        param.put("software", SysParamsCache.MARKET_SOFTWARE_FLAG);
        param.put("bundle", bundle);
        param.put("category", category);
        param.put("description", description);
        param.put("logo", logo);
        param.put("name", name);
        logger.warn(ApiConfig.getMarketComponentsListUrl());
        String doPost = HttpUtils.doPostComCustomizeHeaderAndFile(ApiConfig.getMarketComponentsListUrl(), headerParam, param, file, 50000);
        if (doPost.contains(MessageConfig.INTERFACE_CALL_ERROR_MSG()) || doPost.contains("Exception")) {
            return ReturnMapUtils.setFailedMsg(MessageConfig.INTERFACE_CALL_ERROR_MSG() + " : " + doPost);
        }
        try {
            // Convert a json string to a json object
            JSONObject obj = JSONObject.fromObject(doPost);
            String code = obj.getString("code");
            if (StringUtils.isBlank(code) || !"0".equals(code)) {
                return ReturnMapUtils.setFailedMsg("Error : " + MessageConfig.INTERFACE_RETURN_VALUE_IS_NULL_MSG());
            }
            return ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        } catch (Exception e) {
            logger.error("error: ", e);
            return ReturnMapUtils.setFailedMsg(MessageConfig.CONVERSION_FAILED_MSG());
        }
    }

    @Override
    public Map<String, Object> publishComponents(String accessKey, PublishComponentVo publishComponentVo, File file) throws JsonProcessingException {
        if (StringUtils.isBlank(accessKey)) {
            return ReturnMapUtils.setFailedMsg(MessageConfig.PARAM_IS_NULL_MSG("accessKey")+",you need to bind market account!!");
        }
        if (StringUtils.isBlank(publishComponentVo.getBundle())) {
            return ReturnMapUtils.setFailedMsg(MessageConfig.PARAM_IS_NULL_MSG("bundle"));
        }
        if (StringUtils.isBlank(publishComponentVo.getCategory())) {
            return ReturnMapUtils.setFailedMsg(MessageConfig.PARAM_IS_NULL_MSG("category"));
        }
        if (StringUtils.isBlank(publishComponentVo.getDescription())) {
            return ReturnMapUtils.setFailedMsg(MessageConfig.PARAM_IS_NULL_MSG("description"));
        }
        if (StringUtils.isBlank(publishComponentVo.getLogo())) {
            return ReturnMapUtils.setFailedMsg(MessageConfig.PARAM_IS_NULL_MSG("logo"));
        }
        if (StringUtils.isBlank(publishComponentVo.getName())) {
            return ReturnMapUtils.setFailedMsg(MessageConfig.PARAM_IS_NULL_MSG("name"));
        }
        if (null == file || !file.exists()) {
            return ReturnMapUtils.setFailedMsg(MessageConfig.PARAM_ERROR_MSG());
        }
        Map<String, String> headerParam = new HashMap<>();
        //accessKey: User unique ID
        headerParam.put("accessKey", accessKey);
        Map<String, String> param = new HashMap<>();
        // * file: Uploaded files
        // * name: Component Name
        // * logo: Component LOGO
        // * description: Component introduction
        // * category: Component Classification
        // * bundle: --
        // * software: 621f4987583197d50685102b(fixed)
        //File file = new File("/home/nature/Downloads/ExcelRead.jar");
        param.put("data", JsonUtils.toString(publishComponentVo));

        logger.warn(ApiConfig.getMarketComponentsListUrl());
        String doPost = HttpUtils.doPostComCustomizeHeaderAndFile(ApiConfig.getMarketComponentsListUrl(), headerParam, param, file, 50000);
        if (doPost.contains(MessageConfig.INTERFACE_CALL_ERROR_MSG()) || doPost.contains("Exception")) {
            return ReturnMapUtils.setFailedMsg(MessageConfig.INTERFACE_CALL_ERROR_MSG() + " : " + doPost);
        }
        try {
            // Convert a json string to a json object
            JSONObject obj = JSONObject.fromObject(doPost);
            String code = obj.getString("code");
            if (StringUtils.isBlank(code) || !"0".equals(code)) {
                return ReturnMapUtils.setFailedMsg("Error : " + MessageConfig.INTERFACE_RETURN_VALUE_IS_NULL_MSG());
            }
            return ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        } catch (Exception e) {
            logger.error("error: ", e);
            return ReturnMapUtils.setFailedMsg(MessageConfig.CONVERSION_FAILED_MSG());
        }
    }

    @Override
    public String searchComponents(String page, String pageSize, String param, String sort) {
        if (StringUtils.isBlank(page)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("page"));
        }
        if (StringUtils.isBlank(pageSize)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("pageSize"));
        }
        if (StringUtils.isBlank(sort)) {
            sort = "0";
        }
        Map<String, String> headerParam = new HashMap<>();
        //accessKey: User unique ID
        headerParam.put("accessKey", "d36fed02-48b3-11ed-b8af-c63dee8a33ef");
        //headerParam.put("accessKey", "621f4987583197d50685102b");
        Map<String, Object> paramMap = new HashMap<>();
        //software: piflow(固定)
        //name: 组件名称
        //category: 组件分类
        //authorName: 作者名称
        //sort: 排序方式 0.默认排序 1.下载较多 2.较早发布 3.较新发布
        //page: 分页
        //pageSize: 分页
        paramMap.put("software", SysParamsCache.MARKET_SOFTWARE_FLAG);
        if (StringUtils.isNotBlank(param)) {
            paramMap.put("name", param);
            paramMap.put("category", param);
            paramMap.put("authorName", param);
        }
        paramMap.put("sort", sort);
        paramMap.put("page", page);
        paramMap.put("pageSize", pageSize);
        String doGet = HttpUtils.doGetComCustomizeHeader(ApiConfig.getMarketComponentsListUrl(), paramMap, 50000, headerParam);
        return doGet;
    }

    /**
     * send post request
     */
    @Override
    public String downloadComponents(String jarName, String bundle) {
        if (StringUtils.isBlank(jarName)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("jarName"));
        }
        if (StringUtils.isBlank(bundle)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("bundle"));
        }
        String doGet = HttpUtils.doGet(ApiConfig.getFlowProgressUrl(), null, 10 * 1000);
        if (StringUtils.isBlank(doGet) || doGet.contains(MessageConfig.INTERFACE_CALL_ERROR_MSG()) || doGet.contains("Exception")) {
            logger.warn(MessageConfig.INTERFACE_CALL_ERROR_MSG() + ": " + doGet);
            return null;
        }
        String jsonResult = JSONObject.fromObject(doGet).getString("FlowInfo");
        if (StringUtils.isNotBlank(jsonResult)) {
            return null;
        }
        // Also convert the json string to a json object, 
        // and then convert the json object to a java object, 
        // as shown below.

        // Convert a json string to a json object
        JSONObject obj = JSONObject.fromObject(jsonResult);
        // Convert a json object to a java object
        ThirdProgressVo jd = (ThirdProgressVo) JSONObject.toBean(obj, ThirdProgressVo.class);
        String progressNums = jd.getProgress();
        if (StringUtils.isNotBlank(progressNums)) {
            try {
                double progressNumsD = Double.parseDouble(progressNums);
                jd.setProgress(String.format("%.2f", progressNumsD));
            } catch (Throwable e) {
                logger.warn("Progress conversion failed");
            }
        }
        return doGet;
    }

}

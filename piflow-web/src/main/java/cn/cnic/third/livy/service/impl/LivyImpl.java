package cn.cnic.third.livy.service.impl;

import cn.cnic.base.utils.HttpUtils;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.common.constant.ApiConfig;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.third.livy.service.ILivy;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class LivyImpl implements ILivy {

	/**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();

    @Override
    public Map<String, Object> getAllSessions() {
        String doGet = HttpUtils.doGet(ApiConfig.getLivySessionsUrl(), null, null);
        logger.info("return msg: " + doGet);
        return ReturnMapUtils.setSucceededCustomParam("data", doGet);
    }

    @Override
    public Map<String, Object> startSessions() {
        String doPost = HttpUtils.doPost(ApiConfig.getLivySessionsUrl(), "{\"kind\":\"spark\"}", null);
        logger.info("return msg: " + doPost);
        if(StringUtils.isBlank(doPost)) {
        	return ReturnMapUtils.setFailedMsg("Error : " + MessageConfig.INTERFACE_RETURN_VALUE_IS_NULL_MSG());
        }
        if (doPost.startsWith(MessageConfig.INTERFACE_CALL_ERROR_MSG())) {
        	return ReturnMapUtils.setFailedMsg(doPost);
        }
        try {
            JSONObject obj = JSONObject.fromObject(doPost);// Convert a json string to a json object
            String sessionsId = obj.getString("id");
            if(StringUtils.isBlank(sessionsId)){
                return ReturnMapUtils.setFailedMsg("Error : " + MessageConfig.INTERFACE_RETURN_VALUE_IS_NULL_MSG());
            }
            return ReturnMapUtils.setSucceededCustomParam("sessionsId", sessionsId);
        } catch (Exception e) {
            logger.error("error: ", e);
            return ReturnMapUtils.setFailedMsg("Error : Interface call succeeded, conversion error");
        }
    }

    @Override
    public Map<String, Object> stopSessions(String sessionsId) {
        String url = ApiConfig.getLivySessionsUrl() + "/" + sessionsId;
        String doDelete = HttpUtils.doDelete(url, null);
        logger.info("return msg: " + doDelete);
        if(StringUtils.isBlank(doDelete)) {
        	return ReturnMapUtils.setFailedMsg("Error : " + MessageConfig.INTERFACE_RETURN_VALUE_IS_NULL_MSG());
        }
        if (doDelete.startsWith(MessageConfig.INTERFACE_CALL_ERROR_MSG())) {
            return ReturnMapUtils.setFailedMsg(doDelete);
        }
        return ReturnMapUtils.setSucceededCustomParam("data", doDelete);
    }

    @Override
    public Map<String, Object> getSessionsState(String sessionsId) {
        String url = ApiConfig.getLivySessionsUrl() + "/" + sessionsId + "/state";
        String doGet = HttpUtils.doGet(url, null, null);
        logger.info("return msg: " + doGet);
        if(StringUtils.isBlank(doGet)) {
        	return ReturnMapUtils.setFailedMsg("Error : " + MessageConfig.INTERFACE_RETURN_VALUE_IS_NULL_MSG());
        }
        if (doGet.startsWith(MessageConfig.INTERFACE_CALL_ERROR_MSG())) {
            return ReturnMapUtils.setFailedMsg(doGet);
        }
        return ReturnMapUtils.setSucceededCustomParam("data", doGet);
    }

    @Override
    public Map<String, Object> runStatements(String sessionsId, String code) {
    	Map<String, Object> jsonMap = new HashMap<>();
    	jsonMap.put("kind", "spark");
    	jsonMap.put("code", code);
    	String json = ReturnMapUtils.toJson(jsonMap);
        String url = ApiConfig.getLivySessionsUrl() + "/" + sessionsId + "/statements";
        String doPost = HttpUtils.doPost(url, json, null);
        logger.info("return msg: " + doPost);
        if(StringUtils.isBlank(doPost)) {
        	return ReturnMapUtils.setFailedMsg("Error : " + MessageConfig.INTERFACE_RETURN_VALUE_IS_NULL_MSG());
        }
        if (doPost.startsWith(MessageConfig.INTERFACE_CALL_ERROR_MSG())) {
        	return ReturnMapUtils.setFailedMsg(doPost);
        }
        try {
            JSONObject obj = JSONObject.fromObject(doPost);// Convert a json string to a json object
            String statementsId = obj.getString("id");
            if(StringUtils.isBlank(statementsId)){
                return ReturnMapUtils.setFailedMsg("Error : " + MessageConfig.INTERFACE_RETURN_VALUE_IS_NULL_MSG());
            }
            return ReturnMapUtils.setSucceededCustomParam("statementsId", statementsId);
        } catch (Exception e) {
            logger.error("error: ", e);
            return ReturnMapUtils.setFailedMsg("Error : Interface call succeeded, conversion error");
        }
    }

    @Override
    public Map<String, Object> getStatementsResult(String sessionsId, String statementsId) {
        String url = ApiConfig.getLivySessionsUrl() + "/" + sessionsId + "/statements/" + statementsId;
        String doGet = HttpUtils.doGet(url, null, null);
        logger.info("return msg: " + doGet);
        if(StringUtils.isBlank(doGet)) {
        	return ReturnMapUtils.setFailedMsg("Error : " + MessageConfig.INTERFACE_RETURN_VALUE_IS_NULL_MSG());
        }
        if (doGet.startsWith(MessageConfig.INTERFACE_CALL_ERROR_MSG())) {
        	return ReturnMapUtils.setFailedMsg(doGet);
        }
        return ReturnMapUtils.setSucceededCustomParam("data", doGet);
    }
    
}

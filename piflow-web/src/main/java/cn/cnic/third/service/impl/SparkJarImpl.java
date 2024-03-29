package cn.cnic.third.service.impl;

import java.util.HashMap;
import java.util.Map;

import cn.cnic.common.constant.MessageConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import cn.cnic.base.utils.HttpUtils;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.common.constant.ApiConfig;
import cn.cnic.third.service.ISparkJar;
import cn.cnic.third.vo.sparkJar.SparkJarVo;
import net.sf.json.JSONObject;


@Component
public class SparkJarImpl implements ISparkJar {

	/**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();

    @Override
    public String getSparkJarPath() {

        Map<String, String> map = new HashMap<>();
        //map.put("bundle", bundleStr);
        String sendGetData = HttpUtils.doGet(ApiConfig.getSparkJarPathUrl(), map, 30 * 1000);
        logger.info("return msg：" + sendGetData);
        if (StringUtils.isBlank(sendGetData)) {
            logger.warn("Interface return value is null");
            return null;
        }
        if (sendGetData.contains("Error") || sendGetData.contains(MessageConfig.INTERFACE_CALL_ERROR_MSG())) {
            logger.warn("return err: " + sendGetData);
            return null;
        }

        String sparkJarPath = JSONObject.fromObject(sendGetData).getString("sparkJarPath");
        return sparkJarPath;
    }

    @Override
    public SparkJarVo mountSparkJar(String sparkjarName) {


        Map<String, String> map = new HashMap<>();
        map.put("sparkJar", sparkjarName);
        String json = JSONObject.fromObject(map).toString();
        String doPost = HttpUtils.doPost(ApiConfig.getSparkJarMountUrl(), json, 5 * 1000);
        if (StringUtils.isBlank(doPost)) {
            logger.warn("Interface return values is null");
            return null;
        }
        if (doPost.contains(MessageConfig.INTERFACE_CALL_ERROR_MSG()) || doPost.contains("Fail")) {
            logger.warn("Interface return exception: " + doPost);
            return null;
        }
        logger.info("Interface return value: " + doPost);
        SparkJarVo sparkJarVo = constructSparkJarVo(JSONObject.fromObject(doPost));
        return sparkJarVo;

    }

    @Override
    public SparkJarVo unmountSparkJar(String sparkJarMountId) {

        Map<String, String> map = new HashMap<>();
        map.put("sparkJarId", sparkJarMountId);
        String json = JSONObject.fromObject(map).toString();
        String doPost = HttpUtils.doPost(ApiConfig.getSparkJarUNMountUrl(), json, 5 * 1000);
        if (StringUtils.isBlank(doPost)) {
            logger.warn("Interface return values is null");
            return null;
        }
        if (doPost.contains(MessageConfig.INTERFACE_CALL_ERROR_MSG()) || doPost.contains("Fail")) {
            logger.warn("Interface return exception : " + doPost);
            return null;
        }
        logger.info("Interface return value: " + doPost);
        SparkJarVo sparkJarVo = constructSparkJarVo(JSONObject.fromObject(doPost));
        return sparkJarVo;
    }

    private SparkJarVo constructSparkJarVo(JSONObject jsonObject){

        SparkJarVo sparkJarVo = new SparkJarVo();
        String sparkJarMountId = jsonObject.getJSONObject("sparkJar").getString("id");
        sparkJarVo.setMountId(sparkJarMountId);
        return sparkJarVo;
    }

}

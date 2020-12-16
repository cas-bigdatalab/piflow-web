package cn.cnic.third.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import cn.cnic.base.util.HttpUtils;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.stopsComponent.mapper.StopsComponentGroupMapper;
import cn.cnic.third.service.ISparkJar;
import cn.cnic.third.vo.sparkJar.SparkJarVo;
import net.sf.json.JSONObject;

@Component
public class SparkJarImpl implements ISparkJar {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private StopsComponentGroupMapper stopsComponentGroupMapper;


    @Override
    public String getSparkJarPath() {

        Map<String, String> map = new HashMap<>();
        //map.put("bundle", bundleStr);
        String sendGetData = HttpUtils.doGet(SysParamsCache.getSparkJarPathUrl(), map, 30 * 1000);
        logger.info("return msg：" + sendGetData);
        if (StringUtils.isBlank(sendGetData)) {
            logger.warn("Interface return value is null");
            return null;
        }
        if (sendGetData.contains("Error")) {
            logger.warn("return err");
            return null;
        }

        String sparkJarPath = JSONObject.fromObject(sendGetData).getString("sparkJarPath");
        return sparkJarPath;
    }

    @Override
    public SparkJarVo mountSparkJar(String sparkjarName) {


        Map<String, String> map = new HashMap<>();
        map.put("sparkJar", sparkjarName);
        String json = JSON.toJSON(map).toString();
        String doPost = HttpUtils.doPost(SysParamsCache.getSparkJarMountUrl(), json, 5 * 1000);
        SparkJarVo sparkJarVo = null;
        if (StringUtils.isNotBlank(doPost) && !doPost.contains("Fail")) {
            logger.info("Interface return value: " + doPost);
            sparkJarVo = constructSparkJarVo(JSONObject.fromObject(doPost));

        } else {
            logger.warn("Interface return exception");
        }
        return sparkJarVo;

    }

    @Override
    public SparkJarVo unmountSparkJar(String sparkJarMountId) {

        Map<String, String> map = new HashMap<>();
        map.put("sparkJarId", sparkJarMountId);
        String json = JSON.toJSON(map).toString();
        String doPost = HttpUtils.doPost(SysParamsCache.getSparkJarUNMountUrl(), json, 5 * 1000);
        SparkJarVo sparkJarVo = null;
        if (StringUtils.isNotBlank(doPost) && !doPost.contains("Fail")) {
            logger.info("Interface return value: " + doPost);
            sparkJarVo = constructSparkJarVo(JSONObject.fromObject(doPost));
        } else {
            logger.warn("Interface return exception");

        }

        return sparkJarVo;
    }

    private SparkJarVo constructSparkJarVo(JSONObject jsonObject){

        SparkJarVo sparkJarVo = new SparkJarVo();
        String sparkJarMountId = jsonObject.getJSONObject("sparkJar").getString("id");
        sparkJarVo.setMountId(sparkJarMountId);
        return sparkJarVo;
    }

}

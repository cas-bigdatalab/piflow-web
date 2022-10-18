package cn.cnic.third;

import cn.cnic.base.utils.HttpUtils;
import cn.cnic.base.utils.LoggerUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.common.constant.MessageConfig;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;


public class test {

    private static Logger logger = LoggerUtil.getLogger();

    public static void main(String[] args) {
        String doPost = "{\n" +
                "\t\"code\": 0,\n" +
                "\t\"data\": {},\n" +
                "\t\"message\": \"操作成功\"\n" +
                "}";
        try {
            // Convert a json string to a json object
            JSONObject obj = JSONObject.fromObject(doPost);
            String code = obj.getString("code");
            if (StringUtils.isBlank(code)) {
                logger.info(ReturnMapUtils.setFailedMsgRtnJsonStr("Error : " + MessageConfig.INTERFACE_RETURN_VALUE_IS_NULL_MSG()));
            }
            logger.info(ReturnMapUtils.setFailedMsgRtnJsonStr("error"));
        } catch (Exception e) {
            logger.error("error: ", e);
            logger.info(ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.CONVERSION_FAILED_MSG()));
        }

    }
}

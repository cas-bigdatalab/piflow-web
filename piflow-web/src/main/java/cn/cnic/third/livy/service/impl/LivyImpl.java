package cn.cnic.third.livy.service.impl;

import cn.cnic.base.util.HttpUtils;
import cn.cnic.base.util.ReturnMapUtils;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.third.livy.service.ILivy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class LivyImpl implements ILivy {

    @Override
    public Map<String, Object> getAllSessions() {
        String doGet = HttpUtils.doGet(SysParamsCache.getLivySessionsUrl(), null, null);
        return ReturnMapUtils.setSucceededCustomParam("data", doGet);
    }

    @Override
    public Map<String, Object> startSessions() {
        String doPost = HttpUtils.doPost(SysParamsCache.getLivySessionsUrl(), "{\"kind\":\"spark\"}", null);
        return ReturnMapUtils.setSucceededCustomParam("data", doPost);
    }

    @Override
    public Map<String, Object> stopSessions(String sessionsId) {
        String url = SysParamsCache.getLivySessionsUrl() + "/" + sessionsId;
        String doDelete = HttpUtils.doDelete(url, null);
        return ReturnMapUtils.setSucceededCustomParam("data", doDelete);
    }

    @Override
    public Map<String, Object> runStatements(String sessionsId, String json) {
        String url = SysParamsCache.getLivySessionsUrl() + "/" + sessionsId + "/statements";
        String doPost = HttpUtils.doPost(url, json, null);
        return ReturnMapUtils.setSucceededCustomParam("data", doPost);
    }

    @Override
    public Map<String, Object> getStatementsResult(String sessionsId, String statementsId) {
        String url = SysParamsCache.getLivySessionsUrl() + "/" + sessionsId + "/statements/" + statementsId;
        String doPost = HttpUtils.doPost(url, null, null);
        return ReturnMapUtils.setSucceededCustomParam("data", doPost);
    }
}

package com.nature.third.service;

import com.nature.third.vo.flowInfo.ThirdFlowInfoVo;

public interface IGetFlowInfo {

    public ThirdFlowInfoVo getFlowInfo(String appid);

    public void getProcessInfoAndSave(String appid);
}

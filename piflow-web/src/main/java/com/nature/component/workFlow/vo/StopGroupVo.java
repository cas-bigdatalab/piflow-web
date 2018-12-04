package com.nature.component.workFlow.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 组名称表
 *
 * @author Nature
 */
public class StopGroupVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String groupName; // 组名称

    private List<StopsTemplateVo> stopsTemplateVoList = new ArrayList<StopsTemplateVo>();

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<StopsTemplateVo> getStopsTemplateVoList() {
        return stopsTemplateVoList;
    }

    public void setStopsTemplateVoList(List<StopsTemplateVo> stopsTemplateVoList) {
        this.stopsTemplateVoList = stopsTemplateVoList;
    }
}


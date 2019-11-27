package com.nature.component.group.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Group name table
 *
 * @author Nature
 */
public class StopGroupVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String groupName; // Group name

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


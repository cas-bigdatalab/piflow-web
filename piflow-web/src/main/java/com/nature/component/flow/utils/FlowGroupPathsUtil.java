package com.nature.component.flow.utils;

import com.nature.base.util.SessionUserUtil;
import com.nature.base.util.SqlUtils;
import com.nature.base.vo.UserVo;
import com.nature.component.flow.model.FlowGroupPaths;
import com.nature.component.flow.vo.FlowGroupPathsVo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FlowGroupPathsUtil {
    /**
     * pathsList Po To Vo
     *
     * @param flowGroupPathsList
     * @return
     */
    public static List<FlowGroupPathsVo> flowGroupPathsPoToVo(List<FlowGroupPaths> flowGroupPathsList) {
        List<FlowGroupPathsVo> flowGroupPathsVoList = null;
        if (null != flowGroupPathsList && flowGroupPathsList.size() > 0) {
            flowGroupPathsVoList = new ArrayList<>();
            for (FlowGroupPaths flowGroupPaths : flowGroupPathsList) {
                if (null != flowGroupPaths) {
                    FlowGroupPathsVo flowGroupPathsVo = new FlowGroupPathsVo();
                    BeanUtils.copyProperties(flowGroupPaths, flowGroupPathsVo);
                    flowGroupPathsVoList.add(flowGroupPathsVo);
                }
            }
        }
        return flowGroupPathsVoList;
    }

    /**
     * pathsVoList Vo To Po
     *
     * @param flowGroupPathsVoList
     * @return
     */
    public static List<FlowGroupPaths> flowGroupPathsListVoToPo(List<FlowGroupPathsVo> flowGroupPathsVoList) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        List<FlowGroupPaths> flowGroupPathsList = null;
        if (null != flowGroupPathsList && flowGroupPathsList.size() > 0) {
            flowGroupPathsList = new ArrayList<>();
            for (FlowGroupPathsVo flowGroupPathsVo : flowGroupPathsVoList) {
                if (null != flowGroupPathsVo) {
                    FlowGroupPaths flowGroupPaths = new FlowGroupPaths();
                    BeanUtils.copyProperties(flowGroupPathsVo, flowGroupPaths);
                    flowGroupPaths.setId(SqlUtils.getUUID32());
                    flowGroupPaths.setCrtDttm(new Date());
                    flowGroupPaths.setCrtUser(username);
                    flowGroupPaths.setLastUpdateDttm(new Date());
                    flowGroupPaths.setLastUpdateUser(username);
                    flowGroupPaths.setEnableFlag(true);
                    flowGroupPathsList.add(flowGroupPaths);
                }
            }
        }
        return flowGroupPathsList;
    }


}

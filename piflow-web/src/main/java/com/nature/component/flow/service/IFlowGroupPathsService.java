package com.nature.component.flow.service;

import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.Paths;
import com.nature.component.flow.vo.PathsVo;

import java.util.List;

public interface IFlowGroupPathsService {

    /**
     * Query connection information according to flowGroupId and pageid
     *
     * @param flowGroupId
     * @param pageId
     * @return
     */
    public String queryPathInfoFlowGroup(String flowGroupId, String pageId);

}

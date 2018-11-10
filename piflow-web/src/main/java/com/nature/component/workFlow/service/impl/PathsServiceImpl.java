package com.nature.component.workFlow.service.impl;

import com.nature.component.workFlow.model.Paths;
import com.nature.component.workFlow.utils.PathsUtil;
import com.nature.component.workFlow.vo.PathsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nature.component.workFlow.service.IPathsService;
import com.nature.mapper.PathsMapper;

import java.util.List;

@Service
public class PathsServiceImpl implements IPathsService {

    @Autowired
    private PathsMapper pathsMapper;

    @Override
    public int deletePathsByFlowId(String id) {
        return pathsMapper.deletePathsByFlowId(id);
    }

    @Override
    public PathsVo getPathsByFlowIdAndPageId(String flowId, String pageId) {
        PathsVo pathsVo = null;
        List<Paths> pathsList = pathsMapper.getPaths(flowId, pageId, null, null);
        if (null != pathsList && pathsList.size() > 0) {
            Paths paths = pathsList.get(0);
            if (null != paths) {
                pathsVo = new PathsVo();
                BeanUtils.copyProperties(paths, pathsVo);
            }
        }
        return pathsVo;
    }
    /**
     * 查询连线信息
     *
     * @param flowId
     * @param from
     * @param to
     * @return
     */
    @Override
    public List<PathsVo> getPaths(String flowId, String from, String to) {
        List<PathsVo> pathsVoList = null;
        List<Paths> pathsList = pathsMapper.getPaths(flowId, null, from, to);
        if (null != pathsList && pathsList.size() > 0) {
            pathsVoList=PathsUtil.pathsListPoToVo(pathsList);
        }
        return pathsVoList;
    }

    /**
     * 查询连线的数量
     *
     * @param flowId
     * @param from
     * @param to
     * @return
     */
    @Override
    public Integer getPathsCounts(String flowId, String from, String to) {
        Integer pathsCounts = pathsMapper.getPathsCounts(flowId, null, from, to);
        return pathsCounts;
    }
}

package com.nature.component.flow.service.impl;

import com.nature.base.util.SessionUserUtil;
import com.nature.base.util.SqlUtils;
import com.nature.base.vo.UserVo;
import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.Paths;
import com.nature.component.flow.model.Stops;
import com.nature.component.flow.service.IPathsService;
import com.nature.component.flow.utils.PathsUtil;
import com.nature.component.flow.vo.FlowVo;
import com.nature.component.flow.vo.PathsVo;
import com.nature.mapper.flow.FlowMapper;
import com.nature.mapper.flow.PathsMapper;
import com.nature.mapper.flow.PropertyMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PathsServiceImpl implements IPathsService {

    @Resource
    private PathsMapper pathsMapper;

    @Resource
    private PropertyMapper propertyMapper;

    @Override
    public int deletePathsByFlowId(String id) {
        return pathsMapper.updateEnableFlagByFlowId(id);
    }

    @Override
    public PathsVo getPathsByFlowIdAndPageId(String flowId, String pageId) {
        PathsVo pathsVo = null;
        List<Paths> pathsList = pathsMapper.getPaths(flowId, pageId, null, null);
        if (null != pathsList && pathsList.size() > 0) {
            Paths paths = pathsList.get(0);
            if (null != paths) {
                Stops stopFrom = null;
                Stops stopTo = null;
                if (StringUtils.isNotBlank(paths.getFrom()) && StringUtils.isNotBlank(paths.getTo())) {
                    stopFrom = propertyMapper.getStopGroupList(flowId, paths.getFrom());
                    stopTo = propertyMapper.getStopGroupList(flowId, paths.getTo());
                }

                pathsVo = new PathsVo();
                BeanUtils.copyProperties(paths, pathsVo);
                Flow flow = paths.getFlow();
                if (null != flow) {
                    FlowVo flowVo = new FlowVo();
                    BeanUtils.copyProperties(flow, flowVo);
                    pathsVo.setFlowVo(flowVo);
                }
                if (null != stopFrom) {
                    pathsVo.setStopFrom(stopFrom);
                }
                if (null != stopTo) {
                    pathsVo.setStopTo(stopTo);
                }
                if (StringUtils.isBlank(pathsVo.getInport())) {
                    pathsVo.setInport("default");
                }
                if (StringUtils.isBlank(pathsVo.getOutport())) {
                    pathsVo.setOutport("default");
                }
            }
        }
        return pathsVo;
    }

    /**
     * Query connection information
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
            pathsVoList = PathsUtil.pathsListPoToVo(pathsList);
        }
        return pathsVoList;
    }

    /**
     * Query the number of connections
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

    @Override
    public int upDatePathsVo(PathsVo pathsVo) {
        if (null != pathsVo) {
            Paths pathsById = pathsMapper.getPathsById(pathsVo.getId());
            if (null != pathsById) {
                BeanUtils.copyProperties(pathsVo, pathsById);
                pathsById.setLastUpdateDttm(new Date());
                pathsById.setLastUpdateUser("-1");
                int i = pathsMapper.updatePaths(pathsById);
                return i;
            }
        }
        return 0;
    }

    @Override
    public int addPathsList(List<Paths> pathsList, Flow flow) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        List<Paths> list = new ArrayList<Paths>();
        if (null != pathsList && pathsList.size() > 0) {
            for (Paths paths : pathsList) {
                if (null != paths) {
                    paths.setId(SqlUtils.getUUID32());
                    paths.setCrtDttm(new Date());
                    paths.setFlow(flow);
                    paths.setEnableFlag(true);
                    paths.setLastUpdateDttm(new Date());
                    paths.setLastUpdateUser(username);
                    list.add(paths);
                }
            }
        }
        return pathsMapper.addPathsList(list);
    }

}

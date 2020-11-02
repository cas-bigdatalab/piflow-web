package cn.cnic.component.flow.service.impl;

import cn.cnic.base.util.ReturnMapUtils;
import cn.cnic.base.util.UUIDUtils;
import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.entity.Paths;
import cn.cnic.component.flow.entity.Stops;
import cn.cnic.component.flow.service.IPathsService;
import cn.cnic.component.flow.utils.PathsUtil;
import cn.cnic.component.flow.vo.FlowVo;
import cn.cnic.component.flow.vo.PathsVo;
import cn.cnic.component.flow.mapper.PathsMapper;
import cn.cnic.component.flow.mapper.PropertyMapper;
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
    public int deletePathsByFlowId(String username, String id) {
        return pathsMapper.updateEnableFlagByFlowId(username, id);
    }

    @Override
    public String getPathsByFlowIdAndPageId(String flowId, String pageId) {
        if (StringUtils.isBlank(flowId) || StringUtils.isBlank(pageId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The parameter'fid'or'id' is empty");
        }
        List<Paths> pathsList = pathsMapper.getPaths(flowId, pageId, null, null);
        if (null == pathsList || pathsList.isEmpty()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No'paths'information was queried");
        }
        Paths paths = pathsList.get(0);
        if (null == paths) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No'paths'information was queried");
        }
        Stops stopFrom = null;
        Stops stopTo = null;
        if (StringUtils.isNotBlank(paths.getFrom()) && StringUtils.isNotBlank(paths.getTo())) {
            stopFrom = propertyMapper.getStopGroupList(flowId, paths.getFrom());
            stopTo = propertyMapper.getStopGroupList(flowId, paths.getTo());
        }
        PathsVo pathsVo = new PathsVo();
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
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("queryInfo", pathsVo);
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
    public int upDatePathsVo(String username, PathsVo pathsVo) {
        if (null != pathsVo) {
            Paths pathsById = pathsMapper.getPathsById(pathsVo.getId());
            if (null != pathsById) {
                BeanUtils.copyProperties(pathsVo, pathsById);
                pathsById.setLastUpdateDttm(new Date());
                pathsById.setLastUpdateUser("-1");
                int i = pathsMapper.updatePaths(username, pathsById);
                return i;
            }
        }
        return 0;
    }

    @Override
    public int addPathsList(String username, List<Paths> pathsList, Flow flow) {
        if (StringUtils.isBlank(username)) {
            return 0;
        }
        List<Paths> list = new ArrayList<>();
        if (null == pathsList || pathsList.isEmpty()) {
        }
        for (Paths paths : pathsList) {
            if (null != paths) {
                paths.setId(UUIDUtils.getUUID32());
                paths.setCrtDttm(new Date());
                paths.setFlow(flow);
                paths.setEnableFlag(true);
                paths.setLastUpdateDttm(new Date());
                paths.setLastUpdateUser(username);
                list.add(paths);
            }
        }
        return pathsMapper.addPathsList(username, list);
    }

}

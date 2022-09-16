package cn.cnic.component.flow.service.impl;

import java.rmi.MarshalException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.flow.domain.FlowDomain;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.base.utils.UUIDUtils;
import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.entity.Paths;
import cn.cnic.component.flow.entity.Stops;
import cn.cnic.component.flow.service.IPathsService;
import cn.cnic.component.flow.utils.PathsUtil;
import cn.cnic.component.flow.vo.FlowVo;
import cn.cnic.component.flow.vo.PathsVo;

@Service
public class PathsServiceImpl implements IPathsService {

    @Autowired
    private FlowDomain flowDomain;

    @Override
    public int deletePathsByFlowId(String username, String id) {
        return flowDomain.updatePathsEnableFlagByFlowId(username, id);
    }

    @Override
    public String getPathsByFlowIdAndPageId(String flowId, String pageId) {
        if (StringUtils.isBlank(flowId) || StringUtils.isBlank(pageId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        List<Paths> pathsList = flowDomain.getPaths(flowId, pageId, null, null);
        if (null == pathsList || pathsList.isEmpty()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_PATH_DATA_MSG());
        }
        Paths paths = pathsList.get(0);
        if (null == paths) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_PATH_DATA_MSG());
        }
        Stops stopFrom = null;
        Stops stopTo = null;
        if (StringUtils.isNotBlank(paths.getFrom()) && StringUtils.isNotBlank(paths.getTo())) {
            stopFrom = flowDomain.getStopGroupList(flowId, paths.getFrom());
            stopTo = flowDomain.getStopGroupList(flowId, paths.getTo());
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
        List<Paths> pathsList = flowDomain.getPaths(flowId, null, from, to);
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
        Integer pathsCounts = flowDomain.getPathsCounts(flowId, null, from, to);
        return pathsCounts;
    }

    @Override
    public int upDatePathsVo(String username, PathsVo pathsVo) {
        if (null != pathsVo) {
            Paths pathsById = flowDomain.getPathsById(pathsVo.getId());
            if (null != pathsById) {
                BeanUtils.copyProperties(pathsVo, pathsById);
                pathsById.setLastUpdateDttm(new Date());
                pathsById.setLastUpdateUser("-1");
                int i = flowDomain.updatePaths(pathsById);
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
        return flowDomain.addPathsList(list);
    }

}

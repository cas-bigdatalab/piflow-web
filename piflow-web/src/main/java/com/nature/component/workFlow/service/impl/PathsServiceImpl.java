package com.nature.component.workFlow.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.nature.base.util.SessionUserUtil;
import com.nature.base.util.Utils;
import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.Paths;
import com.nature.component.workFlow.model.Stops;
import com.nature.component.workFlow.service.IPathsService;
import com.nature.component.workFlow.utils.PathsUtil;
import com.nature.component.workFlow.vo.PathsVo;
import com.nature.mapper.PathsMapper;
import com.nature.mapper.PropertyMapper;

@Service
public class PathsServiceImpl implements IPathsService {

    @Autowired
    private PathsMapper pathsMapper;
    @Autowired
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
                if (null != stopFrom) {
                    pathsVo.setStopFrom(stopFrom);
                }
                if (null != stopTo) {
                    pathsVo.setStopTo(stopTo);
                }
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
            pathsVoList = PathsUtil.pathsListPoToVo(pathsList);
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
	public int addPathsList(List<Paths> pathsList,Flow flow) {
		User user = SessionUserUtil.getCurrentUser();
	    String username = (null != user) ? user.getUsername() : "-1";
		List<Paths> list = new ArrayList<Paths>();
		if (null != pathsList && pathsList.size() > 0) {
			for (Paths paths : pathsList) {
				if (null != paths) {
					paths.setId(Utils.getUUID32());
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

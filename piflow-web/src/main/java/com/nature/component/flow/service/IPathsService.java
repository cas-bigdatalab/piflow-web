package com.nature.component.flow.service;

import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.Paths;
import com.nature.component.flow.vo.PathsVo;
import java.util.List;

public interface IPathsService {

	public int deletePathsByFlowId(String id);

	/**
	 * 根据flowId和pageid查询连线信息
	 *
	 * @param flowId
	 * @param pageId
	 * @return
	 */
	public PathsVo getPathsByFlowIdAndPageId(String flowId, String pageId);

	/**
	 * 查询连线信息
	 *
	 * @param flowId
	 * @param from
	 * @param to
	 * @return
	 */
	public List<PathsVo> getPaths(String flowId, String from, String to);

	/**
	 * 查询连线的数量
	 *
	 * @param flowId
	 * @param from
	 * @param to
	 * @return
	 */
	public Integer getPathsCounts(String flowId, String from, String to);

	/**
	 * 保存更新连线信息
	 *
	 * @param pathsVo
	 * @return
	 */
	public int upDatePathsVo(PathsVo pathsVo);
	
	/**
	 * 插入list<Paths> 
	 * @param pathsList
	 * @return
	 */
	public int addPathsList(List<Paths> pathsList,Flow flow);

}

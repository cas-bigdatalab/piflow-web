package com.nature.component.flow.service;

import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.Paths;
import com.nature.component.flow.vo.PathsVo;

import java.util.List;

public interface IPathsService {

	public int deletePathsByFlowId(String id);

	/**
	 * Query connection information according to flowId and pageid
	 *
	 * @param flowId
	 * @param pageId
	 * @return
	 */
	public PathsVo getPathsByFlowIdAndPageId(String flowId, String pageId);

	/**
	 * Query connection information
	 *
	 * @param flowId
	 * @param from
	 * @param to
	 * @return
	 */
	public List<PathsVo> getPaths(String flowId, String from, String to);

	/**
	 * Query the number of connections
	 *
	 * @param flowId
	 * @param from
	 * @param to
	 * @return
	 */
	public Integer getPathsCounts(String flowId, String from, String to);

	/**
	 * Save update connection information
	 *
	 * @param pathsVo
	 * @return
	 */
	public int upDatePathsVo(PathsVo pathsVo);
	
	/**
	 * Insert list<Paths>
	 * @param pathsList
	 * @return
	 */
	public int addPathsList(List<Paths> pathsList,Flow flow);


}

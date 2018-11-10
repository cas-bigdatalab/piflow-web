package com.nature.component.workFlow.service;

import com.nature.component.workFlow.vo.PathsVo;
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

}

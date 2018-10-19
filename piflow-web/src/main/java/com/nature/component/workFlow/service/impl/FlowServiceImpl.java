package com.nature.component.workFlow.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nature.base.util.LoggerUtil;
import com.nature.base.util.Utils;
import com.nature.base.vo.StatefulRtnBase;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGeometry;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.mxGraph.vo.MxCellVo;
import com.nature.component.mxGraph.vo.MxGeometryVo;
import com.nature.component.mxGraph.vo.MxGraphModelVo;
import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.Paths;
import com.nature.component.workFlow.model.Property;
import com.nature.component.workFlow.model.PropertyTemplate;
import com.nature.component.workFlow.model.Stops;
import com.nature.component.workFlow.model.StopsTemplate;
import com.nature.component.workFlow.service.FlowService;
import com.nature.mapper.FlowMapper;
import com.nature.mapper.PathsMapper;
import com.nature.mapper.PropertyMapper;
import com.nature.mapper.StopsMapper;
import com.nature.mapper.StopsTemplateMapper;
import com.nature.mapper.mxGraph.MxCellMapper;
import com.nature.mapper.mxGraph.MxGeometryMapper;
import com.nature.mapper.mxGraph.MxGraphModelMapper;

@Service
public class FlowServiceImpl implements FlowService {

	Logger logger = LoggerUtil.getLogger();

	@Autowired
	private FlowMapper flowMapper;

	@Autowired
	private StopsTemplateMapper stopsTemplateMapper;

	@Autowired
	private MxGraphModelMapper mxGraphModelMapper;

	@Autowired
	private MxCellMapper mxCellMapper;

	@Autowired
	private MxGeometryMapper mxGeometryMapper;

	@Autowired
	private PathsMapper pathsMapper;

	@Autowired
	private StopsMapper stopsMapper;

	@Autowired
	private PropertyMapper propertyMapper;

	private boolean flag = false;

	/**
	 * @Title 向数据库添加flow
	 * 
	 * @param mxGraphModel
	 * @param flowId
	 * @return
	 */
	@Override
	public StatefulRtnBase saveOrUpdateFlow(MxGraphModelVo mxGraphModelVo, String flowId) {
		StatefulRtnBase satefulRtnBase = new StatefulRtnBase();
		if (StringUtils.isNotBlank(flowId)) {
			// 根据flowId查询flow
			Flow flow = flowMapper.getFlowById(flowId);
			if (null == flow) {
				logger.info("新建");
				if (flag) {
					satefulRtnBase = this.addFlow(mxGraphModelVo, flowId);
					flag = false;
				} else {
					flag = true;
				}
			} else {
				logger.info("在'" + flowId + "'的基礎上保存");
				satefulRtnBase = this.updateFlow(mxGraphModelVo, flow);
			}
		} else {
			satefulRtnBase = setStatefulRtnBase("flowId为空，保存失败");
		}
		return satefulRtnBase;
	}

	/**
	 * @Title 根据id查询流信息
	 * @param id
	 * @return
	 * @author Nature
	 */
	@Override
	public Flow getFlowById(String id) {
		return flowMapper.getFlowById(id);
	}

	private StatefulRtnBase setStatefulRtnBase(String errMsg) {
		StatefulRtnBase statefulRtnBase = new StatefulRtnBase();
		logger.info(errMsg);
		statefulRtnBase.setReqRtnStatus(false);
		statefulRtnBase.setErrorCode(statefulRtnBase.ERRCODE_FAIL);
		statefulRtnBase.setErrorMsg(errMsg);
		return statefulRtnBase;
	}

	@SuppressWarnings("unchecked")
	private StatefulRtnBase addFlow(MxGraphModelVo mxGraphModelVo, String flowId) {
		StatefulRtnBase statefulRtnBase = new StatefulRtnBase();
		// 判断mxGraphModelVo是否为空
		if (null != mxGraphModelVo && StringUtils.isNotBlank(flowId)) {
			// 新建流水线
			Flow flow = new Flow();
			// setId
			flow.setId(flowId);
			// setUuid
			flow.setUuid(flowId);
			// 创建时间
			flow.setCrtDttm(new Date());
			// 创建人
			flow.setCrtUser("Nature");
			// 最后更新时间
			flow.setLastUpdateDttm(new Date());
			// 最后更新人
			flow.setLastUpdateUser("Nature");
			// 是否有效
			flow.setEnableFlag(true);
			// 版本号
			flow.setVersion(0L);
			// 流水线Name
			flow.setName("默认");
			// 保存mxGraphModel
			// 新建
			MxGraphModel mxGraphModel = new MxGraphModel();
			// mxGeometry 的基本属性(创建时必填)
			mxGraphModel.setId(Utils.getUUID32());
			mxGraphModel.setCrtDttm(new Date());
			mxGraphModel.setCrtUser("Nature");
			mxGraphModel.setVersion(0L);
			// setmxGraphModel基本属性
			mxGraphModel.setEnableFlag(true);
			mxGraphModel.setLastUpdateUser("Nature");
			mxGraphModel.setLastUpdateDttm(new Date());
			// 将mxGraphModelVo中的值copy到mxGraphModel中
			BeanUtils.copyProperties(mxGraphModelVo, mxGraphModel);
			// 保存mxGraphModel
			int addMxGraphModel = mxGraphModelMapper.addMxGraphModel(mxGraphModel);
			if (addMxGraphModel > 0) {
				// 页面传过来的数据MxCellVo
				List<MxCellVo> mxCellVoList = mxGraphModelVo.getRootVo();
				if (null != mxCellVoList && mxCellVoList.size() > 0) {
					for (MxCellVo mxCellVo : mxCellVoList) {
						MxGeometryVo mxGeometryVo = mxCellVo.getMxGeometryVo();
						MxGeometry mxGeometry = null;
						// 判断页面传来的数据mxGeometryVo是否为空，如果为空直接返回，否则继续操作
						if (null != mxGeometryVo) {
							// 保存MxGeometry
							// 新建
							mxGeometry = new MxGeometry();
							// mxGeometry 的基本属性(创建时必填)
							mxGeometry.setId(Utils.getUUID32());
							mxGeometry.setCrtDttm(new Date());
							mxGeometry.setCrtUser("Nature");
							mxGeometry.setVersion(0L);
							// setmxGraphModel基本属性
							mxGeometry.setEnableFlag(true);
							mxGeometry.setLastUpdateUser("Nature");
							mxGeometry.setLastUpdateDttm(new Date());
							// 将mxGeometryVo中的值copy到mxGeometry中
							BeanUtils.copyProperties(mxGeometryVo, mxGeometry);
							// 保存mxGeometry
							mxGeometryMapper.addMxGeometry(mxGeometry);
						}
						// 保存MxCell
						// 新建
						MxCell mxCell = new MxCell();
						// mxCell 的基本属性(创建时必填)
						mxCell.setId(Utils.getUUID32());
						mxCell.setCrtDttm(new Date());
						mxCell.setCrtUser("Nature");
						mxCell.setVersion(0L);
						// mxCell 的基本属性
						mxCell.setEnableFlag(true);
						mxCell.setLastUpdateUser("Nature");
						mxCell.setLastUpdateDttm(new Date());
						// 将mxCellVo中的值copy到mxCell中
						BeanUtils.copyProperties(mxCellVo, mxCell);
						// mxGraphModel外键
						mxCell.setMxGraphModel(mxGraphModel);
						// mxGeometry外键
						mxCell.setMxGeometry(mxGeometry);
						// 保存mxCell
						mxCellMapper.addMxCell(mxCell);

					}
				}
				flow.setMxGraphModel(mxGraphModel);
				// 保存flow
				int addFlow = flowMapper.addFlow(flow);
				// 判断是否保存成功
				if (addFlow > 0) {
					// 线的List
					List<Paths> pathsList = new ArrayList<Paths>();
					// stops的list
					List<Stops> stopsList = new ArrayList<Stops>();
					// 取出所有的MxCellVo(里边有线也有stops)
					List<MxCellVo> root = mxGraphModelVo.getRootVo();
					// 判空
					if (null != root && root.size() > 0) {
						// 把root中的stops和线分开
						Map<String, Object> stopsPathsMap = distinguishStopsPaths(root);
						// 从Map中取出mxCellVoList(线的list)
						List<MxCellVo> objectPaths = (ArrayList<MxCellVo>) stopsPathsMap.get("paths");
						// 从Map中取出mxCellVoList(stops的list)
						List<MxCellVo> objectStops = (ArrayList<MxCellVo>) stopsPathsMap.get("stops");
						// 根据MxCellList中的内容生成stops的list
						stopsList = this.mxCellVoListToStopsList(objectStops, flow);
						// 根据MxCellList中的内容生成paths的list
						pathsList = this.mxCellListToPathsList(objectPaths, flow);
						// 把list放入flow中
						flow.setPathsList(pathsList);
						flow.setStopsList(stopsList);
						// stopsList判空
						if (null != stopsList && stopsList.size() > 0) {
							// 保存stopsList
							int addStopsList = stopsMapper.addStopsList(stopsList);
							if (addStopsList > 0) {
								for (Stops stops : stopsList) {
									// 取出propertyList
									List<Property> propertyList = stops.getProperties();
									// 判空propertyList
									if (null != propertyList && propertyList.size() > 0) {
										// 保存propertyList
										int addPropertyList = propertyMapper.addPropertyList(propertyList);
										if (addPropertyList <= 0) {
											logger.info("Property保存失败：stopsId(" + stops.getId() + "),stopsName("
													+ stops.getName() + ")");
										}
									} else {
										logger.info("propertyList为空,不保存");
									}
								}
							} else {
								setStatefulRtnBase("新建保存失败stopsList");
							}
						} else {
							logger.info("stopsList为空,不保存");
						}
						// 判空pathsList
						if (null != pathsList && pathsList.size() > 0) {
							// 保存pathsList
							int addPathsList = pathsMapper.addPathsList(pathsList);
							if (addPathsList <= 0) {
								logger.info("pathsList保存失败所属流：flowId(" + flow.getId() + ")");
							}
						} else {
							logger.info("stopsList为空,不保存");
						}
					}
				} else {
					setStatefulRtnBase("新建保存失败flow");
				}
			} else {
				setStatefulRtnBase("新建保存失败mxGraphModel");
			}
		} else {
			setStatefulRtnBase("新建保存失败，flowId为空或mxGraphModelVo为空");
		}
		return statefulRtnBase;

	}

	/**
	 * @Title 修改Flow
	 * 
	 * @param mxGraphModelVo 页面传出的信息
	 * @param flow           要修改的数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private StatefulRtnBase updateFlow(MxGraphModelVo mxGraphModelVo, Flow flow) {
		StatefulRtnBase statefulRtnBase = new StatefulRtnBase();
		if (null != flow) {
			// 最后更新时间
			flow.setLastUpdateDttm(new Date());
			// 最后更新人
			flow.setLastUpdateUser("Natrue");
			// 版本号
			flow.setVersion(flow.getVersion() + 1);
			// 取出数据库查询出来的画板信息
			MxGraphModel mxGraphModel = flow.getMxGraphModel();
			boolean isAddMxGraphModel = false;
			// 判断要修改的数据库数据是否为空,如果为空则新建,否则为修改版本号加1
			if (null == mxGraphModel) {
				// 新建
				mxGraphModel = new MxGraphModel();
				mxGraphModel.setId(Utils.getUUID32());
				mxGraphModel.setCrtDttm(new Date());
				mxGraphModel.setCrtUser("Nature");
				mxGraphModel.setEnableFlag(true);
				mxGraphModel.setVersion(0L);
				isAddMxGraphModel = true;
			} else {
				// 修改时版本号递增
				mxGraphModel.setVersion(mxGraphModel.getVersion() + 1);
			}
			// 判断页面传来的数据mxGraphModelVo是否为空，如果为空直接逻辑删除，否则继续操作
			if (null == mxGraphModelVo) {
				mxGraphModel.setEnableFlag(false);
			} else {
				// 将mxGraphModelVo中的值copy到mxGraphModel中
				BeanUtils.copyProperties(mxGraphModelVo, mxGraphModel);
			}
			// setmxGraphModel基本属性
			mxGraphModel.setLastUpdateUser("Nature");
			mxGraphModel.setLastUpdateDttm(new Date());
			// 修改或新增MxGraphModel
			int isSuccess = 0;
			if (isAddMxGraphModel) {
				// 新增MxGraphModel
				isSuccess = mxGraphModelMapper.addMxGraphModel(mxGraphModel);
			} else {
				// 保存MxGraphModel
				isSuccess = mxGraphModelMapper.updateMxGraphModel(mxGraphModel);
			}
			// 判断mxGraphModel是否保存成功
			if (isSuccess > 0) {
				// 需要新增的mxCellList
				List<MxCell> addMxCellList = new ArrayList<MxCell>();
				// 需要修改的mxCellList
				List<MxCell> updateMxCellList = new ArrayList<MxCell>();

				// 页面传过来的数据MxCellVo
				List<MxCellVo> mxCellVoList = mxGraphModelVo.getRootVo();
				// 取出数据库查询出来的MxCellList信息
				List<MxCell> mxCellList = mxGraphModel.getRoot();
				// 如果mxCellVoList不为空，则继续操作
				// 否则说明页面把所有的stops都删除了，
				// 需要把数据库查出的mxCellList中的内容都添加到修改的list中进行逻辑删除
				if (null != mxCellVoList && mxCellVoList.size() > 0) {
					// 将数据库查出的List转为map 键为pageId
					Map<String, MxCell> mxCellMap = new HashMap<String, MxCell>();
					// 判空
					if (null != mxCellList) {
						for (MxCell mxCell : mxCellList) {
							mxCellMap.put(mxCell.getPageId(), mxCell);
						}
					}
					for (MxCellVo mxCellVo : mxCellVoList) {
						// 是否新到增加的List中的增标志位
						boolean isAddList = false;
						// 画板上的图形ID(pageId)
						String pageId = mxCellVo.getPageId();
						// 根据pageId去map取，取到说明数据库中有，做修改操作，否则做新增操作
						MxCell mxCell = mxCellMap.get(pageId);

						// 判断object是否为空，当为空时视为新增
						if (null == mxCell) {
							// 新增
							mxCell = new MxCell();
							// mxGeometry 的基本属性(创建时必填)
							mxCell.setId(Utils.getUUID32());
							mxCell.setCrtDttm(new Date());
							mxCell.setCrtUser("Nature");
							mxCell.setEnableFlag(true);
							mxCell.setVersion(0L);
							isAddList = true;
						} else {
							// 修改时版本号递增
							mxCell.setVersion(mxCell.getVersion() + 1);
						}
						// mxGeometry 的基本属性
						mxCell.setLastUpdateUser("Nature");
						mxCell.setLastUpdateDttm(new Date());
						// 将mxCellVo中的值copy到mxCell中
						BeanUtils.copyProperties(mxCellVo, mxCell);

						// 取出mxCellVo（页面传过来的数据）中mxGeometryVo
						MxGeometryVo mxGeometryVo = mxCellVo.getMxGeometryVo();
						if (null != mxGeometryVo) {
							MxGeometry mxGeometry = mxCell.getMxGeometry();
							if (null == mxGeometry) {
								// 新建
								mxGeometry = new MxGeometry();
								// mxGeometry 的基本属性(创建时必填)
								mxGeometry.setId(Utils.getUUID32());
								mxGeometry.setCrtDttm(new Date());
								mxGeometry.setCrtUser("Nature");
								mxGeometry.setEnableFlag(true);
								mxGeometry.setVersion(0L);
							} else {
								// 修改时版本号递增
								mxGeometry.setVersion(mxGeometry.getVersion() + 1);
							}
							// mxGeometry 的基本属性
							mxGeometry.setLastUpdateUser("Nature");
							mxGeometry.setLastUpdateDttm(new Date());
							// 将mxGeometryVo中的值copy到mxGeometry中
							BeanUtils.copyProperties(mxGeometryVo, mxGeometry);
							mxCell.setMxGeometry(mxGeometry);
						}
						mxCell.setMxGraphModel(mxGraphModel);

						// 判断是添加还是修改放到对应的List中
						if (isAddList) {
							addMxCellList.add(mxCell);
						} else {
							updateMxCellList.add(mxCell);
						}
					}
				} else {
					if (null != mxCellList) {
						for (MxCell mxCell : mxCellList) {
							mxCell.setEnableFlag(false);
							mxCell.setLastUpdateDttm(new Date());
							mxCell.setLastUpdateUser("Nature");
							updateMxCellList.add(mxCell);
						}
					}
				}
				// 保存updateMxCellList和addMxCellList
				// 保存
				if (null != addMxCellList) {
					for (MxCell mxCell : addMxCellList) {
						if (null != mxCell) {
							MxGeometry mxGeometry = mxCell.getMxGeometry();
							// 判空，保存
							if (null != mxGeometry) {
								// 新增
								int addMxGeometry = mxGeometryMapper.addMxGeometry(mxGeometry);
								if (addMxGeometry > 0) {
									logger.info("mxGeometry保存成功ID:" + mxGeometry.getId());
								}
							}
							mxCell.setMxGeometry(mxGeometry);
							// 新增
							int addMxCell = mxCellMapper.addMxCell(mxCell);
							if (addMxCell > 0) {
								logger.info("mxCell保存成功ID:" + mxCell.getId());
							}
						}
					}
				}
				// 修改
				if (null != updateMxCellList) {
					for (MxCell mxCell : updateMxCellList) {
						if (null != mxCell) {
							MxGeometry mxGeometry = mxCell.getMxGeometry();
							// 判空，保存
							if (null != mxGeometry) {
								int updateMxGeometry = mxGeometryMapper.updateMxGeometry(mxGeometry);
								if (updateMxGeometry > 0) {
									logger.info("mxGeometry保存成功ID:" + mxGeometry.getId());
								}
							}
							mxCell.setMxGeometry(mxGeometry);
							int updateMxCell = mxCellMapper.updateMxCell(mxCell);
							if (updateMxCell > 0) {
								logger.info("mxCell保存成功ID:" + mxCell.getId());
							}
						}

					}
				}
				flow.setMxGraphModel(mxGraphModel);
				// 保存flow
				int updateFlow = flowMapper.updateFlow(flow);
				// 判断是否保存成功
				if (updateFlow > 0) {
					// get出数据库中存的Stopslist
					List<Stops> stopsList = flow.getStopsList();
					// get出数据库中存的PathsList
					List<Paths> pathsList = flow.getPathsList();
					// 把mxCellVoList中的stops和线分开
					Map<String, Object> stopsPathsMap = this.distinguishStopsPaths(mxCellVoList);
					if (null != stopsPathsMap) {
						// 取线的mxCellVoList
						List<MxCellVo> objectPaths = (ArrayList<MxCellVo>) stopsPathsMap.get("paths");
						// 根据MxCellList中的内容生成paths的list(转换过后的)
						List<Paths> toPathsList = this.mxCellListToPathsList(objectPaths, flow);
						// 判断数据库中的线的list是否为空，不为空继续下边的判断操作，否则直接添加
						if (null != pathsList && pathsList.size() > 0) {
							// key为paths的PageId(画板中所属id),value为Paths
							Map<String, Paths> objectPathsMap = new HashMap<String, Paths>();
							// 需添加的pathsList
							List<Paths> addPaths = new ArrayList<Paths>();
							// 需修改的pathsList
							List<Paths> updatePaths = new ArrayList<Paths>();

							// 判断转换后的页面传过来的值是否为空，不为空就是修改
							// 否则说明页面把所有线都删除了，我们也做逻辑删除
							if (null != toPathsList && toPathsList.size() > 0) {
								// 循环 把objectPaths转为objectPathsMap
								for (Paths paths : pathsList) {
									if (null != paths) {
										objectPathsMap.put(paths.getPageId(), paths);
									}
								}
								for (Paths paths : toPathsList) {
									String pageId = paths.getPageId();
									Paths objPaths = objectPathsMap.get(pageId);
									if (null != objPaths) {
										// 取到说明数据库中已存在不用操作，移除map即可
										objectPathsMap.remove(pageId);
									} else {
										// 如果取到的是空则新增
										addPaths.add(objPaths);
									}
								}
								// objectPathsMap中的所有不需要动的都以移除，剩下为要逻辑删除的
								for (String pageid : objectPathsMap.keySet()) {
									Paths paths = objectPathsMap.get(pageid);
									if (null != paths) {
										paths.setEnableFlag(false);
										paths.setLastUpdateDttm(new Date());
										paths.setLastUpdateUser("Nature");
										paths.setVersion(paths.getVersion() + 1);
										updatePaths.add(paths);
									}
								}

							} else {
								// 循环做逻辑删除操作
								for (Paths paths : pathsList) {
									paths.setEnableFlag(false);
									paths.setLastUpdateDttm(new Date());
									paths.setLastUpdateUser("Nature");
									paths.setVersion(paths.getVersion() + 1);
									updatePaths.add(paths);
								}
							}
							if (null != updatePaths && updatePaths.size() > 0) {
								for (Paths paths : updatePaths) {
									if (null != paths) {
										pathsMapper.updatePathsList(paths);
									}
								}
							}
							if (null != addPaths && addPaths.size() > 0) {
								// 保存pathsList
								int addPathsListInt = pathsMapper.addPathsList(addPaths);
								if (addPathsListInt <= 0) {
									logger.info("pathsList保存失败所属流：flowId(" + flow.getId() + ")");
								}
							}
						} else {
							// 判断页面传的值是否为空，不为空则直接添加线，否则不操作
							if (null != toPathsList && toPathsList.size() > 0) {
								// 保存pathsList
								int addPathsListInt = pathsMapper.addPathsList(toPathsList);
								if (addPathsListInt <= 0) {
									logger.info("pathsList保存失败所属流：flowId(" + flow.getId() + ")");
								}
							}
						}
						// 取stops的明显CellVoList
						List<MxCellVo> objectStops = (ArrayList<MxCellVo>) stopsPathsMap.get("stops");
						// 判断数据库中的线的list是否为空，不为空继续下边的判断操作，否则直接添加
						if (null != stopsList && stopsList.size() > 0) {
							// key为stops的PageId(画板中所属id),value为Stops
							Map<String, Stops> objectStopsMap = new HashMap<String, Stops>();
							// 需添加的stopsList
							List<Stops> addStops = new ArrayList<Stops>();
							// 需修改的stopsList
							List<Stops> updateStops = new ArrayList<Stops>();
							if (null != objectStops && objectStops.size() > 0) {
								// 循环 把stopsList转为objectStopsMap
								for (Stops stops : stopsList) {
									if (null != stops) {
										objectStopsMap.put(stops.getPageId(), stops);
									}
								}

								// 循环页面传的值通过pageId在map中取数据库中数据并做判断
								for (MxCellVo mxCellVo : objectStops) {
									String pageId = mxCellVo.getPageId();
									Stops stops = objectStopsMap.get(pageId);
									if (null != stops) {
										// 取到说明数据库中已存在不用操作，移除map即可
										objectStopsMap.remove(pageId);
									} else {
										// 如果取到的是空则新增
										Stops toStops = this.stopsTemplateToStops(mxCellVo);
										toStops.setFlow(flow);
										addStops.add(toStops);
									}
								}
							} else {
								// 循环做逻辑删除操作
								for (Stops stops : stopsList) {
									stops.setEnableFlag(false);
									stops.setLastUpdateDttm(new Date());
									stops.setLastUpdateUser("Nature");
									stops.setVersion(stops.getVersion() + 1);
									updateStops.add(stops);
								}
							}
							// 做添加操作
							if (null != addStops && addStops.size() > 0) {
								// 保存addStops
								int addStopsList = stopsMapper.addStopsList(addStops);
								if (addStopsList > 0) {
									for (Stops stops : addStops) {
										// 取出propertyList
										List<Property> propertyList = stops.getProperties();
										// 判空propertyList
										if (null != propertyList && propertyList.size() > 0) {
											// 保存propertyList
											int addPropertyList = propertyMapper.addPropertyList(propertyList);
											if (addPropertyList <= 0) {
												logger.info("Property保存失败：stopsId(" + stops.getId() + "),stopsName("
														+ stops.getName() + ")");
											}
										} else {
											logger.info("propertyList为空,不保存");
										}
									}
								} else {
									setStatefulRtnBase("新建保存失败stopsList");
								}
							}
							// 做删除操作
							if (null != updateStops && updateStops.size() > 0) {
								for (Stops stops : updateStops) {
									if (null != stops) {
										stopsMapper.updateStops(stops);
									}
								}
							}
						} else {
							// 判断页面传的值是否为空，不为空则直接添加线，否则不操作
							if (null != objectStops && objectStops.size() > 0) {
								// 根据MxCellList中的内容生成paths的list
								List<Stops> toStopsList = this.mxCellVoListToStopsList(objectStops, flow);
								if (null != toStopsList && toStopsList.size() > 0) {
									// 保存stopsList
									int addStopsListInt = stopsMapper.addStopsList(toStopsList);
									if (addStopsListInt > 0) {
										for (Stops stops : toStopsList) {
											// 取出propertyList
											List<Property> propertyList = stops.getProperties();
											// 判空propertyList
											if (null != propertyList && propertyList.size() > 0) {
												// 保存propertyList
												int addPropertyList = propertyMapper.addPropertyList(propertyList);
												if (addPropertyList <= 0) {
													logger.info("Property保存失败：stopsId(" + stops.getId() + "),stopsName("
															+ stops.getName() + ")");
												}
											} else {
												logger.info("propertyList为空,不保存");
											}
										}
									} else {
										logger.info("stopsList保存失败所属流：flowId(" + flow.getId() + ")");
									}
								} else {
									logger.info("页面传值无stops不做保存");
								}
							} else {
								logger.info("页面传值无stops不做保存");
							}

						}
					}
				} else {
					statefulRtnBase = setStatefulRtnBase("修改保存失败flow");
				}
			} else {
				statefulRtnBase = setStatefulRtnBase("mxGraphModel修改失败");
			}
		} else {
			statefulRtnBase = setStatefulRtnBase("flowId查不到对应的flow，修改失败");
		}
		return statefulRtnBase;
	}

	/**
	 * @Title 区分stop和path
	 * @param root
	 * @return 返回map中有stops和paths的Mxcell型的List(键为：paths和stops)
	 */
	private Map<String, Object> distinguishStopsPaths(List<MxCellVo> root) {
		Map<String, Object> map = null;
		if (null != root && root.size() > 0) {
			map = new HashMap<String, Object>();
			List<MxCellVo> pathsList = new ArrayList<MxCellVo>();
			List<MxCellVo> stopsList = new ArrayList<MxCellVo>();
			// 循环root
			for (MxCellVo mxCellVo : root) {
				if (null != mxCellVo) {
					// 取出style属性
					String style = mxCellVo.getStyle();
					// 判空
					if (StringUtils.isNotBlank(style)) {
						// 取出线特有的属性判断是否为空
						String edge = mxCellVo.getEdge();
						if (StringUtils.isNotBlank(edge)) {
							pathsList.add(mxCellVo);
						} else {
							stopsList.add(mxCellVo);
						}
					}
				}
			}
			map.put("stops", stopsList);
			map.put("paths", pathsList);
		}
		return map;
	}

	/**
	 * @Title 根据MxCellList中的内容生成paths的list
	 * 
	 * @param objectPaths
	 * @param flow
	 * @return
	 */
	private List<Paths> mxCellListToPathsList(List<MxCellVo> objectPaths, Flow flow) {
		List<Paths> pathsList = null;
		if (null != objectPaths && objectPaths.size() > 0) {
			pathsList = new ArrayList<Paths>();
			// 循环objectPaths
			for (MxCellVo mxCellVo : objectPaths) {
				Paths paths = this.mxCellToPaths(mxCellVo);
				if (null != paths) {
					paths.setFlow(flow);
					pathsList.add(paths);
				}
			}
		}
		return pathsList;
	}

	/**
	 * @Title mxCell转Paths
	 * 
	 * @param mxCellVo
	 * @return
	 */
	private Paths mxCellToPaths(MxCellVo mxCellVo) {
		Paths paths = null;
		if (null != mxCellVo) {
			paths = new Paths();
			paths.setId(Utils.getUUID32());
			paths.setCrtDttm(new Date());
			paths.setCrtUser("-1");
			paths.setLastUpdateDttm(new Date());
			paths.setLastUpdateUser("-1");
			paths.setVersion(0L);
			paths.setEnableFlag(true);
			paths.setFrom(mxCellVo.getSource());
			paths.setTo(mxCellVo.getTarget());
			paths.setOutport("");
			paths.setInport("");
			paths.setPageId(mxCellVo.getPageId());
		}
		return paths;
	}

	/**
	 * @Title 根据MxCellList中的内容生成stops的list
	 * 
	 * @param objectStops
	 * @param flow
	 * @return
	 */
	private List<Stops> mxCellVoListToStopsList(List<MxCellVo> objectStops, Flow flow) {
		List<Stops> stopsList = null;
		if (null != objectStops && objectStops.size() > 0) {
			stopsList = new ArrayList<Stops>();
			// 循环objectStops
			for (MxCellVo mxCellVo : objectStops) {
				Stops stops = this.stopsTemplateToStops(mxCellVo);
				if (null != stops) {
					stops.setFlow(flow);
					stopsList.add(stops);
				}
			}
		}
		return stopsList;
	}

	/**
	 * @Title mxCell转stops
	 * 
	 * @param mxCellVo
	 * @return
	 */
	private Stops stopsTemplateToStops(MxCellVo mxCellVo) {
		Stops stops = null;
		if (null != mxCellVo) {
			// 取出style属性(属性中有stops的name)
			// stops的style属性例子
			// （image;html=1;labelBackgroundColor=#ffffff;image=/grapheditor/stencils/clipart/test_stops_1_128x128.png）
			// 我们需要的是"test_stops_1"
			String style = mxCellVo.getStyle();
			if (StringUtils.isNotBlank(style)) {
				// 通过截取关键字来判断是否为stops
				String[] split = style.split("_128x128.");
				// 当有且仅有一个关键字("_128x128.")时则认为是stops
				if (null != split && split.length == 2) {
					// 取数组第一位继续截取
					String string = split[0];
					String[] split2 = string.split("/");
					// 判空，取数组最后一位（stops的name）
					if (null != split2 && split2.length > 0) {
						// 取到stops的名字
						String stopsName = split2[split2.length - 1];
						// 根据stops的Name查询stops模板
						StopsTemplate stopsTemplate = this.getStopsTemplate(stopsName);
						// 模板判空
						if (null != stopsTemplate) {
							stops = new Stops();
							stops.setCrtDttm(new Date());
							stops.setCrtUser("-1");
							stops.setLastUpdateDttm(new Date());
							stops.setLastUpdateUser("-1");
							stops.setVersion(0L);
							stops.setEnableFlag(true);
							stops.setId(Utils.getUUID32());
							stops.setBundel(stopsTemplate.getBundel());
							stops.setDescription(stopsTemplate.getDescription());
							stops.setGroups(stopsTemplate.getGroups());
							stops.setName(stopsTemplate.getName());
							stops.setNumberOfEntrances(stopsTemplate.getNumberOfEntrances() + "");
							stops.setNumberOfExports(stopsTemplate.getNumberOfExports() + "");
							stops.setOwner(stopsTemplate.getOwner());
							stops.setPageId(mxCellVo.getPageId());
							List<Property> propertiesList = null;
							List<PropertyTemplate> propertiesTemplateList = stopsTemplate.getProperties();
							if (null != propertiesTemplateList && propertiesTemplateList.size() > 0) {
								propertiesList = new ArrayList<Property>();
								for (PropertyTemplate propertyTemplate : propertiesTemplateList) {
									Property property = new Property();
									property.setId(Utils.getUUID32());
									property.setCrtDttm(new Date());
									property.setLastUpdateDttm(new Date());
									property.setLastUpdateUser("-1");
									property.setVersion(0L);
									property.setEnableFlag(propertyTemplate.getEnableFlag());
									property.setStops(stops);
									property.setName(property.getName());
									property.setRequired(propertyTemplate.isRequired());
									property.setSensitive(propertyTemplate.isSensitive());
									property.setDisplayName(propertyTemplate.getDisplayName());
									property.setDescription(propertyTemplate.getDescription());
									property.setCustomValue(propertyTemplate.getDefaultValue());
									property.setAllowableValues(propertyTemplate.getAllowableValues());
									propertiesList.add(property);
								}
							}
							stops.setProperties(propertiesList);
						}
					}
				}
			}
		}
		return stops;
	}

	/**
	 * @Title 根据stopsName查询stops
	 * 
	 * @param stopsName
	 * @return
	 */
	private StopsTemplate getStopsTemplate(String stopsName) {
		StopsTemplate stopsTemplate = null;
		// 根据stops的Name查询stops模板
		List<StopsTemplate> stopsTemplateList = stopsTemplateMapper.getStopsTemplateByName(stopsName);
		if (null != stopsTemplateList && stopsTemplateList.size() > 0) {
			stopsTemplate = stopsTemplateList.get(0);
		}
		return stopsTemplate;
	}

}

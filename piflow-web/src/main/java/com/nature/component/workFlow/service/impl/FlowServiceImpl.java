package com.nature.component.workFlow.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nature.base.util.LoggerUtil;
import com.nature.base.util.Utils;
import com.nature.base.vo.StatefulRtnBase;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGeometry;
import com.nature.component.mxGraph.model.MxGraphModel;
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

	@SuppressWarnings("unchecked")
	@Override
	public StatefulRtnBase addFlow(MxGraphModel mxGraphModel, String flowId) {
		StatefulRtnBase statefulRtnBase = new StatefulRtnBase();
		// 判断mxGraphModel是否为空
		if (null != mxGraphModel && StringUtils.isNotBlank(flowId)) {
			// 新建流水线
			Flow flow = new Flow();
			// setId
			flow.setId(flowId);
			// setUuid
			flow.setUuid(flowId);
			// 创建时间
			flow.setCrtDttm(new Date());
			// 创建人
			flow.setCrtUser("-1");
			// 最后更新时间
			flow.setLastUpdateDttm(new Date());
			// 最后更新人
			flow.setLastUpdateUser("-1");
			// 是否有效
			flow.setEnableFlag(true);
			// 版本号
			flow.setVersion(0L);
			// 流水线Name
			flow.setName("默认");
			// setmxGraphModel基本属性
			mxGraphModel.setId(Utils.getUUID32());
			mxGraphModel.setCrtDttm(new Date());
			mxGraphModel.setCrtUser("Nature");
			mxGraphModel.setEnableFlag(true);
			mxGraphModel.setLastUpdateUser("Nature");
			mxGraphModel.setLastUpdateDttm(new Date());
			mxGraphModel.setVersion(0L);
			StatefulRtnBase addMxGraphModel = this.addMxGraphModel(mxGraphModel);
			if (null != addMxGraphModel && addMxGraphModel.isReqRtnStatus()) {
				flow.setMxGraphModel(mxGraphModel);
				int isSuccess = flowMapper.addFlow(flow);
				if (isSuccess > 0) {
					// 线的List
					List<Paths> pathsList = new ArrayList<Paths>();
					// stops的list
					List<Stops> stopsList = new ArrayList<Stops>();

					// 取出所有的MxCell(里边有线也有stops)
					List<MxCell> root = mxGraphModel.getRoot();
					if (null != root && root.size() > 0) {
						Map<String, Object> setStopsAndPathsList = setStopsAndPathsList(flow, root);
						stopsList = (List<Stops>) setStopsAndPathsList.get("stopsList");
						pathsList = (List<Paths>) setStopsAndPathsList.get("pathsList");
						flow.setPaths(pathsList);
						flow.setStops(stopsList);
						if (null != stopsList && stopsList.size() > 0) {
							int addStopsList = stopsMapper.addStopsList(stopsList);
							if (addStopsList > 0) {
								for (Stops stops : stopsList) {
									List<Property> propertyList = stops.getProperties();
									if (null != propertyList & propertyList.size() > 0) {
										int addPropertyList = propertyMapper.addPropertyList(propertyList);
										if (addPropertyList <= 0) {
											logger.info("Property保存失败：stopsId(" + stops.getId() + "),stopsName("
													+ stops.getName() + ")");
										}
									} else {
										logger.info("stopsList为空,不保存");
									}
								}
								if (null != pathsList & pathsList.size() > 0) {
									int addPathsList = pathsMapper.addPathsList(pathsList);
									if (addPathsList <= 0) {
										logger.info("Property保存失败：flowId(" + flow.getId() + "),flowName("
												+ flow.getName() + ")");
									}
								} else {
									logger.info("stopsList为空,不保存");
								}
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
			setStatefulRtnBase("新建保存失败");
		}
		return statefulRtnBase;
	}

	@Override
	public Flow getFlowById(String id) {
		return flowMapper.getFlowById(id);
	}

	private Map<String, Object> setStopsAndPathsList(Flow flow, List<MxCell> root) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (null != flow && null != root && root.size() > 0) {
			List<Paths> pathsList = new ArrayList<Paths>();
			List<Stops> stopsList = new ArrayList<Stops>();
			// 循环root
			for (MxCell mxCell : root) {
				// 取出style属性(属性中有stops的name)
				// stops的style属性例子
				// （image;html=1;labelBackgroundColor=#ffffff;image=/grapheditor/stencils/clipart/test_stops_1_128x128.png）
				// 我们需要的是"test_stops_1"
				String style = mxCell.getStyle();
				// 判空
				if (StringUtils.isNotBlank(style)) {
					// 取出线特有的属性判断是否为空
					String edge = mxCell.getEdge();
					if (StringUtils.isNotBlank(edge)) {
						Paths paths = new Paths();
						paths.setId(Utils.getUUID32());
						paths.setCrtDttm(new Date());
						paths.setCrtUser("-1");
						paths.setLastUpdateDttm(new Date());
						paths.setLastUpdateUser("-1");
						paths.setVersion(0L);
						paths.setEnableFlag(true);
						paths.setFrom(mxCell.getSource());
						paths.setTo(mxCell.getTarget());
						paths.setOutport("");
						paths.setInport("");
						paths.setFlow(flow);
						pathsList.add(paths);
					} else {
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
								List<StopsTemplate> stopsTemplateList = stopsTemplateMapper.getStopsTemplateByName(stopsName);
								// 模板判空
								if (null != stopsTemplateList && stopsTemplateList.size() > 0) {
									StopsTemplate stopsTemplate = stopsTemplateList.get(0);
									Stops stops = new Stops();
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
									stops.setPageId(mxCell.getPageId());
									stops.setFlow(flow);
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
											property.setCustomValue(property.getCustomValue());
											property.setAllowableValues(propertyTemplate.getAllowableValues());
											propertiesList.add(property);
										}
									}
									stops.setProperties(propertiesList);
									stopsList.add(stops);
								}
							}
						}
					}
				}
			}
			map.put("stopsList", stopsList);
			map.put("pathsList", pathsList);
		}
		return map;

	}

	private StatefulRtnBase addMxGraphModel(MxGraphModel mxGraphModel) {
		StatefulRtnBase statefulRtnBase = new StatefulRtnBase();
		int addMxGraphModel = mxGraphModelMapper.addMxGraphModel(mxGraphModel);
		if (addMxGraphModel > 0) {
			List<MxCell> mxCellList = mxGraphModel.getRoot();
			for (MxCell mxCell : mxCellList) {
				MxGeometry mxGeometry = mxCell.getMxGeometry();
				if (null != mxGeometry) {
					// mxGeometry 的基本属性
					mxGeometry.setId(Utils.getUUID32());
					mxGeometry.setCrtDttm(new Date());
					mxGeometry.setCrtUser("Nature");
					mxGeometry.setEnableFlag(true);
					mxGeometry.setLastUpdateUser("Nature");
					mxGeometry.setLastUpdateDttm(new Date());
					mxGeometry.setVersion(0L);
					int addMxGeometry = mxGeometryMapper.addMxGeometry(mxGeometry);
					if (addMxGeometry > 0) {
						mxCell.setMxGraphModel(mxGraphModel);
						mxCell.setMxGeometry(mxGeometry);
						// mxCell 的基本属性
						mxCell.setId(Utils.getUUID32());
						mxCell.setCrtDttm(new Date());
						mxCell.setCrtUser("Nature");
						mxCell.setEnableFlag(true);
						mxCell.setLastUpdateUser("Nature");
						mxCell.setLastUpdateDttm(new Date());
						mxCell.setVersion(0L);
						int addMxCell = mxCellMapper.addMxCell(mxCell);
						if (addMxCell <= 0) {
							statefulRtnBase = setStatefulRtnBase("MxCell保存失败");
						}
					} else {
						statefulRtnBase = setStatefulRtnBase("MxGraphModel保存失败");
					}
				}
			}
		} else {
			statefulRtnBase = setStatefulRtnBase("MxGraphModel保存失败");
		}
		return statefulRtnBase;
	}

	private StatefulRtnBase setStatefulRtnBase(String errMsg) {
		StatefulRtnBase statefulRtnBase = new StatefulRtnBase();
		logger.info(errMsg);
		statefulRtnBase.setReqRtnStatus(false);
		statefulRtnBase.setErrorCode(statefulRtnBase.ERRCODE_FAIL);
		statefulRtnBase.setErrorMsg(errMsg);
		return statefulRtnBase;
	}
}

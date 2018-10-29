package com.nature.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nature.base.util.FlowXmlUtils;
import com.nature.base.util.HttpUtils;
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
import com.nature.component.workFlow.model.FlowInfoDb;
import com.nature.component.workFlow.model.StopGroup;
import com.nature.component.workFlow.service.FlowService;
import com.nature.component.workFlow.service.StopGroupService;
import com.nature.third.GetFlowInfo;
import com.nature.third.inf.IGetFlowInfo;
import com.nature.third.inf.IGetFlowLog;
import com.nature.third.inf.IStartFlow;
import com.nature.third.inf.IStopFlow;
import com.nature.third.vo.flowLog.AppVo;
import com.nature.third.vo.flowLog.FlowLog;

@Controller
@RequestMapping("/flow/*")
public class GrapheditorCtrl {

	/**
	 * @Title 引入日志，注意都是"org.slf4j"包下
	 */
	Logger logger = LoggerUtil.getLogger();

	@Autowired
	private FlowService flowServiceImpl;

	@Autowired
	private StopGroupService stopGroupServiceImpl;

	@Autowired
	private IStartFlow startFlowImpl;

	@Autowired
	private IStopFlow stopFlowImpl;

	@Autowired
	private IGetFlowLog getFlowLogImpl;

	@Resource
	private IGetFlowInfo getFlowInfoImpl;

	@Resource
	private GetFlowInfo getFlowInfo;

	@RequestMapping("/grapheditor")
	public String kitchenSink(Model model, String load) {
		// 判斷是否存在Flow的id(load),如果存在則加載，否則生成UUID返回返回頁面
		if (StringUtils.isNotBlank(load)) {
			// 左側的組和stops
			List<StopGroup> groupsList = stopGroupServiceImpl.getStopGroupAll();
			model.addAttribute("groupsList", groupsList);
			Flow flowById = flowServiceImpl.getFlowById(load);
			if (null != flowById) {
				model.addAttribute("appId", flowById.getAppId());
			}
			// 把查詢出來的Flow轉爲XML
			String loadXml = FlowXmlUtils.mxGraphModelToXml(flowToMxGraphModelVo(flowById));
			model.addAttribute("xmlDate", loadXml);
			model.addAttribute("load", load);
		} else {
			// 生成32位UUID
			load = Utils.getUUID32();
			return "redirect:grapheditor?load=" + load;
		}
		return "grapheditor/index";
	}

	@RequestMapping("/open")
	public String open(Model model) {
		model.addAttribute("josnStr", "");
		return "grapheditor/open";
	}

	@RequestMapping("/saveData")
	@ResponseBody
	public String saveData(HttpServletRequest request, Model model) {
		String rtnStr = "0";
		String imageXML = request.getParameter("imageXML");
		String loadId = request.getParameter("load");
		if (StringUtils.isNotBlank(imageXML) && StringUtils.isNotBlank(loadId)) {
			// 把页面传來的XML转为mxGraphModel
			MxGraphModelVo xmlToMxGraphModel = FlowXmlUtils.xmlToMxGraphModel(imageXML);
			StatefulRtnBase addFlow = flowServiceImpl.saveOrUpdateFlowAll(xmlToMxGraphModel, loadId);
			// addFlow不为空且ReqRtnStatus的值为true,则保存成功
			if (null != addFlow && addFlow.isReqRtnStatus()) {
				rtnStr = "1";
			}
		}
		return rtnStr;
	}

	@RequestMapping("/runFlow")
	@ResponseBody
	public String runFlow(HttpServletRequest request, Model model) {
		String rtnMsg = "0";
		String flowId = request.getParameter("flowId");
		if (StringUtils.isNotBlank(flowId)) {
			// 根据flowId查询flow
			Flow flowById = flowServiceImpl.getFlowById(flowId);
			// addFlow不为空且ReqRtnStatus的值为true,则保存成功
			if (null != flowById) {
				String startFlow = startFlowImpl.startFlow(flowById);
				if (StringUtils.isNotBlank(startFlow)) {
					FlowInfoDb flowInfoDb = getFlowInfoImpl.AddFlowInfo(startFlow);
					if (null != flowInfoDb) {
						StatefulRtnBase saveAppId = flowServiceImpl.saveAppId(flowId, flowInfoDb);
						if (null != saveAppId && saveAppId.isReqRtnStatus()) {
							logger.info("flowId为" + flowId + "的flow，保存appID成功" + startFlow);
						} else {
							logger.warn("flowId为" + flowId + "的flow，保存appID出错");
						}
						rtnMsg = "启动成功，返回的appid为：" + startFlow;
					} else {
						rtnMsg = "flowInfoDb创建失败";
					}
				} else {
					rtnMsg = "启动失败";
				}
			} else {
				logger.warn("未查询到flowId为" + flowId + "的flow");
			}
		} else {
			logger.warn("flowId为空");
		}
		return rtnMsg;
	}

	@RequestMapping("/stopFlow")
	@ResponseBody
	public String stopFlow(HttpServletRequest request, Model model) {
		String rtnMsg = "0";
		String flowId = request.getParameter("flowId");
		if (StringUtils.isNotBlank(flowId)) {
			// 根据flowId查询flow
			Flow flowById = flowServiceImpl.getFlowById(flowId);
			// addFlow不为空且ReqRtnStatus的值为true,则保存成功
			if (null != flowById) {
				String appId = flowById.getAppId().getId();
				if (StringUtils.isNotBlank(appId)) {
					String flowStop = stopFlowImpl.stopFlow(appId);
					if (StringUtils.isNotBlank(flowStop)) {
						rtnMsg = "停止成功，返回状态为：" + flowStop;
					}
				}
			} else {
				logger.warn("未查询到flowId为" + flowId + "的flow");
			}
		} else {
			logger.warn("flowId为空");
		}
		return rtnMsg;
	}

	@RequestMapping("/getLogUrl")
	@ResponseBody
	public Map<String, String> getLogUrl(HttpServletRequest request, Model model) {
		Map<String, String> rtnMap = new HashMap<>();
		rtnMap.put("code", "0");
		String appId = request.getParameter("appId");
		if (StringUtils.isNotBlank(appId)) {
			FlowLog flowlog = getFlowLogImpl.getFlowLog(appId);
			if (null != flowlog) {
				AppVo app = flowlog.getApp();
				if (null != app) {
					rtnMap.put("code", "1");
					rtnMap.put("stdoutLog", app.getAmContainerLogs() + "/stdout/?start=0");
					rtnMap.put("stderrLog", app.getAmContainerLogs() + "/stderr/?start=0");
				}
			}
		} else {
			logger.warn("appId为空");
		}

		return rtnMap;
	}

	@RequestMapping("/getLog")
	@ResponseBody
	public String getLog(HttpServletRequest request, Model model) {
		String rtnMsg = "";
		String urlStr = request.getParameter("url");
		if (StringUtils.isNotBlank(urlStr)) {
			rtnMsg = HttpUtils.getHtml(urlStr);
		} else {
			logger.warn("urlStr为空");
		}

		return rtnMsg;
	}

	private MxGraphModelVo flowToMxGraphModelVo(Flow flow) {
		MxGraphModelVo mxGraphModelVo = null;
		// 判空
		if (null != flow) {
			// 取出mxGraphModel
			MxGraphModel mxGraphModel = flow.getMxGraphModel();
			// 判空mxGraphModel
			if (null != mxGraphModel) {
				mxGraphModelVo = new MxGraphModelVo();
				// 拷贝mxGraphModel的内容到mxGraphModelVo中
				BeanUtils.copyProperties(mxGraphModel, mxGraphModelVo);
				// 取出mxCellList
				List<MxCell> root = mxGraphModel.getRoot();
				// 判空
				if (null != root && root.size() > 0) {
					List<MxCellVo> mxCellVoList = new ArrayList<MxCellVo>();
					// 循环拷贝
					for (MxCell mxCell : root) {
						if (null != mxCell) {
							MxCellVo mxCellVo = new MxCellVo();
							// 拷贝mxGraphModel的内容到mxGraphModelVo中
							BeanUtils.copyProperties(mxCell, mxCellVo);
							MxGeometry mxGeometry = mxCell.getMxGeometry();
							if (null != mxGeometry) {
								MxGeometryVo mxGeometryVo = new MxGeometryVo();
								// 拷贝mxGeometry的内容到mxGeometryVo中
								BeanUtils.copyProperties(mxGeometry, mxGeometryVo);
								mxCellVo.setMxGeometryVo(mxGeometryVo);
							}
							mxCellVoList.add(mxCellVo);
						}
					}
					mxGraphModelVo.setRootVo(mxCellVoList);
				}

			}
		}
		return mxGraphModelVo;
	}

	@RequestMapping("/queryFlowData")
	@ResponseBody
	public Flow saveData(String load) {
		Flow flow = flowServiceImpl.getFlowById(load);
		return flow;
	}

	@RequestMapping("/saveFlowInfo")
	@ResponseBody
	public Flow saveFlowInfo(Flow flow) {
		String id = Utils.getUUID32();
		flow.setId(id);
		flow.setName(flow.getName());
		flow.setDescription(flow.getDescription());
		flow.setCrtDttm(new Date());
		flow.setCrtUser("wdd");
		flow.setLastUpdateDttm(new Date());
		flow.setLastUpdateUser("ddw");
		flow.setEnableFlag(true);
		flow.setVersion(0L);
		flow.setUuid(id);
		flowServiceImpl.addFlow(flow);
		return flow;
	}

	@RequestMapping("/updateFlowInfo")
	@ResponseBody
	public int updateFlowInfo(Flow flow) {
		String id = flow.getId();
		flow.setId(id);
		flow.setName(flow.getName());
		flow.setDescription(flow.getDescription());
		flow.setLastUpdateDttm(new Date());
		flow.setLastUpdateUser("ddw");
		flow.setVersion(0L + 1);
		int result = flowServiceImpl.updateFlow(flow);
		return result;
	}

}

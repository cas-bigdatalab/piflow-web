package com.nature.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.nature.component.workFlow.model.StopGroup;
import com.nature.component.workFlow.service.FlowService;
import com.nature.component.workFlow.service.StopGroupService;
import com.nature.third.StartFlow;
import com.nature.third.StopFlow;

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

	@Resource
	private StartFlow startFlowInf;

	@Resource
	private StopFlow stopFlowInf;

	@RequestMapping("/grapheditor")
	public String kitchenSink(Model model, String load) {
		// 判斷是否存在Flow的id(load),如果存在則加載，否則生成UUID返回返回頁面
		if (StringUtils.isNotBlank(load)) {
			// 左側的組和stops
			List<StopGroup> groupsList = stopGroupServiceImpl.getStopGroupAll();
			model.addAttribute("groupsList", groupsList);
			Flow flowById = flowServiceImpl.getFlowById(load);
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
		return "/grapheditor/open";
	}

	@RequestMapping("/table")
	public String runningList(Model model) {
		model.addAttribute("say", "say hello spring boot !!!!!");
		model.addAttribute("sss", "ss后台返回数据ss");
		return "/charisma/table";
	}

	@RequestMapping("/index")
	public String index(Model model) {
		model.addAttribute("now", "say hello spring boot !!!!!");
		return "/charisma/index";
	}

	@RequestMapping("/login")
	public String login(Model model) {
		model.addAttribute("now", "say hello spring boot !!!!!");
		return "/charisma/login";
	}

	@RequestMapping("/tour")
	public String tour(Model model) {
		model.addAttribute("now", "say hello spring boot !!!!!");
		return "/charisma/tour";
	}

	@RequestMapping("/ui")
	public String ui(Model model) {
		model.addAttribute("now", "say hello spring boot !!!!!");
		return "/charisma/ui";
	}

	@RequestMapping("/errorPage")
	public String error(Model model) {
		model.addAttribute("now", "say hello spring boot !!!!!");
		return "/charisma/errorPage";
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
		String rtnMsg = "失败";
		String flowId = request.getParameter("flowId");
		if (StringUtils.isNotBlank(flowId)) {
			// 根据flowId查询flow
			Flow flowById = flowServiceImpl.getFlowById(flowId);
			// addFlow不为空且ReqRtnStatus的值为true,则保存成功
			if (null != flowById) {
				String startFlow = startFlowInf.startFlow(flowById);
				if (StringUtils.isNotBlank(startFlow)) {
					StatefulRtnBase saveAppId = flowServiceImpl.saveAppId(flowId, startFlow);
					if (null != saveAppId && saveAppId.isReqRtnStatus()) {
						logger.info("flowId为" + flowId + "的flow，保存appID成功" + startFlow);
					} else {
						logger.warn("flowId为" + flowId + "的flow，保存appID出错");
					}
					rtnMsg = "启动成功，返回的appid为：" + startFlow;
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
		String rtnMsg = "失败";
		String flowId = request.getParameter("flowId");
		if (StringUtils.isNotBlank(flowId)) {
			// 根据flowId查询flow
			Flow flowById = flowServiceImpl.getFlowById(flowId);
			// addFlow不为空且ReqRtnStatus的值为true,则保存成功
			if (null != flowById) {
				String appId = flowById.getAppId();
				if (StringUtils.isNotBlank(appId)) {
					String flowStop = stopFlowInf.flowStop(appId);
					if (StringUtils.isNotBlank(flowStop)) {
						rtnMsg = "停止成功，appid为：" + flowStop;
					} else {
						rtnMsg = "停止失败";
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

}

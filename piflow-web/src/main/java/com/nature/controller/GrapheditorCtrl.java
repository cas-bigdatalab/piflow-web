package com.nature.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nature.base.util.FlowXmlUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.base.util.UUIDUtils;
import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.StopGroup;
import com.nature.component.workFlow.model.StopsTemplate;
import com.nature.component.workFlow.service.FlowService;

@Controller
@RequestMapping("/flow/*")
public class GrapheditorCtrl {

	/**
	 * 引入日志，注意都是"org.slf4j"包下
	 */
	Logger logger = LoggerUtil.getLogger();

	@Autowired
	private FlowService flowService;

	private String saveXml = "";

	@RequestMapping("/grapheditor")
	public String kitchenSink(Model model, String load) {
		// 左側的組和stops
		List<StopGroup> groupsList = getStopGroupTemplate();
		model.addAttribute("groupsList", groupsList);
		// 判斷是否存在Flow的id(load),如果存在則加載，否則生成UUID返回返回頁面
		if (StringUtils.isNotBlank(load)) {
			Flow flowById = flowService.getFlowById(load);
			// 把查詢出來的Flow轉爲XML
			FlowXmlUtils.flowToXml(flowById);
			model.addAttribute("xmlDate", saveXml);
			model.addAttribute("load", load);
		} else {
			// 生成32位UUID
			load = UUIDUtils.getUUID32();
			return "redirect:grapheditor?load=" + load;
		}

		model.addAttribute("say", "say hello spring boot !!!!!");
		model.addAttribute("sss", "ss后台返回数据ss");
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
		String parameter = request.getParameter("imageXML");
		String loadId = request.getParameter("load");
		Flow flowById = flowService.getFlowById(loadId);
		if (null != flowById) {
			logger.info("在'" + loadId + "'的基礎上保存");
		} else {
			logger.info("新建");
		}
		saveXml = parameter;
		logger.info(parameter);
		model.addAttribute("say", "say hello spring boot !!!!!");
		return "flwoId";
	}

	@RequestMapping("/loadData")
	@ResponseBody
	public String loadData(HttpServletRequest request, Model model) {
		String parameter = saveXml;
		logger.info(parameter);
		model.addAttribute("now", "say hello spring boot !!!!!");
		return parameter;
	}

	private List<StopGroup> getStopGroupTemplate() {
		// 测试stops组件1
		StopsTemplate stopsTemplate1 = new StopsTemplate();
		stopsTemplate1.setName("test_stops_Components_1");
		stopsTemplate1.setBundel("测试stops组件1");
		stopsTemplate1.setGroups("测试stops组件1");
		stopsTemplate1.setOwner("测试stops组件1");
		stopsTemplate1.setDescription("测试stops组件1");
		stopsTemplate1.setNumberOfEntrances(1);
		stopsTemplate1.setNumberOfExports(1);
		// 测试stops组件2
		StopsTemplate stopsTemplate2 = new StopsTemplate();
		stopsTemplate2.setName("test_stops_Components_2");
		stopsTemplate2.setBundel("测试stops组件2");
		stopsTemplate2.setGroups("测试stops组件2");
		stopsTemplate2.setOwner("测试stops组件2");
		stopsTemplate2.setDescription("测试stops组件2");
		stopsTemplate2.setNumberOfEntrances(1);
		stopsTemplate2.setNumberOfExports(1);
		// 测试stops组件3
		StopsTemplate stopsTemplate3 = new StopsTemplate();
		stopsTemplate3.setName("test_stops_Components_3");
		stopsTemplate3.setBundel("测试stops组件3");
		stopsTemplate3.setGroups("测试stops组件3");
		stopsTemplate3.setOwner("测试stops组件3");
		stopsTemplate3.setDescription("测试stops组件3");
		stopsTemplate3.setNumberOfEntrances(1);
		stopsTemplate3.setNumberOfExports(1);

		List<StopGroup> groupsList = new ArrayList<StopGroup>();
		StopGroup stopGroup = null;
		List<StopsTemplate> stopsList = null;
		// 测试一组
		stopGroup = new StopGroup();
		stopsList = new ArrayList<StopsTemplate>();
		stopGroup.setGroupName("test_group_1");
		stopsList.add(stopsTemplate1);
		stopsList.add(stopsTemplate2);
		stopGroup.setStopsList(stopsList);
		groupsList.add(stopGroup);
		// 测试二组
		stopGroup = new StopGroup();
		stopsList = new ArrayList<StopsTemplate>();
		stopGroup.setGroupName("Test_group_2");
		stopsList.add(stopsTemplate1);
		stopsList.add(stopsTemplate3);
		stopGroup.setStopsList(stopsList);
		groupsList.add(stopGroup);
		// 测试三组
		stopGroup = new StopGroup();
		stopsList = new ArrayList<StopsTemplate>();
		stopGroup.setGroupName("Test_group_3");
		stopsList.add(stopsTemplate2);
		stopsList.add(stopsTemplate3);
		stopGroup.setStopsList(stopsList);
		groupsList.add(stopGroup);
		return groupsList;
	}

}

package com.nature.controller;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.nature.base.util.LoggerUtil;
import com.nature.component.template.service.ITemplateService;
import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.Template;
import com.nature.component.workFlow.service.IFlowInfoDbService;

@Controller
@RequestMapping("/web/*")
public class IndexCtrl {

	/**
	 * 引入日志，注意都是"org.slf4j"包下
	 */
	Logger logger = LoggerUtil.getLogger();

	@Autowired
    IFlowInfoDbService appService;
	
	@Autowired
	private ITemplateService iTemplateService;

	/**
	 * 首页
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/index")
	public ModelAndView index(ModelAndView modelAndView) {
		modelAndView.setViewName("indexPage");
		return modelAndView;
	}

	/**
	 * 首页右侧
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/indexHome")
	public ModelAndView indexHome(ModelAndView modelAndView) {
		modelAndView.setViewName("flow/inc/index_right");
		modelAndView.addObject("now", "say hello spring boot !!!!!");
		modelAndView.addObject("test", "say hello spring boot !!!!!");
		return modelAndView;
	}

	@RequestMapping("/flowList")
	public ModelAndView flowList(ModelAndView modelAndView) {
		modelAndView.setViewName("flow/table");
		List<Flow> appInfo = appService.findAppList();
		modelAndView.addObject("appInfo", appInfo);
		return modelAndView;
	}

	@RequestMapping("/tour")
	public ModelAndView tour(ModelAndView modelAndView) {
		modelAndView.setViewName("flow/inc/tour");
		modelAndView.addObject("now", "say hello spring boot !!!!!");
		return modelAndView;
	}

	@RequestMapping("/ui")
	public ModelAndView ui(ModelAndView modelAndView) {
		modelAndView.setViewName("flow/inc/ui");
		modelAndView.addObject("now", "say hello spring boot !!!!!");
		return modelAndView;
	}

	@RequestMapping("/errorPage")
	public ModelAndView error(ModelAndView modelAndView) {
		modelAndView.setViewName("errorPage");
		modelAndView.addObject("now", "say hello spring boot !!!!!");
		return modelAndView;
	}
	
	@RequestMapping("/template")
	public ModelAndView template(ModelAndView modelAndView) {
		List<Template> findTemPlateList = iTemplateService.findTemPlateList();
		modelAndView.setViewName("flow/template");
		modelAndView.addObject("TemPlateList",findTemPlateList);
		return modelAndView;
	}

}

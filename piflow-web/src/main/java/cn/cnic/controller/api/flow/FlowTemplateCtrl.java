package cn.cnic.controller.api.flow;

import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.component.template.service.IFlowTemplateService;
import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * template的ctrl
 */
@Api(value = "flowTemplate api", tags="flowTemplate api")
@RestController
@RequestMapping("/flowTemplate")
public class FlowTemplateCtrl {

	@Autowired
	private IFlowTemplateService flowTemplateServiceImpl;

	@RequestMapping(value = "/saveFlowTemplate", method = RequestMethod.POST)
	@ResponseBody
	public String saveFlowTemplate(String name, String load, String templateType) {
		String username = SessionUserUtil.getCurrentUsername();
		return flowTemplateServiceImpl.addFlowTemplate(username, name, load, templateType);
	}

	@RequestMapping(value = "/flowTemplatePage", method = RequestMethod.GET)
	@ResponseBody
	public String templatePage(Integer page, Integer limit, String param) {
		String username = SessionUserUtil.getCurrentUsername();
		boolean isAdmin = SessionUserUtil.isAdmin();
		return flowTemplateServiceImpl.getFlowTemplateListPage(username, isAdmin, page, limit, param);
	}

	/**
	 * Delete the template based on id
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/deleteFlowTemplate", method = RequestMethod.GET)
	@ResponseBody
	public String deleteFlowTemplate(String id) {
		return flowTemplateServiceImpl.deleteFlowTemplate(id);
	}

	/**
	 * Download template
	 *
	 * @param response
	 * @param flowTemplateId
	 * @throws Exception
	 */
	@RequestMapping(value = "/templateDownload", method = RequestMethod.GET)
	public void templateDownload(HttpServletResponse response, String flowTemplateId) throws Exception {
		flowTemplateServiceImpl.templateDownload(response, flowTemplateId);
	}

	/**
	 * Upload xml file and save flowTemplate
	 *
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/uploadXmlFile", method = RequestMethod.POST)
	@ResponseBody
	public String uploadXmlFile(@RequestParam("file") MultipartFile file) {
		String username = SessionUserUtil.getCurrentUsername();
		return flowTemplateServiceImpl.uploadXmlFile(username, file);
	}
	
	/**
	 * Upload xml file and save flowTemplate
	 *
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/uploadGalaxFile", method = RequestMethod.POST)
	@ResponseBody
	public String uploadGalaxFile(@RequestParam("file") MultipartFile file) {
		String username = SessionUserUtil.getCurrentUsername();
		return flowTemplateServiceImpl.uploadGalaxFile(username, file);
	}

	/**
	 * Query all templates for drop-down displays
	 *
	 * @return
	 */
	@RequestMapping(value = "/flowTemplateList", method = RequestMethod.POST)
	@ResponseBody
	public String flowTemplateList(String type) {
		String username = SessionUserUtil.getCurrentUsername();
		boolean isAdmin = SessionUserUtil.isAdmin();
		return flowTemplateServiceImpl.flowTemplateList(username, isAdmin, type);
	}

	/**
	 * load template
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loadingXmlPage", method = RequestMethod.POST)
	@ResponseBody
	public String loadingXml(String templateId, String load, String loadType) throws Exception {
		String username = SessionUserUtil.getCurrentUsername();
		if ("TASK".equals(loadType)) {
			return flowTemplateServiceImpl.loadTaskTemplate(username, templateId, load);
		} else if ("GROUP".equals(loadType)) {
			return flowTemplateServiceImpl.loadGroupTemplate(username, templateId, load);
		} else {
			return ReturnMapUtils.setFailedMsgRtnJsonStr("loadType is null");
		}
	}
	
	/**
	 * load Galax template
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loadingGalaxPage", method = RequestMethod.POST)
	@ResponseBody
	public String loadingGalax(String templateId, String load, String loadType) throws Exception {
		String username = SessionUserUtil.getCurrentUsername();
		if ("GALAX".equals(loadType)) {
			return flowTemplateServiceImpl.loadGalaxTemplate(username, templateId, load);
		} else {
			return ReturnMapUtils.setFailedMsgRtnJsonStr("loadType is null");
		}
	}
}
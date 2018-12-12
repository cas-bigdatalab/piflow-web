package com.nature.controller;

import com.alibaba.fastjson.JSON;
import com.nature.base.util.*;
import com.nature.base.vo.StatefulRtnBase;
import com.nature.common.constant.SysParamsCache;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.mxGraph.vo.MxGraphModelVo;
import com.nature.component.template.model.StopTemplateModel;
import com.nature.component.template.service.IFlowAndStopsTemplateVoService;
import com.nature.component.template.service.ITemplateService;
import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.Stops;
import com.nature.component.workFlow.model.Template;
import com.nature.component.workFlow.service.IFlowService;
import com.nature.component.workFlow.service.IStopGroupService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * template的ctrl
 */
@RestController
@RequestMapping("/template/")
public class TemplateCtrl {
	
	@Autowired
	private IFlowService iFlowServiceImpl;
	
	@Autowired
	private ITemplateService iTemplateService;
	
	@Autowired
	private IStopGroupService stopGroupServiceImpl;
	
	@Autowired
	private IFlowAndStopsTemplateVoService flowAndStopsTemplateVoServiceImpl;
	
	
	
	  /**
     * 引入日志，注意都是"org.slf4j"包下
     */
    Logger logger = LoggerUtil.getLogger();
	
	    @RequestMapping("/saveTemplate")
	    @ResponseBody
	    @Transactional
	    public String saveData(HttpServletRequest request, Model model) {
		  	Map<String, String> rtnMap = new HashMap<String, String>();
	        rtnMap.put("code", "0");
	        String name = request.getParameter("name");
	        String loadId = request.getParameter("load");
	        String value = request.getParameter("value");
	        MxGraphModelVo mxGraphModelVo = null;
	        if (StringUtils.isAnyEmpty(name, loadId)) {
	            rtnMap.put("errMsg", "传入参数有空的");
	            logger.info("传入参数有空的");
	            return JsonUtils.toJsonNoException(rtnMap);
	        }
	        	Flow flowById = iFlowServiceImpl.getFlowById(loadId);
	        	if (null != flowById) {
	        		if (StringUtils.isBlank(value)) {
	        			MxGraphModel mxGraphModel = flowById.getMxGraphModel();
	    	            if (null != mxGraphModel) {
	    	            	mxGraphModelVo = FlowXmlUtils.mxGraphModelPoToVo(mxGraphModel);
	    	            	// 把查询出來的mxGraphModelVo转为XML
	    	            	value = FlowXmlUtils.mxGraphModelToXml(mxGraphModelVo);
	    				}
	    			}
	        		//根据flowById 去拼接xml 
	        		String flowAndStopInfoToXml = FlowXmlUtils.flowAndStopInfoToXml(flowById,value);
	        		logger.info(flowAndStopInfoToXml);
	        		
	        		Template template = new Template();
			        template.setId(Utils.getUUID32());
			    	template.setCrtDttm(new Date());
					template.setCrtUser("wdd");
					template.setVersion(0L);
					template.setEnableFlag(true);
					template.setLastUpdateUser("-1");
					template.setLastUpdateDttm(new Date());
					template.setName(name);
					//数据库保存一份
					template.setValue(value);
					template.setFlow(flowById);
					//xml转文件并保存到指定目录
					String path = FileUtils.createXml(flowAndStopInfoToXml,name,".xml",SysParamsCache.XML_PATH);
					template.setPath(path);
					int addTemplate = iTemplateService.addTemplate(template);
					 if (addTemplate > 0) {
			                rtnMap.put("code", "1");
			                rtnMap.put("errMsg", "保存模板成功");
			              //保存stop，属性信息
							List<Stops> stopsList = flowById.getStopsList();
							if (null != stopsList && stopsList.size() > 0) {
								flowAndStopsTemplateVoServiceImpl.addStopsList(stopsList,template);
							}
					 } else {
					rtnMap.put("errMsg", "保存模板失败");
					logger.info("保存模板失败");
					}
					 return JsonUtils.toJsonNoException(rtnMap);
	        	} else {
				rtnMap.put("errMsg", "Flow信息为空");
				logger.info("Flow信息为空,loadId：" + loadId);
				return JsonUtils.toJsonNoException(rtnMap);
				}
	    }
	    
	    /**
	     * 根据id删除模板
	     * @param id
	     * @return
	     */
	    @RequestMapping("/deleteTemplate")
	    @ResponseBody
	    @Transactional
	    public int deleteTemplate(String id) {
	    	int deleteTemplate = 0;
			if(StringUtils.isNoneBlank(id)){
				Template template = iTemplateService.queryTemplate(id);
				if (null != template) {
					List<StopTemplateModel> stopsList = template.getStopsList();
					if (null != stopsList && stopsList.size() > 0) {
						for (StopTemplateModel stopTemplateVo : stopsList) {
							//先根据stopid删除stop属性
							flowAndStopsTemplateVoServiceImpl.deleteStopPropertyTemByStopId(stopTemplateVo.getId());
						}
						//先根据templateId删除stop
						flowAndStopsTemplateVoServiceImpl.deleteStopTemByTemplateId(template.getId());
					}
					//删除template
					deleteTemplate = iTemplateService.deleteTemplate(template.getId());
				}
			}
			return deleteTemplate;
	    }
	    
	    /**
	     * 上传xml文件并保存模板
	     * @param file
	     * @return
	     */
	    @RequestMapping(value = "/upload", method = RequestMethod.POST)
	    @ResponseBody
	    public String upload(@RequestParam("templateFile") MultipartFile file) {
	       Map<String, Object> rtnMap = new HashMap<String, Object>();
	       rtnMap.put("code", "0");
	       if (!file.isEmpty()) {
	       String upload = FileUtils.upload(file, SysParamsCache.XML_PATH);
	       Map<String, Object> map = JSON.parseObject(upload);  
	       if (!map.isEmpty() && null != map) {
	    	    String code = (String) map.get("code");
	    	    if ("0".equals(code)) {
	    	    	rtnMap.put("errMsg", "上传文件失败");
	    	    	JsonUtils.toJsonNoException(rtnMap);
				}
	    	    String name = (String) map.get("fileName");
	    	    String path = (String) map.get("url");
	    	    Template template = new Template();
		        template.setId(Utils.getUUID32());
				template.setCrtDttm(new Date());
				template.setCrtUser("wdd");
				template.setVersion(0L);
				template.setEnableFlag(true);
				template.setLastUpdateUser("-1");
				template.setLastUpdateDttm(new Date());
				template.setName(name.substring(0, name.length()-4));
				template.setPath(path);
				//根据保存好的文件路径读取xml文件并返回xml字符串
				String xmlFileToStr = FileUtils.XmlFileToStr(template.getPath());
				if (StringUtils.isBlank(xmlFileToStr)) {
					  logger.info("xml文件读取失败,上传模板失败");
	    			  rtnMap.put("errMsg", "xml文件读取失败,请稍微再试");
	    			  return JsonUtils.toJsonNoException(rtnMap);
				}
				List<StopTemplateModel> stopsList = null;
				//xml转换Template对象,包含stops和属性
				Template xmlToFlowStopInfo = FlowXmlUtils.xmlToFlowStopInfo(xmlFileToStr);
				 if (null != xmlToFlowStopInfo) {
					 stopsList = xmlToFlowStopInfo.getStopsList();
				 }
				//根据xml字符串获取mxGraphModel部分保存至value
				MxGraphModelVo xmlToMxGraphModelVo = FlowXmlUtils.allXmlToMxGraphModel(xmlFileToStr,0);
				if (null != xmlToMxGraphModelVo) {
					// 把查询出來的mxGraphModelVo转为XML
					String loadXml = FlowXmlUtils.mxGraphModelToXml(xmlToMxGraphModelVo);
					template.setValue(loadXml);
				}
				
				int addTemplate = iTemplateService.addTemplate(template);
				 if (addTemplate > 0) {
					//保存stop，属性信息
					 if (null != stopsList && stopsList.size() > 0) {
						 List<Stops> stop = FlowXmlUtils.stopTemplateVoToStop(stopsList);
						 if (null != stopsList && stopsList.size() > 0) {
								flowAndStopsTemplateVoServiceImpl.addStopsList(stop,template);
							}
					 	}
		                rtnMap.put("code", "1");
		                rtnMap.put("errMsg", "模板上传成功");
		                logger.info("模板上传成功");
		            } else {
		                rtnMap.put("errMsg", "模板上传失败");
		                logger.info("模板上传失败");
		            }
		            return JsonUtils.toJsonNoException(rtnMap);
	       		} 
	       } 
	    	   rtnMap.put("errMsg", "上传失败,请稍后再试");
	    	   return JsonUtils.toJsonNoException(rtnMap);
	   		}
	    
	  
	    /**
	     * 加载模板
	     * @param model
	     * @param xmlDate
	     * @return
	     */
	    @RequestMapping("/loadingXmlPage")
	    @ResponseBody
	    public String loadingXml(HttpServletRequest request, Model model) {
	    	Map<String, String> rtnMap = new HashMap<String, String>();
	        rtnMap.put("code", "0");
	        String templateId = request.getParameter("templateId");
	        String loadId = request.getParameter("load");
	    		Template template = iTemplateService.queryTemplate(templateId);
	    		if (null == template) {
	    			  logger.info("template为空,加载模板失败");
	    			  rtnMap.put("errMsg", "加载失败,请稍微再试");
	    			  return JsonUtils.toJsonNoException(rtnMap);
				}
	    		//根据保存好的文件路径读取xml文件并返回
				String xmlFileToStr = FileUtils.XmlFileToStr(template.getPath());
				if (StringUtils.isBlank(xmlFileToStr)) {
					  logger.info("xml文件读取失败,加载模板失败");
	    			  rtnMap.put("errMsg", "xml文件读取失败,请稍微再试");
	    			  return JsonUtils.toJsonNoException(rtnMap);
				}
				// 获取stop 中 pageId最大值
				String maxStopPageId = iFlowServiceImpl.getMaxStopPageId(loadId);
				maxStopPageId = StringUtils.isNotBlank(maxStopPageId) ? maxStopPageId : "0";
				int maxPageId = Integer.parseInt(maxStopPageId);
				StatefulRtnBase addFlow = null;
	            MxGraphModelVo xmlToMxGraphModel = FlowXmlUtils.allXmlToMxGraphModel(xmlFileToStr,maxPageId);
	            if (null != xmlToMxGraphModel) {
	            	xmlToMxGraphModel.getRootVo();
	            	addFlow = iFlowServiceImpl.saveOrUpdateFlowAll(xmlToMxGraphModel, loadId, "ADD",false);
				}
	            // addFlow不为空且ReqRtnStatus的值为true,则保存成功
	            if (null != addFlow && addFlow.isReqRtnStatus()) {
	            	if (null != template) {
						//保存stops和属性信息
						flowAndStopsTemplateVoServiceImpl.addTemplateStopsToFlow(template,loadId,maxPageId);
					}
	                logger.info("加载模板成功");
	                return "grapheditor/index";
	            } else {
	                logger.info("加载模板失败");
	                return "errorPage";
	            }
	    }
	    
	    /**
	     * 查询所有的模板下拉显示
	     * @param modelAndView
	     * @return
	     */
	    @RequestMapping("/templateAllSelect")
	    @ResponseBody
    public String template() {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", "0");
        List<Template> findTemPlateList = iTemplateService.findTemPlateList();
        if (null != findTemPlateList && findTemPlateList.size() > 0) {
            rtnMap.put("code", "1");
            rtnMap.put("temPlateList", findTemPlateList);
        } else {
            rtnMap.put("errMsg", "查询结果为空");
        }
        return JsonUtils.toJsonNoException(rtnMap);
		}
	    
	    /**
	     * 模板下载
	     * @param response
	     * @param templateId
	     * @throws Exception
	     */
	    @RequestMapping("/templateDownload")
	    public void downloadLocal(HttpServletResponse response,String templateId) throws Exception {
	    	Template template = iTemplateService.queryTemplate(templateId);
    		if (null == template) {
    			  logger.info("template为空,下载模板失败");
			}
    		// 下载本地文件
            String fileName = template.getName()+".xml".toString(); // 文件的默认保存名
            // 读到流中
            InputStream inStream = new FileInputStream(template.getPath());// 文件的存放路径
            // 设置输出的格式
            response.reset();
            response.setContentType("bin");
            response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            // 循环取出流中的数据
            byte[] b = new byte[100];
            int len;
            try {
                while ((len = inStream.read(b)) > 0)
                    response.getOutputStream().write(b, 0, len);
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
	    }
}
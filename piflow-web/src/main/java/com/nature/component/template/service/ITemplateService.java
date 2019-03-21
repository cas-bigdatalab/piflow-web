package com.nature.component.template.service;

import com.nature.component.flow.model.Template;
import org.springframework.data.annotation.Transient;

import java.util.List;



public interface ITemplateService {
	 
	/**
	 * 新增Template
	 * @param template
	 * @return
	 */
	@Transient
	public int addTemplate(Template template);
	
	/**
	 * 查询所有模板列表
	 * @return
	 */
	public List<Template> findTemPlateList();
	
	/**
	 * 根据id删除模板
	 * @param id
	 * @return
	 */
	public int deleteTemplate(String id);
	
	/**
	 * 根据id查询模板
	 * @param id
	 * @return
	 */
	public Template queryTemplate(String id);

	/**
	 * 查询所有模板列表分页
	 *
	 * @param offset 页数
	 * @param limit 每页条数
	 * @param param 搜索关键字
	 * @return
	 */
	public String getTemplateListPage(Integer offset, Integer limit, String param);
}

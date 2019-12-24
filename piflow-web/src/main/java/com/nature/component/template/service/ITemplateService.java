package com.nature.component.template.service;

import com.nature.component.template.model.Template;

import javax.transaction.Transactional;
import java.util.List;


public interface ITemplateService {

    /**
     * add Template
     *
     * @param template
     * @return
     */
    @Transactional
    public int addTemplate(Template template);

    /**
     * Query the list of all templates
     *
     * @return
     */
    public List<Template> findTemPlateList();

    /**
     * Delete the template based on id
     *
     * @param id
     * @return
     */
    public int deleteTemplate(String id);

    /**
     * Query the template by id
     *
     * @param id
     * @return
     */
    public Template queryTemplate(String id);

    /**
     * Query all template list pagination
     *
     * @param offset Number of pages
     * @param limit  Number of pages per page
     * @param param  search for the keyword
     * @return
     */
    public String getTemplateListPage(Integer offset, Integer limit, String param);
}

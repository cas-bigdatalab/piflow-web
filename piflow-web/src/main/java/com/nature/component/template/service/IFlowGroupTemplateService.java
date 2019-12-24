package com.nature.component.template.service;

import com.nature.component.template.model.Template;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.util.List;


public interface IFlowGroupTemplateService {

    /**
     * add FlowGroupTemplate
     *
     * @param name
     * @param loadId
     * @param value
     * @return
     */
    public String addFlowGroupTemplate(String name, String loadId, String value);

    /**
     * Query all flowGroupTemplate list pagination
     *
     * @param offset Number of pages
     * @param limit  Number of pages per page
     * @param param  search for the keyword
     * @return
     */
    public String getFlowGroupTemplateListPage(Integer offset, Integer limit, String param);

    /**
     * Delete the template based on id
     *
     * @param id
     * @return
     */
    public int deleteFlowGroupTemplate(String id);

    /**
     * Download template
     *
     * @param flowGroupTemplateId
     */
    public void templateDownload(HttpServletResponse response, String flowGroupTemplateId);

    /**
     * Query all templates for drop-down displays
     *
     * @return
     */
    public String flowGroupTemplateAllSelect();

    /**
     * Upload xml file and save flowGroupTemplate
     *
     * @param file
     * @return
     */
    public String uploadXmlFile(MultipartFile file);

    public String loadFlowGroupTemplate(String templateId, String loadId);
}

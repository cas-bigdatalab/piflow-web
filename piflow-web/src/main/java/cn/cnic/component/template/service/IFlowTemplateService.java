package cn.cnic.component.template.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


public interface IFlowTemplateService {


    /**
     * add FlowTemplate
     *
     * @param name
     * @param loadId
     * @param templateType
     * @return
     */
    public String addFlowTemplate(String name, String loadId, String templateType);

    /**
     * Query all FlowTemplate list pagination
     *
     * @param offset Number of pages
     * @param limit  Number of pages per page
     * @param param  search for the keyword
     * @return
     */
    public String getFlowTemplateListPage(Integer offset, Integer limit, String param);

    /**
     * Delete the template based on id
     *
     * @param id
     * @return
     */
    public int deleteFlowTemplate(String id);

    /**
     * Download template
     *
     * @param flowTemplateId
     */
    public void templateDownload(HttpServletResponse response, String flowTemplateId);

    /**
     * Query all templates for drop-down displays
     *
     * @return
     */
    public String flowTemplateList();

    /**
     * Upload xml file and save flowTemplate
     *
     * @param file
     * @return
     */
    public String uploadXmlFile(MultipartFile file);

    public String loadGroupTemplate(String templateId, String loadId);

    public String loadTaskTemplate(String templateId, String flowId);

}

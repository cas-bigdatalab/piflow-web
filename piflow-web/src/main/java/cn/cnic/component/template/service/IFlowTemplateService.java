package cn.cnic.component.template.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


public interface IFlowTemplateService {


    /**
     * add FlowTemplate
     *
     * @param username
     * @param name
     * @param loadId
     * @param templateType
     * @return
     */
    public String addFlowTemplate(String username, String name, String loadId, String templateType);

    /**
     * Query all FlowTemplate list pagination
     *
     * @param username Username
     * @param isAdmin  Is Admin
     * @param offset   Number of pages
     * @param limit    Number of pages per page
     * @param param    search for the keyword
     * @return
     */
    public String getFlowTemplateListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param);

    /**
     * Delete the template based on id
     *
     * @param id
     * @return
     */
    public String deleteFlowTemplate(String id);

    /**
     * Download template
     *
     * @param flowTemplateId
     */
    public void templateDownload(HttpServletResponse response, String flowTemplateId);

    /**
     * Query all templates for drop-down displays
     *
     * @param username
     * @param isAdmin
     * @return
     */
    public String flowTemplateList(String username, boolean isAdmin);

    /**
     * Upload xml file and save flowTemplate
     *
     * @param username
     * @param file
     * @return
     */
    public String uploadXmlFile(String username, MultipartFile file);

    public String loadGroupTemplate(String username, String templateId, String loadId);

    public String loadTaskTemplate(String username, String templateId, String flowId);

}

package cn.cnic.component.process.service;

import javax.servlet.http.HttpServletResponse;

public interface IProcessStopService {

    /**
     * Query processStop based on processId and pageId
     *
     * @param processId
     * @param pageId
     * @return
     */
    public String getProcessStopVoByPageId(String processId, String pageId);

    /**
     * showViewData
     *
     * @param id       id
     * @return json
     */
    public void showViewData(HttpServletResponse response, String id) throws Exception;
}

package cn.cnic.component.process.service;

public interface IProcessStopService {

    /**
     * Query processStop based on processId and pageId
     *
     * @param processId
     * @param pageId
     * @return
     */
    public String getProcessStopVoByPageId(String processId, String pageId);
}

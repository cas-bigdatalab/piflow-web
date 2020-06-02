package cn.cnic.component.flow.service;

public interface IFlowGroupPathsService {

    /**
     * Query connection information according to flowGroupId and pageid
     *
     * @param flowGroupId
     * @param pageId
     * @return
     */
    public String queryPathInfoFlowGroup(String flowGroupId, String pageId);

}

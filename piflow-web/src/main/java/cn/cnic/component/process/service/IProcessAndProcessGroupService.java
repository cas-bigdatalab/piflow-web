package cn.cnic.component.process.service;


public interface IProcessAndProcessGroupService {

    /**
     * Query ProcessAndProcessGroupVoList (parameter space-time non-paging)
     *
     * @param offset Number of pages
     * @param limit  Number each page
     * @param name  Search content
     * @return json
     */
    public String getProcessAndProcessGroupListPage(String username, boolean isAdmin, Integer offset, Integer limit, String name, Integer triggerMode,
                                                    String state, String ctrDttm, String scheduleName);

    /**
     * getAppInfoList
     *
     * @param taskAppIds task appId array
     * @param groupAppIds group appId array
     * @return json
     */
    public String getAppInfoList(String[] taskAppIds,String[] groupAppIds);

}

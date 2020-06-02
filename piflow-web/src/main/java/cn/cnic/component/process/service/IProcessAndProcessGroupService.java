package cn.cnic.component.process.service;


public interface IProcessAndProcessGroupService {

    /**
     * Query ProcessAndProcessGroupVoList (parameter space-time non-paging)
     *
     * @param offset Number of pages
     * @param limit  Number each page
     * @param param  Search content
     * @return json
     */
    public String getProcessAndProcessGroupListPage(Integer offset, Integer limit, String param);

}

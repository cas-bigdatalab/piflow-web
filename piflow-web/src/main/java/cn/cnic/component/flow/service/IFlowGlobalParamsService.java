package cn.cnic.component.flow.service;

import cn.cnic.controller.requestVo.FlowGlobalParamsVoRequest;
import cn.cnic.controller.requestVo.FlowGlobalParamsVoRequestAdd;

public interface IFlowGlobalParamsService {

    /**
     * add flow(Contains drawing board information)
     * 
     * @param username
     * @param flowVo
     * @return
     * @throws Exception
     */
    public String addFlowGlobalParams(String username, FlowGlobalParamsVoRequestAdd globalParamsVo) throws Exception ;

    public String updateFlowGlobalParams(String username, boolean isAdmin, FlowGlobalParamsVoRequest globalParamsVo) throws Exception;
	
    public String deleteFlowGlobalParamsById(String username, boolean isAdmin, String id);

    /**
     * Paging query FlowGlobalParams
     *
     * @param username
     * @param isAdmin
     * @param offset   Number of pages
     * @param limit    Number of pages per page
     * @param param    search for the keyword
     * @return
     */
    public String getFlowGlobalParamsListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param);

    /**
     * Query FlowGlobalParams
     *
     * @param username
     * @param isAdmin
     * @param param    search for the keyword
     * @return
     */
    public String getFlowGlobalParamsList(String username, boolean isAdmin, String param);

    public String getFlowGlobalParamsById(String username, boolean isAdmin, String id);
}

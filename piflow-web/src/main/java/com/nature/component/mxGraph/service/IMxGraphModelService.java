package com.nature.component.mxGraph.service;


import com.nature.component.mxGraph.vo.MxGraphVo;

public interface IMxGraphModelService {

    public String saveDataForTask(String imageXML,String loadId,String operType);

    /**
     * save or add flowGroup
     *
     * @param imageXML
     * @param loadId
     * @param operType
     * @param flag
     * @return
     */
    public String saveDataForGroup(String imageXML, String loadId, String operType, boolean flag);

    /**
     * addMxCellAndData
     *
     * @param mxGraphVo
     * @param username
     * @return
     */
    public String addMxCellAndData(MxGraphVo mxGraphVo,String username);

}

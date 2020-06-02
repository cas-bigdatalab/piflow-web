package cn.cnic.component.flow.service;

import cn.cnic.component.flow.model.Property;
import cn.cnic.component.flow.request.UpdatePathRequest;
import cn.cnic.component.flow.vo.StopsVo;

import java.util.List;


public interface IPropertyService {

    /**
     * Querying group attribute information based on stopPageId
     *
     * @param stopPageId
     * @return
     */
    public StopsVo queryAll(String fid, String stopPageId);

    /**
     * Modify stops attribute information
     *
     * @param id
     * @param content
     * @return
     */
    public int updateProperty(String content, String id);

    /**
     * query All StopsProperty List;
     *
     * @return
     */
    public List<Property> getStopsPropertyList();

    /**
     * delete StopsProperty according to ID;
     *
     * @return
     */
    public int deleteStopsPropertyById(String id);

    /**
     * delete StopsProperty according to StopId;
     *
     * @return
     */
    public int deleteStopsPropertyByStopId(String id);


    /**
     * check stops template
     *
     * @param stopsId
     */
    public void checkStopTemplateUpdate(String stopsId);

    public String saveOrUpdateRoutePath(UpdatePathRequest updatePathRequest);

    /**
     * deleteLastReloadDataByStopsId
     *
     * @param stopId
     * @return
     */
    public String deleteLastReloadDataByStopsId(String stopId);

}

package cn.cnic.component.flow.service;

import java.util.List;

import cn.cnic.component.flow.entity.Property;
import cn.cnic.component.flow.request.UpdatePathRequest;


public interface IPropertyService {

    /**
     * Querying group attribute information based on stopPageId
     *
     * @param stopPageId
     * @return
     */
    public String queryAll(String fid, String stopPageId);

    /**
     * Modify stops attribute information
     *
     * @param content
     * @return
     */
    public String updatePropertyList(String username, String[] content);

    /**
     * Modify stops attribute information
     *
     * @param id
     * @param content
     * @return
     */
    public String updateProperty(String username, String content, String id);


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
     * check stops template
     *
     * @param username
     * @param stopsId
     */
    public void checkStopTemplateUpdate(String username, String stopsId);

    public String saveOrUpdateRoutePath(String username, UpdatePathRequest updatePathRequest);

    /**
     * deleteLastReloadDataByStopsId
     *
     * @param stopId
     * @return
     */
    public String deleteLastReloadDataByStopsId(String stopId);

}

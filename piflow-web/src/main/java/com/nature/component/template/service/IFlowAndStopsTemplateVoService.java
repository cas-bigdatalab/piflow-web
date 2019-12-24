package com.nature.component.template.service;

import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.Stops;
import com.nature.component.template.model.PropertyTemplateModel;
import com.nature.component.template.model.StopTemplateModel;
import com.nature.component.template.model.Template;
import com.nature.component.template.vo.FlowTemplateModelVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface IFlowAndStopsTemplateVoService {

    /**
     * Add a single stops
     *
     * @param stops
     * @return
     */
    public int addStops(StopTemplateModel stops);

    /**
     * add Flow
     *
     * @param flow
     * @return
     */
    public int addFlow(FlowTemplateModelVo flow);

    /**
     * Insert list<PropertyVo> Note that the method of spelling sql must use Map to connect Param content to key value.
     *
     * @param propertyList (Content: The key is propertyList and the value is List<PropertyVo>)
     * @return
     */
    public int addPropertyList(@Param("propertyList") List<PropertyTemplateModel> propertyList);

    public int deleteStopTemByTemplateId(@Param("id") String templateId);

    public int deleteStopPropertyTemByStopId(@Param("id") String stopId);

    /**
     * Query all stop information according to the template id
     *
     * @param templateId
     * @return
     */
    public List<StopTemplateModel> getStopsListByTemPlateId(@Param("templateId") String templateId);

    /**
     * Query all stop attribute information according to stopId
     *
     * @param stopsId
     * @return
     */
    public List<PropertyTemplateModel> getPropertyListByStopsId(String stopsId);

    /**
     * Load template to save stop information
     *
     * @param template
     * @param flow
     * @param maxPageId The maximum value in stop in flow
     */
    public void addTemplateStopsToFlow(Template template, Flow flow, int maxPageId);

    public void addStopsList(List<Stops> stops, Template template);
}

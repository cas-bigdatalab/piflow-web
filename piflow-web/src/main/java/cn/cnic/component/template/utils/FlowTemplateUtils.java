package cn.cnic.component.template.utils;

import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.common.Eunm.PortType;
import cn.cnic.common.Eunm.TemplateType;
import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.entity.Paths;
import cn.cnic.component.flow.entity.Property;
import cn.cnic.component.flow.entity.Stops;
import cn.cnic.component.flow.utils.FlowUtil;
import cn.cnic.component.flow.utils.PathsUtil;
import cn.cnic.component.flow.utils.PropertyUtils;
import cn.cnic.component.flow.utils.StopsUtils;
import cn.cnic.component.mxGraph.entity.MxCell;
import cn.cnic.component.mxGraph.entity.MxGraphModel;
import cn.cnic.component.mxGraph.utils.MxCellUtils;
import cn.cnic.component.mxGraph.utils.MxGraphModelUtils;
import cn.cnic.component.template.entity.FlowTemplate;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.xml.sax.InputSource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class FlowTemplateUtils {

    private static Logger logger = LoggerUtil.getLogger();
    
    public static FlowTemplate newFlowTemplateNoId(String username){
        FlowTemplate flowTemplate = new FlowTemplate();
        // basic properties (required when creating)
        flowTemplate.setCrtDttm(new Date());
        flowTemplate.setCrtUser(username);
        // basic properties
        flowTemplate.setEnableFlag(true);
        flowTemplate.setLastUpdateUser(username);
        flowTemplate.setLastUpdateDttm(new Date());
        flowTemplate.setVersion(0L);
        return flowTemplate;
    }
    
    public static TemplateType determineTemplateType(String xmlTemplateStr, boolean isEscape) {
        if (StringUtils.isBlank(xmlTemplateStr)) {
            return null;
        }
        try {
            String xmlStr = xmlTemplateStr;
            if (isEscape) {
                //xmlStr = StringEscapeUtils.unescapeHtml(xmlData);
            }
            Document document = DocumentHelper.parseText(xmlStr);
            String strXml = document.getRootElement().asXML();
            String transformation = "<fg>" + strXml + "</fg>";
            InputSource in = new InputSource(new StringReader(transformation));
            in.setEncoding("UTF-8");
            SAXReader reader = new SAXReader();
            document = reader.read(in);
            // Get all nodes with "autoSaveNode" attribute
            Element xmlStrToElement = document.getRootElement(); // Get the root node
            if (null == xmlStrToElement) {
                return null;
            }
            Element flowGroupRecordEle = xmlStrToElement.element("flowGroup");
            if (null != flowGroupRecordEle) {
                return TemplateType.GROUP;
            }
            Element flowRecordEle = xmlStrToElement.element("flow");
            if (null != flowRecordEle) {
                return TemplateType.TASK;
            }
        } catch (Throwable e) {
            JSONObject parse = JSON.parseObject(xmlTemplateStr);
            Object object = parse.get("a_galaxy_workflow");
            Object steps = parse.get("steps");
            if (null != object || null != steps) {
                return TemplateType.GALAX;
            }
        }
        return null;
    }

    /**
     * TemplateXml to Flow
     *
     * @param templateXml xml string data
     * @param username    Operator username
     * @return Flow
     */
    @SuppressWarnings("unchecked")
	public static Map<String, Object> galaxTemplateToFlow(String templateGalax, String username, String stopMaxPageId, String flowMaxPageId, String[] stopNames) {
        if (StringUtils.isBlank(templateGalax)) {
            return ReturnMapUtils.setFailedMsg("templateGalax is null");
        }
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsg("username is null");
        }
        if (StringUtils.isBlank(stopMaxPageId)) {
            stopMaxPageId = "1";
        }
        try {
            Integer flowMaxPageIdInt = null;
            int stopMaxPageIdInt = Integer.parseInt(stopMaxPageId);
            if (StringUtils.isNotBlank(flowMaxPageId)) {
                flowMaxPageIdInt = Integer.parseInt(flowMaxPageId);
            }
            JSONObject galaxJSONObject = JSON.parseObject(templateGalax);
            if (null == galaxJSONObject) {
                return ReturnMapUtils.setFailedMsg("No flow node");
            }
            String name = galaxJSONObject.getString("name");
            String description = galaxJSONObject.getString("name");
            String flowPageId = "1";
            if (StringUtils.isNotBlank(flowPageId) && null != flowMaxPageIdInt) {
                flowPageId = (Integer.parseInt(flowPageId) + flowMaxPageIdInt) + "";
            }
            Flow flow = FlowUtil.setFlowBasicInformation(null, false, username);
            flow.setPageId(flowPageId);
            flow.setDriverMemory("");
            flow.setExecutorCores("");
            flow.setExecutorMemory("");
            flow.setExecutorNumber("");
            flow.setName(name + System.currentTimeMillis());
            flow.setDescription(description);

            // stop
            JSONObject stopsJSONObject = galaxJSONObject.getJSONObject("steps");
            Map<String, Object> xmlToStopsMap = jsonObjectToStopsList(stopsJSONObject, stopMaxPageIdInt, username, stopNames, flow);
            if (200 != (Integer) xmlToStopsMap.get(ReturnMapUtils.KEY_CODE)) {
                return xmlToStopsMap;
            }
            flow.setStopsList((List<Stops>) xmlToStopsMap.get("jsonToStopsList"));
            flow.setPathsList((List<Paths>) xmlToStopsMap.get("jsonToPathsList"));

            MxGraphModel mxGraphModel = jsonToMxGraphModel(galaxJSONObject, stopMaxPageIdInt, username, flow);
            flow.setMxGraphModel(mxGraphModel);
            return ReturnMapUtils.setSucceededCustomParam("flow", flow);
        } catch (Exception e) {
            logger.error("Conversion failed", e);
            return ReturnMapUtils.setFailedMsg("Conversion failed");
        }
    }
   /**
    * String type json String to MxGraphModel
    *
    * @param jsonData json string data
    * @return MxGraphModel
    */
   public static MxGraphModel jsonStringToMxGraphModel(String jsonData, int maxPageId, String username, Flow flow) {
	   if (StringUtils.isBlank(jsonData)) {
           return null;
       }
       try {
           JSONObject galaxJSONObject = JSON.parseObject(jsonData);
           if (null == galaxJSONObject) {
               return null;
           }
           return jsonToMxGraphModel(galaxJSONObject, maxPageId, username, flow);
       } catch (Exception e) {
           logger.error("Conversion failed", e);
           return null;
       }
	   
   }
    
    /**
     * String type json Object to MxGraphModel
     *
     * @param xmlData xml string data
     * @return MxGraphModel
     */
    public static MxGraphModel jsonToMxGraphModel(JSONObject jsonData, int maxPageId, String username, Flow flow) {
    	if (null == jsonData || null == flow) {
            return null;
        }
        try {            
            MxGraphModel mxGraphModel = MxGraphModelUtils.initMxGraphModelBasicPropertiesNoId(username, false);
            //Take out all Stop(Flow) Name and PageId
            Map<String, String> stopOrFlowNamesMap = new HashMap<>();
            List<Stops> stopsList = flow.getStopsList();
        	if (null != stopsList) {
                //Loop take out all Stop Name and PageId
                for (Stops stops : stopsList) {
                    if (null == stops) {
                    	continue;
                    }
                    stopOrFlowNamesMap.put(stops.getPageId(), stops.getName());
                    stopOrFlowNamesMap.put(stops.getPageId() + "bundle", stops.getBundel());
                }
        	}
            List<MxCell> rootList = new ArrayList<>();
            JSONObject mxCellJSONObjectArray = jsonData.getJSONObject("steps");
            for (String key : mxCellJSONObjectArray.keySet()) {
            	JSONObject mxCellJsonObject = mxCellJSONObjectArray.getJSONObject(key);
                MxCell mxCell = jsonToMxCellNode(mxCellJsonObject, maxPageId, username, mxGraphModel);
                if (null != mxCell) {
                  String mxCellValue = stopOrFlowNamesMap.get(mxCell.getPageId());
                  // Canvas composition name synchronized with Stop
                  if (StringUtils.isNotBlank(mxCellValue)) {
                      mxCell.setValue(mxCellValue);
                  }
                  String mxCellBundle = stopOrFlowNamesMap.get(mxCell.getPageId() + "bundle");
                  if (StringUtils.isNotBlank(mxCellBundle)) {
                	  String mxCellName = mxCellBundle.replace("cn.piflow.bundle.galax.", "");
                	  mxCell.setStyle("image;html=1;labelBackgroundColor=#ffffff00;image=/piflow-web/images/" + mxCellName + "_128x128.png");
                  }
                  rootList.add(mxCell);
                }
            }
            List<Paths> pathsList = flow.getPathsList();
        	if (null != pathsList) {
                //Loop take out all Stop Name and PageId
                for (Paths paths : pathsList) {
                    if (null == paths) {
                        continue;
                    }
                    MxCell mxCell = MxCellUtils.AddMxCellLine(username, paths.getPageId(), paths.getFrom(), paths.getTo());
                    if (null != mxCell) {
                    	rootList.add(mxCell);
                    }
                }
        	}
            mxGraphModel.setRoot(rootList);
            mxGraphModel.setFlow(flow);
            return mxGraphModel;
        } catch (Exception e) {
            logger.error("Conversion failed", e);
            return null;
        }
    }

    /**
     * json to MxCell
     *
     * @param xmlData      json string data
     * @param maxPageId    Maximum PageId of MxCell
     * @param username     Operator username
     * @param mxGraphModel link MxGraphModel
     * @return MxCell
     */
    public static MxCell jsonStringToMxCellNode(String jsonData, int maxPageId, String username, MxGraphModel mxGraphModel) {
    	if (StringUtils.isBlank(jsonData)) {
            return null;
        }
        try {
            JSONObject jSONObject = JSON.parseObject(jsonData);
            if (null == jSONObject) {
                return null;
            }
            return jsonToMxCellNode(jSONObject, maxPageId, username, mxGraphModel);
        } catch (Exception e) {
            logger.error("Conversion failed", e);
            return null;
        }
    }

    /**
     * json to MxCell
     *
     * @param xmlData      json object data
     * @param maxPageId    Maximum PageId of MxCell
     * @param username     Operator username
     * @param mxGraphModel link MxGraphModel
     * @return MxCell
     */
    public static MxCell jsonToMxCellNode(JSONObject jsonData, int maxPageId, String username, MxGraphModel mxGraphModel) {
    	if (null == jsonData) {
            return null;
        }
        try {
            
            int pageId = jsonData.getInteger("id");
            JSONObject position = jsonData.getJSONObject("position");
            String mx_x = position.getString("left");
            String mx_y = position.getString("top");
            String pageIdStr = (pageId + maxPageId) + "";
            MxCell mxCell = MxCellUtils.AddMxCellNode(username, pageIdStr, null, null, mx_x, mx_y);
            mxCell.setMxGraphModel(mxGraphModel);
            return mxCell;
        } catch (Exception e) {
            logger.error("Conversion failed", e);
            return null;
        }
    }


    /**
     * String type jsonObject to "stop"
     *
     * @param stopsJsonData JsonObject string data
     * @return Stops
     */
    public static Stops jsonObjectToStops(String stopsJsonData, int maxPageId, String username) {
        if (StringUtils.isBlank(stopsJsonData)) {
            return null;
        }
        try {
            JSONObject galaxStepsJSONObject = JSON.parseObject(stopsJsonData);
            if (null == galaxStepsJSONObject) {
                return null;
            }
            return jsonObjectToStops(galaxStepsJSONObject, maxPageId, username);
        } catch (Exception e) {
            logger.error("Conversion failed", e);
            return null;
        }
    }

    
    /**
     * String type jsonObject to "stop"
     *
     * @param galaxStepsJSONObject JsonObject string data
     * @return Stops
     */
    public static Stops jsonObjectToStops(JSONObject galaxStepsJSONObject, int maxPageId, String username) {
        if (null == galaxStepsJSONObject) {
            return null;
        }
        try {
            String name = galaxStepsJSONObject.getString("name");
            if (StringUtils.isNotBlank(name)) {
                name = name.replaceAll(" ", "_");
            }
            String bundel = ("cn.piflow.bundle.galax." + name);
            String description = name;
            //String id = galaxStepsJSONObject.getString("id");
            String pageId = galaxStepsJSONObject.getString("id");
            String inports = "";
            String inPortType = "";
            JSONArray inputs = galaxStepsJSONObject.getJSONArray("inputs");
            if (inputs.size() > 0) {
                inPortType = "UserDefault";
                for (Object object : inputs) {
                    if (null == object) {
                        continue;
                    }
                    if (StringUtils.isNotBlank(inports)) {
                        inports += ",";
                    }
                    inports += ((JSONObject)object).getString("name");
                    inports += ((JSONObject)object).getString("type");
                }
            }
            String outPortType = "";
            String outports = "";
            JSONArray outputs = galaxStepsJSONObject.getJSONArray("outputs");
            if (outputs.size() > 0) {
                outPortType = "UserDefault";
                for (Object object : outputs) {
                    if (null == object) {
                        continue;
                    }
                    if (StringUtils.isNotBlank(outports)) {
                        outports += ",";
                    }
                    outports += ((JSONObject)object).getString("name");
                    outports += ((JSONObject)object).getString("type");
                }
            }
            String owner = "Galaxy";
            String groups = "Galaxy";
            Stops stops = StopsUtils.stopsNewNoId(username);            
            stops.setPageId((Integer.parseInt(pageId) + maxPageId) + "");
            stops.setName(name + stops.getPageId());
            stops.setDescription(description);
            stops.setBundel(bundel);
            //stops.setId(id);
            stops.setInports(inports);
            stops.setOutports(outports);
            stops.setOutPortType(PortType.selectGender(outPortType));
            stops.setInPortType(PortType.selectGenderByValue(inPortType));
            stops.setGroups(groups);
            stops.setIsCheckpoint(false);
            stops.setIsCustomized(false);
            stops.setOwner(owner);
            List<Property> propertyList = new ArrayList<>();            
            Property inputProperty = PropertyUtils.propertyNewNoId(username);
            inputProperty.setAllowableValues("[]");
            inputProperty.setCustomValue("");
            inputProperty.setDescription("input");
            inputProperty.setDisplayName("input");
            inputProperty.setName("input");
            inputProperty.setRequired(false);
            inputProperty.setSensitive(false);
            inputProperty.setIsSelect(false);
            inputProperty.setStops(stops);
            Property outputProperty = PropertyUtils.propertyNewNoId(username);
            outputProperty.setAllowableValues("[]");
            outputProperty.setCustomValue("");
            outputProperty.setDescription("output");
            outputProperty.setDisplayName("output");
            outputProperty.setName("output");
            outputProperty.setRequired(false);
            outputProperty.setSensitive(false);
            outputProperty.setIsSelect(false);
            outputProperty.setStops(stops);
            propertyList.add(outputProperty);
            stops.setProperties(propertyList);
            return stops;
        } catch (Exception e) {
            logger.error("Conversion failed", e);
            return null;
        }
    }

    
    public static Map<String, Object> jsonObjectToStopsList(JSONObject stopsJSONObjectArray, int stopMaxPageIdInt, String username, String[] stopNames, Flow flow) {
        if (null == stopsJSONObjectArray) {
            return ReturnMapUtils.setFailedMsg("template is null");
        }
        
        List<Stops> stopsList = new ArrayList<>();
        List<Paths> pathsList = new ArrayList<>();
        String duplicateStopName = null;
        int stopsSize = stopsJSONObjectArray.size();
        int path_page_no = stopsSize + 5 + stopMaxPageIdInt;
        for (String key : stopsJSONObjectArray.keySet()) {
            JSONObject stopsJsonObject = stopsJSONObjectArray.getJSONObject(key);
            Stops stops = jsonObjectToStops(stopsJsonObject, stopMaxPageIdInt, username);
            if (null == stops) {
                continue;
            }
            String stopName = stops.getName();

            if (Arrays.asList(stopNames).contains(stopName)) {
                duplicateStopName += (stopName + ",");
            }
            stops.setFlow(flow);
            stopsList.add(stops);
            
            // generate path
            JSONObject input_connections = stopsJsonObject.getJSONObject("input_connections");
            if (input_connections.size() < 0) {
                continue;
            }
            JSONArray inputs = stopsJsonObject.getJSONArray("inputs");
            if (inputs.size() > 0) {
                for (Object input_j : inputs) {

                    String name = ((JSONObject)input_j).getString("name");
                    //
                    Object input_connections_j = input_connections.get(name);
                    if (null == input_connections_j) {
                        input_connections_j = input_connections.get(name + "|input");
                    }
                    if (null == input_connections_j) {
                        continue;
                    }
                    
                    //暂时跳过------------------------------------------------
                    //暂时跳过------------------------------------------------
                    //暂时跳过------------------------------------------------
                    //暂时跳过------------------------------------------------
                    //暂时跳过------------------------------------------------
                    if (input_connections_j instanceof JSONArray) {
                        continue;
                    }
                    //暂时跳过------------------------------------------------
                    //暂时跳过------------------------------------------------
                    //暂时跳过------------------------------------------------
                    //暂时跳过------------------------------------------------
                    //暂时跳过------------------------------------------------
                    
                    
                    int fromPageId = ((JSONObject)input_connections_j).getInteger("id");
                    String input_name = ((JSONObject)input_connections_j).getString("output_name");
                    Paths paths = PathsUtil.newPathsNoId(username);
                    paths.setFrom((fromPageId + stopMaxPageIdInt) + "");
                    paths.setInport(input_name);
                    paths.setTo(stops.getPageId());
                    paths.setOutport(name);
                    paths.setPageId(path_page_no + "");
                    paths.setFlow(flow);
                    pathsList.add(paths);
                    path_page_no ++;
                }
                
            }
        }
        
        // If there are duplicate StopNames, directly return null
        if (StringUtils.isNotBlank(duplicateStopName)) {
            return ReturnMapUtils.setFailedMsg("Duplicate StopName");
        }
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededCustomParam("jsonToStopsList", stopsList);
        return ReturnMapUtils.appendValues(rtnMap, "jsonToPathsList", pathsList);
    }

    
    /**
     * xmlStrToElement
     *
     * @param xmlData  xml string data
     * @param isEscape is escape
     * @return Element
     */
    public static Element xmlStrToElement(String xmlData, boolean isEscape) {
        try {
            String xmlStr = xmlData;
            if (isEscape) {
                logger.debug("test");
                //xmlStr = StringEscapeUtils.unescapeHtml(xmlData);
            }
            Document document = DocumentHelper.parseText(xmlStr);
            String strXml = document.getRootElement().asXML();
            String transformation = "<fg>" + strXml + "</fg>";
            InputSource in = new InputSource(new StringReader(transformation));
            in.setEncoding("UTF-8");
            SAXReader reader = new SAXReader();
            document = reader.read(in);
            // Get all nodes with "autoSaveNode" attribute
            return document.getRootElement(); // Get the root node
        } catch (DocumentException e) {
            logger.error("Conversion failed", e);
            return null;
        }
    }
}

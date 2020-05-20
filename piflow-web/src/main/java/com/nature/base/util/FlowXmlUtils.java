package com.nature.base.util;

import com.nature.common.Eunm.PortType;
import com.nature.component.flow.model.*;
import com.nature.component.flow.utils.FlowUtil;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGeometry;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.mxGraph.utils.MxCellUtils;
import com.nature.component.mxGraph.vo.MxCellVo;
import com.nature.component.mxGraph.vo.MxGeometryVo;
import com.nature.component.mxGraph.vo.MxGraphModelVo;
import com.nature.component.template.model.PropertyTemplateModel;
import com.nature.component.template.model.StopTemplateModel;
import com.nature.component.template.model.FlowTemplate;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.*;

@SuppressWarnings("rawtypes")
public class FlowXmlUtils {

    static Logger logger = LoggerUtil.getLogger();

    private static String spliceStr(String key, Object value) {
        return key + "=\"" + value + "\" ";
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

    /**
     * xmlStrToElement
     *
     * @param xmlData  xml string data
     * @param isEscape is escape
     * @param key      The key of the data to be fetched
     * @return Element
     */
    private static Element xmlStrToElementGetByKey(String xmlData, boolean isEscape, String key) {
        if (StringUtils.isBlank(key)) {
            return xmlStrToElement(xmlData, isEscape);
        }
        Element element = xmlStrToElement(xmlData, isEscape);
        if (null == element) {
            return null;
        }
        return element.element(key);
    }

    /**
     * mxGraphModel to mxGraphModelVo
     *
     * @param mxGraphModel mxGraphModel
     * @return MxGraphModelVo
     */
    public static MxGraphModelVo mxGraphModelPoToVo(MxGraphModel mxGraphModel) {
        MxGraphModelVo mxGraphModelVo = null;
        // Judge empty "mxGraphModel"
        if (null != mxGraphModel) {
            mxGraphModelVo = new MxGraphModelVo();
            //Copy the contents of "mxGraphModel" into "mxGraphModelVo"
            BeanUtils.copyProperties(mxGraphModel, mxGraphModelVo);
            // Take out "mxCellList"
            List<MxCell> root = mxGraphModel.getRoot();
            // Judge empty
            if (null != root && root.size() > 0) {
                List<MxCellVo> mxCellVoList = new ArrayList<>();
                // Loop copy
                for (MxCell mxCell : root) {
                    if (null != mxCell) {
                        MxCellVo mxCellVo = new MxCellVo();
                        // Copy the contents of "mxGraphModel" to "mxGraphModelVo"
                        BeanUtils.copyProperties(mxCell, mxCellVo);
                        MxGeometry mxGeometry = mxCell.getMxGeometry();
                        if (null != mxGeometry) {
                            MxGeometryVo mxGeometryVo = new MxGeometryVo();
                            // Copy the contents of "mxGeometry" to "mxGeometryVo"
                            BeanUtils.copyProperties(mxGeometry, mxGeometryVo);
                            mxCellVo.setMxGeometryVo(mxGeometryVo);
                        }
                        mxCellVoList.add(mxCellVo);
                    }
                }
                mxGraphModelVo.setRootVo(mxCellVoList);
            }

        }
        return mxGraphModelVo;
    }

    /**
     * "MxGraphModel" to string "xml"
     *
     * @param mxGraphModelVo mxGraphModelVo
     * @return String
     */
    public static String mxGraphModelToXml(MxGraphModelVo mxGraphModelVo) {
        //Fight 'xml' note must write spaces
        if (null != mxGraphModelVo) {
            StringBuffer xmlStrSb = new StringBuffer();
            String dx = StringCustomUtils.replaceSpecialSymbolsXml(mxGraphModelVo.getDx());
            String dy = StringCustomUtils.replaceSpecialSymbolsXml(mxGraphModelVo.getDy());
            String grid = StringCustomUtils.replaceSpecialSymbolsXml(mxGraphModelVo.getGrid());
            String gridSize = StringCustomUtils.replaceSpecialSymbolsXml(mxGraphModelVo.getGridSize());
            String guides = StringCustomUtils.replaceSpecialSymbolsXml(mxGraphModelVo.getGuides());
            String tooltips = StringCustomUtils.replaceSpecialSymbolsXml(mxGraphModelVo.getTooltips());
            String connect = StringCustomUtils.replaceSpecialSymbolsXml(mxGraphModelVo.getConnect());
            String arrows = StringCustomUtils.replaceSpecialSymbolsXml(mxGraphModelVo.getArrows());
            String fold = StringCustomUtils.replaceSpecialSymbolsXml(mxGraphModelVo.getFold());
            String page = StringCustomUtils.replaceSpecialSymbolsXml(mxGraphModelVo.getPage());
            String pageScale = StringCustomUtils.replaceSpecialSymbolsXml(mxGraphModelVo.getPageScale());
            String pageWidth = StringCustomUtils.replaceSpecialSymbolsXml(mxGraphModelVo.getPageWidth());
            String pageHeight = StringCustomUtils.replaceSpecialSymbolsXml(mxGraphModelVo.getPageHeight());
            String background = StringCustomUtils.replaceSpecialSymbolsXml(mxGraphModelVo.getBackground());
            xmlStrSb.append("<mxGraphModel ");
            if (StringUtils.isNotBlank(dx)) {
                xmlStrSb.append(spliceStr("dx", dx));
            }
            if (StringUtils.isNotBlank(dy)) {
                xmlStrSb.append(spliceStr("dy", dy));
            }
            if (StringUtils.isNotBlank(grid)) {
                xmlStrSb.append(spliceStr("grid", grid));
            }
            if (StringUtils.isNotBlank(gridSize)) {
                xmlStrSb.append(spliceStr("gridSize", gridSize));
            }
            if (StringUtils.isNotBlank(guides)) {
                xmlStrSb.append(spliceStr("guides", guides));
            }
            if (StringUtils.isNotBlank(tooltips)) {
                xmlStrSb.append(spliceStr("tooltips", tooltips));
            }
            if (StringUtils.isNotBlank(connect)) {
                xmlStrSb.append(spliceStr("connect", connect));
            }
            if (StringUtils.isNotBlank(arrows)) {
                xmlStrSb.append(spliceStr("arrows", arrows));
            }
            if (StringUtils.isNotBlank(fold)) {
                xmlStrSb.append(spliceStr("fold", fold));
            }
            if (StringUtils.isNotBlank(page)) {
                xmlStrSb.append(spliceStr("page", page));
            }
            if (StringUtils.isNotBlank(pageScale)) {
                xmlStrSb.append(spliceStr("pageScale", pageScale));
            }
            if (StringUtils.isNotBlank(pageWidth)) {
                xmlStrSb.append(spliceStr("pageWidth", pageWidth));
            }
            if (StringUtils.isNotBlank(pageHeight)) {
                xmlStrSb.append(spliceStr("pageHeight", pageHeight));
            }
            if (StringUtils.isNotBlank(background)) {
                xmlStrSb.append(spliceStr("background", background));
            }
            xmlStrSb.append("><root>");
            List<MxCellVo> rootVoList = mxGraphModelVo.getRootVo();
            if (null != rootVoList && rootVoList.size() > 0) {
                for (MxCellVo mxCellVo : rootVoList) {
                    String id = StringCustomUtils.replaceSpecialSymbolsXml(mxCellVo.getPageId());
                    String parent = StringCustomUtils.replaceSpecialSymbolsXml(mxCellVo.getParent());
                    String style = StringCustomUtils.replaceSpecialSymbolsXml(mxCellVo.getStyle());
                    String value = StringCustomUtils.replaceSpecialSymbolsXml(mxCellVo.getValue());
                    String vertex = StringCustomUtils.replaceSpecialSymbolsXml(mxCellVo.getVertex());
                    String edge = StringCustomUtils.replaceSpecialSymbolsXml(mxCellVo.getEdge());
                    String source = StringCustomUtils.replaceSpecialSymbolsXml(mxCellVo.getSource());
                    String target = StringCustomUtils.replaceSpecialSymbolsXml(mxCellVo.getTarget());
                    MxGeometryVo mxGeometryVo = mxCellVo.getMxGeometryVo();
                    xmlStrSb.append("<mxCell ");
                    if (StringUtils.isNotBlank(id)) {
                        xmlStrSb.append(spliceStr("id", id));
                    }
                    if (StringUtils.isNotBlank(value)) {
                        xmlStrSb.append(spliceStr("value", value));
                    }
                    if (StringUtils.isNotBlank(style)) {
                        xmlStrSb.append(spliceStr("style", style));
                    }
                    if (StringUtils.isNotBlank(parent)) {
                        xmlStrSb.append(spliceStr("parent", parent));
                    }
                    if (StringUtils.isNotBlank(source)) {
                        xmlStrSb.append(spliceStr("source", source));
                    }
                    if (StringUtils.isNotBlank(target)) {
                        xmlStrSb.append(spliceStr("target", target));
                    }
                    if (StringUtils.isNotBlank(vertex)) {
                        xmlStrSb.append(spliceStr("vertex", vertex));
                    }
                    if (StringUtils.isNotBlank(edge)) {
                        xmlStrSb.append(spliceStr("edge", edge));
                    }
                    if (null != mxGeometryVo) {
                        String relative = StringCustomUtils.replaceSpecialSymbolsXml(mxGeometryVo.getRelative());
                        String as = StringCustomUtils.replaceSpecialSymbolsXml(mxGeometryVo.getAs());
                        String x = StringCustomUtils.replaceSpecialSymbolsXml(mxGeometryVo.getX());
                        String y = StringCustomUtils.replaceSpecialSymbolsXml(mxGeometryVo.getY());
                        String width = StringCustomUtils.replaceSpecialSymbolsXml(mxGeometryVo.getWidth());
                        String height = StringCustomUtils.replaceSpecialSymbolsXml(mxGeometryVo.getHeight());
                        xmlStrSb.append("><mxGeometry ");
                        if (StringUtils.isNotBlank(x)) {
                            xmlStrSb.append(spliceStr("x", x));
                        }
                        if (StringUtils.isNotBlank(y)) {
                            xmlStrSb.append(spliceStr("y", y));
                        }
                        if (StringUtils.isNotBlank(width)) {
                            xmlStrSb.append(spliceStr("width", width));
                        }
                        if (StringUtils.isNotBlank(height)) {
                            xmlStrSb.append(spliceStr("height", height));
                        }
                        if (StringUtils.isNotBlank(relative)) {
                            xmlStrSb.append(spliceStr("relative", relative));
                        }
                        if (StringUtils.isNotBlank(as)) {
                            xmlStrSb.append(spliceStr("as", as));
                        }
                        xmlStrSb.append("/></mxCell>");
                    } else {
                        xmlStrSb.append("/>");
                    }
                }
            }
            xmlStrSb.append("</root>");
            xmlStrSb.append("</mxGraphModel>");

            return new String(xmlStrSb);
        }
        return null;
    }

    /**
     * "MxGraphModel" to string "xml"
     *
     * @param mxGraphModel mxGraphModel
     * @return String
     */
    public static String mxGraphModelToXml(MxGraphModel mxGraphModel) {
        //Fight 'xml' note must write spaces
        if (null != mxGraphModel) {
            StringBuffer xmlStrSb = new StringBuffer();
            String dx = StringCustomUtils.replaceSpecialSymbolsXml(mxGraphModel.getDx());
            String dy = StringCustomUtils.replaceSpecialSymbolsXml(mxGraphModel.getDy());
            String grid = StringCustomUtils.replaceSpecialSymbolsXml(mxGraphModel.getGrid());
            String gridSize = StringCustomUtils.replaceSpecialSymbolsXml(mxGraphModel.getGridSize());
            String guides = StringCustomUtils.replaceSpecialSymbolsXml(mxGraphModel.getGuides());
            String tooltips = StringCustomUtils.replaceSpecialSymbolsXml(mxGraphModel.getTooltips());
            String connect = StringCustomUtils.replaceSpecialSymbolsXml(mxGraphModel.getConnect());
            String arrows = StringCustomUtils.replaceSpecialSymbolsXml(mxGraphModel.getArrows());
            String fold = StringCustomUtils.replaceSpecialSymbolsXml(mxGraphModel.getFold());
            String page = StringCustomUtils.replaceSpecialSymbolsXml(mxGraphModel.getPage());
            String pageScale = StringCustomUtils.replaceSpecialSymbolsXml(mxGraphModel.getPageScale());
            String pageWidth = StringCustomUtils.replaceSpecialSymbolsXml(mxGraphModel.getPageWidth());
            String pageHeight = StringCustomUtils.replaceSpecialSymbolsXml(mxGraphModel.getPageHeight());
            String background = StringCustomUtils.replaceSpecialSymbolsXml(mxGraphModel.getBackground());
            xmlStrSb.append("<mxGraphModel ");
            if (StringUtils.isNotBlank(dx)) {
                xmlStrSb.append(spliceStr("dx", dx));
            }
            if (StringUtils.isNotBlank(dy)) {
                xmlStrSb.append(spliceStr("dy", dy));
            }
            if (StringUtils.isNotBlank(grid)) {
                xmlStrSb.append(spliceStr("grid", grid));
            }
            if (StringUtils.isNotBlank(gridSize)) {
                xmlStrSb.append(spliceStr("gridSize", gridSize));
            }
            if (StringUtils.isNotBlank(guides)) {
                xmlStrSb.append(spliceStr("guides", guides));
            }
            if (StringUtils.isNotBlank(tooltips)) {
                xmlStrSb.append(spliceStr("tooltips", tooltips));
            }
            if (StringUtils.isNotBlank(connect)) {
                xmlStrSb.append(spliceStr("connect", connect));
            }
            if (StringUtils.isNotBlank(arrows)) {
                xmlStrSb.append(spliceStr("arrows", arrows));
            }
            if (StringUtils.isNotBlank(fold)) {
                xmlStrSb.append(spliceStr("fold", fold));
            }
            if (StringUtils.isNotBlank(page)) {
                xmlStrSb.append(spliceStr("page", page));
            }
            if (StringUtils.isNotBlank(pageScale)) {
                xmlStrSb.append(spliceStr("pageScale", pageScale));
            }
            if (StringUtils.isNotBlank(pageWidth)) {
                xmlStrSb.append(spliceStr("pageWidth", pageWidth));
            }
            if (StringUtils.isNotBlank(pageHeight)) {
                xmlStrSb.append(spliceStr("pageHeight", pageHeight));
            }
            if (StringUtils.isNotBlank(background)) {
                xmlStrSb.append(spliceStr("background", background));
            }
            xmlStrSb.append("><root>");
            List<MxCell> rootList = mxGraphModel.getRoot();
            if (null != rootList && rootList.size() > 0) {
                for (MxCell mxCell : rootList) {
                    String id = StringCustomUtils.replaceSpecialSymbolsXml(mxCell.getPageId());
                    String parent = StringCustomUtils.replaceSpecialSymbolsXml(mxCell.getParent());
                    String style = StringCustomUtils.replaceSpecialSymbolsXml(mxCell.getStyle());
                    String value = StringCustomUtils.replaceSpecialSymbolsXml(mxCell.getValue());
                    String vertex = StringCustomUtils.replaceSpecialSymbolsXml(mxCell.getVertex());
                    String edge = StringCustomUtils.replaceSpecialSymbolsXml(mxCell.getEdge());
                    String source = StringCustomUtils.replaceSpecialSymbolsXml(mxCell.getSource());
                    String target = StringCustomUtils.replaceSpecialSymbolsXml(mxCell.getTarget());
                    xmlStrSb.append("<mxCell ");
                    if (StringUtils.isNotBlank(id)) {
                        xmlStrSb.append(spliceStr("id", id));
                    }
                    if (StringUtils.isNotBlank(value)) {
                        xmlStrSb.append(spliceStr("value", value));
                    }
                    if (StringUtils.isNotBlank(style)) {
                        xmlStrSb.append(spliceStr("style", style));
                    }
                    if (StringUtils.isNotBlank(parent)) {
                        xmlStrSb.append(spliceStr("parent", parent));
                    }
                    if (StringUtils.isNotBlank(source)) {
                        xmlStrSb.append(spliceStr("source", source));
                    }
                    if (StringUtils.isNotBlank(target)) {
                        xmlStrSb.append(spliceStr("target", target));
                    }
                    if (StringUtils.isNotBlank(vertex)) {
                        xmlStrSb.append(spliceStr("vertex", vertex));
                    }
                    if (StringUtils.isNotBlank(edge)) {
                        xmlStrSb.append(spliceStr("edge", edge));
                    }
                    MxGeometry mxGeometry = mxCell.getMxGeometry();
                    if (null != mxGeometry) {
                        String relative = StringCustomUtils.replaceSpecialSymbolsXml(mxGeometry.getRelative());
                        String as = StringCustomUtils.replaceSpecialSymbolsXml(mxGeometry.getAs());
                        String x = StringCustomUtils.replaceSpecialSymbolsXml(mxGeometry.getX());
                        String y = StringCustomUtils.replaceSpecialSymbolsXml(mxGeometry.getY());
                        String width = StringCustomUtils.replaceSpecialSymbolsXml(mxGeometry.getWidth());
                        String height = StringCustomUtils.replaceSpecialSymbolsXml(mxGeometry.getHeight());
                        xmlStrSb.append("><mxGeometry ");
                        if (StringUtils.isNotBlank(x)) {
                            xmlStrSb.append(spliceStr("x", x));
                        }
                        if (StringUtils.isNotBlank(y)) {
                            xmlStrSb.append(spliceStr("y", y));
                        }
                        if (StringUtils.isNotBlank(width)) {
                            xmlStrSb.append(spliceStr("width", width));
                        }
                        if (StringUtils.isNotBlank(height)) {
                            xmlStrSb.append(spliceStr("height", height));
                        }
                        if (StringUtils.isNotBlank(relative)) {
                            xmlStrSb.append(spliceStr("relative", relative));
                        }
                        if (StringUtils.isNotBlank(as)) {
                            xmlStrSb.append(spliceStr("as", as));
                        }
                        xmlStrSb.append("/></mxCell>");
                    } else {
                        xmlStrSb.append("/>");
                    }
                }
            }
            xmlStrSb.append("</root>");
            xmlStrSb.append("</mxGraphModel>");

            return new String(xmlStrSb);
        }
        return null;
    }

    /**
     * Flow to string xml
     *
     * @param flow flow
     * @return String
     */
    public static String flowAndStopInfoToXml(Flow flow, String xmlStr) {
        // Spell xml note must write spaces
        if (null != flow) {
            StringBuffer xmlStrSb = new StringBuffer();
            String id = StringCustomUtils.replaceSpecialSymbolsXml(flow.getId());
            String name = StringCustomUtils.replaceSpecialSymbolsXml(flow.getName());
            String description = StringCustomUtils.replaceSpecialSymbolsXml(flow.getDescription());
            String driverMemory = StringCustomUtils.replaceSpecialSymbolsXml(flow.getDriverMemory());
            String executorCores = StringCustomUtils.replaceSpecialSymbolsXml(flow.getExecutorCores());
            String executorMemory = StringCustomUtils.replaceSpecialSymbolsXml(flow.getExecutorMemory());
            String executorNumber = StringCustomUtils.replaceSpecialSymbolsXml(flow.getExecutorNumber());
            String flowPageId = StringCustomUtils.replaceSpecialSymbolsXml(flow.getPageId());
            xmlStrSb.append("<flow ");
            if (StringUtils.isNotBlank(id)) {
                xmlStrSb.append(spliceStr("id", id));
            }
            if (StringUtils.isNotBlank(name)) {
                xmlStrSb.append(spliceStr("name", name));
            }
            if (StringUtils.isNotBlank(description)) {
                xmlStrSb.append(spliceStr("description", description));
            }
            if (StringUtils.isNotBlank(driverMemory)) {
                xmlStrSb.append(spliceStr("driverMemory", driverMemory));
            }
            if (StringUtils.isNotBlank(executorCores)) {
                xmlStrSb.append(spliceStr("executorCores", executorCores));
            }
            if (StringUtils.isNotBlank(executorMemory)) {
                xmlStrSb.append(spliceStr("executorMemory", executorMemory));
            }
            if (StringUtils.isNotBlank(executorNumber)) {
                xmlStrSb.append(spliceStr("executorNumber", executorNumber));
            }
            if (StringUtils.isNotBlank(flowPageId)) {
                xmlStrSb.append(spliceStr("pageId", flowPageId));
            }
            xmlStrSb.append("> \n");
            List<Stops> stopsVoList = flow.getStopsList();
            if (null != stopsVoList && stopsVoList.size() > 0) {
                for (Stops stopVo : stopsVoList) {
                    String stopId = StringCustomUtils.replaceSpecialSymbolsXml(stopVo.getId());
                    String pageId = StringCustomUtils.replaceSpecialSymbolsXml(stopVo.getPageId());
                    String stopName = StringCustomUtils.replaceSpecialSymbolsXml(stopVo.getName());
                    String bundel = StringCustomUtils.replaceSpecialSymbolsXml(stopVo.getBundel());
                    String stopDescription = StringCustomUtils.replaceSpecialSymbolsXml(stopVo.getDescription());
                    Boolean checkpoint = stopVo.getIsCheckpoint();
                    String inports = StringCustomUtils.replaceSpecialSymbolsXml(stopVo.getInports());
                    PortType inPortType = stopVo.getInPortType();
                    String outports = StringCustomUtils.replaceSpecialSymbolsXml(stopVo.getOutports());
                    PortType outPortType = stopVo.getOutPortType();
                    String owner = StringCustomUtils.replaceSpecialSymbolsXml(stopVo.getOwner());
                    String groups = StringCustomUtils.replaceSpecialSymbolsXml(stopVo.getGroups());
                    String crtUser = StringCustomUtils.replaceSpecialSymbolsXml(stopVo.getCrtUser());
                    xmlStrSb.append("<stop ");
                    if (StringUtils.isNotBlank(stopId)) {
                        xmlStrSb.append(spliceStr("id", stopId));
                    }
                    if (StringUtils.isNotBlank(pageId)) {
                        xmlStrSb.append(spliceStr("pageId", pageId));
                    }
                    if (StringUtils.isNotBlank(stopName)) {
                        xmlStrSb.append(spliceStr("name", stopName));
                    }
                    if (StringUtils.isNotBlank(bundel)) {
                        xmlStrSb.append(spliceStr("bundel", bundel));
                    }
                    if (StringUtils.isNotBlank(stopDescription)) {
                        xmlStrSb.append(spliceStr("description", stopDescription));
                    }
                    if (null != checkpoint) {
                        xmlStrSb.append(spliceStr("isCheckpoint", (checkpoint ? 1 : 0)));
                    }
                    if (StringUtils.isNotBlank(inports)) {
                        xmlStrSb.append(spliceStr("inports", inports));
                    }

                    if (StringUtils.isNotBlank(outports)) {
                        xmlStrSb.append(spliceStr("outports", outports));
                    }
                    if (StringUtils.isNotBlank(owner)) {
                        xmlStrSb.append(spliceStr("owner", owner));
                    }
                    if (null != inPortType) {
                        xmlStrSb.append(spliceStr("inPortType", inPortType));
                    }
                    if (null != outPortType) {
                        xmlStrSb.append(spliceStr("outPortType", outPortType));
                    }
                    if (StringUtils.isNotBlank(groups)) {
                        xmlStrSb.append(spliceStr("groups", groups));
                    }
                    if (StringUtils.isNotBlank(crtUser)) {
                        xmlStrSb.append(spliceStr("crtUser", crtUser));
                    }
                    List<Property> property = stopVo.getProperties();
                    if (null != property && property.size() > 0) {
                        xmlStrSb.append("> \n");
                        for (Property propertyVo : property) {
                            xmlStrSb.append("<property ");
                            String propertyId = StringCustomUtils.replaceSpecialSymbolsXml(propertyVo.getId());
                            String displayName = StringCustomUtils.replaceSpecialSymbolsXml(propertyVo.getDisplayName());
                            String propertyName = StringCustomUtils.replaceSpecialSymbolsXml(propertyVo.getName());
                            String customValue = StringCustomUtils.replaceSpecialSymbolsXml(propertyVo.getCustomValue());
                            String propertyDescription = StringCustomUtils.replaceSpecialSymbolsXml(propertyVo.getDescription());
                            String allowableValues = StringCustomUtils.replaceSpecialSymbolsXml(propertyVo.getAllowableValues());
                            boolean required = propertyVo.getRequired();
                            boolean sensitive = propertyVo.getSensitive();
                            boolean isSelect = propertyVo.getIsSelect();
                            String propertyVocrtUser = StringCustomUtils.replaceSpecialSymbolsXml(propertyVo.getCrtUser());
                            if (StringUtils.isNotBlank(propertyId)) {
                                xmlStrSb.append(spliceStr("id", id));
                            }
                            if (StringUtils.isNotBlank(displayName)) {
                                xmlStrSb.append(spliceStr("displayName", displayName));
                            }
                            if (StringUtils.isNotBlank(propertyName)) {
                                xmlStrSb.append(spliceStr("name", propertyName));
                            }
                            if (StringUtils.isNotBlank(propertyDescription)) {
                                xmlStrSb.append(spliceStr("description", propertyDescription));
                            }
                            if (StringUtils.isNotBlank(allowableValues)) {
                                xmlStrSb.append(spliceStr("allowableValues", allowableValues.replaceAll("\"", "")));
                            }
                            if (StringUtils.isNotBlank(customValue)) {
                                xmlStrSb.append(spliceStr("customValue", customValue));
                            }
                            if (StringUtils.isNotBlank(propertyVocrtUser)) {
                                xmlStrSb.append(spliceStr("crtUser", propertyVocrtUser));
                            }
                            xmlStrSb.append(spliceStr("required", required));
                            xmlStrSb.append(spliceStr("sensitive", sensitive));
                            xmlStrSb.append(spliceStr("isSelect", isSelect));
                            xmlStrSb.append("/> \n");
                        }
                        xmlStrSb.append("</stop> \n");
                    } else {
                        xmlStrSb.append("/> \n");
                    }
                }
                List<Paths> pathsList = flow.getPathsList();
                if (null != pathsList && pathsList.size() > 0) {
                    for (Paths paths : pathsList) {
                        xmlStrSb.append("<paths ");
                        String crtUser = StringCustomUtils.replaceSpecialSymbolsXml(paths.getCrtUser());
                        String from = StringCustomUtils.replaceSpecialSymbolsXml(paths.getFrom());
                        String to = StringCustomUtils.replaceSpecialSymbolsXml(paths.getTo());
                        String inport = StringCustomUtils.replaceSpecialSymbolsXml(paths.getInport());
                        String outport = StringCustomUtils.replaceSpecialSymbolsXml(paths.getOutport());
                        String pageId = StringCustomUtils.replaceSpecialSymbolsXml(paths.getPageId());
                        String filterCondition = StringCustomUtils.replaceSpecialSymbolsXml(paths.getFilterCondition());
                        if (StringUtils.isNotBlank(crtUser)) {
                            xmlStrSb.append(spliceStr("crtUser", crtUser));
                        }
                        if (StringUtils.isNotBlank(from)) {
                            xmlStrSb.append(spliceStr("from", from));
                        }
                        if (StringUtils.isNotBlank(to)) {
                            xmlStrSb.append(spliceStr("to", to));
                        }
                        if (StringUtils.isNotBlank(inport)) {
                            xmlStrSb.append(spliceStr("inport", inport));
                        }
                        if (StringUtils.isNotBlank(outport)) {
                            xmlStrSb.append(spliceStr("outport", outport));
                        }
                        if (StringUtils.isNotBlank(pageId)) {
                            xmlStrSb.append(spliceStr("pageId", pageId));
                        }
                        if (StringUtils.isNotBlank(filterCondition)) {
                            xmlStrSb.append(spliceStr("filterCondition", filterCondition));
                        }
                        xmlStrSb.append(" /> \n");
                    }
                }

            }
            if (StringUtils.isNoneBlank(xmlStr)) {
                xmlStrSb.append(xmlStr);
            }
            xmlStrSb.append("</flow>");
            return new String(xmlStrSb);
        }
        return null;
    }

    /**
     * flowList to xml
     *
     * @param flowList flowList
     * @return String
     */
    public static String flowListToXmlStr(List<Flow> flowList) {
        StringBuilder xmlStrBuf = new StringBuilder();
        if (null != flowList && flowList.size() > 0) {
            for (Flow flow : flowList) {
                MxGraphModel mxGraphModel = flow.getMxGraphModel();
                String mxGraphModelXmlStr = mxGraphModelToXml(mxGraphModel);
                String flowXmlStr = flowAndStopInfoToXml(flow, mxGraphModelXmlStr);
                if (StringUtils.isNotBlank(flowXmlStr)) {
                    xmlStrBuf.append(flowXmlStr);
                }
            }
        }
        return xmlStrBuf.toString();
    }

    /**
     * flowGroupPathsList to xml
     *
     * @param flowGroupPathsList flowGroupPathsList
     * @return String
     */
    public static String flowGroupPathsListToXmlStr(List<FlowGroupPaths> flowGroupPathsList) {
        StringBuilder xmlStrBuf = new StringBuilder();
        if (null != flowGroupPathsList && flowGroupPathsList.size() > 0) {
            for (FlowGroupPaths flowGroupPaths : flowGroupPathsList) {
                xmlStrBuf.append("<flowGroupPaths ");
                String from = StringCustomUtils.replaceSpecialSymbolsXml(flowGroupPaths.getFrom());
                String to = StringCustomUtils.replaceSpecialSymbolsXml(flowGroupPaths.getTo());
                String inport = StringCustomUtils.replaceSpecialSymbolsXml(flowGroupPaths.getInport());
                String outport = StringCustomUtils.replaceSpecialSymbolsXml(flowGroupPaths.getOutport());
                String pageId = StringCustomUtils.replaceSpecialSymbolsXml(flowGroupPaths.getPageId());
                if (StringUtils.isNotBlank(from)) {
                    xmlStrBuf.append(spliceStr("from", from));
                }
                if (StringUtils.isNotBlank(to)) {
                    xmlStrBuf.append(spliceStr("to", to));
                }
                if (StringUtils.isNotBlank(inport)) {
                    xmlStrBuf.append(spliceStr("inport", inport));
                }
                if (StringUtils.isNotBlank(outport)) {
                    xmlStrBuf.append(spliceStr("outport", outport));
                }
                if (StringUtils.isNotBlank(pageId)) {
                    xmlStrBuf.append(spliceStr("pageId", pageId));
                }
                xmlStrBuf.append(" /> \n");
            }
        }
        return xmlStrBuf.toString();
    }

    /**
     * flowGroup to xml
     *
     * @param flowGroup flowGroup
     * @return String
     */
    public static String flowGroupToXmlStr(FlowGroup flowGroup) {
        StringBuilder xmlStrBuf = new StringBuilder();
        if (null != flowGroup) {
            xmlStrBuf.append("<flowGroup ");
            String id = StringCustomUtils.replaceSpecialSymbolsXml(flowGroup.getId());
            String name = StringCustomUtils.replaceSpecialSymbolsXml(flowGroup.getName());
            String description = StringCustomUtils.replaceSpecialSymbolsXml(flowGroup.getDescription());
            String flowGroupPageId = StringCustomUtils.replaceSpecialSymbolsXml(flowGroup.getPageId());
            if (StringUtils.isNotBlank(id)) {
                xmlStrBuf.append(spliceStr("id", id));
            }
            if (StringUtils.isNotBlank(name)) {
                xmlStrBuf.append(spliceStr("name", name));
            }
            if (StringUtils.isNotBlank(description)) {
                xmlStrBuf.append(spliceStr("description", description));
            }
            if (StringUtils.isNotBlank(flowGroupPageId)) {
                xmlStrBuf.append(spliceStr("pageId", flowGroupPageId));
            }
            xmlStrBuf.append("> \n");
            String mxGraphModelXml = mxGraphModelToXml(flowGroup.getMxGraphModel());
            if (StringUtils.isNotBlank(mxGraphModelXml)) {
                xmlStrBuf.append(mxGraphModelXml);
            }
            String flowListXmlStr = flowListToXmlStr(flowGroup.getFlowList());
            if (StringUtils.isNotBlank(flowListXmlStr)) {
                xmlStrBuf.append(flowListXmlStr);
            }
            String flowGroupPathsListXmlStr = flowGroupPathsListToXmlStr(flowGroup.getFlowGroupPathsList());
            if (StringUtils.isNotBlank(flowGroupPathsListXmlStr)) {
                xmlStrBuf.append(flowGroupPathsListXmlStr);
            }
            String flowGroupListToXmlStr = flowGroupListToXmlStr(flowGroup.getFlowGroupList());
            if (StringUtils.isNotBlank(flowGroupListToXmlStr)) {
                xmlStrBuf.append(flowGroupListToXmlStr);
            }
            xmlStrBuf.append("</flowGroup> \n");
        }
        return xmlStrBuf.toString();
    }

    public static String flowGroupListToXmlStr(List<FlowGroup> flowGroupList) {
        StringBuilder xmlStrBuf = new StringBuilder();
        if (null != flowGroupList && flowGroupList.size() > 0) {
            for (FlowGroup flowGroup : flowGroupList) {
                String flowGroupToXmlStr = flowGroupToXmlStr(flowGroup);
                if (StringUtils.isNotBlank(flowGroupToXmlStr)) {
                    xmlStrBuf.append(flowGroupToXmlStr);
                }
            }
        }
        return xmlStrBuf.toString();
    }

    /**
     * String type "xml" to "MxGraphModel"
     *
     * @param xmlData xml string data
     * @return MxGraphModelVo
     */
    public static MxGraphModelVo xmlToMxGraphModel(String xmlData) {

        try {
            Element mxGraphModelXml = xmlStrToElementGetByKey(xmlData, false, "mxGraphModel");
            if (null == mxGraphModelXml) {
                return null;
            }
            String dx = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("dx"));
            String dy = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("dy"));
            String grid = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("grid"));
            String gridSize = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("gridSize"));
            String guides = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("guides"));
            String tooltips = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("tooltips"));
            String connect = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("connect"));
            String arrows = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("arrows"));
            String fold = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("fold"));
            String page = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("page"));
            String pageScale = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("pageScale"));
            String pageWidth = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("pageWidth"));
            String pageHeight = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("pageHeight"));
            String background = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("background"));

            MxGraphModelVo mxGraphModelVo = new MxGraphModelVo();
            mxGraphModelVo.setDx(dx);
            mxGraphModelVo.setDy(dy);
            mxGraphModelVo.setGrid(grid);
            mxGraphModelVo.setGridSize(gridSize);
            mxGraphModelVo.setGuides(guides);
            mxGraphModelVo.setTooltips(tooltips);
            mxGraphModelVo.setConnect(connect);
            mxGraphModelVo.setArrows(arrows);
            mxGraphModelVo.setFold(fold);
            mxGraphModelVo.setPage(page);
            mxGraphModelVo.setPageScale(pageScale);
            mxGraphModelVo.setPageWidth(pageWidth);
            mxGraphModelVo.setPageHeight(pageHeight);
            mxGraphModelVo.setBackground(background);
            List<MxCellVo> rootVoList = new ArrayList<>();
            Element rootjd = mxGraphModelXml.element("root");
            Iterator rootiter = rootjd.elementIterator("mxCell"); // Get the child node mxCell under the root node
            while (rootiter.hasNext()) {
                MxCellVo mxCellVo = new MxCellVo();
                MxGeometryVo mxGeometryVo = null;
                Element recordEle = (Element) rootiter.next();
                String mxCellId = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("id"));
                String parent = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("parent"));
                String style = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("style"));
                String edge = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("edge"));
                String source = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("source"));
                String target = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("target"));
                String value = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("value"));
                String vertex = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("vertex"));
                if ("1".equals(edge)) {
                    if (StringUtils.isBlank(source) || StringUtils.isBlank(target)) {
                        continue;
                    }
                }
                mxCellVo.setPageId(mxCellId);
                mxCellVo.setParent(parent);
                mxCellVo.setStyle(style);
                mxCellVo.setEdge(edge);
                mxCellVo.setSource(source);
                mxCellVo.setTarget(target);
                mxCellVo.setValue(value);
                mxCellVo.setVertex(vertex);
                if (StringUtils.isNotBlank(style)) {
                    mxGeometryVo = new MxGeometryVo();
                    Element mxGeometry = recordEle.element("mxGeometry");
                    String relative = StringCustomUtils.recoverSpecialSymbolsXml(mxGeometry.attributeValue("relative"));
                    String as = StringCustomUtils.recoverSpecialSymbolsXml(mxGeometry.attributeValue("as"));
                    String x = StringCustomUtils.recoverSpecialSymbolsXml(mxGeometry.attributeValue("x"));
                    String y = StringCustomUtils.recoverSpecialSymbolsXml(mxGeometry.attributeValue("y"));
                    String width = StringCustomUtils.recoverSpecialSymbolsXml(mxGeometry.attributeValue("width"));
                    String height = StringCustomUtils.recoverSpecialSymbolsXml(mxGeometry.attributeValue("height"));
                    mxGeometryVo.setRelative(relative);
                    mxGeometryVo.setAs(as);
                    mxGeometryVo.setX(x);
                    mxGeometryVo.setY(y);
                    mxGeometryVo.setWidth(width);
                    mxGeometryVo.setHeight(height);
                }
                mxCellVo.setMxGeometryVo(mxGeometryVo);
                rootVoList.add(mxCellVo);
            }
            mxGraphModelVo.setRootVo(rootVoList);
            return mxGraphModelVo;
        } catch (Exception e) {
            logger.error("Conversion failed", e);
            return null;
        }
    }

    /**
     * String type xml to MxGraphModel
     *
     * @param xmlData xml string data
     * @return MxGraphModelVo
     */
    public static MxGraphModelVo allXmlToMxGraphModelVo(String xmlData, int PageId) {
        try {
            Element rootElt = xmlStrToElement(xmlData, false);
            if (null == rootElt) {
                return null;
            }
            Element flow = rootElt.element("flow");
            Element mxGraphModel = flow.element("mxGraphModel");
            String dx = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModel.attributeValue("dx"));
            String dy = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModel.attributeValue("dy"));
            String grid = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModel.attributeValue("grid"));
            String gridSize = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModel.attributeValue("gridSize"));
            String guides = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModel.attributeValue("guides"));
            String tooltips = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModel.attributeValue("tooltips"));
            String connect = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModel.attributeValue("connect"));
            String arrows = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModel.attributeValue("arrows"));
            String fold = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModel.attributeValue("fold"));
            String page = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModel.attributeValue("page"));
            String pageScale = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModel.attributeValue("pageScale"));
            String pageWidth = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModel.attributeValue("pageWidth"));
            String pageHeight = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModel.attributeValue("pageHeight"));
            String background = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModel.attributeValue("background"));

            MxGraphModelVo mxGraphModelVo = new MxGraphModelVo();
            mxGraphModelVo.setDx(dx);
            mxGraphModelVo.setDy(dy);
            mxGraphModelVo.setGrid(grid);
            mxGraphModelVo.setGridSize(gridSize);
            mxGraphModelVo.setGuides(guides);
            mxGraphModelVo.setTooltips(tooltips);
            mxGraphModelVo.setConnect(connect);
            mxGraphModelVo.setArrows(arrows);
            mxGraphModelVo.setFold(fold);
            mxGraphModelVo.setPage(page);
            mxGraphModelVo.setPageScale(pageScale);
            mxGraphModelVo.setPageWidth(pageWidth);
            mxGraphModelVo.setPageHeight(pageHeight);
            mxGraphModelVo.setBackground(background);
            List<MxCellVo> rootVoList = new ArrayList<>();
            Element rootjd = mxGraphModel.element("root");
            Iterator rootiter = rootjd.elementIterator("mxCell"); // Get the child node "mxCell" under the root node
            while (rootiter.hasNext()) {
                MxCellVo mxCellVo = new MxCellVo();
                MxGeometryVo mxGeometryVo = null;
                Element recordEle = (Element) rootiter.next();
                String mxCellId = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("id"));
                String parent = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("parent"));
                String style = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("style"));
                String edge = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("edge"));
                String source = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("source"));
                String target = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("target"));
                String value = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("value"));
                String vertex = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("vertex"));
                if (PageId >= 1) {
                    if (Integer.parseInt(mxCellId) < 2) {
                        continue;
                    }
                }
                if ("1".equals(edge)) {
                    if (StringUtils.isBlank(source) || StringUtils.isBlank(target)) {
                        continue;
                    }
                }
                mxCellVo.setPageId((Integer.parseInt(mxCellId) + PageId) + "");
                mxCellVo.setParent(parent);
                mxCellVo.setStyle(style);
                mxCellVo.setEdge(edge);
                if (StringUtils.isNotBlank(source) && StringUtils.isNotBlank(target)) {
                    mxCellVo.setSource((Integer.parseInt(source) + PageId) + "");
                    mxCellVo.setTarget((Integer.parseInt(target) + PageId) + "");
                }
                mxCellVo.setValue(value);
                mxCellVo.setVertex(vertex);
                if (StringUtils.isNotBlank(style)) {
                    mxGeometryVo = new MxGeometryVo();
                    Element mxGeometry = recordEle.element("mxGeometry");
                    String relative = StringCustomUtils.recoverSpecialSymbolsXml(mxGeometry.attributeValue("relative"));
                    String as = StringCustomUtils.recoverSpecialSymbolsXml(mxGeometry.attributeValue("as"));
                    String x = StringCustomUtils.recoverSpecialSymbolsXml(mxGeometry.attributeValue("x"));
                    String y = StringCustomUtils.recoverSpecialSymbolsXml(mxGeometry.attributeValue("y"));
                    String width = StringCustomUtils.recoverSpecialSymbolsXml(mxGeometry.attributeValue("width"));
                    String height = StringCustomUtils.recoverSpecialSymbolsXml(mxGeometry.attributeValue("height"));
                    mxGeometryVo.setRelative(relative);
                    mxGeometryVo.setAs(as);
                    mxGeometryVo.setX(x);
                    mxGeometryVo.setY(y);
                    mxGeometryVo.setWidth(width);
                    mxGeometryVo.setHeight(height);
                }
                mxCellVo.setMxGeometryVo(mxGeometryVo);
                rootVoList.add(mxCellVo);
            }
            mxGraphModelVo.setRootVo(rootVoList);
            return mxGraphModelVo;
        } catch (Exception e) {
            logger.error("Conversion failed", e);
            return null;
        }
    }

    /**
     * List<StopTemplateModel>  to  List<Stops>
     *
     * @param stopsListTemplate stopsList template
     * @return List<Stops>
     */
    public static List<Stops> stopTemplateVoToStop(List<StopTemplateModel> stopsListTemplate) {
        List<Stops> stopsList = new ArrayList<>();
        // 判空
        if (null != stopsListTemplate && stopsListTemplate.size() > 0) {
            // Loop copy
            for (StopTemplateModel stopTemplate : stopsListTemplate) {
                if (null != stopTemplate) {
                    Stops stops = new Stops();
                    // Copy the contents of "StopTemplateModel" to "Stops"
                    BeanUtils.copyProperties(stopTemplate, stops);
                    stops.setIsCheckpoint(stopTemplate.getIsCheckpoint());
                    List<PropertyTemplateModel> propertyTemplateModel = stopTemplate.getProperties();
                    if (null != propertyTemplateModel && propertyTemplateModel.size() > 0) {
                        List<Property> propertyList = new ArrayList<>();
                        for (PropertyTemplateModel propertyTemplate : propertyTemplateModel) {
                            if (null != propertyTemplate) {
                                Property property = new Property();
                                // Copy the contents of "propertyTemplate" into "property"
                                BeanUtils.copyProperties(propertyTemplate, property);
                                property.setStops(stops);
                                propertyList.add(property);
                            }
                        }
                        stops.setProperties(propertyList);
                    }
                    stopsList.add(stops);
                }
            }
        }
        return stopsList;
    }

    /**
     * String type xml to MxGraphModel
     *
     * @param xmlData xml string data
     * @return MxGraphModel
     */
    public static MxGraphModel xmlToMxGraphModel(String xmlData, int maxPageId, String username, Boolean isAppend) {
        if (StringUtils.isBlank(xmlData)) {
            return null;
        }
        try {
            Element rootElt = xmlStrToElement(xmlData, false);
            if (null == rootElt) {
                return null;
            }
            Element mxGraphModelXml = rootElt.element("mxGraphModel");
            String dx = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("dx"));
            String dy = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("dy"));
            String grid = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("grid"));
            String gridSize = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("gridSize"));
            String guides = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("guides"));
            String tooltips = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("tooltips"));
            String connect = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("connect"));
            String arrows = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("arrows"));
            String fold = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("fold"));
            String page = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("page"));
            String pageScale = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("pageScale"));
            String pageWidth = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("pageWidth"));
            String pageHeight = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("pageHeight"));
            String background = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("background"));

            MxGraphModel mxGraphModel = new MxGraphModel();
            mxGraphModel.setCrtDttm(new Date());
            mxGraphModel.setCrtUser(username);
            mxGraphModel.setLastUpdateDttm(new Date());
            mxGraphModel.setLastUpdateUser(username);
            mxGraphModel.setVersion(0L);
            mxGraphModel.setDx(dx);
            mxGraphModel.setDy(dy);
            mxGraphModel.setGrid(grid);
            mxGraphModel.setGridSize(gridSize);
            mxGraphModel.setGuides(guides);
            mxGraphModel.setTooltips(tooltips);
            mxGraphModel.setConnect(connect);
            mxGraphModel.setArrows(arrows);
            mxGraphModel.setFold(fold);
            mxGraphModel.setPage(page);
            mxGraphModel.setPageScale(pageScale);
            mxGraphModel.setPageWidth(pageWidth);
            mxGraphModel.setPageHeight(pageHeight);
            mxGraphModel.setBackground(background);
            List<MxCell> rootList = new ArrayList<>();
            if (isAppend) {
                rootList = new ArrayList<>();
            } else {
                rootList = MxCellUtils.initMxCell(username, mxGraphModel);
            }
            Element rootjd = mxGraphModelXml.element("root");
            Iterator rootiter = rootjd.elementIterator("mxCell"); // Get the child node "mxCell" under the root node
            while (rootiter.hasNext()) {

                Element recordEle = (Element) rootiter.next();
                String mxCellId = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("id"));
                String parent = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("parent"));
                String style = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("style"));
                String edge = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("edge"));
                String source = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("source"));
                String target = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("target"));
                String value = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("value"));
                String vertex = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("vertex"));
                if (maxPageId >= 1) {
                    if (Integer.parseInt(mxCellId) < 2) {
                        continue;
                    }
                }
                if ("1".equals(edge)) {
                    if (StringUtils.isBlank(source) || StringUtils.isBlank(target)) {
                        continue;
                    }
                }

                MxCell mxCell = MxCellUtils.mxCellNewNoId(username);
                mxCell.setPageId((Integer.parseInt(mxCellId) + maxPageId) + "");
                mxCell.setParent(parent);
                mxCell.setStyle(style);
                mxCell.setEdge(edge);
                if (StringUtils.isNotBlank(source) && StringUtils.isNotBlank(target)) {
                    mxCell.setSource((Integer.parseInt(source) + maxPageId) + "");
                    mxCell.setTarget((Integer.parseInt(target) + maxPageId) + "");
                }
                mxCell.setValue(value);
                mxCell.setVertex(vertex);
                mxCell.setMxGraphModel(mxGraphModel);
                if (StringUtils.isNotBlank(style)) {
                    MxGeometry mxGeometry = new MxGeometry();
                    Element mxGeometryXml = recordEle.element("mxGeometry");
                    String relative = StringCustomUtils.recoverSpecialSymbolsXml(mxGeometryXml.attributeValue("relative"));
                    String as = StringCustomUtils.recoverSpecialSymbolsXml(mxGeometryXml.attributeValue("as"));
                    String x = StringCustomUtils.recoverSpecialSymbolsXml(mxGeometryXml.attributeValue("x"));
                    String y = StringCustomUtils.recoverSpecialSymbolsXml(mxGeometryXml.attributeValue("y"));
                    String width = StringCustomUtils.recoverSpecialSymbolsXml(mxGeometryXml.attributeValue("width"));
                    String height = StringCustomUtils.recoverSpecialSymbolsXml(mxGeometryXml.attributeValue("height"));
                    mxGeometry.setCrtDttm(new Date());
                    mxGeometry.setCrtUser(username);
                    mxGeometry.setLastUpdateDttm(new Date());
                    mxGeometry.setLastUpdateUser(username);
                    mxGeometry.setVersion(0L);
                    mxGeometry.setRelative(relative);
                    mxGeometry.setAs(as);
                    mxGeometry.setX(x);
                    mxGeometry.setY(y);
                    mxGeometry.setWidth(width);
                    mxGeometry.setHeight(height);
                    mxGeometry.setMxCell(mxCell);
                    mxCell.setMxGeometry(mxGeometry);
                }
                rootList.add(mxCell);
            }
            mxGraphModel.setRoot(rootList);
            return mxGraphModel;
        } catch (Exception e) {
            logger.error("Conversion failed", e);
            return null;
        }
    }

    /**
     * String type xml to FlowGroupPaths
     *
     * @param xmlData   xml string data
     * @param maxPageId Maximum PageId of FlowGroupPaths
     * @return FlowGroupPaths
     */
    public static FlowGroupPaths xmlToFlowGroupPaths(String xmlData, int maxPageId, String username) {
        if (StringUtils.isBlank(xmlData)) {
            return null;
        }
        Element rootElt = xmlStrToElement(xmlData, false);
        if (null == rootElt) {
            return null;
        }
        FlowGroupPaths flowGroupPaths = new FlowGroupPaths();
        Element flowGroupPathsElement = rootElt.element("flowGroupPaths");
        String from = StringCustomUtils.recoverSpecialSymbolsXml(flowGroupPathsElement.attributeValue("from"));
        String to = StringCustomUtils.recoverSpecialSymbolsXml(flowGroupPathsElement.attributeValue("to"));
        String outPort = StringCustomUtils.recoverSpecialSymbolsXml(flowGroupPathsElement.attributeValue("outport"));
        String inPort = StringCustomUtils.recoverSpecialSymbolsXml(flowGroupPathsElement.attributeValue("inport"));
        String pageId = StringCustomUtils.recoverSpecialSymbolsXml(flowGroupPathsElement.attributeValue("pageId"));
        flowGroupPaths.setCrtDttm(new Date());
        flowGroupPaths.setCrtUser(username);
        flowGroupPaths.setLastUpdateDttm(new Date());
        flowGroupPaths.setLastUpdateUser(username);
        flowGroupPaths.setVersion(0L);
        if (StringUtils.isNotBlank(from)) {
            flowGroupPaths.setFrom((Integer.parseInt(from) + maxPageId) + "");
        }
        if (StringUtils.isNotBlank(to)) {
            flowGroupPaths.setTo((Integer.parseInt(to) + maxPageId) + "");
        }
        if (StringUtils.isNotBlank(outPort)) {
            flowGroupPaths.setOutport(outPort);
        }
        if (StringUtils.isNotBlank(inPort)) {
            flowGroupPaths.setInport(inPort);
        }
        if (StringUtils.isNotBlank(pageId)) {
            flowGroupPaths.setPageId((Integer.parseInt(pageId) + maxPageId) + "");
        }
        return flowGroupPaths;

    }

    /**
     * xmlToPathsOne
     *
     * @param xmlData   xml string data
     * @param maxPageId Maximum PageId of Paths
     * @param username  Operator username
     * @return Paths
     */
    public static Paths xmlToPathsOne(String xmlData, int maxPageId, String username) {
        if (StringUtils.isBlank(xmlData)) {
            return null;
        }
        try {
            Element rootElt = xmlStrToElement(xmlData, false);
            if (null == rootElt) {
                return null;
            }
            Element pathsElement = rootElt.element("paths"); // Get the child node "paths" under the root node
            Paths paths = new Paths();
            String from = StringCustomUtils.recoverSpecialSymbolsXml(pathsElement.attributeValue("from"));
            String to = StringCustomUtils.recoverSpecialSymbolsXml(pathsElement.attributeValue("to"));
            String outport = StringCustomUtils.recoverSpecialSymbolsXml(pathsElement.attributeValue("outport"));
            String inport = StringCustomUtils.recoverSpecialSymbolsXml(pathsElement.attributeValue("inport"));
            String pageId = StringCustomUtils.recoverSpecialSymbolsXml(pathsElement.attributeValue("pageId"));
            String filterCondition = StringCustomUtils.recoverSpecialSymbolsXml(pathsElement.attributeValue("filterCondition"));
            paths.setCrtDttm(new Date());
            paths.setCrtUser(username);
            paths.setLastUpdateDttm(new Date());
            paths.setLastUpdateUser(username);
            if (StringUtils.isNotBlank(from)) {
                paths.setFrom((Integer.parseInt(from) + maxPageId) + "");
            }
            if (StringUtils.isNotBlank(to)) {
                paths.setTo((Integer.parseInt(to) + maxPageId) + "");
            }
            paths.setOutport(outport);
            paths.setInport(inport);
            paths.setPageId((Integer.parseInt(pageId) + maxPageId) + "");
            paths.setFilterCondition(filterCondition);
            return paths;
        } catch (Exception e) {
            logger.error("Conversion failed", e);
            return null;
        }
    }

    /**
     * String type xml to "stop"
     *
     * @param xmlData xml string data
     * @return Stops
     */
    public static Stops xmlToStops(String xmlData, int maxPageId, String username) {
        if (StringUtils.isBlank(xmlData)) {
            return null;
        }
        try {
            Element rootElt = xmlStrToElement(xmlData, true);
            if (null == rootElt) {
                return null;
            }
            Element stopElement = rootElt.element("stop");
            Stops stops = new Stops();
            String bundel = StringCustomUtils.recoverSpecialSymbolsXml(stopElement.attributeValue("bundel"));
            String description = StringCustomUtils.recoverSpecialSymbolsXml(stopElement.attributeValue("description"));
            String id = StringCustomUtils.recoverSpecialSymbolsXml(stopElement.attributeValue("id"));
            String name = StringCustomUtils.recoverSpecialSymbolsXml(stopElement.attributeValue("name"));
            String pageId = StringCustomUtils.recoverSpecialSymbolsXml(stopElement.attributeValue("pageId"));
            String inPortType = StringCustomUtils.recoverSpecialSymbolsXml(stopElement.attributeValue("inPortType"));
            String inports = StringCustomUtils.recoverSpecialSymbolsXml(stopElement.attributeValue("inports"));
            String outPortType = StringCustomUtils.recoverSpecialSymbolsXml(stopElement.attributeValue("outPortType"));
            String outports = StringCustomUtils.recoverSpecialSymbolsXml(stopElement.attributeValue("outports"));
            String isCheckpoint = StringCustomUtils.recoverSpecialSymbolsXml(stopElement.attributeValue("isCheckpoint"));
            String owner = StringCustomUtils.recoverSpecialSymbolsXml(stopElement.attributeValue("owner"));
            String groups = StringCustomUtils.recoverSpecialSymbolsXml(stopElement.attributeValue("groups"));
            stops.setCrtDttm(new Date());
            stops.setCrtUser(username);
            stops.setLastUpdateDttm(new Date());
            stops.setLastUpdateUser(username);
            stops.setPageId((Integer.parseInt(pageId) + maxPageId) + "");
            stops.setName(name);
            stops.setDescription(description);
            stops.setBundel(bundel);
            stops.setId(id);
            stops.setInports(inports);
            stops.setOutports(outports);
            stops.setOutPortType(PortType.selectGender(outPortType));
            stops.setInPortType(PortType.selectGenderByValue(inPortType));
            stops.setGroups(groups);
            Boolean Checkpoint = "1".equals(isCheckpoint);
            stops.setIsCheckpoint(Checkpoint);
            stops.setOwner(owner);
            Iterator propertyXmlIterator = stopElement.elementIterator("property");
            if (null != propertyXmlIterator) {
                List<Property> propertyList = new ArrayList<>();
                while (propertyXmlIterator.hasNext()) {
                    Element propertyValue = (Element) propertyXmlIterator.next();
                    Property property = new Property();
                    String allowableValues = StringCustomUtils.recoverSpecialSymbolsXml(propertyValue.attributeValue("allowableValues"));
                    String customValue = StringCustomUtils.recoverSpecialSymbolsXml(propertyValue.attributeValue("customValue"));
                    String propertyDescription = StringCustomUtils.recoverSpecialSymbolsXml(propertyValue.attributeValue("description"));
                    String displayName = StringCustomUtils.recoverSpecialSymbolsXml(propertyValue.attributeValue("displayName"));
                    String propertyId = StringCustomUtils.recoverSpecialSymbolsXml(propertyValue.attributeValue("id"));
                    String propertyName = StringCustomUtils.recoverSpecialSymbolsXml(propertyValue.attributeValue("name"));
                    boolean required = "true".equals(propertyValue.attributeValue("required"));
                    boolean sensitive = "true".equals(propertyValue.attributeValue("sensitive"));
                    boolean isSelect = "true".equals(propertyValue.attributeValue("isSelect"));
                    if (isSelect && null != allowableValues && allowableValues.length() > 1) {
                        String temp = allowableValues.substring(1, allowableValues.length() - 1);
                        String[] tempArray = temp.split(",");
                        StringBuilder tempStringBuffer = new StringBuilder();
                        tempStringBuffer.append("[");
                        for (int i = 0; i < tempArray.length; i++) {
                            tempStringBuffer.append("\"");
                            tempStringBuffer.append(tempArray[i]);
                            tempStringBuffer.append("\"");
                            if (i + 1 != tempArray.length) {
                                tempStringBuffer.append(",");
                            }
                        }
                        tempStringBuffer.append("]");
                        allowableValues = tempStringBuffer.toString();
                    }
                    property.setCrtDttm(new Date());
                    property.setCrtUser(username);
                    property.setLastUpdateDttm(new Date());
                    property.setLastUpdateUser(username);
                    property.setAllowableValues(allowableValues);
                    property.setCustomValue(customValue);
                    property.setDescription(propertyDescription);
                    property.setDisplayName(displayName);
                    property.setId(propertyId);
                    property.setName(propertyName);
                    property.setRequired(required);
                    property.setSensitive(sensitive);
                    property.setIsSelect(isSelect);
                    property.setStops(stops);
                    propertyList.add(property);
                }
                stops.setProperties(propertyList);
            }
            return stops;
        } catch (Exception e) {
            logger.error("Conversion failed", e);
            return null;
        }
    }

    /**
     * String type xml to FlowGroupPaths
     *
     * @param xmlData   xml string data
     * @param maxPageId Maximum PageId of Flow
     * @return Flow
     */
    public static Flow xmlToFlow(String xmlData, int maxPageId, String username, Boolean isAppend) {
        if (StringUtils.isBlank(xmlData)) {
            return null;
        }
        try {
            Element flowElement = xmlStrToElementGetByKey(xmlData, false, "flow");
            if (null == flowElement) {
                return null;
            }
            String flowPageId = StringCustomUtils.recoverSpecialSymbolsXml(flowElement.attributeValue("pageId"));
            String driverMemory = StringCustomUtils.recoverSpecialSymbolsXml(flowElement.attributeValue("driverMemory"));
            String executorCores = StringCustomUtils.recoverSpecialSymbolsXml(flowElement.attributeValue("executorCores"));
            String executorMemory = StringCustomUtils.recoverSpecialSymbolsXml(flowElement.attributeValue("executorMemory"));
            String executorNumber = StringCustomUtils.recoverSpecialSymbolsXml(flowElement.attributeValue("executorNumber"));
            String name = StringCustomUtils.recoverSpecialSymbolsXml(flowElement.attributeValue("name"));
            String description = StringCustomUtils.recoverSpecialSymbolsXml(flowElement.attributeValue("description"));
            flowPageId = (StringUtils.isNotBlank(flowPageId) ? flowPageId : "1");

            Flow flow = new Flow();
            flow.setCrtDttm(new Date());
            flow.setCrtUser(username);
            flow.setLastUpdateDttm(new Date());
            flow.setLastUpdateUser(username);
            flow.setVersion(0L);
            flow.setPageId((null != flowPageId) ? ((Integer.parseInt(flowPageId) + maxPageId) + "") : null);
            flow.setDriverMemory(driverMemory);
            flow.setExecutorCores(executorCores);
            flow.setExecutorMemory(executorMemory);
            flow.setExecutorNumber(executorNumber);
            flow.setName(name);
            flow.setDescription(description);
            // mxGraphModel
            Element mxGraphModelElement = flowElement.element("mxGraphModel");
            MxGraphModel mxGraphModel = xmlToMxGraphModel(mxGraphModelElement.asXML(), maxPageId, username, isAppend);
            if (null != mxGraphModel) {
                mxGraphModel.setFlow(flow);
            }
            flow.setMxGraphModel(mxGraphModel);
            // paths
            Iterator pathXmlIterator = flowElement.elementIterator("paths");
            List<Paths> pathsList = new ArrayList<>();
            while (pathXmlIterator.hasNext()) {
                Element recordEle = (Element) pathXmlIterator.next();
                Paths paths = xmlToPathsOne(recordEle.asXML(), maxPageId, username);
                if (null != paths) {
                    paths.setFlow(flow);
                    pathsList.add(paths);
                }
            }
            flow.setPathsList(pathsList);
            // stop
            Iterator stopXmlIterator = flowElement.elementIterator("stop");
            List<Stops> stopsList = new ArrayList<>();
            while (stopXmlIterator.hasNext()) {
                Element recordEle = (Element) stopXmlIterator.next();
                Stops stops = xmlToStops(recordEle.asXML(), maxPageId, username);
                if (null != stops) {
                    stops.setFlow(flow);
                    stopsList.add(stops);
                }
            }
            flow.setStopsList(stopsList);
            return flow;
        } catch (Exception e) {
            logger.error("Conversion failed", e);
            return null;
        }
    }

    /**
     * flowGroup to xml
     *
     * @param flowGroupXmlStr xml string data
     * @return FlowGroup
     */
    public static Map<String, Object> XmlStrToFlowGroup(String flowGroupXmlStr, int maxPageId, String username, String[] flowNames, boolean isChildren) {
        if (StringUtils.isBlank(flowGroupXmlStr)) {
            return ReturnMapUtils.setFailedMsg("flowGroupXmlStr is null");
        }
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsg("username is null");
        }
        try {
            Element flowGroupElement = xmlStrToElementGetByKey(flowGroupXmlStr, false, "flowGroup");
            if (null == flowGroupElement) {
                return ReturnMapUtils.setFailedMsg("No flowGroup node");
            }
            String name = StringCustomUtils.recoverSpecialSymbolsXml(flowGroupElement.attributeValue("name"));
            String description = StringCustomUtils.recoverSpecialSymbolsXml(flowGroupElement.attributeValue("description"));
            String flowGroupPageId = StringCustomUtils.recoverSpecialSymbolsXml(flowGroupElement.attributeValue("pageId"));
            flowGroupPageId = StringUtils.isNotBlank(flowGroupPageId) ? ((Integer.parseInt(flowGroupPageId) + maxPageId) + "") : "";
            FlowGroup flowGroup = new FlowGroup();
            flowGroup.setCrtDttm(new Date());
            flowGroup.setCrtUser(username);
            flowGroup.setLastUpdateDttm(new Date());
            flowGroup.setLastUpdateUser(username);
            flowGroup.setVersion(0L);
            flowGroup.setName(name);
            flowGroup.setDescription(description);
            flowGroup.setPageId(flowGroupPageId);

            // flow list
            Iterator flowXmlIterator = flowGroupElement.elementIterator("flow");
            List<Flow> flowList = new ArrayList<>();
            String duplicateFlowName = "";
            while (flowXmlIterator.hasNext()) {
                Element recordEle = (Element) flowXmlIterator.next();
                Flow flow = xmlToFlow(recordEle.asXML(), maxPageId, username, false);
                if (null != flow) {
                    String flowName = flow.getName();
                    if (Arrays.asList(flowNames).contains(flowName)) {
                        duplicateFlowName += (flowName + ",");
                    }
                    flow.setFlowGroup(flowGroup);
                    flowList.add(flow);
                }
            }
            // If there are duplicate FlowNames, directly return null
            if (StringUtils.isNotBlank(duplicateFlowName)) {
                return ReturnMapUtils.setFailedMsg("Duplicate FlowName");
            }
            flowGroup.setFlowList(flowList);

            //flowGroupPaths
            Iterator flowGroupPathsXmlIterator = flowGroupElement.elementIterator("flowGroupPaths");
            List<FlowGroupPaths> flowGroupPathsList = new ArrayList<>();
            while (flowGroupPathsXmlIterator.hasNext()) {
                Element recordEle = (Element) flowGroupPathsXmlIterator.next();
                FlowGroupPaths flowGroupPaths = xmlToFlowGroupPaths(recordEle.asXML(), maxPageId, username);
                if (null != flowGroupPaths) {
                    flowGroupPaths.setFlowGroup(flowGroup);
                    flowGroupPathsList.add(flowGroupPaths);
                }
            }
            flowGroup.setFlowGroupPathsList(flowGroupPathsList);

            //flowGroupList
            Iterator flowGroupListXmlIterator = flowGroupElement.elementIterator("flowGroup");
            List<FlowGroup> flowGroupList = new ArrayList<>();
            while (flowGroupListXmlIterator.hasNext()) {
                Element recordEle = (Element) flowGroupListXmlIterator.next();
                Map<String, Object> stringObjectMap = XmlStrToFlowGroup(recordEle.asXML(), 1, username, new String[]{}, true);
                if (200 == (Integer) stringObjectMap.get("code")) {
                    FlowGroup flowGroupXml = (FlowGroup) stringObjectMap.get("flowGroup");
                    if (null != flowGroupXml) {
                        flowGroupXml.setFlowGroup(flowGroup);
                        flowGroupList.add(flowGroupXml);
                    }
                }
            }
            flowGroup.setFlowGroupList(flowGroupList);

            // mxGraphModel
            Element mxGraphModelXml = flowGroupElement.element("mxGraphModel");
            String mxGraphModelXmlAsXML = mxGraphModelXml.asXML();
            MxGraphModel mxGraphModel = xmlToMxGraphModelNew(mxGraphModelXmlAsXML, maxPageId, username, null, flowGroup);
            if (isChildren) {
                List<MxCell> root = mxGraphModel.getRoot();
                if (null != root) {
                    root.addAll(MxCellUtils.initMxCell(username, mxGraphModel));
                }
                mxGraphModel.setRoot(root);
            }
            flowGroup.setMxGraphModel(mxGraphModel);

            return ReturnMapUtils.setSucceededCustomParam("flowGroup", flowGroup);
        } catch (Exception e) {
            logger.error("Conversion failed", e);
            return ReturnMapUtils.setFailedMsg("Conversion failed");
        }
    }

    /**
     * TemplateXml to Flow
     *
     * @param templateXml xml string data
     * @param username    Operator username
     * @return Flow
     */
    public static Map<String, Object> flowTemplateXmlToFlow(String templateXml, String username, String stopMaxPageId, String flowMaxPageId, String[] stopNames) {
        if (StringUtils.isBlank(templateXml)) {
            return ReturnMapUtils.setFailedMsg("templateXml is null");
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
            Element flowElement = xmlStrToElementGetByKey(templateXml, false, "flow");
            if (null == flowElement) {
                return ReturnMapUtils.setFailedMsg("No flow node");
            }
            String driverMemory = StringCustomUtils.recoverSpecialSymbolsXml(flowElement.attributeValue("driverMemory"));
            String executorCores = StringCustomUtils.recoverSpecialSymbolsXml(flowElement.attributeValue("executorCores"));
            String executorMemory = StringCustomUtils.recoverSpecialSymbolsXml(flowElement.attributeValue("executorMemory"));
            String executorNumber = StringCustomUtils.recoverSpecialSymbolsXml(flowElement.attributeValue("executorNumber"));
            String name = StringCustomUtils.recoverSpecialSymbolsXml(flowElement.attributeValue("name"));
            String description = StringCustomUtils.recoverSpecialSymbolsXml(flowElement.attributeValue("description"));
            String flowPageId = StringCustomUtils.recoverSpecialSymbolsXml(flowElement.attributeValue("pageId"));
            if (StringUtils.isNotBlank(flowPageId) && null != flowMaxPageIdInt) {
                flowPageId = (Integer.parseInt(flowPageId) + flowMaxPageIdInt) + "";
            }
            Flow flow = FlowUtil.setFlowBasicInformation(null, false, username);
            flow.setPageId(flowPageId);
            flow.setDriverMemory(driverMemory);
            flow.setExecutorCores(executorCores);
            flow.setExecutorMemory(executorMemory);
            flow.setExecutorNumber(executorNumber);
            flow.setName(name + System.currentTimeMillis());
            flow.setDescription(description);

            // stop
            Iterator stopXmlIterator = flowElement.elementIterator("stop");
            Map<String, Object> xmlToStopsMap = xmlToStopsList(stopXmlIterator, stopMaxPageIdInt, username, stopNames, flow);
            if (200 != (Integer) xmlToStopsMap.get(ReturnMapUtils.KEY_CODE)) {
                return xmlToStopsMap;
            }
            flow.setStopsList((List<Stops>) xmlToStopsMap.get("xmlToStopsList"));

            // paths
            Iterator pathXmlIterator = flowElement.elementIterator("paths");
            List<Paths> pathsList = new ArrayList<>();
            while (pathXmlIterator.hasNext()) {
                Element recordEle = (Element) pathXmlIterator.next();
                Paths paths = xmlToPathsOne(recordEle.asXML(), stopMaxPageIdInt, username);
                if (null != paths) {
                    paths.setFlow(flow);
                    pathsList.add(paths);
                }
            }
            flow.setPathsList(pathsList);
            // mxGraphModel
            Element mxGraphModelElement = flowElement.element("mxGraphModel");
            MxGraphModel mxGraphModel = xmlToMxGraphModelNew(mxGraphModelElement.asXML(), stopMaxPageIdInt, username, flow, null);
            flow.setMxGraphModel(mxGraphModel);
            return ReturnMapUtils.setSucceededCustomParam("flow", flow);
        } catch (Exception e) {
            logger.error("Conversion failed", e);
            return ReturnMapUtils.setFailedMsg("Conversion failed");
        }
    }

    /**
     * String type xml to "stop"
     *
     * @param xmlData xml string data
     * @return Stops
     */
    public static Stops xmlToStopsNew(String xmlData, int maxPageId, String username) {
        if (StringUtils.isBlank(xmlData)) {
            return null;
        }
        try {
            Element stopElement = xmlStrToElementGetByKey(xmlData, true, "stop");
            if (null == stopElement) {
                return null;
            }
            Stops stops = new Stops();
            String bundel = StringCustomUtils.recoverSpecialSymbolsXml(stopElement.attributeValue("bundel"));
            String description = StringCustomUtils.recoverSpecialSymbolsXml(stopElement.attributeValue("description"));
            String id = StringCustomUtils.recoverSpecialSymbolsXml(stopElement.attributeValue("id"));
            String name = StringCustomUtils.recoverSpecialSymbolsXml(stopElement.attributeValue("name"));
            String pageId = StringCustomUtils.recoverSpecialSymbolsXml(stopElement.attributeValue("pageId"));
            String inPortType = StringCustomUtils.recoverSpecialSymbolsXml(stopElement.attributeValue("inPortType"));
            String inports = StringCustomUtils.recoverSpecialSymbolsXml(stopElement.attributeValue("inports"));
            String outPortType = StringCustomUtils.recoverSpecialSymbolsXml(stopElement.attributeValue("outPortType"));
            String outports = StringCustomUtils.recoverSpecialSymbolsXml(stopElement.attributeValue("outports"));
            String isCheckpoint = StringCustomUtils.recoverSpecialSymbolsXml(stopElement.attributeValue("isCheckpoint"));
            String owner = StringCustomUtils.recoverSpecialSymbolsXml(stopElement.attributeValue("owner"));
            String groups = StringCustomUtils.recoverSpecialSymbolsXml(stopElement.attributeValue("groups"));
            stops.setCrtDttm(new Date());
            stops.setCrtUser(username);
            stops.setLastUpdateDttm(new Date());
            stops.setLastUpdateUser(username);
            stops.setPageId((Integer.parseInt(pageId) + maxPageId) + "");
            stops.setName(name);
            stops.setDescription(description);
            stops.setBundel(bundel);
            stops.setId(id);
            stops.setInports(inports);
            stops.setOutports(outports);
            stops.setOutPortType(PortType.selectGender(outPortType));
            stops.setInPortType(PortType.selectGenderByValue(inPortType));
            stops.setGroups(groups);
            Boolean Checkpoint = "0".equals(isCheckpoint);
            stops.setIsCheckpoint(Checkpoint);
            stops.setOwner(owner);
            Iterator propertyXmlIterator = stopElement.elementIterator("property");
            if (null != propertyXmlIterator) {
                List<Property> propertyList = new ArrayList<>();
                while (propertyXmlIterator.hasNext()) {
                    Element propertyValue = (Element) propertyXmlIterator.next();
                    Property property = new Property();
                    String allowableValues = StringCustomUtils.recoverSpecialSymbolsXml(propertyValue.attributeValue("allowableValues"));
                    String customValue = StringCustomUtils.recoverSpecialSymbolsXml(propertyValue.attributeValue("customValue"));
                    String propertyDescription = StringCustomUtils.recoverSpecialSymbolsXml(propertyValue.attributeValue("description"));
                    String displayName = StringCustomUtils.recoverSpecialSymbolsXml(propertyValue.attributeValue("displayName"));
                    String propertyId = StringCustomUtils.recoverSpecialSymbolsXml(propertyValue.attributeValue("id"));
                    String propertyName = StringCustomUtils.recoverSpecialSymbolsXml(propertyValue.attributeValue("name"));
                    boolean required = "true".equals(propertyValue.attributeValue("required"));
                    boolean sensitive = "true".equals(propertyValue.attributeValue("sensitive"));
                    boolean isSelect = "true".equals(propertyValue.attributeValue("isSelect"));
                    if (isSelect && null != allowableValues && allowableValues.length() > 1) {
                        String temp = allowableValues.substring(1, allowableValues.length() - 1);
                        String[] tempArray = temp.split(",");
                        StringBuilder tempStringBuffer = new StringBuilder();
                        tempStringBuffer.append("[");
                        for (int i = 0; i < tempArray.length; i++) {
                            tempStringBuffer.append("\"");
                            tempStringBuffer.append(tempArray[i]);
                            tempStringBuffer.append("\"");
                            if (i + 1 != tempArray.length) {
                                tempStringBuffer.append(",");
                            }
                        }
                        tempStringBuffer.append("]");
                        allowableValues = tempStringBuffer.toString();
                    }
                    property.setCrtDttm(new Date());
                    property.setCrtUser(username);
                    property.setLastUpdateDttm(new Date());
                    property.setLastUpdateUser(username);
                    property.setAllowableValues(allowableValues);
                    property.setCustomValue(customValue);
                    property.setDescription(propertyDescription);
                    property.setDisplayName(displayName);
                    property.setId(propertyId);
                    property.setName(propertyName);
                    property.setRequired(required);
                    property.setSensitive(sensitive);
                    property.setIsSelect(isSelect);
                    property.setStops(stops);
                    propertyList.add(property);
                }
                stops.setProperties(propertyList);
            }
            return stops;
        } catch (Exception e) {
            logger.error("Conversion failed", e);
            return null;
        }
    }

    /**
     * String type xml to MxGraphModel
     *
     * @param xmlData xml string data
     * @return MxGraphModel
     */
    public static MxGraphModel xmlToMxGraphModelNew(String xmlData, int maxPageId, String username, Flow flow, FlowGroup flowGroup) {
        if (StringUtils.isBlank(xmlData)) {
            return null;
        }
        try {
            Element mxGraphModelXml = xmlStrToElementGetByKey(xmlData, false, "mxGraphModel");
            if (null == mxGraphModelXml) {
                return null;
            }
            String dx = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("dx"));
            String dy = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("dy"));
            String grid = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("grid"));
            String gridSize = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("gridSize"));
            String guides = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("guides"));
            String tooltips = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("tooltips"));
            String connect = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("connect"));
            String arrows = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("arrows"));
            String fold = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("fold"));
            String page = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("page"));
            String pageScale = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("pageScale"));
            String pageWidth = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("pageWidth"));
            String pageHeight = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("pageHeight"));
            String background = StringCustomUtils.recoverSpecialSymbolsXml(mxGraphModelXml.attributeValue("background"));

            MxGraphModel mxGraphModel = new MxGraphModel();
            mxGraphModel.setCrtDttm(new Date());
            mxGraphModel.setCrtUser(username);
            mxGraphModel.setLastUpdateDttm(new Date());
            mxGraphModel.setLastUpdateUser(username);
            mxGraphModel.setVersion(0L);
            mxGraphModel.setDx(dx);
            mxGraphModel.setDy(dy);
            mxGraphModel.setGrid(grid);
            mxGraphModel.setGridSize(gridSize);
            mxGraphModel.setGuides(guides);
            mxGraphModel.setTooltips(tooltips);
            mxGraphModel.setConnect(connect);
            mxGraphModel.setArrows(arrows);
            mxGraphModel.setFold(fold);
            mxGraphModel.setPage(page);
            mxGraphModel.setPageScale(pageScale);
            mxGraphModel.setPageWidth(pageWidth);
            mxGraphModel.setPageHeight(pageHeight);
            mxGraphModel.setBackground(background);

            //Take out all Stop(Flow) Name and PageId
            Map<String, String> stopOrFlowNamesMap = new HashMap<>();
            if (null != flow && null != flow.getStopsList()) {
                List<Stops> stopsList = flow.getStopsList();
                //Loop take out all Stop Name and PageId
                for (Stops stops : stopsList) {
                    if (null != stops) {
                        stopOrFlowNamesMap.put(stops.getPageId(), stops.getName());
                    }
                }
            } else if (null != flowGroup && null != flowGroup.getFlowList()) {
                List<Flow> flowList = flowGroup.getFlowList();
                //Loop take out all Stop(Flow) Name and PageId
                for (Flow flowOne : flowList) {
                    if (null != flowOne) {
                        stopOrFlowNamesMap.put(flowOne.getPageId(), flowOne.getName());
                    }
                }
            }
            List<MxCell> rootList = new ArrayList<>();
            Element rootjd = mxGraphModelXml.element("root");
            Iterator rootiter = rootjd.elementIterator("mxCell"); // Get the child node "mxCell" under the root node
            while (rootiter.hasNext()) {
                Element recordEle = (Element) rootiter.next();
                MxCell mxCell = xmlToMxCellNew(recordEle.asXML(), maxPageId, username, mxGraphModel);
                if (null != mxCell) {
                    String mxCellValue = stopOrFlowNamesMap.get(mxCell.getPageId());
                    // Canvas composition name synchronized with Stop
                    if (StringUtils.isNotBlank(mxCellValue)) {
                        mxCell.setValue(mxCellValue);
                    }
                    rootList.add(mxCell);
                }
            }
            mxGraphModel.setRoot(rootList);
            mxGraphModel.setFlow(flow);
            mxGraphModel.setFlowGroup(flowGroup);
            return mxGraphModel;
        } catch (Exception e) {
            logger.error("Conversion failed", e);
            return null;
        }
    }

    /**
     * XML to MxCell
     *
     * @param xmlData      xml string data
     * @param maxPageId    Maximum PageId of MxCell
     * @param username     Operator username
     * @param mxGraphModel link MxGraphModel
     * @return MxCell
     */
    public static MxCell xmlToMxCellNew(String xmlData, int maxPageId, String username, MxGraphModel mxGraphModel) {
        if (StringUtils.isBlank(xmlData)) {
            return null;
        }
        Element recordEle = xmlStrToElementGetByKey(xmlData, false, "mxCell");
        if (null == recordEle) {
            return null;
        }
        MxCell mxCell = new MxCell();
        String pageId = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("id"));
        String parent = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("parent"));
        String style = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("style"));
        String edge = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("edge"));
        String source = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("source"));
        String target = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("target"));
        String value = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("value"));
        String vertex = StringCustomUtils.recoverSpecialSymbolsXml(recordEle.attributeValue("vertex"));
        if (maxPageId >= 1) {
            if (Integer.parseInt(pageId) < 2) {
                return null;
            }
        }
        if ("1".equals(edge)) {
            if (StringUtils.isBlank(source) || StringUtils.isBlank(target)) {
                return null;
            }
        }
        mxCell.setCrtDttm(new Date());
        mxCell.setCrtUser(username);
        mxCell.setLastUpdateDttm(new Date());
        mxCell.setLastUpdateUser(username);
        mxCell.setVersion(0L);
        mxCell.setPageId((Integer.parseInt(pageId) + maxPageId) + "");
        mxCell.setParent(parent);
        mxCell.setStyle(style);
        mxCell.setEdge(edge);
        if (StringUtils.isNotBlank(source) && StringUtils.isNotBlank(target)) {
            mxCell.setSource((Integer.parseInt(source) + maxPageId) + "");
            mxCell.setTarget((Integer.parseInt(target) + maxPageId) + "");
        }
        mxCell.setValue(value);
        mxCell.setVertex(vertex);
        if (StringUtils.isNotBlank(style)) {
            MxGeometry mxGeometry = new MxGeometry();
            Element mxGeometryXml = recordEle.element("mxGeometry");
            String relative = StringCustomUtils.recoverSpecialSymbolsXml(mxGeometryXml.attributeValue("relative"));
            String as = StringCustomUtils.recoverSpecialSymbolsXml(mxGeometryXml.attributeValue("as"));
            String x = StringCustomUtils.recoverSpecialSymbolsXml(mxGeometryXml.attributeValue("x"));
            String y = StringCustomUtils.recoverSpecialSymbolsXml(mxGeometryXml.attributeValue("y"));
            String width = StringCustomUtils.recoverSpecialSymbolsXml(mxGeometryXml.attributeValue("width"));
            String height = StringCustomUtils.recoverSpecialSymbolsXml(mxGeometryXml.attributeValue("height"));
            mxGeometry.setCrtDttm(new Date());
            mxGeometry.setCrtUser(username);
            mxGeometry.setLastUpdateDttm(new Date());
            mxGeometry.setLastUpdateUser(username);
            mxGeometry.setVersion(0L);
            mxGeometry.setRelative(relative);
            mxGeometry.setAs(as);
            mxGeometry.setX(x);
            mxGeometry.setY(y);
            mxGeometry.setWidth(width);
            mxGeometry.setHeight(height);
            mxGeometry.setMxCell(mxCell);
            mxCell.setMxGeometry(mxGeometry);
        }
        mxCell.setMxGraphModel(mxGraphModel);
        return mxCell;
    }

    public static Map<String, Object> xmlToStopsList(Iterator stopXmlIterator, int stopMaxPageIdInt, String username, String[] stopNames, Flow flow) {
        List<Stops> stopsList = new ArrayList<>();
        String duplicateStopName = null;
        while (stopXmlIterator.hasNext()) {
            Element recordEle = (Element) stopXmlIterator.next();
            Stops stops = xmlToStopsNew(recordEle.asXML(), stopMaxPageIdInt, username);
            if (null != stops) {
                String stopName = stops.getName();

                if (Arrays.asList(stopNames).contains(stopName)) {
                    duplicateStopName += (stopName + ",");
                }
                stops.setFlow(flow);
                stopsList.add(stops);
            }
        }
        // If there are duplicate StopNames, directly return null
        if (StringUtils.isNotBlank(duplicateStopName)) {
            return ReturnMapUtils.setFailedMsg("Duplicate StopName");
        }
        return ReturnMapUtils.setSucceededCustomParam("xmlToStopsList", stopsList);
    }

}

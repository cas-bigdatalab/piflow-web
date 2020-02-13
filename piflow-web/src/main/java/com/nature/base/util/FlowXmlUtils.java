package com.nature.base.util;

import com.nature.common.Eunm.PortType;
import com.nature.component.flow.model.*;
import com.nature.component.flow.utils.FlowUtil;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGeometry;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.mxGraph.vo.MxCellVo;
import com.nature.component.mxGraph.vo.MxGeometryVo;
import com.nature.component.mxGraph.vo.MxGraphModelVo;
import com.nature.component.template.model.PropertyTemplateModel;
import com.nature.component.template.model.StopTemplateModel;
import com.nature.component.template.model.Template;
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
            String dx = mxGraphModelVo.getDx();
            String dy = mxGraphModelVo.getDy();
            String grid = mxGraphModelVo.getGrid();
            String gridSize = mxGraphModelVo.getGridSize();
            String guides = mxGraphModelVo.getGuides();
            String tooltips = mxGraphModelVo.getTooltips();
            String connect = mxGraphModelVo.getConnect();
            String arrows = mxGraphModelVo.getArrows();
            String fold = mxGraphModelVo.getFold();
            String page = mxGraphModelVo.getPage();
            String pageScale = mxGraphModelVo.getPageScale();
            String pageWidth = mxGraphModelVo.getPageWidth();
            String pageHeight = mxGraphModelVo.getPageHeight();
            String background = mxGraphModelVo.getBackground();
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
                    String id = mxCellVo.getPageId();
                    String parent = mxCellVo.getParent();
                    String style = mxCellVo.getStyle();
                    String value = mxCellVo.getValue();
                    String vertex = mxCellVo.getVertex();
                    String edge = mxCellVo.getEdge();
                    String source = mxCellVo.getSource();
                    String target = mxCellVo.getTarget();
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
                        String relative = mxGeometryVo.getRelative();
                        String as = mxGeometryVo.getAs();
                        String x = mxGeometryVo.getX();
                        String y = mxGeometryVo.getY();
                        String width = mxGeometryVo.getWidth();
                        String height = mxGeometryVo.getHeight();
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
            String dx = mxGraphModel.getDx();
            String dy = mxGraphModel.getDy();
            String grid = mxGraphModel.getGrid();
            String gridSize = mxGraphModel.getGridSize();
            String guides = mxGraphModel.getGuides();
            String tooltips = mxGraphModel.getTooltips();
            String connect = mxGraphModel.getConnect();
            String arrows = mxGraphModel.getArrows();
            String fold = mxGraphModel.getFold();
            String page = mxGraphModel.getPage();
            String pageScale = mxGraphModel.getPageScale();
            String pageWidth = mxGraphModel.getPageWidth();
            String pageHeight = mxGraphModel.getPageHeight();
            String background = mxGraphModel.getBackground();
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
                    String id = mxCell.getPageId();
                    String parent = mxCell.getParent();
                    String style = mxCell.getStyle();
                    String value = mxCell.getValue();
                    String vertex = mxCell.getVertex();
                    String edge = mxCell.getEdge();
                    String source = mxCell.getSource();
                    String target = mxCell.getTarget();
                    MxGeometry mxGeometry = mxCell.getMxGeometry();
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
                    if (null != mxGeometry) {
                        String relative = mxGeometry.getRelative();
                        String as = mxGeometry.getAs();
                        String x = mxGeometry.getX();
                        String y = mxGeometry.getY();
                        String width = mxGeometry.getWidth();
                        String height = mxGeometry.getHeight();
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
            String id = flow.getId();
            String name = flow.getName();
            String description = flow.getDescription();
            String driverMemory = flow.getDriverMemory();
            String executorCores = flow.getExecutorCores();
            String executorMemory = flow.getExecutorMemory();
            String executorNumber = flow.getExecutorNumber();
            String flowPageId = flow.getPageId();
            xmlStrSb.append("<flow ");
            if (StringUtils.isNotBlank(id)) {
                //xmlStrSb.append(spliceStr("id", StringEscapeUtils.escapeHtml(id)));
                xmlStrSb.append(spliceStr("id", id));
            }
            if (StringUtils.isNotBlank(name)) {
                //xmlStrSb.append(spliceStr("name", StringEscapeUtils.escapeHtml(name)));
                xmlStrSb.append(spliceStr("name", name));
            }
            if (StringUtils.isNotBlank(description)) {
                //xmlStrSb.append(spliceStr("description", StringEscapeUtils.escapeHtml(description)));
                xmlStrSb.append(spliceStr("description", description));
            }
            if (StringUtils.isNotBlank(driverMemory)) {
                //xmlStrSb.append(spliceStr("driverMemory", StringEscapeUtils.escapeHtml(driverMemory)));
                xmlStrSb.append(spliceStr("driverMemory", driverMemory));
            }
            if (StringUtils.isNotBlank(executorCores)) {
                //xmlStrSb.append(spliceStr("executorCores", StringEscapeUtils.escapeHtml(executorCores)));
                xmlStrSb.append(spliceStr("executorCores", executorCores));
            }
            if (StringUtils.isNotBlank(executorMemory)) {
                //xmlStrSb.append(spliceStr("executorMemory", StringEscapeUtils.escapeHtml(executorMemory)));
                xmlStrSb.append(spliceStr("executorMemory", executorMemory));
            }
            if (StringUtils.isNotBlank(executorNumber)) {
                //xmlStrSb.append(spliceStr("executorNumber", StringEscapeUtils.escapeHtml(executorNumber)));
                xmlStrSb.append(spliceStr("executorNumber", executorNumber));
            }
            if (StringUtils.isNotBlank(flowPageId)) {
                //xmlStrSb.append(spliceStr("pageId", StringEscapeUtils.escapeHtml(flowPageId)));
                xmlStrSb.append(spliceStr("pageId", flowPageId));
            }
            xmlStrSb.append("> \n");
            List<Stops> stopsVoList = flow.getStopsList();
            List<Paths> pathsList = flow.getPathsList();
            if (null != stopsVoList && stopsVoList.size() > 0) {
                for (Stops stopVo : stopsVoList) {
                    String stopId = stopVo.getId();
                    String pageId = stopVo.getPageId();
                    String stopName = stopVo.getName();
                    String bundel = stopVo.getBundel();
                    String stopDescription = stopVo.getDescription();
                    Boolean checkpoint = stopVo.getIsCheckpoint();
                    String inports = stopVo.getInports();
                    PortType inPortType = stopVo.getInPortType();
                    String outports = stopVo.getOutports();
                    PortType outPortType = stopVo.getOutPortType();
                    String owner = stopVo.getOwner();
                    String groups = stopVo.getGroups();
                    String crtUser = stopVo.getCrtUser();
                    xmlStrSb.append("<stop ");
                    if (StringUtils.isNotBlank(stopId)) {
                        //xmlStrSb.append(spliceStr("id", StringEscapeUtils.escapeHtml(stopId)));
                        xmlStrSb.append(spliceStr("id", stopId));
                    }
                    if (StringUtils.isNotBlank(pageId)) {
                        //xmlStrSb.append(spliceStr("pageId", StringEscapeUtils.escapeHtml(pageId)));
                        xmlStrSb.append(spliceStr("pageId", pageId));
                    }
                    if (StringUtils.isNotBlank(stopName)) {
                        //xmlStrSb.append(spliceStr("name", StringEscapeUtils.escapeHtml(stopName)));
                        xmlStrSb.append(spliceStr("name", stopName));
                    }
                    if (StringUtils.isNotBlank(bundel)) {
                        //xmlStrSb.append(spliceStr("bundel", StringEscapeUtils.escapeHtml(bundel)));
                        xmlStrSb.append(spliceStr("bundel", bundel));
                    }
                    if (StringUtils.isNotBlank(stopDescription)) {
                        //stopDescription = stopDescription.replace("&", "&amp;");
                        //xmlStrSb.append(spliceStr("description", StringEscapeUtils.escapeHtml(stopDescription)));
                        xmlStrSb.append(spliceStr("description", stopDescription));
                    }
                    if (null != checkpoint) {
                        xmlStrSb.append(spliceStr("isCheckpoint", (checkpoint ? 1 : 0)));
                    }
                    if (StringUtils.isNotBlank(inports)) {
                        //xmlStrSb.append(spliceStr("inports", StringEscapeUtils.escapeHtml(inports)));
                        xmlStrSb.append(spliceStr("inports", inports));
                    }

                    if (StringUtils.isNotBlank(outports)) {
                        //xmlStrSb.append(spliceStr("outports", StringEscapeUtils.escapeHtml(outports)));
                        xmlStrSb.append(spliceStr("outports", outports));
                    }
                    if (StringUtils.isNotBlank(owner)) {
                        //xmlStrSb.append(spliceStr("owner", StringEscapeUtils.escapeHtml(owner)));
                        xmlStrSb.append(spliceStr("owner", owner));
                    }
                    if (null != inPortType) {
                        xmlStrSb.append(spliceStr("inPortType", inPortType));
                    }
                    if (null != outPortType) {
                        xmlStrSb.append(spliceStr("outPortType", outPortType));
                    }
                    if (StringUtils.isNotBlank(groups)) {
                        //xmlStrSb.append(spliceStr("groups", StringEscapeUtils.escapeHtml(groups)));
                        xmlStrSb.append(spliceStr("groups", groups));
                    }
                    if (StringUtils.isNotBlank(crtUser)) {
                        //xmlStrSb.append(spliceStr("crtUser", StringEscapeUtils.escapeHtml(crtUser)));
                        xmlStrSb.append(spliceStr("crtUser", crtUser));
                    }
                    List<Property> property = stopVo.getProperties();
                    if (null != property && property.size() > 0) {
                        xmlStrSb.append("> \n");
                        for (Property propertyVo : property) {
                            xmlStrSb.append("<property ");
                            String propertyId = propertyVo.getId();
                            String displayName = propertyVo.getDisplayName();
                            String propertyName = propertyVo.getName();
                            String customValue = propertyVo.getCustomValue();
                            String propertyDescription = propertyVo.getDescription();
                            String allowableValues = propertyVo.getAllowableValues();
                            boolean required = propertyVo.getRequired();
                            boolean sensitive = propertyVo.getSensitive();
                            boolean isSelect = propertyVo.getIsSelect();
                            String propertyVocrtUser = propertyVo.getCrtUser();
                            if (StringUtils.isNotBlank(propertyId)) {
                                //xmlStrSb.append(spliceStr("id", StringEscapeUtils.escapeHtml(id)));
                                xmlStrSb.append(spliceStr("id", id));
                            }
                            if (StringUtils.isNotBlank(displayName)) {
                                //xmlStrSb.append(spliceStr("displayName", StringEscapeUtils.escapeHtml(displayName)));
                                xmlStrSb.append(spliceStr("displayName", displayName));
                            }
                            if (StringUtils.isNotBlank(propertyName)) {
                                //xmlStrSb.append(spliceStr("name", StringEscapeUtils.escapeHtml(propertyName)));
                                xmlStrSb.append(spliceStr("name", propertyName));
                            }
                            if (StringUtils.isNotBlank(propertyDescription)) {
                                //xmlStrSb.append(spliceStr("description", StringEscapeUtils.escapeHtml(propertyDescription)));
                                xmlStrSb.append(spliceStr("description", propertyDescription));
                            }
                            if (StringUtils.isNotBlank(allowableValues)) {
                                xmlStrSb.append(spliceStr("allowableValues", allowableValues.replaceAll("\"", "")));
                            }
                            if (StringUtils.isNotBlank(customValue)) {
                                //xmlStrSb.append(spliceStr("customValue", StringEscapeUtils.escapeHtml(customValue)));
                                xmlStrSb.append(spliceStr("customValue", customValue));
                            }
                            if (StringUtils.isNotBlank(propertyVocrtUser)) {
                                //xmlStrSb.append(spliceStr("crtUser", StringEscapeUtils.escapeHtml(propertyVocrtUser)));
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
                if (null != pathsList && pathsList.size() > 0) {
                    for (Paths paths : pathsList) {
                        xmlStrSb.append("<paths ");
                        String crtUser = paths.getCrtUser();
                        String from = paths.getFrom();
                        String to = paths.getTo();
                        String inport = paths.getInport();
                        String outport = paths.getOutport();
                        String pageId = paths.getPageId();
                        String filterCondition = paths.getFilterCondition();
                        if (StringUtils.isNotBlank(crtUser)) {
                            //xmlStrSb.append(spliceStr("crtUser", StringEscapeUtils.escapeHtml(crtUser)));
                            xmlStrSb.append(spliceStr("crtUser", crtUser));
                        }
                        if (StringUtils.isNotBlank(from)) {
                            //xmlStrSb.append(spliceStr("from", StringEscapeUtils.escapeHtml(from)));
                            xmlStrSb.append(spliceStr("from", from));
                        }
                        if (StringUtils.isNotBlank(to)) {
                            //xmlStrSb.append(spliceStr("to", StringEscapeUtils.escapeHtml(to)));
                            xmlStrSb.append(spliceStr("to", to));
                        }
                        if (StringUtils.isNotBlank(inport)) {
                            //xmlStrSb.append(spliceStr("inport", StringEscapeUtils.escapeHtml(inport)));
                            xmlStrSb.append(spliceStr("inport", inport));
                        }
                        if (StringUtils.isNotBlank(outport)) {
                            //xmlStrSb.append(spliceStr("outport", StringEscapeUtils.escapeHtml(outport)));
                            xmlStrSb.append(spliceStr("outport", outport));
                        }
                        if (StringUtils.isNotBlank(pageId)) {
                            //xmlStrSb.append(spliceStr("pageId", StringEscapeUtils.escapeHtml(pageId)));
                            xmlStrSb.append(spliceStr("pageId", pageId));
                        }
                        if (StringUtils.isNotBlank(filterCondition)) {
                            //xmlStrSb.append(spliceStr("filterCondition", StringEscapeUtils.escapeHtml(filterCondition)));
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
                String from = flowGroupPaths.getFrom();
                String to = flowGroupPaths.getTo();
                String inport = flowGroupPaths.getInport();
                String outport = flowGroupPaths.getOutport();
                String pageId = flowGroupPaths.getPageId();
                if (StringUtils.isNotBlank(from)) {
                    //xmlStrBuf.append(spliceStr("from", StringEscapeUtils.escapeHtml(from)));
                    xmlStrBuf.append(spliceStr("from", from));
                }
                if (StringUtils.isNotBlank(to)) {
                    //xmlStrBuf.append(spliceStr("to", StringEscapeUtils.escapeHtml(to)));
                    xmlStrBuf.append(spliceStr("to", to));
                }
                if (StringUtils.isNotBlank(inport)) {
                    //xmlStrBuf.append(spliceStr("inport", StringEscapeUtils.escapeHtml(inport)));
                    xmlStrBuf.append(spliceStr("inport", inport));
                }
                if (StringUtils.isNotBlank(outport)) {
                    //xmlStrBuf.append(spliceStr("outport", StringEscapeUtils.escapeHtml(outport)));
                    xmlStrBuf.append(spliceStr("outport", outport));
                }
                if (StringUtils.isNotBlank(pageId)) {
                    //xmlStrBuf.append(spliceStr("pageId", StringEscapeUtils.escapeHtml(pageId)));
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
            String id = flowGroup.getId();
            String name = flowGroup.getName();
            String description = flowGroup.getDescription();
            String flowGroupPageId = flowGroup.getPageId();
            if (StringUtils.isNotBlank(id)) {
                //xmlStrBuf.append(spliceStr("id", StringEscapeUtils.escapeHtml(id)));
                xmlStrBuf.append(spliceStr("id", id));
            }
            if (StringUtils.isNotBlank(name)) {
                //xmlStrBuf.append(spliceStr("name", StringEscapeUtils.escapeHtml(name)));
                xmlStrBuf.append(spliceStr("name", name));
            }
            if (StringUtils.isNotBlank(description)) {
                //xmlStrBuf.append(spliceStr("description", StringEscapeUtils.escapeHtml(description)));
                xmlStrBuf.append(spliceStr("description", description));
            }
            if (StringUtils.isNotBlank(flowGroupPageId)) {
                //xmlStrBuf.append(spliceStr("pageId", StringEscapeUtils.escapeHtml(flowGroupPageId)));
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
            xmlStrBuf.append("</flowGroup> \n");
        }
        return xmlStrBuf.toString();
    }

    private static String spliceStr(String key, Object value) {
        return key + "=\"" + value + "\" ";
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
            String dx = mxGraphModelXml.attributeValue("dx");
            String dy = mxGraphModelXml.attributeValue("dy");
            String grid = mxGraphModelXml.attributeValue("grid");
            String gridSize = mxGraphModelXml.attributeValue("gridSize");
            String guides = mxGraphModelXml.attributeValue("guides");
            String tooltips = mxGraphModelXml.attributeValue("tooltips");
            String connect = mxGraphModelXml.attributeValue("connect");
            String arrows = mxGraphModelXml.attributeValue("arrows");
            String fold = mxGraphModelXml.attributeValue("fold");
            String page = mxGraphModelXml.attributeValue("page");
            String pageScale = mxGraphModelXml.attributeValue("pageScale");
            String pageWidth = mxGraphModelXml.attributeValue("pageWidth");
            String pageHeight = mxGraphModelXml.attributeValue("pageHeight");
            String background = mxGraphModelXml.attributeValue("background");

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
                String mxCellId = recordEle.attributeValue("id");
                String parent = recordEle.attributeValue("parent");
                String style = recordEle.attributeValue("style");
                String edge = recordEle.attributeValue("edge");
                String source = recordEle.attributeValue("source");
                String target = recordEle.attributeValue("target");
                String value = recordEle.attributeValue("value");
                String vertex = recordEle.attributeValue("vertex");
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
                    String relative = mxGeometry.attributeValue("relative");
                    String as = mxGeometry.attributeValue("as");
                    String x = mxGeometry.attributeValue("x");
                    String y = mxGeometry.attributeValue("y");
                    String width = mxGeometry.attributeValue("width");
                    String height = mxGeometry.attributeValue("height");
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
     * String type xml to "stop" and other information
     *
     * @param xmlData xml string data
     * @return Template
     */
    public static Template xmlToFlowStopInfo(String xmlData) {
        try {
            Element flow = xmlStrToElementGetByKey(xmlData, false, "flow");
            if (null == flow) {
                return null;
            }
            Template template = new Template();
            List<StopTemplateModel> stopVoList = new ArrayList<>();
            Iterator rootiter = flow.elementIterator("stop"); // Get the child node "stop" under the root node
            while (rootiter.hasNext()) {
                List<PropertyTemplateModel> propertyList = new ArrayList<>();
                StopTemplateModel stopVo = new StopTemplateModel();
                Element recordEle = (Element) rootiter.next();
                String bundel = recordEle.attributeValue("bundel");
                String description = recordEle.attributeValue("description");
                String id = recordEle.attributeValue("id");
                String name = recordEle.attributeValue("name");
                String pageId = recordEle.attributeValue("pageId");
                String inPortType = recordEle.attributeValue("inPortType");
                String inports = recordEle.attributeValue("inports");
                String outPortType = recordEle.attributeValue("outPortType");
                String outports = recordEle.attributeValue("outports");
                String isCheckpoint = recordEle.attributeValue("isCheckpoint");
                String owner = recordEle.attributeValue("owner");
                String groups = recordEle.attributeValue("groups");
                stopVo.setPageId(pageId);
                stopVo.setName(name);
                stopVo.setDescription(description);
                stopVo.setBundel(bundel);
                stopVo.setId(id);
                stopVo.setInports(inports);
                stopVo.setOutports(outports);
                stopVo.setOutPortType(PortType.selectGender(outPortType));
                stopVo.setInPortType(PortType.selectGenderByValue(inPortType));
                stopVo.setGroups(groups);
                Boolean Checkpoint = "0".equals(isCheckpoint);
                stopVo.setIsCheckpoint(Checkpoint);
                stopVo.setOwner(owner);
                Iterator property = recordEle.elementIterator("property");
                if (null != property) {
                    while (property.hasNext()) {
                        Element propertyValue = (Element) property.next();
                        PropertyTemplateModel propertyVo = new PropertyTemplateModel();
                        String allowableValues = propertyValue.attributeValue("allowableValues");
                        String customValue = propertyValue.attributeValue("customValue");
                        String propertyDescription = propertyValue.attributeValue("description");
                        String displayName = propertyValue.attributeValue("displayName");
                        String propertyId = propertyValue.attributeValue("id");
                        String propertyName = propertyValue.attributeValue("name");
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
                        propertyVo.setAllowableValues(allowableValues);
                        propertyVo.setCustomValue(customValue);
                        propertyVo.setDescription(propertyDescription);
                        propertyVo.setDisplayName(displayName);
                        propertyVo.setId(propertyId);
                        propertyVo.setName(propertyName);
                        propertyVo.setRequired(required);
                        propertyVo.setSensitive(sensitive);
                        propertyVo.setStopsVo(stopVo);
                        propertyVo.setIsSelect(isSelect);
                        propertyList.add(propertyVo);
                    }
                }
                stopVo.setProperties(propertyList);
                stopVoList.add(stopVo);
            }
            template.setStopsList(stopVoList);
            return template;
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
            String dx = mxGraphModel.attributeValue("dx");
            String dy = mxGraphModel.attributeValue("dy");
            String grid = mxGraphModel.attributeValue("grid");
            String gridSize = mxGraphModel.attributeValue("gridSize");
            String guides = mxGraphModel.attributeValue("guides");
            String tooltips = mxGraphModel.attributeValue("tooltips");
            String connect = mxGraphModel.attributeValue("connect");
            String arrows = mxGraphModel.attributeValue("arrows");
            String fold = mxGraphModel.attributeValue("fold");
            String page = mxGraphModel.attributeValue("page");
            String pageScale = mxGraphModel.attributeValue("pageScale");
            String pageWidth = mxGraphModel.attributeValue("pageWidth");
            String pageHeight = mxGraphModel.attributeValue("pageHeight");
            String background = mxGraphModel.attributeValue("background");

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
                String mxCellId = recordEle.attributeValue("id");
                String parent = recordEle.attributeValue("parent");
                String style = recordEle.attributeValue("style");
                String edge = recordEle.attributeValue("edge");
                String source = recordEle.attributeValue("source");
                String target = recordEle.attributeValue("target");
                String value = recordEle.attributeValue("value");
                String vertex = recordEle.attributeValue("vertex");
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
                    String relative = mxGeometry.attributeValue("relative");
                    String as = mxGeometry.attributeValue("as");
                    String x = mxGeometry.attributeValue("x");
                    String y = mxGeometry.attributeValue("y");
                    String width = mxGeometry.attributeValue("width");
                    String height = mxGeometry.attributeValue("height");
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
    public static MxGraphModel xmlToMxGraphModel(String xmlData, int maxPageId, String username) {
        if (StringUtils.isBlank(xmlData)) {
            return null;
        }
        try {
            Element rootElt = xmlStrToElement(xmlData, false);
            if (null == rootElt) {
                return null;
            }
            Element mxGraphModelXml = rootElt.element("mxGraphModel");
            String dx = mxGraphModelXml.attributeValue("dx");
            String dy = mxGraphModelXml.attributeValue("dy");
            String grid = mxGraphModelXml.attributeValue("grid");
            String gridSize = mxGraphModelXml.attributeValue("gridSize");
            String guides = mxGraphModelXml.attributeValue("guides");
            String tooltips = mxGraphModelXml.attributeValue("tooltips");
            String connect = mxGraphModelXml.attributeValue("connect");
            String arrows = mxGraphModelXml.attributeValue("arrows");
            String fold = mxGraphModelXml.attributeValue("fold");
            String page = mxGraphModelXml.attributeValue("page");
            String pageScale = mxGraphModelXml.attributeValue("pageScale");
            String pageWidth = mxGraphModelXml.attributeValue("pageWidth");
            String pageHeight = mxGraphModelXml.attributeValue("pageHeight");
            String background = mxGraphModelXml.attributeValue("background");

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
            Element rootjd = mxGraphModelXml.element("root");
            Iterator rootiter = rootjd.elementIterator("mxCell"); // Get the child node "mxCell" under the root node
            while (rootiter.hasNext()) {
                MxCell mxCell = new MxCell();

                Element recordEle = (Element) rootiter.next();
                String mxCellId = recordEle.attributeValue("id");
                String parent = recordEle.attributeValue("parent");
                String style = recordEle.attributeValue("style");
                String edge = recordEle.attributeValue("edge");
                String source = recordEle.attributeValue("source");
                String target = recordEle.attributeValue("target");
                String value = recordEle.attributeValue("value");
                String vertex = recordEle.attributeValue("vertex");
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
                mxCell.setCrtDttm(new Date());
                mxCell.setCrtUser(username);
                mxCell.setLastUpdateDttm(new Date());
                mxCell.setLastUpdateUser(username);
                mxCell.setVersion(0L);
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
                    String relative = mxGeometryXml.attributeValue("relative");
                    String as = mxGeometryXml.attributeValue("as");
                    String x = mxGeometryXml.attributeValue("x");
                    String y = mxGeometryXml.attributeValue("y");
                    String width = mxGeometryXml.attributeValue("width");
                    String height = mxGeometryXml.attributeValue("height");
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
        String from = flowGroupPathsElement.attributeValue("from");
        String to = flowGroupPathsElement.attributeValue("to");
        String outPort = flowGroupPathsElement.attributeValue("outport");
        String inPort = flowGroupPathsElement.attributeValue("inport");
        String pageId = flowGroupPathsElement.attributeValue("pageId");
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
            String from = pathsElement.attributeValue("from");
            String to = pathsElement.attributeValue("to");
            String outport = pathsElement.attributeValue("outport");
            String inport = pathsElement.attributeValue("inport");
            String pageId = pathsElement.attributeValue("pageId");
            String filterCondition = pathsElement.attributeValue("filterCondition");
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
            String bundel = stopElement.attributeValue("bundel");
            String description = stopElement.attributeValue("description");
            String id = stopElement.attributeValue("id");
            String name = stopElement.attributeValue("name");
            String pageId = stopElement.attributeValue("pageId");
            String inPortType = stopElement.attributeValue("inPortType");
            String inports = stopElement.attributeValue("inports");
            String outPortType = stopElement.attributeValue("outPortType");
            String outports = stopElement.attributeValue("outports");
            String isCheckpoint = stopElement.attributeValue("isCheckpoint");
            String owner = stopElement.attributeValue("owner");
            String groups = stopElement.attributeValue("groups");
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
                    String allowableValues = propertyValue.attributeValue("allowableValues");
                    String customValue = propertyValue.attributeValue("customValue");
                    String propertyDescription = propertyValue.attributeValue("description");
                    String displayName = propertyValue.attributeValue("displayName");
                    String propertyId = propertyValue.attributeValue("id");
                    String propertyName = propertyValue.attributeValue("name");
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
    public static Flow xmlToFlow(String xmlData, int maxPageId, String username) {
        if (StringUtils.isBlank(xmlData)) {
            return null;
        }
        try {
            Element flowElement = xmlStrToElementGetByKey(xmlData, false, "flow");
            if (null == flowElement) {
                return null;
            }
            String flowPageId = flowElement.attributeValue("pageId");
            String driverMemory = flowElement.attributeValue("driverMemory");
            String executorCores = flowElement.attributeValue("executorCores");
            String executorMemory = flowElement.attributeValue("executorMemory");
            String executorNumber = flowElement.attributeValue("executorNumber");
            String name = flowElement.attributeValue("name");
            String description = flowElement.attributeValue("description");

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
            MxGraphModel mxGraphModel = xmlToMxGraphModel(mxGraphModelElement.asXML(), maxPageId, username);
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
    public static Map<String, Object> XmlStrToFlowGroup(String flowGroupXmlStr, int maxPageId, String username, String[] flowNames) {
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
            String name = flowGroupElement.attributeValue("name");
            String description = flowGroupElement.attributeValue("description");

            FlowGroup flowGroup = new FlowGroup();
            flowGroup.setCrtDttm(new Date());
            flowGroup.setCrtUser(username);
            flowGroup.setLastUpdateDttm(new Date());
            flowGroup.setLastUpdateUser(username);
            flowGroup.setVersion(0L);
            flowGroup.setName(name);
            flowGroup.setDescription(description);

            // flow list
            Iterator flowXmlIterator = flowGroupElement.elementIterator("flow");
            List<Flow> flowList = new ArrayList<>();
            String duplicateFlowName = "";
            while (flowXmlIterator.hasNext()) {
                Element recordEle = (Element) flowXmlIterator.next();
                Flow flow = xmlToFlow(recordEle.asXML(), maxPageId, username);
                if (null != flow) {
                    String flowName = flow.getName();
                    if (Arrays.asList(flowNames).contains(flowName)) {
                        duplicateFlowName += (flowName + ",");
                    }
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
                    flowGroupPathsList.add(flowGroupPaths);
                }
            }
            flowGroup.setFlowGroupPathsList(flowGroupPathsList);

            // mxGraphModel
            Element mxGraphModelXml = flowGroupElement.element("mxGraphModel");
            String mxGraphModelXmlAsXML = mxGraphModelXml.asXML();
            MxGraphModel mxGraphModel = xmlToMxGraphModelNew(mxGraphModelXmlAsXML, maxPageId, username, null, flowGroup);
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
    public static Map<String, Object> templateXmlToFlow(String templateXml, String username, String stopMaxPageId, String flowMaxPageId, String[] stopNames) {
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
            String driverMemory = flowElement.attributeValue("driverMemory");
            String executorCores = flowElement.attributeValue("executorCores");
            String executorMemory = flowElement.attributeValue("executorMemory");
            String executorNumber = flowElement.attributeValue("executorNumber");
            String name = flowElement.attributeValue("name");
            String description = flowElement.attributeValue("description");
            String flowPageId = flowElement.attributeValue("pageId");
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
            flow.setStopsList(stopsList);

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
            String bundel = stopElement.attributeValue("bundel");
            String description = stopElement.attributeValue("description");
            String id = stopElement.attributeValue("id");
            String name = stopElement.attributeValue("name");
            String pageId = stopElement.attributeValue("pageId");
            String inPortType = stopElement.attributeValue("inPortType");
            String inports = stopElement.attributeValue("inports");
            String outPortType = stopElement.attributeValue("outPortType");
            String outports = stopElement.attributeValue("outports");
            String isCheckpoint = stopElement.attributeValue("isCheckpoint");
            String owner = stopElement.attributeValue("owner");
            String groups = stopElement.attributeValue("groups");
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
                    String allowableValues = propertyValue.attributeValue("allowableValues");
                    String customValue = propertyValue.attributeValue("customValue");
                    String propertyDescription = propertyValue.attributeValue("description");
                    String displayName = propertyValue.attributeValue("displayName");
                    String propertyId = propertyValue.attributeValue("id");
                    String propertyName = propertyValue.attributeValue("name");
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
            String dx = mxGraphModelXml.attributeValue("dx");
            String dy = mxGraphModelXml.attributeValue("dy");
            String grid = mxGraphModelXml.attributeValue("grid");
            String gridSize = mxGraphModelXml.attributeValue("gridSize");
            String guides = mxGraphModelXml.attributeValue("guides");
            String tooltips = mxGraphModelXml.attributeValue("tooltips");
            String connect = mxGraphModelXml.attributeValue("connect");
            String arrows = mxGraphModelXml.attributeValue("arrows");
            String fold = mxGraphModelXml.attributeValue("fold");
            String page = mxGraphModelXml.attributeValue("page");
            String pageScale = mxGraphModelXml.attributeValue("pageScale");
            String pageWidth = mxGraphModelXml.attributeValue("pageWidth");
            String pageHeight = mxGraphModelXml.attributeValue("pageHeight");
            String background = mxGraphModelXml.attributeValue("background");

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
        String pageId = recordEle.attributeValue("id");
        String parent = recordEle.attributeValue("parent");
        String style = recordEle.attributeValue("style");
        String edge = recordEle.attributeValue("edge");
        String source = recordEle.attributeValue("source");
        String target = recordEle.attributeValue("target");
        String value = recordEle.attributeValue("value");
        String vertex = recordEle.attributeValue("vertex");
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
            String relative = mxGeometryXml.attributeValue("relative");
            String as = mxGeometryXml.attributeValue("as");
            String x = mxGeometryXml.attributeValue("x");
            String y = mxGeometryXml.attributeValue("y");
            String width = mxGeometryXml.attributeValue("width");
            String height = mxGeometryXml.attributeValue("height");
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

    /**
     * xmlStrToElement
     *
     * @param xmlData  xml string data
     * @param isEscape is escape
     * @return Element
     */
    private static Element xmlStrToElement(String xmlData, boolean isEscape) {
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

}

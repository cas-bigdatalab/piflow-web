package com.nature.base.util;

import com.nature.common.Eunm.PortType;
import com.nature.component.flow.model.*;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGeometry;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.mxGraph.vo.MxCellVo;
import com.nature.component.mxGraph.vo.MxGeometryVo;
import com.nature.component.mxGraph.vo.MxGraphModelVo;
import com.nature.component.template.model.PropertyTemplateModel;
import com.nature.component.template.model.StopTemplateModel;
import com.nature.component.template.model.Template;
import org.apache.commons.lang.StringEscapeUtils;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("rawtypes")
public class FlowXmlUtils {

    static Logger logger = LoggerUtil.getLogger();

    /**
     * mxGraphModel to mxGraphModelVo
     *
     * @param mxGraphModel
     * @return
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
                List<MxCellVo> mxCellVoList = new ArrayList<MxCellVo>();
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
     * @param mxGraphModelVo
     * @return
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
                xmlStrSb.append("dx=\"" + dx + "\" ");
            }
            if (StringUtils.isNotBlank(dy)) {
                xmlStrSb.append("dy=\"" + dy + "\" ");
            }
            if (StringUtils.isNotBlank(grid)) {
                xmlStrSb.append("grid=\"" + grid + "\" ");
            }
            if (StringUtils.isNotBlank(gridSize)) {
                xmlStrSb.append("gridSize=\"" + gridSize + "\" ");
            }
            if (StringUtils.isNotBlank(guides)) {
                xmlStrSb.append("guides=\"" + guides + "\" ");
            }
            if (StringUtils.isNotBlank(tooltips)) {
                xmlStrSb.append("tooltips=\"" + tooltips + "\" ");
            }
            if (StringUtils.isNotBlank(connect)) {
                xmlStrSb.append("connect=\"" + connect + "\" ");
            }
            if (StringUtils.isNotBlank(arrows)) {
                xmlStrSb.append("arrows=\"" + arrows + "\" ");
            }
            if (StringUtils.isNotBlank(fold)) {
                xmlStrSb.append("fold=\"" + fold + "\" ");
            }
            if (StringUtils.isNotBlank(page)) {
                xmlStrSb.append("page=\"" + page + "\" ");
            }
            if (StringUtils.isNotBlank(pageScale)) {
                xmlStrSb.append("pageScale=\"" + pageScale + "\" ");
            }
            if (StringUtils.isNotBlank(pageWidth)) {
                xmlStrSb.append("pageWidth=\"" + pageWidth + "\" ");
            }
            if (StringUtils.isNotBlank(pageHeight)) {
                xmlStrSb.append("pageHeight=\"" + pageHeight + "\" ");
            }
            if (StringUtils.isNotBlank(background)) {
                xmlStrSb.append("background=\"" + background + "\" ");
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
                        xmlStrSb.append("id=\"" + id + "\" ");
                    }
                    if (StringUtils.isNotBlank(value)) {
                        xmlStrSb.append("value=\"" + value + "\" ");
                    }
                    if (StringUtils.isNotBlank(style)) {
                        xmlStrSb.append("style=\"" + style + "\" ");
                    }
                    if (StringUtils.isNotBlank(parent)) {
                        xmlStrSb.append("parent=\"" + parent + "\" ");
                    }
                    if (StringUtils.isNotBlank(source)) {
                        xmlStrSb.append("source=\"" + source + "\" ");
                    }
                    if (StringUtils.isNotBlank(target)) {
                        xmlStrSb.append("target=\"" + target + "\" ");
                    }
                    if (StringUtils.isNotBlank(vertex)) {
                        xmlStrSb.append("vertex=\"" + vertex + "\" ");
                    }
                    if (StringUtils.isNotBlank(edge)) {
                        xmlStrSb.append("edge=\"" + edge + "\" ");
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
                            xmlStrSb.append("x=\"" + x + "\" ");
                        }
                        if (StringUtils.isNotBlank(y)) {
                            xmlStrSb.append("y=\"" + y + "\" ");
                        }
                        if (StringUtils.isNotBlank(width)) {
                            xmlStrSb.append("width=\"" + width + "\" ");
                        }
                        if (StringUtils.isNotBlank(height)) {
                            xmlStrSb.append("height=\"" + height + "\" ");
                        }
                        if (StringUtils.isNotBlank(relative)) {
                            xmlStrSb.append("relative=\"" + relative + "\" ");
                        }
                        if (StringUtils.isNotBlank(as)) {
                            xmlStrSb.append("as=\"" + as + "\" ");
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
     * @param mxGraphModel
     * @return
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
                xmlStrSb.append("dx=\"" + dx + "\" ");
            }
            if (StringUtils.isNotBlank(dy)) {
                xmlStrSb.append("dy=\"" + dy + "\" ");
            }
            if (StringUtils.isNotBlank(grid)) {
                xmlStrSb.append("grid=\"" + grid + "\" ");
            }
            if (StringUtils.isNotBlank(gridSize)) {
                xmlStrSb.append("gridSize=\"" + gridSize + "\" ");
            }
            if (StringUtils.isNotBlank(guides)) {
                xmlStrSb.append("guides=\"" + guides + "\" ");
            }
            if (StringUtils.isNotBlank(tooltips)) {
                xmlStrSb.append("tooltips=\"" + tooltips + "\" ");
            }
            if (StringUtils.isNotBlank(connect)) {
                xmlStrSb.append("connect=\"" + connect + "\" ");
            }
            if (StringUtils.isNotBlank(arrows)) {
                xmlStrSb.append("arrows=\"" + arrows + "\" ");
            }
            if (StringUtils.isNotBlank(fold)) {
                xmlStrSb.append("fold=\"" + fold + "\" ");
            }
            if (StringUtils.isNotBlank(page)) {
                xmlStrSb.append("page=\"" + page + "\" ");
            }
            if (StringUtils.isNotBlank(pageScale)) {
                xmlStrSb.append("pageScale=\"" + pageScale + "\" ");
            }
            if (StringUtils.isNotBlank(pageWidth)) {
                xmlStrSb.append("pageWidth=\"" + pageWidth + "\" ");
            }
            if (StringUtils.isNotBlank(pageHeight)) {
                xmlStrSb.append("pageHeight=\"" + pageHeight + "\" ");
            }
            if (StringUtils.isNotBlank(background)) {
                xmlStrSb.append("background=\"" + background + "\" ");
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
                        xmlStrSb.append("id=\"" + id + "\" ");
                    }
                    if (StringUtils.isNotBlank(value)) {
                        xmlStrSb.append("value=\"" + value + "\" ");
                    }
                    if (StringUtils.isNotBlank(style)) {
                        xmlStrSb.append("style=\"" + style + "\" ");
                    }
                    if (StringUtils.isNotBlank(parent)) {
                        xmlStrSb.append("parent=\"" + parent + "\" ");
                    }
                    if (StringUtils.isNotBlank(source)) {
                        xmlStrSb.append("source=\"" + source + "\" ");
                    }
                    if (StringUtils.isNotBlank(target)) {
                        xmlStrSb.append("target=\"" + target + "\" ");
                    }
                    if (StringUtils.isNotBlank(vertex)) {
                        xmlStrSb.append("vertex=\"" + vertex + "\" ");
                    }
                    if (StringUtils.isNotBlank(edge)) {
                        xmlStrSb.append("edge=\"" + edge + "\" ");
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
                            xmlStrSb.append("x=\"" + x + "\" ");
                        }
                        if (StringUtils.isNotBlank(y)) {
                            xmlStrSb.append("y=\"" + y + "\" ");
                        }
                        if (StringUtils.isNotBlank(width)) {
                            xmlStrSb.append("width=\"" + width + "\" ");
                        }
                        if (StringUtils.isNotBlank(height)) {
                            xmlStrSb.append("height=\"" + height + "\" ");
                        }
                        if (StringUtils.isNotBlank(relative)) {
                            xmlStrSb.append("relative=\"" + relative + "\" ");
                        }
                        if (StringUtils.isNotBlank(as)) {
                            xmlStrSb.append("as=\"" + as + "\" ");
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
     * @param flow
     * @return
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
                xmlStrSb.append("id=\"" + StringEscapeUtils.escapeHtml(id) + "\" ");
            }
            if (StringUtils.isNotBlank(name)) {
                xmlStrSb.append("name=\"" + StringEscapeUtils.escapeHtml(name) + "\" ");
            }
            if (StringUtils.isNotBlank(description)) {
                xmlStrSb.append("description=\"" + StringEscapeUtils.escapeHtml(description) + "\" ");
            }
            if (StringUtils.isNotBlank(driverMemory)) {
                xmlStrSb.append("driverMemory=\"" + StringEscapeUtils.escapeHtml(driverMemory) + "\" ");
            }
            if (StringUtils.isNotBlank(executorCores)) {
                xmlStrSb.append("executorCores=\"" + StringEscapeUtils.escapeHtml(executorCores) + "\" ");
            }
            if (StringUtils.isNotBlank(executorMemory)) {
                xmlStrSb.append("executorMemory=\"" + StringEscapeUtils.escapeHtml(executorMemory) + "\" ");
            }
            if (StringUtils.isNotBlank(executorNumber)) {
                xmlStrSb.append("executorNumber=\"" + StringEscapeUtils.escapeHtml(executorNumber) + "\" ");
            }
            if (StringUtils.isNotBlank(flowPageId)) {
                xmlStrSb.append("pageId=\"" + StringEscapeUtils.escapeHtml(flowPageId) + "\" ");
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
                        xmlStrSb.append("id=\"" + StringEscapeUtils.escapeHtml(stopId) + "\" ");
                    }
                    if (StringUtils.isNotBlank(pageId)) {
                        xmlStrSb.append("pageId=\"" + StringEscapeUtils.escapeHtml(pageId) + "\" ");
                    }
                    if (StringUtils.isNotBlank(stopName)) {
                        xmlStrSb.append("name=\"" + StringEscapeUtils.escapeHtml(stopName) + "\" ");
                    }
                    if (StringUtils.isNotBlank(bundel)) {
                        xmlStrSb.append("bundel=\"" + StringEscapeUtils.escapeHtml(bundel) + "\" ");
                    }
                    if (StringUtils.isNotBlank(stopDescription)) {
                        stopDescription = stopDescription.replace("&", "&amp;");
                        xmlStrSb.append("description=\"" + StringEscapeUtils.escapeHtml(stopDescription) + "\" ");
                    }
                    if (null != checkpoint) {
                        int ischeckpoint = checkpoint ? 1 : 0;
                        xmlStrSb.append("isCheckpoint=\"" + ischeckpoint + "\" ");
                    }
                    if (StringUtils.isNotBlank(inports)) {
                        xmlStrSb.append("inports=\"" + StringEscapeUtils.escapeHtml(inports) + "\" ");
                    }

                    if (StringUtils.isNotBlank(outports)) {
                        xmlStrSb.append("outports=\"" + StringEscapeUtils.escapeHtml(outports) + "\" ");
                    }
                    if (StringUtils.isNotBlank(owner)) {
                        xmlStrSb.append("owner=\"" + StringEscapeUtils.escapeHtml(owner) + "\" ");
                    }
                    if (null != inPortType) {
                        xmlStrSb.append("inPortType=\"" + inPortType + "\" ");
                    }
                    if (null != outPortType) {
                        xmlStrSb.append("outPortType=\"" + outPortType + "\" ");
                    }
                    if (StringUtils.isNotBlank(groups)) {
                        xmlStrSb.append("groups=\"" + StringEscapeUtils.escapeHtml(groups) + "\" ");
                    }
                    if (StringUtils.isNotBlank(crtUser)) {
                        xmlStrSb.append("crtUser=\"" + StringEscapeUtils.escapeHtml(crtUser) + "\" ");
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
                            Boolean required = propertyVo.getRequired();
                            Boolean sensitive = propertyVo.getSensitive();
                            Boolean isSelect = propertyVo.getIsSelect();
                            String propertyVocrtUser = propertyVo.getCrtUser();
                            if (StringUtils.isNotBlank(propertyId)) {
                                xmlStrSb.append("id=\"" + StringEscapeUtils.escapeHtml(propertyId) + "\" ");
                            }
                            if (StringUtils.isNotBlank(displayName)) {
                                xmlStrSb.append("displayName=\"" + StringEscapeUtils.escapeHtml(displayName) + "\" ");
                            }
                            if (StringUtils.isNotBlank(propertyName)) {
                                xmlStrSb.append("name=\"" + StringEscapeUtils.escapeHtml(propertyName) + "\" ");
                            }
                            if (StringUtils.isNotBlank(propertyDescription)) {
                                xmlStrSb.append("description=\"" + StringEscapeUtils.escapeHtml(propertyDescription) + "\" ");
                            }
                            if (StringUtils.isNotBlank(allowableValues)) {
                                xmlStrSb.append("allowableValues=\"" + allowableValues.replaceAll("\"", "") + "\" ");
                            }
                            if (StringUtils.isNotBlank(customValue)) {
                                xmlStrSb.append("customValue=\"" + StringEscapeUtils.escapeHtml(customValue) + "\" ");
                            }
                            if (StringUtils.isNotBlank(propertyVocrtUser)) {
                                xmlStrSb.append("crtUser=\"" + StringEscapeUtils.escapeHtml(propertyVocrtUser) + "\" ");
                            }
                            xmlStrSb.append("required=\"" + required + "\" ");
                            xmlStrSb.append("sensitive=\"" + sensitive + "\" ");
                            xmlStrSb.append("isSelect=\"" + isSelect + "\" ");
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
                            xmlStrSb.append("crtUser=\"" + StringEscapeUtils.escapeHtml(crtUser) + "\" ");
                        }
                        if (StringUtils.isNotBlank(from)) {
                            xmlStrSb.append("from=\"" + StringEscapeUtils.escapeHtml(from) + "\" ");
                        }
                        if (StringUtils.isNotBlank(to)) {
                            xmlStrSb.append("to=\"" + StringEscapeUtils.escapeHtml(to) + "\" ");
                        }
                        if (StringUtils.isNotBlank(inport)) {
                            xmlStrSb.append("inport=\"" + StringEscapeUtils.escapeHtml(inport) + "\" ");
                        }
                        if (StringUtils.isNotBlank(outport)) {
                            xmlStrSb.append("outport=\"" + StringEscapeUtils.escapeHtml(outport) + "\" ");
                        }
                        if (StringUtils.isNotBlank(pageId)) {
                            xmlStrSb.append("pageId=\"" + StringEscapeUtils.escapeHtml(pageId) + "\" ");
                        }
                        if (StringUtils.isNotBlank(filterCondition)) {
                            xmlStrSb.append("filterCondition=\"" + StringEscapeUtils.escapeHtml(filterCondition) + "\" ");
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
     * @param flowList
     * @return
     */
    public static String flowListToXmlStr(List<Flow> flowList) {
        StringBuffer xmlStrBuf = new StringBuffer();
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
     * @param flowGroupPathsList
     * @return
     */
    public static String flowGroupPathsListToXmlStr(List<FlowGroupPaths> flowGroupPathsList) {
        StringBuffer xmlStrBuf = new StringBuffer();
        if (null != flowGroupPathsList && flowGroupPathsList.size() > 0) {
            for (FlowGroupPaths flowGroupPaths : flowGroupPathsList) {
                xmlStrBuf.append("<flowGroupPaths ");
                String from = flowGroupPaths.getFrom();
                String to = flowGroupPaths.getTo();
                String inport = flowGroupPaths.getInport();
                String outport = flowGroupPaths.getOutport();
                String pageId = flowGroupPaths.getPageId();
                if (StringUtils.isNotBlank(from)) {
                    xmlStrBuf.append("from=\"" + StringEscapeUtils.escapeHtml(from) + "\" ");
                }
                if (StringUtils.isNotBlank(to)) {
                    xmlStrBuf.append("to=\"" + StringEscapeUtils.escapeHtml(to) + "\" ");
                }
                if (StringUtils.isNotBlank(inport)) {
                    xmlStrBuf.append("inport=\"" + StringEscapeUtils.escapeHtml(inport) + "\" ");
                }
                if (StringUtils.isNotBlank(outport)) {
                    xmlStrBuf.append("outport=\"" + StringEscapeUtils.escapeHtml(outport) + "\" ");
                }
                if (StringUtils.isNotBlank(pageId)) {
                    xmlStrBuf.append("pageId=\"" + StringEscapeUtils.escapeHtml(pageId) + "\" ");
                }
                xmlStrBuf.append(" /> \n");
            }
        }
        return xmlStrBuf.toString();
    }

    /**
     * flowGroup to xml
     *
     * @param flowGroup
     * @return
     */
    public static String flowGroupToXmlStr(FlowGroup flowGroup) {
        StringBuffer xmlStrBuf = new StringBuffer();
        if (null != flowGroup) {
            xmlStrBuf.append("<flowGroup ");
            String id = flowGroup.getId();
            String name = flowGroup.getName();
            String description = flowGroup.getDescription();
            String flowGroupPageId = flowGroup.getPageId();
            if (StringUtils.isNotBlank(id)) {
                xmlStrBuf.append("id=\"" + StringEscapeUtils.escapeHtml(id) + "\" ");
            }
            if (StringUtils.isNotBlank(name)) {
                xmlStrBuf.append("name=\"" + StringEscapeUtils.escapeHtml(name) + "\" ");
            }
            if (StringUtils.isNotBlank(description)) {
                xmlStrBuf.append("description=\"" + StringEscapeUtils.escapeHtml(description) + "\" ");
            }
            if (StringUtils.isNotBlank(flowGroupPageId)) {
                xmlStrBuf.append("pageId=\"" + StringEscapeUtils.escapeHtml(flowGroupPageId) + "\" ");
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

    /**
     * String type "xml" to "MxGraphModel"
     *
     * @param xmldata
     * @return
     */
    public static MxGraphModelVo xmlToMxGraphModel(String xmldata) {
        String transformation = "<nature>" + xmldata + "</nature>";
        MxGraphModelVo mxGraphModelVo = new MxGraphModelVo();
        InputSource in = new InputSource(new StringReader(transformation));
        in.setEncoding("UTF-8");
        SAXReader reader = new SAXReader();
        Document document;
        try {
            document = reader.read(in);
            // Get all "mxCell" nodes with "autoSaveNode" attribute
            Element rootElt = document.getRootElement(); // Get the root node
            Element mxGraphModel = rootElt.element("mxGraphModel");
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
            List<MxCellVo> rootVoList = new ArrayList<MxCellVo>();
            Element rootjd = mxGraphModel.element("root");
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
        } catch (Exception e) {
            logger.error("Conversion failed", e);
            return null;
        }
        return mxGraphModelVo;
    }

    /**
     * String type xml to "stop" and other information
     *
     * @param xmlData
     * @return
     * @throws DocumentException
     */
    public static Template xmlToFlowStopInfo(String xmlData) {
        Document document = null;
        try {
            document = DocumentHelper.parseText(StringEscapeUtils.unescapeHtml(xmlData));
        } catch (DocumentException e1) {
            e1.printStackTrace();
        }
        String strXml = document.getRootElement().asXML();
        String transformation = "<sdds>" + strXml + "</sdds>";
        InputSource in = new InputSource(new StringReader(transformation));
        in.setEncoding("UTF-8");
        SAXReader reader = new SAXReader();
        List<StopTemplateModel> stopVoList = new ArrayList<StopTemplateModel>();

        Template template = new Template();
        try {
            document = reader.read(in);
            // Get all "mxCell" nodes with "autoSaveNode" attribute
            Element rootElt = document.getRootElement(); // Get the root node
            Element flow = rootElt.element("flow");
            Iterator rootiter = flow.elementIterator("stop"); // Get the child node "stop" under the root node
            while (rootiter.hasNext()) {
                List<PropertyTemplateModel> propertyList = new ArrayList<PropertyTemplateModel>();
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
                Boolean Checkpoint = false;
                Checkpoint = "0".equals(isCheckpoint) ? false : true;
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
                        Boolean required = propertyValue.attributeValue("required").equals("true") ? true : false;
                        Boolean sensitive = propertyValue.attributeValue("sensitive").equals("true") ? true : false;
                        Boolean isSelect = propertyValue.attributeValue("isSelect").equals("true") ? true : false;
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
        } catch (Exception e) {
            logger.error("Conversion failed", e);
            return null;
        }
        return template;
    }

    /**
     * String type xml to MxGraphModel
     *
     * @param xmldata
     * @return
     */
    public static MxGraphModelVo allXmlToMxGraphModelVo(String xmldata, int PageId) {
        Document document = null;
        try {
            document = DocumentHelper.parseText(xmldata);
        } catch (DocumentException e1) {
            e1.printStackTrace();
        }
        String strXml = document.getRootElement().asXML();
        String transformation = "<sdds>" + strXml + "</sdds>";
        MxGraphModelVo mxGraphModelVo = new MxGraphModelVo();
        InputSource in = new InputSource(new StringReader(transformation));
        in.setEncoding("UTF-8");
        SAXReader reader = new SAXReader();
        try {
            document = reader.read(in);
            // Get all "mxCell" nodes with "autoSaveNode" attribute
            Element rootElt = document.getRootElement(); // Get the root node
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
            List<MxCellVo> rootVoList = new ArrayList<MxCellVo>();
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
        } catch (Exception e) {
            logger.error("Conversion failed", e);
            return null;
        }
        return mxGraphModelVo;
    }

    /**
     * List<StopTemplateModel>  to  List<Stops>
     *
     * @param stopsListTemplate
     * @return
     */
    public static List<Stops> stopTemplateVoToStop(List<StopTemplateModel> stopsListTemplate) {
        List<Stops> stopsList = new ArrayList<Stops>();
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
                        List<Property> propertyList = new ArrayList<Property>();
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
     * xmlToPaths
     *
     * @param xmldata
     * @return
     */
    public static List<Paths> xmlToPaths(String xmldata) {
        Document document = null;
        try {
            document = DocumentHelper.parseText(xmldata);
        } catch (DocumentException e1) {
            e1.printStackTrace();
        }
        String strXml = document.getRootElement().asXML();
        String transformation = "<sdds>" + strXml + "</sdds>";
        InputSource in = new InputSource(new StringReader(transformation));
        in.setEncoding("UTF-8");
        SAXReader reader = new SAXReader();
        List<Paths> PathsList = new ArrayList<Paths>();
        try {
            document = reader.read(in);
            // Get all "mxCell" nodes with "autoSaveNode" attribute
            Element rootElt = document.getRootElement(); // Get the root node
            Element flow = rootElt.element("flow");
            Iterator rootiter = flow.elementIterator("paths"); // Get the child node "paths" under the root node
            while (rootiter.hasNext()) {
                Paths paths = new Paths();
                Element recordEle = (Element) rootiter.next();
                String crtUser = recordEle.attributeValue("crtUser");
                String from = recordEle.attributeValue("from");
                String to = recordEle.attributeValue("to");
                String outport = recordEle.attributeValue("outport");
                String pageId = recordEle.attributeValue("pageId");
                String inport = recordEle.attributeValue("inport");
                paths.setCrtUser(crtUser);
                paths.setFrom(from);
                paths.setTo(to);
                paths.setOutport(outport);
                paths.setInport(inport);
                paths.setPageId(pageId);
                PathsList.add(paths);
            }
        } catch (Exception e) {
            logger.error("Conversion failed", e);
            return null;
        }
        return PathsList;
    }

    /**
     * String type xml to MxGraphModel
     *
     * @param xmlData
     * @return
     */
    public static MxGraphModel allXmlToMxGraphModel(String xmlData, int maxPageId, String username) {
        Document document = null;
        try {
            document = DocumentHelper.parseText(xmlData);
        } catch (DocumentException e1) {
            e1.printStackTrace();
        }
        String strXml = document.getRootElement().asXML();
        String transformation = "<sdds>" + strXml + "</sdds>";
        MxGraphModel mxGraphModel = new MxGraphModel();
        InputSource in = new InputSource(new StringReader(transformation));
        in.setEncoding("UTF-8");
        SAXReader reader = new SAXReader();
        try {
            document = reader.read(in);
            // Get all "mxCell" nodes with "autoSaveNode" attribute
            Element rootElt = document.getRootElement(); // Get the root node
            Element flow = rootElt.element("flow");
            Element mxGraphModelXml = flow.element("mxGraphModel");
            mxGraphModel = xmlToMxGraphModel(mxGraphModelXml.asXML(), maxPageId, username);
        } catch (Exception e) {
            logger.error("Conversion failed", e);
            return null;
        }
        return mxGraphModel;
    }

    /**
     * String type xml to MxGraphModel
     *
     * @param xmlData
     * @return
     */
    public static MxGraphModel xmlToMxGraphModel(String xmlData, int maxPageId, String username) {
        if (StringUtils.isBlank(xmlData)) {
            return null;
        }
        try {
            Document document = DocumentHelper.parseText(xmlData);
            String strXml = document.getRootElement().asXML();
            String transformation = "<sdds>" + strXml + "</sdds>";
            MxGraphModel mxGraphModel = new MxGraphModel();
            InputSource in = new InputSource(new StringReader(transformation));
            in.setEncoding("UTF-8");
            SAXReader reader = new SAXReader();
            document = reader.read(in);
            // Get all "mxCell" nodes with "autoSaveNode" attribute
            Element rootElt = document.getRootElement(); // Get the root node
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
     * @param xmlData
     * @param maxPageId
     * @return
     */
    public static FlowGroupPaths xmlToFlowGroupPaths(String xmlData, int maxPageId, String username) {
        if (StringUtils.isBlank(xmlData)) {
            return null;
        }
        try {
            Document document = DocumentHelper.parseText(xmlData);
            String strXml = document.getRootElement().asXML();
            String transformation = "<fg>" + strXml + "</fg>";
            InputSource in = new InputSource(new StringReader(transformation));
            in.setEncoding("UTF-8");
            SAXReader reader = new SAXReader();

            document = reader.read(in);
            // Get all "mxCell" nodes with "autoSaveNode" attribute
            Element rootElt = document.getRootElement(); // Get the root node
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
                flowGroupPaths.setFrom(from);
            }
            if (StringUtils.isNotBlank(to)) {
                flowGroupPaths.setTo(to);
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
        } catch (DocumentException e) {
            logger.error("Conversion failed", e);
            return null;
        }

    }

    /**
     * xmlToFlowGroupPathsList
     *
     * @param xmlDataList
     * @param maxPageId
     * @param username
     * @return
     */
    public static List<FlowGroupPaths> xmlListToFlowGroupPathsList(List<String> xmlDataList, int maxPageId, String username) {
        if (null == xmlDataList || xmlDataList.size() <= 0) {
            return null;
        }
        List<FlowGroupPaths> flowGroupPathsList = new ArrayList<>();
        for (String xmlData : xmlDataList) {
            FlowGroupPaths flowGroupPaths = xmlToFlowGroupPaths(xmlData, maxPageId, username);
            if (null != flowGroupPaths) {
                flowGroupPathsList.add(flowGroupPaths);
            }
        }
        return flowGroupPathsList;
    }

    /**
     * xmlToPathsOne
     *
     * @param xmlData
     * @param maxPageId
     * @param username
     * @return
     */
    public static Paths xmlToPathsOne(String xmlData, int maxPageId, String username) {
        if (StringUtils.isBlank(xmlData)) {
            return null;
        }
        try {
            Document document = DocumentHelper.parseText(xmlData);
            String strXml = document.getRootElement().asXML();
            String transformation = "<fg>" + strXml + "</fg>";
            InputSource in = new InputSource(new StringReader(transformation));
            in.setEncoding("UTF-8");
            SAXReader reader = new SAXReader();
            document = reader.read(in);
            // Get all nodes with "autoSaveNode" attribute
            Element rootElt = document.getRootElement(); // Get the root node
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
            paths.setFrom(from);
            paths.setTo(to);
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
     * xmlToPathsList
     *
     * @param xmlDataList
     * @param maxPageId
     * @param username
     * @return
     */
    public static List<Paths> xmlListToPathsList(List<String> xmlDataList, int maxPageId, String username) {
        if (null == xmlDataList || xmlDataList.size() <= 0) {
            return null;
        }
        List<Paths> pathsList = new ArrayList<>();
        for (String xmlData : xmlDataList) {
            Paths paths = xmlToPathsOne(xmlData, maxPageId, username);
            if (null != paths) {
                pathsList.add(paths);
            }
        }
        return pathsList;
    }

    /**
     * String type xml to "stop"
     *
     * @param xmlData
     * @return
     * @throws DocumentException
     */
    public static Stops xmlToStops(String xmlData, int maxPageId, String username) {
        if (StringUtils.isBlank(xmlData)) {
            return null;
        }
        try {
            Document document = DocumentHelper.parseText(StringEscapeUtils.unescapeHtml(xmlData));
            String strXml = document.getRootElement().asXML();
            String transformation = "<fg>" + strXml + "</fg>";
            InputSource in = new InputSource(new StringReader(transformation));
            in.setEncoding("UTF-8");
            SAXReader reader = new SAXReader();
            document = reader.read(in);
            // Get all nodes with "autoSaveNode" attribute
            Element rootElt = document.getRootElement(); // Get the root node
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
            Boolean Checkpoint = false;
            Checkpoint = "0".equals(isCheckpoint) ? false : true;
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
                    Boolean required = propertyValue.attributeValue("required").equals("true") ? true : false;
                    Boolean sensitive = propertyValue.attributeValue("sensitive").equals("true") ? true : false;
                    Boolean isSelect = propertyValue.attributeValue("isSelect").equals("true") ? true : false;
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
     * xmlToStopsList
     *
     * @param xmlDataList
     * @param maxPageId
     * @param username
     * @return
     */
    public static List<Stops> xmlListToStopsList(List<String> xmlDataList, int maxPageId, String username) {
        if (null == xmlDataList || xmlDataList.size() <= 0) {
            return null;
        }
        List<Stops> stopsList = new ArrayList<>();
        for (String xmlData : xmlDataList) {
            Stops stops = xmlToStops(xmlData, maxPageId, username);
            if (null != stops) {
                stopsList.add(stops);
            }
        }
        return stopsList;
    }

    /**
     * String type xml to FlowGroupPaths
     *
     * @param xmlData
     * @param maxPageId
     * @return
     */
    public static Flow xmlToFlow(String xmlData, int maxPageId, String username) {
        Flow flow = null;
        if (StringUtils.isBlank(xmlData)) {
            return flow;
        }
        try {
            Document document = DocumentHelper.parseText(xmlData);
            String strXml = document.getRootElement().asXML();
            String transformation = "<fg>" + strXml + "</fg>";
            InputSource in = new InputSource(new StringReader(transformation));
            in.setEncoding("UTF-8");
            SAXReader reader = new SAXReader();
            flow = new Flow();

            document = reader.read(in);
            // Get all "mxCell" nodes with "autoSaveNode" attribute
            Element rootElt = document.getRootElement(); // Get the root node
            Element flowElement = rootElt.element("flow");
            String flowPageId = flowElement.attributeValue("pageId");
            String driverMemory = flowElement.attributeValue("driverMemory");
            String executorCores = flowElement.attributeValue("executorCores");
            String executorMemory = flowElement.attributeValue("executorMemory");
            String executorNumber = flowElement.attributeValue("executorNumber");
            String name = flowElement.attributeValue("name");
            String description = flowElement.attributeValue("description");
            flow.setCrtDttm(new Date());
            flow.setCrtUser(username);
            flow.setLastUpdateDttm(new Date());
            flow.setLastUpdateUser(username);
            flow.setVersion(0L);
            flow.setPageId(null != flowPageId ? (Integer.parseInt(flowPageId) + maxPageId) + "" : "");
            flow.setDriverMemory(driverMemory);
            flow.setExecutorCores(executorCores);
            flow.setExecutorMemory(executorMemory);
            flow.setExecutorNumber(executorNumber);
            flow.setName(name + maxPageId);
            flow.setDescription(description);
            // mxGraphModel
            Element mxGraphModelElement = flowElement.element("mxGraphModel");
            MxGraphModel mxGraphModel = xmlToMxGraphModel(mxGraphModelElement.asXML(), maxPageId, username);
            flow.setMxGraphModel(mxGraphModel);
            // paths
            Iterator pathXmlIterator = flowElement.elementIterator("paths");
            List<String> pathXmlStrArr = new ArrayList<>();
            while (pathXmlIterator.hasNext()) {
                Element recordEle = (Element) pathXmlIterator.next();
                pathXmlStrArr.add(recordEle.asXML());
            }
            List<Paths> pathsList = xmlListToPathsList(pathXmlStrArr, maxPageId, username);
            flow.setPathsList(pathsList);
            // stop
            Iterator stopXmlIterator = flowElement.elementIterator("stop");
            List<String> stopXmlStrArr = new ArrayList<>();
            while (stopXmlIterator.hasNext()) {
                Element recordEle = (Element) stopXmlIterator.next();
                stopXmlStrArr.add(recordEle.asXML());
            }
            List<Stops> stopsList = xmlListToStopsList(stopXmlStrArr, maxPageId, username);
            flow.setStopsList(stopsList);
        } catch (Exception e) {
            logger.error("Conversion failed", e);
            return null;
        }
        return flow;
    }

    /**
     * xmlListToFlowList
     *
     * @param xmlDataList
     * @param maxPageId
     * @param username
     * @return
     */
    public static List<Flow> xmlListToFlowList(List<String> xmlDataList, int maxPageId, String username) {
        if (null == xmlDataList || xmlDataList.size() <= 0) {
            return null;
        }
        List<Flow> flowList = new ArrayList<>();
        for (String xmlData : xmlDataList) {
            Flow flow = xmlToFlow(xmlData, maxPageId, username);
            if (null != flow) {
                flowList.add(flow);
            }
        }
        return flowList;
    }

    /**
     * flowGroup to xml
     *
     * @param flowGroupXmlStr
     * @return
     */
    public static FlowGroup XmlStrToFlowGroup(String flowGroupXmlStr, int maxPageId, String username) {
        if (StringUtils.isBlank(flowGroupXmlStr)) {
            return null;
        }
        try {
            Document document = DocumentHelper.parseText(flowGroupXmlStr);
            String strXml = document.getRootElement().asXML();
            String transformation = "<fg>" + strXml + "</fg>";
            InputSource in = new InputSource(new StringReader(transformation));
            in.setEncoding("UTF-8");
            SAXReader reader = new SAXReader();
            FlowGroup flowGroup = new FlowGroup();

            document = reader.read(in);
            // Get all nodes with "autoSaveNode" attribute
            Element rootElt = document.getRootElement(); // Get the root node
            Element flowGroupElement = rootElt.element("flowGroup");
            String name = flowGroupElement.attributeValue("name");
            String description = flowGroupElement.attributeValue("description");
            flowGroup.setCrtDttm(new Date());
            flowGroup.setCrtUser(username);
            flowGroup.setLastUpdateDttm(new Date());
            flowGroup.setLastUpdateUser(username);
            flowGroup.setVersion(0L);
            flowGroup.setName(name);
            flowGroup.setDescription(description);

            // mxGraphModel
            Element mxGraphModelXml = flowGroupElement.element("mxGraphModel");
            String mxGraphModelXmlAsXML = mxGraphModelXml.asXML();
            MxGraphModel mxGraphModel = xmlToMxGraphModel(mxGraphModelXmlAsXML, maxPageId, username);
            flowGroup.setMxGraphModel(mxGraphModel);

            // flow list
            Iterator flowXmlIterator = flowGroupElement.elementIterator("flow");
            List<String> flowXmlStrArr = new ArrayList<>();
            while (flowXmlIterator.hasNext()) {
                Element recordEle = (Element) flowXmlIterator.next();
                flowXmlStrArr.add(recordEle.asXML());
            }
            List<Flow> flowList = xmlListToFlowList(flowXmlStrArr, maxPageId, username);
            flowGroup.setFlowList(flowList);

            //flowGroupPaths
            Iterator flowGroupPathsXmlIterator = flowGroupElement.elementIterator("flowGroupPaths");
            List<String> flowGroupPathsXmlStrArr = new ArrayList<>();
            while (flowGroupPathsXmlIterator.hasNext()) {
                Element recordEle = (Element) flowGroupPathsXmlIterator.next();
                flowGroupPathsXmlStrArr.add(recordEle.asXML());
            }
            List<FlowGroupPaths> flowGroupPathsList = xmlListToFlowGroupPathsList(flowGroupPathsXmlStrArr, maxPageId, username);
            flowGroup.setFlowGroupPathsList(flowGroupPathsList);
            return flowGroup;
        } catch (Exception e) {
            logger.error("Conversion failed", e);
            return null;
        }
    }
}

package cn.cnic.base.util;

import cn.cnic.common.Eunm.TemplateType;
import cn.cnic.component.mxGraph.entity.MxCell;
import cn.cnic.component.mxGraph.entity.MxGeometry;
import cn.cnic.component.mxGraph.entity.MxGraphModel;
import cn.cnic.component.mxGraph.vo.MxCellVo;
import cn.cnic.component.mxGraph.vo.MxGeometryVo;
import cn.cnic.component.mxGraph.vo.MxGraphModelVo;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MxGraphUtils {

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

    /**
     * "mxGraphModelVo" to string "xml"
     *
     * @param mxGraphModelVo mxGraphModelVo
     * @return String
     */
    public static String mxGraphModelVoToMxGraphXml(MxGraphModelVo mxGraphModelVo) {
        //Fight 'xml' note must write spaces
        if (null != mxGraphModelVo) {
            StringBuffer xmlStrSb = new StringBuffer();
            String dx = StringCustomUtils.replaceSpecialSymbolsPage(mxGraphModelVo.getDx());
            String dy = StringCustomUtils.replaceSpecialSymbolsPage(mxGraphModelVo.getDy());
            String grid = StringCustomUtils.replaceSpecialSymbolsPage(mxGraphModelVo.getGrid());
            String gridSize = StringCustomUtils.replaceSpecialSymbolsPage(mxGraphModelVo.getGridSize());
            String guides = StringCustomUtils.replaceSpecialSymbolsPage(mxGraphModelVo.getGuides());
            String tooltips = StringCustomUtils.replaceSpecialSymbolsPage(mxGraphModelVo.getTooltips());
            String connect = StringCustomUtils.replaceSpecialSymbolsPage(mxGraphModelVo.getConnect());
            String arrows = StringCustomUtils.replaceSpecialSymbolsPage(mxGraphModelVo.getArrows());
            String fold = StringCustomUtils.replaceSpecialSymbolsPage(mxGraphModelVo.getFold());
            String page = StringCustomUtils.replaceSpecialSymbolsPage(mxGraphModelVo.getPage());
            String pageScale = StringCustomUtils.replaceSpecialSymbolsPage(mxGraphModelVo.getPageScale());
            String pageWidth = StringCustomUtils.replaceSpecialSymbolsPage(mxGraphModelVo.getPageWidth());
            String pageHeight = StringCustomUtils.replaceSpecialSymbolsPage(mxGraphModelVo.getPageHeight());
            String background = StringCustomUtils.replaceSpecialSymbolsPage(mxGraphModelVo.getBackground());
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
            Map<String, String> mxCellPageIds = new HashMap<>();
            if (null != rootVoList && rootVoList.size() > 0) {
                for (MxCellVo mxCellVo : rootVoList) {
                    String id = StringCustomUtils.replaceSpecialSymbolsPage(mxCellVo.getPageId());
                    String parent = StringCustomUtils.replaceSpecialSymbolsPage(mxCellVo.getParent());
                    String style = StringCustomUtils.replaceSpecialSymbolsPage(mxCellVo.getStyle());
                    String value = StringCustomUtils.replaceSpecialSymbolsPage(mxCellVo.getValue());
                    String vertex = StringCustomUtils.replaceSpecialSymbolsPage(mxCellVo.getVertex());
                    String edge = StringCustomUtils.replaceSpecialSymbolsPage(mxCellVo.getEdge());
                    String source = StringCustomUtils.replaceSpecialSymbolsPage(mxCellVo.getSource());
                    String target = StringCustomUtils.replaceSpecialSymbolsPage(mxCellVo.getTarget());
                    MxGeometryVo mxGeometryVo = mxCellVo.getMxGeometryVo();
                    if (StringUtils.isBlank(id)) {
                        continue;
                    }
                    String isSplicedId = mxCellPageIds.get(id);
                    if (StringUtils.isNotBlank(isSplicedId)) {
                        continue;
                    }
                    mxCellPageIds.put(id, id);
                    xmlStrSb.append("<mxCell ");
                    xmlStrSb.append(spliceStr("id", id));
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
                        String relative = StringCustomUtils.replaceSpecialSymbolsPage(mxGeometryVo.getRelative());
                        String as = StringCustomUtils.replaceSpecialSymbolsPage(mxGeometryVo.getAs());
                        String x = StringCustomUtils.replaceSpecialSymbolsPage(mxGeometryVo.getX());
                        String y = StringCustomUtils.replaceSpecialSymbolsPage(mxGeometryVo.getY());
                        String width = StringCustomUtils.replaceSpecialSymbolsPage(mxGeometryVo.getWidth());
                        String height = StringCustomUtils.replaceSpecialSymbolsPage(mxGeometryVo.getHeight());
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
     * @param isSaveXml    isSaveXml
     * @return String
     */
    public static String mxGraphModelToMxGraph(boolean isSaveXml, MxGraphModel mxGraphModel) {
        //Fight 'xml' note must write spaces
        if (null != mxGraphModel) {
            StringBuffer xmlStrSb = new StringBuffer();
            String dx = StringCustomUtils.replaceSpecialSymbols(mxGraphModel.getDx(), isSaveXml);
            String dy = StringCustomUtils.replaceSpecialSymbols(mxGraphModel.getDy(), isSaveXml);
            String grid = StringCustomUtils.replaceSpecialSymbols(mxGraphModel.getGrid(), isSaveXml);
            String gridSize = StringCustomUtils.replaceSpecialSymbols(mxGraphModel.getGridSize(), isSaveXml);
            String guides = StringCustomUtils.replaceSpecialSymbols(mxGraphModel.getGuides(), isSaveXml);
            String tooltips = StringCustomUtils.replaceSpecialSymbols(mxGraphModel.getTooltips(), isSaveXml);
            String connect = StringCustomUtils.replaceSpecialSymbols(mxGraphModel.getConnect(), isSaveXml);
            String arrows = StringCustomUtils.replaceSpecialSymbols(mxGraphModel.getArrows(), isSaveXml);
            String fold = StringCustomUtils.replaceSpecialSymbols(mxGraphModel.getFold(), isSaveXml);
            String page = StringCustomUtils.replaceSpecialSymbols(mxGraphModel.getPage(), isSaveXml);
            String pageScale = StringCustomUtils.replaceSpecialSymbols(mxGraphModel.getPageScale(), isSaveXml);
            String pageWidth = StringCustomUtils.replaceSpecialSymbols(mxGraphModel.getPageWidth(), isSaveXml);
            String pageHeight = StringCustomUtils.replaceSpecialSymbols(mxGraphModel.getPageHeight(), isSaveXml);
            String background = StringCustomUtils.replaceSpecialSymbols(mxGraphModel.getBackground(), isSaveXml);
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
            Map<String, String> mxCellPageIds = new HashMap<>();
            if (null != rootList && rootList.size() > 0) {
                for (MxCell mxCell : rootList) {
                    String id = StringCustomUtils.replaceSpecialSymbols(mxCell.getPageId(), isSaveXml);
                    String parent = StringCustomUtils.replaceSpecialSymbols(mxCell.getParent(), isSaveXml);
                    String style = StringCustomUtils.replaceSpecialSymbols(mxCell.getStyle(), isSaveXml);
                    String value = StringCustomUtils.replaceSpecialSymbols(mxCell.getValue(), isSaveXml);
                    String vertex = StringCustomUtils.replaceSpecialSymbols(mxCell.getVertex(), isSaveXml);
                    String edge = StringCustomUtils.replaceSpecialSymbols(mxCell.getEdge(), isSaveXml);
                    String source = StringCustomUtils.replaceSpecialSymbols(mxCell.getSource(), isSaveXml);
                    String target = StringCustomUtils.replaceSpecialSymbols(mxCell.getTarget(), isSaveXml);
                    if (StringUtils.isBlank(id)) {
                        continue;
                    }
                    String isSplicedId = mxCellPageIds.get(id);
                    if (StringUtils.isNotBlank(isSplicedId)) {
                        continue;
                    }
                    mxCellPageIds.put(id, id);
                    xmlStrSb.append("<mxCell ");
                    xmlStrSb.append(spliceStr("id", id));
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
                        String relative = StringCustomUtils.replaceSpecialSymbols(mxGeometry.getRelative(), isSaveXml);
                        String as = StringCustomUtils.replaceSpecialSymbols(mxGeometry.getAs(), isSaveXml);
                        String x = StringCustomUtils.replaceSpecialSymbols(mxGeometry.getX(), isSaveXml);
                        String y = StringCustomUtils.replaceSpecialSymbols(mxGeometry.getY(), isSaveXml);
                        String width = StringCustomUtils.replaceSpecialSymbols(mxGeometry.getWidth(), isSaveXml);
                        String height = StringCustomUtils.replaceSpecialSymbols(mxGeometry.getHeight(), isSaveXml);
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


    public static TemplateType determineTemplateType(String xmlTemplateStr) {
        if (StringUtils.isBlank(xmlTemplateStr)) {
            return null;
        }
        Element flowGroupRecordEle = xmlStrToElementGetByKey(xmlTemplateStr, false, "flowGroup");
        if (null != flowGroupRecordEle) {
            return TemplateType.GROUP;
        }
        Element flowRecordEle = xmlStrToElementGetByKey(xmlTemplateStr, false, "flow");
        if (null != flowRecordEle) {
            return TemplateType.TASK;
        }
        return null;
    }

}

package com.nature.base.util;

import com.nature.component.mxGraph.vo.MxCellVo;
import com.nature.component.mxGraph.vo.MxGeometryVo;
import com.nature.component.mxGraph.vo.MxGraphModelVo;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MxGraphUtils {

    private static String spliceStr(String key, Object value) {
        return key + "=\"" + value + "\" ";
    }


    /**
     * "MxGraphModel" to string "xml"
     *
     * @param mxGraphModelVo mxGraphModelVo
     * @return String
     */
    public static String mxGraphModelToMxGraphXml(MxGraphModelVo mxGraphModelVo) {
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

}

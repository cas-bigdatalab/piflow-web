package com.nature.base.util;

import com.nature.common.Eunm.ArrowDirection;
import com.nature.common.Eunm.PortType;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGeometry;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.mxGraph.vo.MxCellVo;
import com.nature.component.mxGraph.vo.MxGeometryVo;
import com.nature.component.mxGraph.vo.MxGraphModelVo;
import com.nature.component.template.model.PropertyTemplateModel;
import com.nature.component.template.model.StopTemplateModel;
import com.nature.component.workFlow.model.*;
import com.nature.component.workFlow.utils.MxGraphModelUtil;
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


public class SvgUtils {

    static Logger logger = LoggerUtil.getLogger();


    /**
     * 把画板转换成svg图
     *
     * @param mxGraphModel
     * @return
     */
    public static String mxGraphModelToViewXml(MxGraphModel mxGraphModel) {
        String viewXml = "";
        if (null != mxGraphModel) {
            StringBuffer viewXmlStrBuf = new StringBuffer();
            viewXmlStrBuf.append("<svg style='width: 100%; height: 100%; display: block; min-width: 2391px; min-height: 2167px; position: absolute; background-image: none;'>");
            viewXmlStrBuf.append("<g>");
            List<MxCell> mxCellList = mxGraphModel.getRoot();
            if (null != mxCellList && mxCellList.size() > 0) {
                // 把需添加addMxCellVoList中的stops和线分开
                Map<String, Object> stopsPathsMap = MxGraphModelUtil.mxCellDistinguishStopsPaths(mxCellList);

                // 取出stops
                List<MxCell> stops = (List<MxCell>) stopsPathsMap.get("stops");
                Map<String, MxCell> stopPageIdKeyMap = new HashMap<String, MxCell>();
                if (null != stops && stops.size() > 0) {
                    // 放stops的g标签开始
                    viewXmlStrBuf.append("<g>");
                    // stop图片的坐标
                    double imageX = 0;
                    double imageY = 0;
                    for (MxCell mxCell : stops) {
                        if (null != mxCell) {
                            //stop的name
                            String name = (null != mxCell.getValue() ? mxCell.getValue() : "");
                            //取出样式
                            String style = mxCell.getStyle();
                            String imgPath = (null != style ? StringUtils.substringAfterLast(style, "=") : "");
                            MxGeometry mxGeometry = mxCell.getMxGeometry();
                            if (null != mxGeometry) {
                                //mxCell的x坐标
                                double mxGeometryX = (StringUtils.isNotBlank(mxGeometry.getX()) ? Double.parseDouble(mxGeometry.getX()) : 0);
                                //mxCell的y坐标
                                double mxGeometryY = (StringUtils.isNotBlank(mxGeometry.getY()) ? Double.parseDouble(mxGeometry.getY()) : 0);
                                //mxCell的高度
                                double mxGeometryHeight = (StringUtils.isNotBlank(mxGeometry.getHeight()) ? Double.parseDouble(mxGeometry.getHeight()) : 0);
                                //mxCell的宽度
                                double mxGeometryWidth = (StringUtils.isNotBlank(mxGeometry.getWidth()) ? Double.parseDouble(mxGeometry.getWidth()) : 0);
                                // stop 图片信息计算
                                // stop图片的坐标
                                imageX = mxGeometryX;
                                imageY = mxGeometryY;

                                // 点击的边框
                                viewXmlStrBuf.append("<g onclick=\"selectedFormation('" + mxCell.getPageId() + "',this)\">");//点击时选中效果坐标

                                // 开始拼接监控图标
                                // fail图标
                                viewXmlStrBuf.append("<g style='visibility: visible;'>");
                                viewXmlStrBuf.append("<image id='stopFailShow" + mxCell.getPageId() + "' style='display: none;'");
                                viewXmlStrBuf.append("x='" + (imageX + mxGeometryWidth) + "' ");//监控的图标为图片X坐标+宽
                                viewXmlStrBuf.append("y='" + imageY + "' ");//监控的图标为图片Y坐标
                                viewXmlStrBuf.append("width = '15' height = '15'");
                                viewXmlStrBuf.append("xlink:href = '/piflow-web/img/Fail.png'");//监控图标地址
                                viewXmlStrBuf.append(" ></image >");
                                // ok图标
                                viewXmlStrBuf.append("<image id='stopOkShow" + mxCell.getPageId() + "' style='display: none;'");
                                viewXmlStrBuf.append("x='" + (imageX + mxGeometryWidth) + "' ");//监控的图标为图片X坐标+宽
                                viewXmlStrBuf.append("y='" + imageY + "' ");//监控的图标为图片Y坐标
                                viewXmlStrBuf.append("width = '15' height = '15'");
                                viewXmlStrBuf.append("xlink:href = '/piflow-web/img/Ok.png'");//监控图标地址
                                viewXmlStrBuf.append(" ></image >");
                                // Loading图标
                                viewXmlStrBuf.append("<image id='stopLoadingShow" + mxCell.getPageId() + "' style='display: none;'");
                                viewXmlStrBuf.append("x='" + (imageX + mxGeometryWidth) + "' ");//监控的图标为图片X坐标+宽
                                viewXmlStrBuf.append("y='" + imageY + "' ");//监控的图标为图片Y坐标
                                viewXmlStrBuf.append("width = '15' height = '15'");
                                viewXmlStrBuf.append("xlink:href = '/piflow-web/img/Loading.gif'");//监控图标地址
                                viewXmlStrBuf.append(" ></image >");
                                viewXmlStrBuf.append("</g>");

                                // 开始拼图片
                                viewXmlStrBuf.append("<g style='visibility: visible;'>");
                                viewXmlStrBuf.append("<image id='stopImg" + mxCell.getPageId()+"' ");
                                viewXmlStrBuf.append("x='" + imageX + "' ");//图片X坐标
                                viewXmlStrBuf.append("y='" + imageY + "' ");//图片Y坐标
                                viewXmlStrBuf.append("width='" + mxGeometryWidth + "' ");//图片宽
                                viewXmlStrBuf.append("height='" + mxGeometryHeight + "' ");//图片高
                                viewXmlStrBuf.append("xlink:href='" + imgPath + "'");//图片地址
                                viewXmlStrBuf.append("></image>");
                                viewXmlStrBuf.append("</g>");
                                // stop 文字信息计算
                                // 字的坐标
                                double fontX = ((mxGeometryWidth - name.length() * 6) / 2) + imageX;
                                double fontY = imageY + mxGeometryHeight + 8;
                                double fontWidth = name.length() * 6;
                                double fontHeight = 12;
                                // 开始拼字
                                viewXmlStrBuf.append("<g transform='translate(" + fontX + "," + fontY + ")'>");//x和y坐标
                                viewXmlStrBuf.append("<foreignObject style='overflow:visible;' pointer-events='all' ");
                                viewXmlStrBuf.append("width='" + fontWidth + "' height='" + fontHeight + "'>");//宽度和高度
                                viewXmlStrBuf.append("<div style='display:inline-block;font-size:12px;font-family:Helvetica;color:#000000;line-height:1.2;vertical-align:top;white-space:nowrap;text-align:center;'>");
                                viewXmlStrBuf.append("<div xmlns='http://www.w3.org/1999/xhtml' style = 'display:inline-block;text-align:inherit;text-decoration:inherit;background-color:#ffffff;'>");
                                viewXmlStrBuf.append(name); //stop的name
                                viewXmlStrBuf.append("</div>");
                                viewXmlStrBuf.append("</div>");
                                viewXmlStrBuf.append("</foreignObject>");
                                viewXmlStrBuf.append("</g>");
                                viewXmlStrBuf.append("</g>");
                                // 把stop的mxCell放入map用于生成线的坐标
                                stopPageIdKeyMap.put(mxCell.getPageId(), mxCell);
                            }
                        }
                    }
                    // 放stops的g标签结束
                    viewXmlStrBuf.append("</g>");
                }
                // 取出paths
                List<MxCell> paths = (List<MxCell>) stopsPathsMap.get("paths");
                if (null != paths && paths.size() > 0) {
                    // 放paths的g标签开始
                    viewXmlStrBuf.append("<g>");
                    for (MxCell mxCell : paths) {
                        if (null != mxCell) {
                            MxCell sourceMxCell1 = stopPageIdKeyMap.get(mxCell.getSource());
                            MxCell targetMxCell1 = stopPageIdKeyMap.get(mxCell.getTarget());
                            if (null != sourceMxCell1 && null != targetMxCell1) {
                                MxGeometry sourceMxGeometry = sourceMxCell1.getMxGeometry();
                                MxGeometry targetMxGeometry = targetMxCell1.getMxGeometry();
                                if (null != sourceMxGeometry && null != targetMxGeometry) {
                                    String drawingLine = drawingLine(sourceMxCell1, targetMxCell1, mxCell.getPageId());
                                    if (StringUtils.isNotBlank(drawingLine)) {
                                        viewXmlStrBuf.append(drawingLine);
                                    }
                                }
                            }
                        }
                    }
                    // 放paths的g标签结束
                    viewXmlStrBuf.append("</g>");
                }
            }
            viewXmlStrBuf.append(" <g transform='translate(0,0)'>");
            viewXmlStrBuf.append("<rect id='selectedRectShow' x='0' y='0' width='66' height='66' fill='none' stroke='#00a8ff' stroke-dasharray='3 3' pointer-events='none' style='display: none;'></rect>");
            viewXmlStrBuf.append("<path id='selectedPathShow' d='M 0 0 L 0 0 ' fill='none' stroke='#00a8ff' stroke-width='5' stroke-miterlimit='10' style='display: none;'></path>");
            viewXmlStrBuf.append("<path id='selectedArrowShow'd='M 0 0 L 0 0 L 0 0 L 0 0 Z' fill='none' stroke='#00a8ff' stroke-width='5' stroke-miterlimit='00' pointer-events='all'></path>");
            viewXmlStrBuf.append("</g>");
            viewXmlStrBuf.append("</g>");
            viewXmlStrBuf.append("</svg>");
            viewXml = viewXmlStrBuf.toString();
        }
        return viewXml;
    }


    /**
     * 根据source属性和target属性画连接线
     *
     * @param sourceMxCell
     * @param sourceMxCell
     * @param pageID
     * @return
     */
    public static String drawingLine(MxCell sourceMxCell, MxCell targetMxCell, String pageID) {
        String lineSvg = "";
        if (null != sourceMxCell && null != targetMxCell) {
            MxGeometry sourceMxGeometry = sourceMxCell.getMxGeometry();
            MxGeometry targetMxGeometry = targetMxCell.getMxGeometry();
            if (null != sourceMxGeometry && null != targetMxGeometry) {
                // 取坐标和高宽参数
                String sourceXStr = sourceMxGeometry.getX();
                String sourceYStr = sourceMxGeometry.getY();
                String sourceWidthStr = sourceMxGeometry.getWidth();
                String sourceHeightStr = sourceMxGeometry.getHeight();
                String targetXStr = targetMxGeometry.getX();
                String targetYStr = targetMxGeometry.getY();
                String targetWidthStr = targetMxGeometry.getWidth();
                String targetHeightStr = targetMxGeometry.getHeight();
                // 参数判空
                if (!StringUtils.isAnyEmpty(sourceXStr, sourceYStr, sourceWidthStr, sourceHeightStr, targetXStr, targetYStr, targetWidthStr, targetHeightStr)) {
                    // 坐标和高宽参数转double
                    double sourceX = Double.parseDouble(sourceXStr);
                    double sourceY = Double.parseDouble(sourceYStr);
                    double sourceWidth = Double.parseDouble(sourceWidthStr);
                    double sourceHeight = Double.parseDouble(sourceHeightStr);
                    double targetX = Double.parseDouble(targetXStr);
                    double targetY = Double.parseDouble(targetYStr);
                    double targetWidth = Double.parseDouble(targetWidthStr);
                    double targetHeight = Double.parseDouble(targetHeightStr);
                    // 线的起点坐标
                    double sourceDotX = 0;
                    double sourceDotY = 0;
                    // 线的终点点坐标
                    double targetDotX = 0;
                    double targetDotY = 0;
                    // 箭头方向
                    ArrowDirection arrowDirection = null;

                    //线的生成规则如下：
                    //             ||           ||
                    //     E区     ||    A区    ||     F区
                    //             ||           ||
                    //=============||===========||=============
                    //             ||           ||
                    //     D区     ||   target  ||     B区
                    //             ||           ||
                    //=============||===========||=============
                    //             ||           ||
                    //     H区     ||     C区   ||     G区
                    //             ||           ||
                    //-----------------------------------------
                    // 以target为中心，source的位置分布在A到H的9个区中，通过source和target的位置判断出线入线方向，以此画线

                    // A区为下出上入，条件：sourceY < targetY 且 (targetX-source宽) <= sourceX <= (targetX+target宽)
                    // B区为左出右入，条件：sourceX > (targetX+target宽) 且 (targetY-source高) <= sourceY <= (targetY+target高)
                    // C区为上出下入，条件：sourceY >= targetY 且 (targetX-source宽) <= sourceX <= (targetX+target宽)
                    // D区为右出左入，条件：sourceX < (targetX-source宽) 且 (targetY-source高) <= sourceY <= (targetY+target高)
                    // E区为右出上入，条件：sourceX < (targetX-source宽) 且 sourceY < (targetY-source高)
                    // F区为左出上入，条件：sourceX > (targetX+target宽) 且 sourceY < (targetY-source高)
                    // G区为左出下入，条件：sourceX > (targetX+target宽) 且 sourceY > (targetY+target高)
                    // H区为右出下入，条件：sourceX < (targetX-source宽) 且 sourceY > (targetY+target高)
                    // 线的折点数,
                    // ABCD区线的折点数为0或2，breakPoint用0表示，
                    // EFGH区线的折点数为1,breakPoint用1表示，
                    int breakPoint = 0;
                    // 根据A到H区的条件判断求出出线点和入线点的坐标(起点终点)
                    if (sourceY < targetY && (targetX - sourceWidth) <= sourceX && sourceX <= (targetX + targetWidth)) {
                        // A区 下出上入
                        // 当source在A区时，起点为source下边的中心点，终点为target上边的中心点
                        sourceDotX = sourceX + (sourceWidth / 2);
                        sourceDotY = sourceY + sourceHeight;
                        targetDotX = targetX + (targetWidth / 2);
                        targetDotY = targetY;
                        arrowDirection = ArrowDirection.DOWN_DIRECTION;
                        breakPoint = 0;
                    } else if (sourceX > (targetX + targetWidth) && (targetY - sourceHeight) <= sourceY && sourceY <= (targetY + targetHeight)) {
                        // B区为左出右入
                        // 当source在B区时，起点为source左边的中心点，终点为target右边的中心点
                        sourceDotX = sourceX;
                        sourceDotY = sourceY + (sourceHeight / 2);
                        targetDotX = targetX + targetWidth;
                        targetDotY = targetY + (targetHeight / 2);
                        arrowDirection = ArrowDirection.LEFT_DIRECTION;
                        breakPoint = 0;
                    } else if (sourceY >= targetY && (targetX - sourceWidth) <= sourceX && sourceX <= (targetX + targetWidth)) {
                        // C区为上出下入
                        // 当source在C区时，起点为source上边的中心点，终点为target下边的中心点
                        sourceDotX = sourceX + (sourceWidth / 2);
                        sourceDotY = sourceY;
                        targetDotX = targetX + (targetWidth / 2);
                        targetDotY = targetY + targetHeight;
                        arrowDirection = ArrowDirection.UP_DIRECTION;
                        breakPoint = 0;
                    } else if (sourceX < (targetX - sourceWidth) && (targetY - sourceHeight) <= sourceY && sourceY <= (targetY + targetHeight)) {
                        // D区为右出左入
                        // 当source在D区时，起点为source右边的中心点，终点为target左边的中心点
                        sourceDotX = sourceX + sourceWidth;
                        sourceDotY = sourceY + (sourceHeight / 2);
                        targetDotX = targetX;
                        targetDotY = targetY + (targetHeight / 2);
                        arrowDirection = ArrowDirection.RIGHT_DIRECTION;
                        breakPoint = 0;
                    } else if (sourceX < (targetX - sourceWidth) && sourceY < (targetY - sourceHeight)) {
                        // E区为右出上入
                        // 当source在E区时，起点为source右边的中心点，终点为target上边的中心点
                        sourceDotX = sourceX + sourceWidth;
                        sourceDotY = sourceY + (sourceHeight / 2);
                        targetDotX = targetX + (targetWidth / 2);
                        targetDotY = targetY;
                        arrowDirection = ArrowDirection.DOWN_DIRECTION;
                        breakPoint = 1;
                    } else if (sourceX > (targetX + targetWidth) && sourceY < (targetY - sourceHeight)) {
                        // F区为左出上入
                        // 当source在F区时，起点为source左边的中心点，终点为target上边的中心点
                        sourceDotX = sourceX;
                        sourceDotY = sourceY + (sourceHeight / 2);
                        targetDotX = targetX + (targetWidth / 2);
                        targetDotY = targetY;
                        arrowDirection = ArrowDirection.DOWN_DIRECTION;
                        breakPoint = 1;
                    } else if (sourceX > (targetX + targetWidth) && sourceY > (targetY + targetHeight)) {
                        // G区为左出下入
                        // 当source在G区时，起点为source左边的中心点，终点为target下边的中心点
                        sourceDotX = sourceX;
                        sourceDotY = sourceY + (sourceHeight / 2);
                        targetDotX = targetX + (targetWidth / 2);
                        targetDotY = targetY + targetHeight;
                        arrowDirection = ArrowDirection.DOWN_DIRECTION;
                        breakPoint = 1;
                    } else if (sourceX < (targetX - sourceWidth) && sourceY > (targetY + targetHeight)) {
                        // H区为右出下入
                        // 当source在H区时，起点为source右边的中心点，终点为target下边的中心点
                        sourceDotX = sourceX + sourceWidth;
                        sourceDotY = sourceY + (sourceHeight / 2);
                        targetDotX = targetX + (targetWidth / 2);
                        targetDotY = targetY + targetHeight;
                        arrowDirection = ArrowDirection.DOWN_DIRECTION;
                        breakPoint = 1;
                    } else {
                        logger.warn("没有判断出位置信息，画线失败");
                        return lineSvg;
                    }
                    if (null != arrowDirection) {
                        // 开始画线
                        StringBuffer lineSvgBuf = new StringBuffer();
                        lineSvgBuf.append("<g name='stopPageId" + sourceMxCell.getPageId() + "' transform='translate(0,0)' style='visibility: visible;'");
                        lineSvgBuf.append("onclick=\"selectedPath('" + pageID + "',this)\">");
                        // 线开始计算
                        switch (breakPoint) {
                            case 0:
                                if (sourceDotX == targetDotX || sourceDotY == targetDotY) {
                                    //无折点，直接拼线
                                    lineSvgBuf.append("<path name='pathName' d='");
                                    lineSvgBuf.append("M " + sourceDotX + " " + sourceDotY + " ");
                                    lineSvgBuf.append("L " + targetDotX + " " + targetDotY + " ");
                                    lineSvgBuf.append("' fill='none' stroke='#000000' stroke-width='1' stroke-miterlimit='10'></path>");
                                } else {
                                    lineSvgBuf.append("<path name='pathName' d='");
                                    lineSvgBuf.append("M " + sourceDotX + " " + sourceDotY + " ");
                                    // 两个折点，计算折点坐标
                                    if (arrowDirection == ArrowDirection.UP_DIRECTION) {
                                        lineSvgBuf.append("L " + sourceDotX + " " + (((sourceDotY - targetDotY) / 2) + targetDotY) + " ");
                                        lineSvgBuf.append("L " + targetDotX + " " + (((sourceDotY - targetDotY) / 2) + targetDotY) + " ");
                                    } else if (arrowDirection == ArrowDirection.DOWN_DIRECTION) {
                                        lineSvgBuf.append("L " + sourceDotX + " " + (((targetDotY - sourceDotY) / 2) + sourceDotY) + " ");
                                        lineSvgBuf.append("L " + targetDotX + " " + (((targetDotY - sourceDotY) / 2) + sourceDotY) + " ");
                                    } else if (arrowDirection == ArrowDirection.LEFT_DIRECTION) {
                                        lineSvgBuf.append("L " + (((sourceDotX - targetDotX) / 2) + targetDotX) + " " + sourceDotY + " ");
                                        lineSvgBuf.append("L " + (((sourceDotX - targetDotX) / 2) + targetDotX) + " " + targetDotY + " ");
                                    } else if (arrowDirection == ArrowDirection.RIGHT_DIRECTION) {
                                        lineSvgBuf.append("L " + (((targetDotX - sourceDotX) / 2) + sourceDotX) + " " + sourceDotX + " ");
                                        lineSvgBuf.append("L " + (((targetDotX - sourceDotX) / 2) + sourceDotX) + " " + targetDotX + " ");
                                    }
                                    lineSvgBuf.append("L " + targetDotX + " " + targetDotY + " ");
                                    lineSvgBuf.append("' fill='none' stroke='#000000' stroke-width='1' stroke-miterlimit='10'></path>");
                                }
                                break;
                            case 1:
                                // 一个折点
                                lineSvgBuf.append("<path name='pathName' d='");
                                lineSvgBuf.append("M " + sourceDotX + " " + sourceDotY + " ");
                                lineSvgBuf.append("L " + targetDotX + " " + sourceDotY + " ");
                                lineSvgBuf.append("L " + targetDotX + " " + targetDotY + " ");
                                lineSvgBuf.append("' fill='none' stroke='#000000' stroke-width='1' stroke-miterlimit='10'></path>");
                                break;
                            default:
                                break;
                        }

                        // 箭头坐标开始计算
                        lineSvgBuf.append("<path name='arrowName' d='");
                        // 箭头是由一个M坐标三个L坐标组成，M坐标为箭头的指向的点
                        lineSvgBuf.append("M " + targetDotX + " " + targetDotY + " ");
                        // 第一个L箭头(右侧点)，
                        lineSvgBuf.append("L ");
                        // 第一个L(右侧点)的X
                        lineSvgBuf.append((targetDotX - (arrowDirection.getUpX() - arrowDirection.getRightX())) + " ");
                        // 第一个L(右侧点)的Y
                        lineSvgBuf.append((targetDotY - (arrowDirection.getUpY() - arrowDirection.getRightY())) + " ");
                        // 第二个L箭头(尾点)，
                        lineSvgBuf.append("L ");
                        // 第二个L(尾点)的X
                        lineSvgBuf.append((targetDotX - (arrowDirection.getUpX() - arrowDirection.getDownX())) + " ");
                        // 第二个L(尾点)的Y
                        lineSvgBuf.append((targetDotY - (arrowDirection.getUpY() - arrowDirection.getDownY())) + " ");
                        // 第三个L箭头(左侧点)，
                        lineSvgBuf.append("L ");
                        // 第三个L(左侧点)的X
                        lineSvgBuf.append((targetDotX - (arrowDirection.getUpX() - arrowDirection.getLfetX())) + " ");
                        // 第三个L(左侧点)的Y
                        lineSvgBuf.append((targetDotY - (arrowDirection.getUpY() - arrowDirection.getLfetY())) + " ");
                        lineSvgBuf.append("Z' fill='#000000' stroke='#000000' stroke-width='1' stroke-miterlimit='10' pointer-events='all'></path>");
                        lineSvgBuf.append("</g>");
                        lineSvg = lineSvgBuf.toString();
                    } else {
                        logger.warn("没有判断出箭头方向，箭头方向为空，画线失败");
                    }
                } else {
                    logger.warn("source或target的坐标或高宽为空，画线失败");
                }
            } else {
                logger.warn("参数有空值，画线失败");
            }
        } else {
            logger.warn("参数有空值，画线失败");
        }
        return lineSvg;
    }
}

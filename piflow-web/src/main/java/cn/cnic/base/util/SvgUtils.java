package cn.cnic.base.util;

import cn.cnic.common.Eunm.ArrowDirection;
import cn.cnic.component.mxGraph.model.MxCell;
import cn.cnic.component.mxGraph.model.MxGeometry;
import cn.cnic.component.mxGraph.model.MxGraphModel;
import cn.cnic.component.mxGraph.utils.MxGraphModelUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SvgUtils {

    static Logger logger = LoggerUtil.getLogger();


    /**
     * Convert the drawing board to svg
     *
     * @param mxGraphModel
     * @return
     */
    @SuppressWarnings("unchecked")
	public static String mxGraphModelToViewXml(MxGraphModel mxGraphModel, boolean isGroup, boolean isProject) {
        String viewXml = "";
        if (null != mxGraphModel) {
            StringBuffer viewXmlStrBuf = new StringBuffer();
            viewXmlStrBuf.append("<svg style='width: 100%; height: 100%; display: block; min-width: 2391px; min-height: 2167px; position: absolute; background-image: none;'>");
            viewXmlStrBuf.append("<g>");
            List<MxCell> mxCellList = mxGraphModel.getRoot();
            if (null != mxCellList && mxCellList.size() > 0) {
                // Separate the stops and lines that need to be added in addMxCellVoList
                Map<String, Object> stopsPathsMap = MxGraphModelUtils.mxCellDistinguishStopsPaths(mxCellList);

                // Take out stops
                List<MxCell> stops = (List<MxCell>) stopsPathsMap.get("stops");
                Map<String, MxCell> stopPageIdKeyMap = new HashMap<String, MxCell>();
                if (null != stops && stops.size() > 0) {
                    // Put the g tag of the stops to start
                    viewXmlStrBuf.append("<g>");
                    // Stop image coordinates
                    double imageX = 0;
                    double imageY = 0;
                    for (MxCell mxCell : stops) {
                        if (null != mxCell) {
                            //stop name
                            String name = (null != mxCell.getValue() ? mxCell.getValue() : "");
                            //Take out style
                            String style = mxCell.getStyle();
                            String imgPath = (null != style ? StringUtils.substringAfterLast(style, "=") : "");
                            MxGeometry mxGeometry = mxCell.getMxGeometry();
                            if (null != mxGeometry) {
                                //x coordinate of mxCell
                                double mxGeometryX = (StringUtils.isNotBlank(mxGeometry.getX()) ? Double.parseDouble(mxGeometry.getX()) : 0);
                                //y coordinate of mxCell
                                double mxGeometryY = (StringUtils.isNotBlank(mxGeometry.getY()) ? Double.parseDouble(mxGeometry.getY()) : 0);
                                //mxCell height
                                double mxGeometryHeight = (StringUtils.isNotBlank(mxGeometry.getHeight()) ? Double.parseDouble(mxGeometry.getHeight()) : 0);
                                //mxCell width
                                double mxGeometryWidth = (StringUtils.isNotBlank(mxGeometry.getWidth()) ? Double.parseDouble(mxGeometry.getWidth()) : 0);
                                // Stop picture information calculation
                                // Stop image coordinates
                                imageX = mxGeometryX;
                                imageY = mxGeometryY;

                                // Clicked border
                                viewXmlStrBuf.append("<g ");
                                viewXmlStrBuf.append("onclick=\"selectedFormation('" + mxCell.getPageId() + "',this)\"");//
                                if (isGroup) {
                                    viewXmlStrBuf.append("ondblclick=\"openProcessMonitor('" + mxCell.getPageId() + "',this)\"");//
                                }
                                viewXmlStrBuf.append(" >");//Select the effect coordinates when clicked

                                // Start stitching monitor icon
                                // Fail icon
                                viewXmlStrBuf.append("<g style='visibility: visible;'>");
                                viewXmlStrBuf.append("<image id='stopFailShow" + mxCell.getPageId() + "' style='display: none;'");
                                viewXmlStrBuf.append("x='" + (imageX + mxGeometryWidth) + "' ");//The monitored icon is the picture X coordinate + width
                                viewXmlStrBuf.append("y='" + imageY + "' ");//The monitored icon is the picture Y coordinate
                                viewXmlStrBuf.append("width = '15' height = '15'");
                                viewXmlStrBuf.append("xlink:href = '/piflow-web/img/Fail.png'");//Monitor icon address
                                viewXmlStrBuf.append(" ></image >");
                                // ok图标
                                viewXmlStrBuf.append("<image id='stopOkShow" + mxCell.getPageId() + "' style='display: none;'");
                                viewXmlStrBuf.append("x='" + (imageX + mxGeometryWidth) + "' ");//The monitored icon is the picture X coordinate + width
                                viewXmlStrBuf.append("y='" + imageY + "' ");//The monitored icon is the picture Y coordinate
                                viewXmlStrBuf.append("width = '15' height = '15'");
                                viewXmlStrBuf.append("xlink:href = '/piflow-web/img/Ok.png'");//Monitor icon address
                                viewXmlStrBuf.append(" ></image >");
                                // Loading图标
                                viewXmlStrBuf.append("<image id='stopLoadingShow" + mxCell.getPageId() + "' style='display: none;'");
                                viewXmlStrBuf.append("x='" + (imageX + mxGeometryWidth) + "' ");//The monitored icon is the picture X coordinate + width
                                viewXmlStrBuf.append("y='" + imageY + "' ");//The monitored icon is the picture Y coordinate
                                viewXmlStrBuf.append("width = '15' height = '15'");
                                viewXmlStrBuf.append("xlink:href = '/piflow-web/img/Loading.gif'");//Monitor icon address
                                viewXmlStrBuf.append(" ></image >");
                                viewXmlStrBuf.append("</g>");

                                // 开始拼图片
                                viewXmlStrBuf.append("<g style='visibility: visible;'>");
                                viewXmlStrBuf.append("<image id='stopImg" + mxCell.getPageId() + "' ");
                                viewXmlStrBuf.append("x='" + imageX + "' ");//Picture X coordinate
                                viewXmlStrBuf.append("y='" + imageY + "' ");//Picture Y coordinate
                                viewXmlStrBuf.append("width='" + mxGeometryWidth + "' ");//Picture width
                                viewXmlStrBuf.append("height='" + mxGeometryHeight + "' ");//Picture height
                                viewXmlStrBuf.append("xlink:href='" + imgPath + "'");//Image address
                                viewXmlStrBuf.append("></image>");
                                viewXmlStrBuf.append("</g>");
                                // Stop text information calculation
                                // Word coordinates
                                double fontX = ((mxGeometryWidth - name.length() * 6) / 2) + imageX;
                                double fontY = imageY + mxGeometryHeight + 8;
                                double fontWidth = name.length() * 6;
                                double fontHeight = 12;
                                // Start spelling
                                viewXmlStrBuf.append("<g transform='translate(" + fontX + "," + fontY + ")'>");//x和y坐标
                                viewXmlStrBuf.append("<foreignObject style='overflow:visible;' pointer-events='all' ");
                                viewXmlStrBuf.append("width='" + fontWidth + "' height='" + fontHeight + "'>");//宽度和高度
                                viewXmlStrBuf.append("<div style='display:inline-block;font-size:12px;font-family:Helvetica;color:#666666;line-height:1.2;vertical-align:top;white-space:nowrap;text-align:center;'>");
                                viewXmlStrBuf.append("<div xmlns='http://www.w3.org/1999/xhtml' style = 'display:inline-block;text-align:inherit;text-decoration:inherit;background-color:#ffffff;'>");
                                viewXmlStrBuf.append(name); //stop的name
                                viewXmlStrBuf.append("</div>");
                                viewXmlStrBuf.append("</div>");
                                viewXmlStrBuf.append("</foreignObject>");
                                viewXmlStrBuf.append("</g>");
                                viewXmlStrBuf.append("</g>");
                                // Put the stop mxCell into the map to generate the coordinates of the line
                                stopPageIdKeyMap.put(mxCell.getPageId(), mxCell);
                            }
                        }
                    }
                    // Put the G tag of Stops to end
                    viewXmlStrBuf.append("</g>");
                }
                // Take out paths
                List<MxCell> paths = (List<MxCell>) stopsPathsMap.get("paths");
                if (null != paths && paths.size() > 0) {
                    // Put the g label of the path to start
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
                    // Put the g label of the path to end
                    viewXmlStrBuf.append("</g>");
                }
            }
            viewXmlStrBuf.append(" <g transform='translate(0,0)'>");
            viewXmlStrBuf.append("<rect id='selectedRectShow' x='0' y='0' width='66' height='66' fill='none' stroke='#7bc89e' stroke-dasharray='3 3' pointer-events='none' style='display: none;'></rect>");
            viewXmlStrBuf.append("<path id='selectedPathShow' d='M 0 0 L 0 0 ' fill='none' stroke='#7bc89e' stroke-width='5' stroke-miterlimit='10' style='display: none;'></path>");
            viewXmlStrBuf.append("<path id='selectedArrowShow'd='M 0 0 L 0 0 L 0 0 L 0 0 Z' fill='none' stroke='#7bc89e' stroke-width='5' stroke-miterlimit='00' pointer-events='all'></path>");
            viewXmlStrBuf.append("</g>");
            viewXmlStrBuf.append("</g>");
            viewXmlStrBuf.append("</svg>");
            viewXml = viewXmlStrBuf.toString();
        }
        return viewXml;
    }


    /**
     * Draw a connection line based on the source attribute and the target attribute
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
                // Take coordinates and height and width parameters
                String sourceXStr = sourceMxGeometry.getX();
                String sourceYStr = sourceMxGeometry.getY();
                String sourceWidthStr = sourceMxGeometry.getWidth();
                String sourceHeightStr = sourceMxGeometry.getHeight();
                String targetXStr = targetMxGeometry.getX();
                String targetYStr = targetMxGeometry.getY();
                String targetWidthStr = targetMxGeometry.getWidth();
                String targetHeightStr = targetMxGeometry.getHeight();
                // Determine if the parameter is empty
                if (!StringUtils.isAnyEmpty(sourceXStr, sourceYStr, sourceWidthStr, sourceHeightStr, targetXStr, targetYStr, targetWidthStr, targetHeightStr)) {
                    // Coordinates and height and width parameters to Double
                    double sourceX = Double.parseDouble(sourceXStr);
                    double sourceY = Double.parseDouble(sourceYStr);
                    double sourceWidth = Double.parseDouble(sourceWidthStr);
                    double sourceHeight = Double.parseDouble(sourceHeightStr);
                    double targetX = Double.parseDouble(targetXStr);
                    double targetY = Double.parseDouble(targetYStr);
                    double targetWidth = Double.parseDouble(targetWidthStr);
                    double targetHeight = Double.parseDouble(targetHeightStr);
                    // Starting point coordinates of the line
                    double sourceDotX = 0;
                    double sourceDotY = 0;
                    // Line end point coordinates
                    double targetDotX = 0;
                    double targetDotY = 0;
                    // Arrow direction
                    ArrowDirection arrowDirection = null;

                    //The rules for generating lines are as follows：
                    //             ||           ||
                    //   Area E    ||  Area A   ||  Area F
                    //             ||           ||
                    //=============||===========||=============
                    //             ||           ||
                    //   Area D    ||  target  ||   Area B
                    //             ||           ||
                    //=============||===========||=============
                    //             ||           ||
                    //   Area H    ||  Area C   ||   Area G
                    //             ||           ||
                    //-----------------------------------------
                    // Centered on the target, the location of the source is distributed in the 9 areas from A to H. The direction of the line entry is judged by the position of the source and the target.

                    // Area A is down out and up in,Condition: sourceY < targetY and (targetX-source width) <= sourceX <= (targetX+target width)
                    // Area B is left out and right in,Condition: sourceX > (targetX+target wide) and (targetY-source high) <= sourceY <= (targetY+target high)
                    // Area C is up out and down in,Condition: sourceY >= targetY and (targetX-source width) <= sourceX <= (targetX+target width)
                    // Area D is right out and left in,Condition: sourceX < (targetX-source wide) and (targetY-source high) <= sourceY <= (targetY+target high)
                    // Area E is right out and up in,Condition: sourceX < (targetX-source wide) and sourceY < (targetY-source high)
                    // Area F is left out and up in,Condition: sourceX > (targetX+target wide) and sourceY < (targetY-source high)
                    // Area G is left out and down in,Condition: sourceX > (targetX+target wide) and sourceY > (targetY+target high)
                    // Area H is right out and down in,Condition: sourceX < (targetX-source wide) and sourceY > (targetY+target high)
                    // Line break point,
                    // The number of vertices in the ABCD area is 0 or 2, and the breakPoint is represented by 0.
                    // The number of vertices in the EFGH zone is 1, and the breakPoint is represented by 1.
                    int breakPoint = 0;
                    // Determine the coordinates of the line point and the line point based on the conditions of the A to H area (starting point and end point)
                    if (sourceY < targetY && (targetX - sourceWidth) <= sourceX && sourceX <= (targetX + targetWidth)) {
                        // Area A down out and up in
                        // When the source is in the A area, the starting point is the center point below the source, and the ending point is the center point above the target.
                        sourceDotX = sourceX + (sourceWidth / 2);
                        sourceDotY = sourceY + sourceHeight;
                        targetDotX = targetX + (targetWidth / 2);
                        targetDotY = targetY;
                        arrowDirection = ArrowDirection.DOWN_DIRECTION;
                        breakPoint = 0;
                    } else if (sourceX > (targetX + targetWidth) && (targetY - sourceHeight) <= sourceY && sourceY <= (targetY + targetHeight)) {
                        // Area B is left out and right in
                        // When the source is in the B area, the starting point is the center point to the left of the source, and the ending point is the center point to the right of the target.
                        sourceDotX = sourceX;
                        sourceDotY = sourceY + (sourceHeight / 2);
                        targetDotX = targetX + targetWidth;
                        targetDotY = targetY + (targetHeight / 2);
                        arrowDirection = ArrowDirection.LEFT_DIRECTION;
                        breakPoint = 0;
                    } else if (sourceY >= targetY && (targetX - sourceWidth) <= sourceX && sourceX <= (targetX + targetWidth)) {
                        // Area C is up out and down in
                        // When the source is in the C area, the starting point is the center point on the source and the end point is the center point below the target.
                        sourceDotX = sourceX + (sourceWidth / 2);
                        sourceDotY = sourceY;
                        targetDotX = targetX + (targetWidth / 2);
                        targetDotY = targetY + targetHeight;
                        arrowDirection = ArrowDirection.UP_DIRECTION;
                        breakPoint = 0;
                    } else if (sourceX < (targetX - sourceWidth) && (targetY - sourceHeight) <= sourceY && sourceY <= (targetY + targetHeight)) {
                        // Area D is right out and left in
                        // When the source is in the D zone, the starting point is the center point to the right of the source, and the ending point is the center point to the left of the target.
                        sourceDotX = sourceX + sourceWidth;
                        sourceDotY = sourceY + (sourceHeight / 2);
                        targetDotX = targetX;
                        targetDotY = targetY + (targetHeight / 2);
                        arrowDirection = ArrowDirection.RIGHT_DIRECTION;
                        breakPoint = 0;
                    } else if (sourceX < (targetX - sourceWidth) && sourceY < (targetY - sourceHeight)) {
                        // Area E is right out and up in
                        // When the source is in the E zone, the starting point is the center point on the right side of the source, and the end point is the center point on the top side of the target.
                        sourceDotX = sourceX + sourceWidth;
                        sourceDotY = sourceY + (sourceHeight / 2);
                        targetDotX = targetX + (targetWidth / 2);
                        targetDotY = targetY;
                        arrowDirection = ArrowDirection.DOWN_DIRECTION;
                        breakPoint = 1;
                    } else if (sourceX > (targetX + targetWidth) && sourceY < (targetY - sourceHeight)) {
                        // Area F is left out and up in
                        // 当source在F区时，起点为source左边的中心点，终点为target上边的中心点
                        sourceDotX = sourceX;
                        sourceDotY = sourceY + (sourceHeight / 2);
                        targetDotX = targetX + (targetWidth / 2);
                        targetDotY = targetY;
                        arrowDirection = ArrowDirection.DOWN_DIRECTION;
                        breakPoint = 1;
                    } else if (sourceX > (targetX + targetWidth) && sourceY > (targetY + targetHeight)) {
                        // Area G is left out and down in
                        // When the source is in the G zone, the starting point is the center point to the left of the source, and the ending point is the center point below the target.
                        sourceDotX = sourceX;
                        sourceDotY = sourceY + (sourceHeight / 2);
                        targetDotX = targetX + (targetWidth / 2);
                        targetDotY = targetY + targetHeight;
                        arrowDirection = ArrowDirection.UP_DIRECTION;
                        breakPoint = 1;
                    } else if (sourceX < (targetX - sourceWidth) && sourceY > (targetY + targetHeight)) {
                        // Area H is right out and down in
                        // When the source is in the H zone, the starting point is the center point on the right side of the source, and the end point is the center point below the target.
                        sourceDotX = sourceX + sourceWidth;
                        sourceDotY = sourceY + (sourceHeight / 2);
                        targetDotX = targetX + (targetWidth / 2);
                        targetDotY = targetY + targetHeight;
                        arrowDirection = ArrowDirection.UP_DIRECTION;
                        breakPoint = 1;
                    } else {
                        logger.warn("Did not judge the location information, the line failed");
                        return lineSvg;
                    }
                    if (null != arrowDirection) {
                        // Start drawing lines
                        StringBuffer lineSvgBuf = new StringBuffer();
                        lineSvgBuf.append("<g name='stopPageId" + sourceMxCell.getPageId() + "' transform='translate(0,0)' style='visibility: visible;'");
                        lineSvgBuf.append("onclick=\"selectedPath('" + pageID + "',this)\">");
                        // Line start calculation
                        switch (breakPoint) {
                            case 0:
                                if (sourceDotX == targetDotX || sourceDotY == targetDotY) {
                                    //No breakpoint, direct line
                                    lineSvgBuf.append("<path name='pathName' d='");
                                    lineSvgBuf.append("M " + sourceDotX + " " + sourceDotY + " ");
                                    lineSvgBuf.append("L " + targetDotX + " " + targetDotY + " ");
                                    lineSvgBuf.append("' fill='none' stroke='#666666' stroke-width='1' stroke-miterlimit='10'></path>");
                                } else {
                                    lineSvgBuf.append("<path name='pathName' d='");
                                    lineSvgBuf.append("M " + sourceDotX + " " + sourceDotY + " ");
                                    // Two vertices, calculate the vertices coordinates
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
                                        lineSvgBuf.append("L " + (((targetDotX - sourceDotX) / 2) + sourceDotX) + " " + sourceDotY + " ");
                                        lineSvgBuf.append("L " + (((targetDotX - sourceDotX) / 2) + sourceDotX) + " " + targetDotY + " ");
                                    }
                                    lineSvgBuf.append("L " + targetDotX + " " + targetDotY + " ");
                                    lineSvgBuf.append("' fill='none' stroke='#666666' stroke-width='1' stroke-miterlimit='10'></path>");
                                }
                                break;
                            case 1:
                                // a break point
                                lineSvgBuf.append("<path name='pathName' d='");
                                lineSvgBuf.append("M " + sourceDotX + " " + sourceDotY + " ");
                                lineSvgBuf.append("L " + targetDotX + " " + sourceDotY + " ");
                                lineSvgBuf.append("L " + targetDotX + " " + targetDotY + " ");
                                lineSvgBuf.append("' fill='none' stroke='#666666' stroke-width='1' stroke-miterlimit='10'></path>");
                                break;
                            default:
                                break;
                        }

                        // Arrow coordinates start to calculate
                        lineSvgBuf.append("<path name='arrowName' d='");
                        // The arrow is composed of one M coordinate and three L coordinates, and the M coordinate is the point pointed by the arrow.
                        lineSvgBuf.append("M " + targetDotX + " " + targetDotY + " ");
                        // The first L arrow (the right point),
                        lineSvgBuf.append("L ");
                        // The first L (right point) X
                        lineSvgBuf.append((targetDotX - (arrowDirection.getUpX() - arrowDirection.getRightX())) + " ");
                        // The first L (right point) Y
                        lineSvgBuf.append((targetDotY - (arrowDirection.getUpY() - arrowDirection.getRightY())) + " ");
                        // The second L arrow (the end point),
                        lineSvgBuf.append("L ");
                        // The second L (tail point) X
                        lineSvgBuf.append((targetDotX - (arrowDirection.getUpX() - arrowDirection.getDownX())) + " ");
                        // The second L (tail point) Y
                        lineSvgBuf.append((targetDotY - (arrowDirection.getUpY() - arrowDirection.getDownY())) + " ");
                        // The third L arrow (left point),
                        lineSvgBuf.append("L ");
                        // The third L (left point) X
                        lineSvgBuf.append((targetDotX - (arrowDirection.getUpX() - arrowDirection.getLfetX())) + " ");
                        // The third L (tail point) Y
                        lineSvgBuf.append((targetDotY - (arrowDirection.getUpY() - arrowDirection.getLfetY())) + " ");
                        lineSvgBuf.append("Z' fill='#666666' stroke='#666666' stroke-width='1' stroke-miterlimit='10' pointer-events='all'></path>");
                        lineSvgBuf.append("</g>");
                        lineSvg = lineSvgBuf.toString();
                    } else {
                        logger.warn("Did not judge the direction of the arrow, the direction of the arrow is empty, the line failed");
                    }
                } else {
                    logger.warn("The coordinates or height and width of the source or target are empty, and the line fails.");
                }
            } else {
                logger.warn("The parameter has a null value and the line fails.");
            }
        } else {
            logger.warn("The parameter has a null value and the line fails.");
        }
        return lineSvg;
    }
}

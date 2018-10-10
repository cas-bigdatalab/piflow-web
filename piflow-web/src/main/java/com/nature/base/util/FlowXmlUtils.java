package com.nature.base.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.xml.sax.InputSource;

import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGeometry;
import com.nature.component.mxGraph.model.MxGraphModel;

public class FlowXmlUtils {

	static Logger logger = LoggerUtil.getLogger();

	/**
	 * MxGraphModel转字符串的xml
	 * 
	 * @param mxGraphModel
	 * @return
	 */
	public static String mxGraphModelToXml(MxGraphModel mxGraphModel) {
		// 拼xml注意一定要写空格
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
					if (StringUtils.isNotBlank(parent)) {
						xmlStrSb.append("parent=\"" + parent + "\" ");
					}
					if (StringUtils.isNotBlank(style)) {
						xmlStrSb.append("style=\"" + style + "\" ");
					}
					if (StringUtils.isNotBlank(value)) {
						xmlStrSb.append("value=\"" + value + "\" ");
					}
					if (StringUtils.isNotBlank(vertex)) {
						xmlStrSb.append("vertex=\"" + vertex + "\" ");
					}
					if (StringUtils.isNotBlank(edge)) {
						xmlStrSb.append("edge=\"" + edge + "\" ");
					}
					if (StringUtils.isNotBlank(source)) {
						xmlStrSb.append("source=\"" + source + "\" ");
					}
					if (StringUtils.isNotBlank(target)) {
						xmlStrSb.append("target=\"" + target + "\" ");
					}
					if (null != mxGeometry) {
						String relative = mxGeometry.getRelative();
						String as = mxGeometry.getAs();
						String x = mxGeometry.getX();
						String y = mxGeometry.getY();
						String width = mxGeometry.getWidth();
						String height = mxGeometry.getHeight();
						xmlStrSb.append("><mxGeometry ");
						if (StringUtils.isNotBlank(relative)) {
							xmlStrSb.append("relative=\"" + relative + "\" ");
						}
						if (StringUtils.isNotBlank(as)) {
							xmlStrSb.append("as=\"" + as + "\" ");
						}
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
	 * String类型的xml转MxGraphModel
	 * 
	 * @param xmldata
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static MxGraphModel xmlToMxGraphModel(String xmldata) {
		String transformation = "<nature>" + xmldata + "</nature>";
		MxGraphModel mxGraphModelVo = new MxGraphModel();
		InputSource in = new InputSource(new StringReader(transformation));
		in.setEncoding("UTF-8");
		SAXReader reader = new SAXReader();
		Document document;
		try {
			document = reader.read(in);
			// 获取所有拥有autoSaveNode属性的mxCell节点
			Element rootElt = document.getRootElement(); // 获取根节点
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
			List<MxCell> rootList = new ArrayList<MxCell>();
			Element rootjd = mxGraphModel.element("root");
			Iterator rootiter = rootjd.elementIterator("mxCell"); // 获取根节点下的子节点mxCell
			while (rootiter.hasNext()) {
				MxCell mxCellVo = new MxCell();
				MxGeometry mxGeometryVo = null;
				Element recordEle = (Element) rootiter.next();
				String mxCellId = recordEle.attributeValue("id");
				String parent = recordEle.attributeValue("parent");
				String style = recordEle.attributeValue("style");
				String edge = recordEle.attributeValue("edge");
				String source = recordEle.attributeValue("source");
				String target = recordEle.attributeValue("target");
				String value = recordEle.attributeValue("value");
				String vertex = recordEle.attributeValue("vertex");
				mxCellVo.setPageId(mxCellId);
				mxCellVo.setParent(parent);
				mxCellVo.setStyle(style);
				mxCellVo.setEdge(edge);
				mxCellVo.setSource(source);
				mxCellVo.setTarget(target);
				mxCellVo.setValue(value);
				mxCellVo.setVertex(vertex);
				if (StringUtils.isNotBlank(style)) {
					mxGeometryVo = new MxGeometry();
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
				mxCellVo.setMxGeometry(mxGeometryVo);
				rootList.add(mxCellVo);
			}
			mxGraphModelVo.setRoot(rootList);
		} catch (Exception e) {
			logger.error("转换失败", e);
			return null;
		}
		return mxGraphModelVo;
	}
}

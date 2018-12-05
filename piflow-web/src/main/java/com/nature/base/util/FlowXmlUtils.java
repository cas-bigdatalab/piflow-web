package com.nature.base.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.xml.sax.InputSource;

import com.nature.common.Eunm.PortType;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGeometry;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.mxGraph.vo.MxCellVo;
import com.nature.component.mxGraph.vo.MxGeometryVo;
import com.nature.component.mxGraph.vo.MxGraphModelVo;
import com.nature.component.template.model.PropertyTemplateModel;
import com.nature.component.template.model.StopTemplateModel;
import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.Property;
import com.nature.component.workFlow.model.Stops;
import com.nature.component.workFlow.model.Template;


public class FlowXmlUtils {

	static Logger logger = LoggerUtil.getLogger();

	/**
	 * mxGraphModel转mxGraphModelVo
	 *
	 * @param mxGraphModel
	 * @return
	 */
	public static MxGraphModelVo mxGraphModelPoToVo(MxGraphModel mxGraphModel) {
		MxGraphModelVo mxGraphModelVo = null;
		// 判空mxGraphModel
		if (null != mxGraphModel) {
			mxGraphModelVo = new MxGraphModelVo();
			// 拷贝mxGraphModel的内容到mxGraphModelVo中
			BeanUtils.copyProperties(mxGraphModel, mxGraphModelVo);
			// 取出mxCellList
			List<MxCell> root = mxGraphModel.getRoot();
			// 判空
			if (null != root && root.size() > 0) {
				List<MxCellVo> mxCellVoList = new ArrayList<MxCellVo>();
				// 循环拷贝
				for (MxCell mxCell : root) {
					if (null != mxCell) {
						MxCellVo mxCellVo = new MxCellVo();
						// 拷贝mxGraphModel的内容到mxGraphModelVo中
						BeanUtils.copyProperties(mxCell, mxCellVo);
						MxGeometry mxGeometry = mxCell.getMxGeometry();
						if (null != mxGeometry) {
							MxGeometryVo mxGeometryVo = new MxGeometryVo();
							// 拷贝mxGeometry的内容到mxGeometryVo中
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
	 * MxGraphModel转字符串的xml
	 * 
	 * @param mxGraphModelVo
	 * @return
	 */
	public static String mxGraphModelToXml(MxGraphModelVo mxGraphModelVo) {
		// 拼xml注意一定要写空格
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
	 * String类型的xml转MxGraphModel
	 * 
	 * @param xmldata
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static MxGraphModelVo xmlToMxGraphModel(String xmldata) {
		String transformation = "<nature>" + xmldata + "</nature>";
		MxGraphModelVo mxGraphModelVo = new MxGraphModelVo();
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
			List<MxCellVo> rootVoList = new ArrayList<MxCellVo>();
			Element rootjd = mxGraphModel.element("root");
			Iterator rootiter = rootjd.elementIterator("mxCell"); // 获取根节点下的子节点mxCell
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
				if("1".equals(edge)){
					if(StringUtils.isBlank(source)||StringUtils.isBlank(target)){
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
			logger.error("转换失败", e);
			return null;
		}
		return mxGraphModelVo;
	}
	
	
	/**
	 * flow转字符串的xml
	 * 
	 * @param flow
	 * @return
	 */
	public static String flowAndStopInfoToXml(Flow flow,String xmlStr) {
		// 拼xml注意一定要写空格
		if (null != flow) {
			StringBuffer xmlStrSb = new StringBuffer();
			String id = flow.getId();
			String name = flow.getName();
			String description = flow.getDescription();
			xmlStrSb.append("<flow ");
			if (StringUtils.isNotBlank(id)) {
				xmlStrSb.append("id=\"" + id + "\" ");
			}
			if (StringUtils.isNotBlank(name)) {
				xmlStrSb.append("name=\"" + name + "\" ");
			}
			if (StringUtils.isNotBlank(description)) {
				xmlStrSb.append("description=\"" + description + "\" ");
			}
			xmlStrSb.append("> \n");
			List<Stops> stopsVoList = flow.getStopsList();
			if (null != stopsVoList && stopsVoList.size() > 0) {
				for (Stops stopVo : stopsVoList) {
					String stopId = stopVo.getId();
					String pageId = stopVo.getPageId();
					String stopName = stopVo.getName();
					String bundel = stopVo.getBundel();
					String stopDescription = stopVo.getDescription();
					Boolean checkpoint = stopVo.getCheckpoint();
					String inports = stopVo.getInports();
					PortType inPortType = stopVo.getInPortType();
					String outports = stopVo.getOutports();
					PortType outPortType = stopVo.getOutPortType();
					String owner = stopVo.getOwner();
					xmlStrSb.append("<stop ");
					if (StringUtils.isNotBlank(stopId)) {
						xmlStrSb.append("id=\"" + stopId + "\" ");
					}
					if (StringUtils.isNotBlank(pageId)) {
						xmlStrSb.append("pageId=\"" + pageId + "\" ");
					}
					if (StringUtils.isNotBlank(stopName)) {
						xmlStrSb.append("name=\"" + stopName + "\" ");
					}
					if (StringUtils.isNotBlank(bundel)) {
						xmlStrSb.append("bundel=\"" + bundel + "\" ");
					}
					if (StringUtils.isNotBlank(stopDescription)) {
						xmlStrSb.append("description=\"" + stopDescription + "\" ");
					}
					if (null != checkpoint) {
		                int ischeckpoint = checkpoint ? 1 : 0;
		                xmlStrSb.append("isCheckpoint=\"" + ischeckpoint + "\" ");
		            }
					if (StringUtils.isNotBlank(inports)) {
						xmlStrSb.append("inports=\"" + inports + "\" ");
					}
					
					if (StringUtils.isNotBlank(outports)) {
						xmlStrSb.append("outports=\"" + outports + "\" ");
					}
					if (StringUtils.isNotBlank(owner)) {
						xmlStrSb.append("owner=\"" + owner + "\" ");
					}
					if (null != inPortType) {
						xmlStrSb.append("inPortType=\"" + inPortType + "\" ");
					}
					if (null != outPortType) {
						xmlStrSb.append("outPortType=\"" + outPortType + "\" ");
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
						if (StringUtils.isNotBlank(propertyId)) {
							xmlStrSb.append("id=\"" + propertyId + "\" ");
						}
						if (StringUtils.isNotBlank(displayName)) {
							xmlStrSb.append("displayName=\"" + displayName + "\" ");
						}
						if (StringUtils.isNotBlank(propertyName)) {
							xmlStrSb.append("name=\"" + propertyName + "\" ");
						}
						if (StringUtils.isNotBlank(propertyDescription)) {
							xmlStrSb.append("description=\"" + propertyDescription + "\" ");
						}
						if (StringUtils.isNotBlank(allowableValues)) {
							xmlStrSb.append("allowableValues=\"" + allowableValues.replaceAll("\"", "") + "\" ");
						}
						if (StringUtils.isNotBlank(customValue)) {
							xmlStrSb.append("customValue=\"" + customValue + "\" ");
						}
							xmlStrSb.append("required=\"" + required + "\" ");
							xmlStrSb.append("sensitive=\"" + sensitive + "\" ");
							xmlStrSb.append("/> \n");
						}
						xmlStrSb.append("</stop> \n");
					} else {
						xmlStrSb.append("/> \n");
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
	 * String类型的xml转stop等信息
	 * 
	 * @param xmldata
	 * @return
	 * @throws DocumentException 
	 */
	@SuppressWarnings("rawtypes")
	public static Template xmlToFlowStopInfo(String xmldata) {
		Document document = null;
		try {
			document = DocumentHelper.parseText(xmldata);
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
		String strXml = document.getRootElement().asXML();
		String transformation = "<sdds>"+strXml + "</sdds>";
		InputSource in = new InputSource(new StringReader(transformation));
		in.setEncoding("UTF-8");
		SAXReader reader = new SAXReader();
		List<StopTemplateModel> stopVoList = new ArrayList<StopTemplateModel>();
		
		Template template = new Template();
		try {
			document = reader.read(in);
			// 获取所有拥有autoSaveNode属性的mxCell节点
			Element rootElt = document.getRootElement(); // 获取根节点
			Element flow = rootElt.element("flow");
			Iterator rootiter = flow.elementIterator("stop"); // 获取根节点下的子节点stop
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
				stopVo.setPageId(pageId);
				stopVo.setName(name);
				stopVo.setDescription(description);
				stopVo.setBundel(bundel);
				stopVo.setId(id);
				stopVo.setInports(inports);
				stopVo.setOutports(outports);
				stopVo.setOutPortType(PortType.selectGender(outPortType));
				stopVo.setInPortType(PortType.selectGenderByValue(inPortType));
				Boolean Checkpoint = false;
				Checkpoint =  "0".equals(isCheckpoint) ? false : true;
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
						Boolean required = propertyValue.attributeValue("required").equals("1") ? true : false;
						Boolean sensitive = propertyValue.attributeValue("sensitive").equals("1") ? true : false;
						propertyVo.setAllowableValues(allowableValues);
						propertyVo.setCustomValue(customValue);
						propertyVo.setDescription(propertyDescription);
						propertyVo.setDisplayName(displayName);
						propertyVo.setId(propertyId);
						propertyVo.setName(propertyName);
						propertyVo.setRequired(required);
						propertyVo.setSensitive(sensitive);
						propertyVo.setStopsVo(stopVo);
						propertyList.add(propertyVo);
				 		}
				 	}
					stopVo.setProperties(propertyList);
					stopVoList.add(stopVo);
			}
			template.setStopsList(stopVoList);
		} catch (Exception e) {
			logger.error("转换失败", e);
			return null;
		}
		return template;
	}
	

	/**
	 * String类型的xml转MxGraphModel
	 * 
	 * @param xmldata
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static MxGraphModelVo allXmlToMxGraphModel(String xmldata,int PageId) {
		Document document = null;
		try {
			document = DocumentHelper.parseText(xmldata);
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
		String strXml = document.getRootElement().asXML();
		String transformation = "<sdds>"+strXml + "</sdds>";
		MxGraphModelVo mxGraphModelVo = new MxGraphModelVo();
		InputSource in = new InputSource(new StringReader(transformation));
		in.setEncoding("UTF-8");
		SAXReader reader = new SAXReader();
		try {
			document = reader.read(in);
			// 获取所有拥有autoSaveNode属性的mxCell节点
			Element rootElt = document.getRootElement(); // 获取根节点
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
			Iterator rootiter = rootjd.elementIterator("mxCell"); // 获取根节点下的子节点mxCell
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
				if (PageId >= 1 ) {
					if (Integer.parseInt(mxCellId) < 2) {
						continue;
					} 
				}
				if("1".equals(edge)){
					if(StringUtils.isBlank(source)||StringUtils.isBlank(target)){
						continue;
					}
				}
				mxCellVo.setPageId((Integer.parseInt(mxCellId)+PageId)+"");
				mxCellVo.setParent(parent);
				mxCellVo.setStyle(style);
				mxCellVo.setEdge(edge);
				if (StringUtils.isNotBlank(source) && StringUtils.isNotBlank(target)) {
					mxCellVo.setSource((Integer.parseInt(source)+PageId)+"");
					mxCellVo.setTarget((Integer.parseInt(target)+PageId)+"");
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
			logger.error("转换失败", e);
			return null;
		}
		return mxGraphModelVo;
	}
	
	/**
	 * List<StopTemplateModel>  to  List<Stops> 
	 * @param stopsListTemplate
	 * @return
	 */
	public static List<Stops> stopTemplateVoToStop(List<StopTemplateModel> stopsListTemplate) {
		List<Stops> stopsList = new ArrayList<Stops>();
			// 判空
			if (null != stopsListTemplate && stopsListTemplate.size() > 0) {
				// 循环拷贝
				for (StopTemplateModel stopTemplate : stopsListTemplate) {
					if (null != stopTemplate) {
						Stops stops = new Stops();
						// 拷贝StopTemplateModel的内容到Stops中
						BeanUtils.copyProperties(stopTemplate, stops);
						stops.setCheckpoint(stopTemplate.getIsCheckpoint());
						List<PropertyTemplateModel> propertyTemplateModel = stopTemplate.getProperties();
						if (null != propertyTemplateModel && propertyTemplateModel.size() > 0) {
							List<Property> propertyList = new ArrayList<Property>();
							for (PropertyTemplateModel propertyTemplate : propertyTemplateModel) {
								if (null != propertyTemplate) {
									Property property = new Property();
									// 拷贝propertyTemplate的内容到property中
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
}

package com.nature.base.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class FileUtils {
	
	private static Logger logger = LoggerUtil.getLogger();
	
	
	/**
	 * 字符串转xml文件并保存指定路径
	 * @param xmlStr  xml字符串
	 * @param fileName  文件名称
	 * @param type  文件类型(后缀)
	 * @param path  (存放路径)
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String createXml(String xmlStr,String fileName,String type,String path) {
		    Document doc = strToDocument(xmlStr);
		    String realPath = path + fileName + type;
	        logger.info("============进入生成方法：" + new Date().toLocaleString() + "=================");
	        try {
	            // 判断文件是否存在，如存在就删掉它
	            File file = new File(realPath);
	            if (!file.getParentFile().exists()) {
	            	//如果不存在则创建
	            	file.getParentFile().mkdirs();
	            	logger.info("==============文件目录不存在,新建文件==============");
	            }
	            /** 将document中的内容写入文件中 */
	            TransformerFactory tFactory = TransformerFactory.newInstance();
	            Transformer transformer = tFactory.newTransformer();
	            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	            DOMSource source = new DOMSource(doc);
	            StreamResult result = new StreamResult(new FileOutputStream(realPath));
	            transformer.transform(source, result);
	            logger.info("--------------------------------" + "更新文件成功" + "-------------------------------------");
	        } catch (final Exception exception) {
	            logger.info("更新" + fileName + "出错："+exception);
	        }
	        logger.info("============退出生成方法：" + new Date().toLocaleString() + "=================");
			return path + fileName + type;
	    }
	
	
	
	 /**
	  * 上传方法
	  * @param file 
	  * @param path
	  * @return
	  */
	 public static String upload(MultipartFile file,String path ) {
			Map<String, String> rtnMap = new HashMap<String, String>();
			rtnMap.put("code", "0");
			String url = "";
	        if (!file.isEmpty()) {
	        	//文件名
	            String saveFileName = file.getOriginalFilename();
	            File saveFile = new File(path + saveFileName);
	            if (!saveFile.getParentFile().exists()) {
	                saveFile.getParentFile().mkdirs();
	            }
	            try {
	                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(saveFile));
	                out.write(file.getBytes());
	                out.flush();
	                out.close();
	                url = FileUtils.getUrl();
	                logger.info(saveFile.getName() + " 上传成功");
	                rtnMap.put("url", url + path + saveFileName);
	                rtnMap.put("fileName", saveFileName);
	                rtnMap.put("msgInfo", "上传成功");
	                rtnMap.put("code", "1");
	            } catch (FileNotFoundException e) {
	                e.printStackTrace();
	                logger.info("上传失败," + e.getMessage());
	                rtnMap.put("msgInfo", "上传失败"+ e.getMessage());
	            } catch (IOException e) {
	                e.printStackTrace();
	                rtnMap.put("msgInfo", "上传失败"+ e.getMessage());
	            }
	        } else {
	            logger.info("上传失败，因为文件为空.");
	            rtnMap.put("msgInfo", "上传失败，因为文件为空.");
	        }
			return JsonUtils.toJsonNoException(rtnMap);
	    }
	 
	 
	/**
	 * 字符串转Document
	 * @param xmlStr
	 * @return
	 */
	 public static Document strToDocument(String xmlStr) {
	        Document doc = null;
	        StringReader sr = new StringReader(xmlStr);
	        InputSource is = new InputSource(sr);
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder;
	        try {
	            builder = factory.newDocumentBuilder();
	            doc = builder.parse(is);
	        } catch (ParserConfigurationException e) {
	            logger.info("ParserConfiguration错误"+e);
	        } catch (SAXException e) {
	            logger.info("SAX错误"+e);
	        } catch (IOException e) {
	            logger.info("IO错误"+e);
	        }
	        return doc;
	    }
	 
	 /**
	  * 获取项目访问路径
	  * @return
	  */
	 public static String getUrl(){
		 	HttpServletRequest request = getRequest();
		    String scheme = request.getScheme();//http
		    String serverName = request.getServerName();//localhost
		    int serverPort = request.getServerPort();//8080
		    String contextPath = request.getContextPath();//项目名
		    String url = scheme+"://"+serverName+":"+serverPort+contextPath;//http://127.0.0.1:8080/test
			return url;
    }
	 
	 
	 /**
	  * 统一获取request
	  * @return
	  */
	 public static HttpServletRequest getRequest(){
		 RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();  
	     HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);  
		 return request;
	 }
	 
	 /**
	  * xml文件转换字符串
	  * @param path
	  * @return
	  */
	 public static String XmlFileToStr(String path){
		 	String xmlString = "";
			byte[] strBuffer = null;
			InputStream in = null;
			int flen = 0;
			File xmlfile = new File(path);
			try {
			in = new FileInputStream(xmlfile);
			flen = (int)xmlfile.length();
			strBuffer = new byte[flen];
			in.read(strBuffer, 0, flen);
			in.close();
			} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.info("FileNotFound错误"+e.getMessage());
			} catch (IOException e) {
			logger.info("转换IO错误"+e.getMessage());
			e.printStackTrace();
			} 
			xmlString = new String(strBuffer); //构建String时，可用byte[]类型，
			logger.info("xml文件转换后的字符串："+xmlString);
			return xmlString;
	 }
	    

}

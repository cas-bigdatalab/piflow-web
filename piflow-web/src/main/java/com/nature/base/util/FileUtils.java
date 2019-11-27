package com.nature.base.util;

import org.slf4j.Logger;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {

    private static Logger logger = LoggerUtil.getLogger();


    /**
     * String to "xml" file and save the specified path
     *
     * @param xmlStr   xml string
     * @param fileName File name
     * @param type     File type (suffix)
     * @param path     (Storage path)
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String createXml(String xmlStr, String fileName, String type, String path) {
        Document doc = strToDocument(xmlStr);
        String realPath = path + fileName + type;
        logger.debug("============Entry Generation Method：" + new Date().toLocaleString() + "=================");
        try {
            // Determine if the file exists, delete it if it exists
            File file = new File(realPath);
            if (!file.getParentFile().exists()) {
                //Create if it does not exist
                file.getParentFile().mkdirs();
                logger.info("==============File directory does not exist, new file==============");
            }
            /** Write the contents of the document to the file */
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileOutputStream(realPath));
            transformer.transform(source, result);
            logger.info("--------------------------------" + "Update file successfully" + "-------------------------------------");
        } catch (final Exception exception) {
            logger.error("update " + fileName + " error ：", exception);
        }
        logger.debug("============Exit Generation Method：" + new Date().toLocaleString() + "=================");
        return path + fileName + type;
    }


    /**
     * Uploading method
     *
     * @param file
     * @param path
     * @return
     */
    public static String upload(MultipartFile file, String path) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
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
                logger.debug(saveFile.getName() + " Upload success");
                rtnMap.put("url", path + saveFileName);
                rtnMap.put("fileName", saveFileName);
                rtnMap.put("msgInfo", "Upload success");
                rtnMap.put("code", 200);
            } catch (FileNotFoundException e) {
                //e.printStackTrace();
                rtnMap.put("msgInfo", "Upload failure" + e.getMessage());
                logger.error("Upload failure," + e.getMessage(), e);
            } catch (IOException e) {
                //e.printStackTrace();
                rtnMap.put("msgInfo", "Upload failure" + e.getMessage());
                logger.error("msgInfo", "Upload failure" + e.getMessage(), e);
            }
        } else {
            rtnMap.put("msgInfo", "The upload failed because the file was empty.");
            logger.warn("The upload failed and the file was empty.");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }


    /**
     * String to "Document"
     *
     * @param xmlStr
     * @return
     */
    public static Document strToDocument(String xmlStr) {
        xmlStr.replace("&", "&amp;");
        Document doc = null;
        StringReader sr = new StringReader(xmlStr);
        InputSource is = new InputSource(sr);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(is);
        } catch (ParserConfigurationException e) {
            logger.error("ParserConfiguration错误", e);
        } catch (SAXException e) {
            logger.error("SAX错误", e);
        } catch (IOException e) {
            logger.error("IO错误", e);
        }
        return doc;
    }

    /**
     * Get project access path
     *
     * @return
     */
    public static String getUrl() {
        HttpServletRequest request = getRequest();
        String scheme = request.getScheme();//http
        String serverName = request.getServerName();//localhost
        int serverPort = request.getServerPort();//8080
        String contextPath = request.getContextPath();//项目名
        String url = scheme + "://" + serverName + ":" + serverPort + contextPath;//http://127.0.0.1:8080/test
        return url;
    }


    /**
     * Unified access to "request"
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        return request;
    }

    /**
     * "xml" file conversion string
     *
     * @param path
     * @return
     */
    public static String XmlFileToStr(String path) {
        String xmlString = "";
        byte[] strBuffer = null;
        InputStream in = null;
        int flen = 0;
        File xmlfile = new File(path);
        try {
            in = new FileInputStream(xmlfile);
            flen = (int) xmlfile.length();
            strBuffer = new byte[flen];
            in.read(strBuffer, 0, flen);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.info("FileNotFound Error" + e.getMessage());
        } catch (IOException e) {
            logger.info("Conversion IO Error" + e.getMessage());
            e.printStackTrace();
        }
        xmlString = new String(strBuffer); //When constructing ‘String’, you can use the ‘byte[]’ type.
        logger.info("'xml' file converted string：" + xmlString);
        return xmlString;
    }


}

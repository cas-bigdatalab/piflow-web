package cn.cnic.base.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FileUtils {

    private static Logger logger = LoggerUtil.getLogger();

    public static String CSV_TITLE_KEY = "CSV_TITLE";
    public static String CSV_DATA_KEY = "CSV_DATA";
    /**
     * String to "xml" file and save the specified path
     *
     * @param xmlStr   xml string
     * @param fileName File name
     * @param path     (Storage path)
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String createXml(String xmlStr, String fileName, String path) {
        CheckPathUtils.isChartPathExist(path);
        Document doc = strToDocument(xmlStr);
        String realPath = path + fileName + ".xml";
        logger.debug("============Entry Generation Method：" + new Date().toLocaleString() + "=================");
        try {
            // Determine if the file exists, delete it if it exists
            File file = new File(realPath);
            if (!file.getParentFile().exists()) {
                //Create if it does not exist
                boolean mkdirs = file.getParentFile().mkdirs();
                if (mkdirs) {
                    logger.info("File created successfully");
                }
                logger.info("==============File directory does not exist, new file==============");
            }
            // Write the contents of the document to the file
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
        return realPath;
    }

    /**
     * Generate JSON format file
     */
    public static String createJsonFile(String jsonString, String fileName, String filePath) {

        // Full path of splicing file
        //String fullPath = filePath + File.separator + fileName + ".json";
        String fullPath = filePath + fileName + ".json";

        // Generate JSON format file
        try {
            // Make sure to create a new file
            File file = new File(fullPath);
            if (!file.getParentFile().exists()) { // If the parent directory does not exist, create the parent directory
                file.getParentFile().mkdirs();
            }
            if (file.exists()) { // If it already exists, delete the old file
                file.delete();
            }
            file.createNewFile();

            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            write.write(jsonString);
            write.flush();
            write.close();
        } catch (Exception e) {
            fullPath = "";
            e.printStackTrace();
        }

        // 返回是否成功的标记
        return fullPath;
    }


    /**
     * Uploading method
     *
     * @param file
     * @param path
     * @return
     */
    public static String upload(MultipartFile file, String path) {
        return JsonUtils.toJsonNoException(uploadRtnMap(file, path, null));
    }

    public static Map<String, Object> uploadRtnMap(MultipartFile file, String path, String saveFileName) {
        if (file.isEmpty()) {
            return ReturnMapUtils.setFailedMsg("The upload failed and the file was empty.");
        }
        CheckPathUtils.isChartPathExist(path);
        //file name
        String fileName = file.getOriginalFilename();
        String[] fileNameSplit = fileName.split("\\.");
        if (saveFileName == null) {
            if (fileNameSplit.length > 0) {
                saveFileName = UUIDUtils.getUUID32() + "." + fileNameSplit[fileNameSplit.length - 1];
            }
        }
        if (StringUtils.isBlank(saveFileName)) {
            saveFileName = UUIDUtils.getUUID32();
        }
        File saveFile = new File(path + saveFileName);
        if (!saveFile.getParentFile().exists()) {
            boolean mkdirs = saveFile.getParentFile().mkdirs();
            if (mkdirs) {
                logger.info("File created successfully");
            }
        }
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        try {
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(saveFile));
            out.write(file.getBytes());
            out.flush();
            out.close();
            logger.debug(saveFile.getName() + " Upload success");
            rtnMap.put("fileName", fileName);
            rtnMap.put("saveFileName", saveFileName);
            rtnMap.put("path", path + saveFileName);
            rtnMap.put("msgInfo", "Upload success");
            rtnMap.put("code", 200);
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            rtnMap.put("msgInfo", "Upload failure");
            logger.error("Upload failure,", e);
        } catch (IOException e) {
            //e.printStackTrace();
            rtnMap.put("msgInfo", "Upload failure");
            logger.error("Upload failure", e);
        }
        return rtnMap;
    }

    /**
     * String to "Document"
     *
     * @param xmlStr
     * @return
     */
    public static Document strToDocument(String xmlStr) {
        Document doc = null;
        if (StringUtils.isBlank(xmlStr)) {
            return null;
        }
        xmlStr = xmlStr.replace("&", "&amp;");
        StringReader sr = new StringReader(xmlStr);
        InputSource is = new InputSource(sr);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(is);
        } catch (ParserConfigurationException e) {
            logger.error("ParserConfiguration Error", e);
        } catch (SAXException e) {
            logger.error("SAX Error", e);
        } catch (IOException e) {
            logger.error("IO Error", e);
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
        String contextPath = request.getContextPath();//projectName
        return scheme + "://" + serverName + ":" + serverPort + contextPath;
    }


    /**
     * Unified access to "request"
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            return (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        }
        return null;
    }

    public static String fileToString(File fileStr) {
        String fileString = "";
        if (null == fileStr) {
            return fileString;
        }
        byte[] strBuffer = null;
        InputStream in;
        try {
            int flen;
            in = new FileInputStream(fileStr);
            flen = (int) fileStr.length();
            strBuffer = new byte[flen];
            in.read(strBuffer, 0, flen);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("FileNotFound Error", e);
        } catch (IOException e) {
            logger.error("Conversion IO Error", e);
            e.printStackTrace();
        }
        if (null != strBuffer) {
            try {
                fileString = new String(strBuffer, "UTF-8"); //When constructing ‘String’, you can use the ‘byte[]’ type.
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            logger.info(" file converted string：" + fileString);
        }
        return fileString;
    }

    /**
     * file conversion string
     *
     * @param path
     * @return
     */
    public static String FileToStrByAbsolutePath(String path) {
        if (StringUtils.isBlank(path)) {
            return null;
        }
        File file = new File(path);
        return fileToString(file);
    }

    public static String FileToStrByRelativePath(String path) {
        String fileString = "";
        try {
            File file = ResourceUtils.getFile("classpath:" + path);
            fileString = fileToString(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("FileNotFound Error", e);
        }
        logger.info(" file converted string：" + fileString);
        return fileString;
    }

    public static void downloadFileResponse(HttpServletResponse response, String fileName, String filePath) {
        try {
            // Download local files
            // Read to the stream
            InputStream inStream = new FileInputStream(filePath);// File storage path
            // Format the output
            response.reset();
            response.setContentType("bin");
            response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            // Loop out the data in the stream
            byte[] b = new byte[100];
            int len;

            while ((len = inStream.read(b)) > 0)
                response.getOutputStream().write(b, 0, len);
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void downloadFile(HttpServletResponse response, String fileName, String filePath) {
        try {
            // Download local files
            // Read to the stream
            InputStream inStream = new FileInputStream(filePath);// File storage path
            // Format the output
            response.reset();
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
            response.setContentType("bin");
            response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            // Loop out the data in the stream
            byte[] b = new byte[100];
            int len;

            while ((len = inStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
                //IOUtils.write(b, response.getOutputStream());
            }
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Delete folder (force delete)
     *
     * @param path
     */
    public static void deleteAllFilesOfDir(File path) {
        if (null != path) {
            if (!path.exists())
                return;
            if (path.isFile()) {
                boolean result = path.delete();
                int tryCount = 0;
                while (!result && tryCount++ < 10) {
                    System.gc(); // Recycling resources
                    result = path.delete();
                }
            }
            File[] files = path.listFiles();
            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    deleteAllFilesOfDir(files[i]);
                }
            }
            path.delete();
        }
    }

    /**
     * deleteFile
     *
     * @param pathname
     * @return
     * @throws IOException
     */
    public static boolean deleteFile(String pathname) {
        boolean result = false;
        File file = new File(pathname);
        if (file.exists()) {
            file.delete();
            result = true;
            System.out.println("The file has been deleted successfully");
        }
        return result;
    }
    
    /**
     * file conversion string
     *
     * @param url
     * @param delimiter
     * @param header
     * @return
     * @throws Exception 
     */
    public static Map<String, Object> ParseCsvFile(String url, String delimiter, String header) throws Exception {
        if (StringUtils.isBlank(url) || StringUtils.isBlank(delimiter)) {
            return null;
        }
        String[] headerArr = null;
        if (StringUtils.isNotBlank(header)) {
            headerArr = header.split(",");
        }
        Map<String,Object> rtnMap = new HashMap<>();
        File csv = new File(url);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(csv));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = "";
        String everyLine = "";
        try {
            List<String[]> dataList = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                everyLine = line;
                if(StringUtils.isBlank(everyLine)) {
                    continue;
                }
                String[] split = everyLine.split(delimiter);
                dataList.add(split);
            }

            if (dataList.size() > 0) {
                if (null != headerArr) {
                    rtnMap.put(FileUtils.CSV_TITLE_KEY, headerArr);
                } else {
                    String[] title = dataList.get(0);
                    rtnMap.put(FileUtils.CSV_TITLE_KEY, title);
                    dataList.remove(0);
                }
                rtnMap.put(FileUtils.CSV_DATA_KEY, dataList);
            }
        } catch (IOException e) {
            throw new Exception("Parse failed");
        }
        return rtnMap;
    }
    
    /**
     * file conversion string
     *
     * @param url
     * @param delimiter
     * @param header
     * @return
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public static LinkedHashMap<String, List<String>> ParseCsvFileRtnColumnData(String url, String delimiter, String header) throws Exception {
        Map<String, Object> parseCsvFile = ParseCsvFile(url, delimiter, header);
        if (null == parseCsvFile) {
            return null;
        }
        Object csv_title = parseCsvFile.get(FileUtils.CSV_TITLE_KEY);
        Object csv_data = parseCsvFile.get(FileUtils.CSV_DATA_KEY);
        if (null == csv_title || null == csv_data) {
            return null;
        }
        String[] csvTitleArray = (String[])csv_title;
        List<String[]> csvDataList = (List<String[]>)csv_data;
        LinkedHashMap <String, List<String>> csvDataMap = new LinkedHashMap<>();
        Map <Integer, String> csvDataNumber = new HashMap<>();
        for (int i = 0; i < csvTitleArray.length; i++) {
            String string = csvTitleArray[i];
            csvDataMap.put(string, null);
            csvDataNumber.put(i, string);
        }
        for (String[] csvData : csvDataList) {
            if (null == csvData || csvData.length <=0) {
                continue;
            }
            for (int j = 0; j < csvData.length; j++) {
                String title = csvDataNumber.get(j);
                List<String> list = csvDataMap.get(title);
                if (null == list) {
                    list = new ArrayList<>();
                }
                list.add(csvData[j]);
                csvDataMap.put(title, list);
            }
        }
        return csvDataMap;
    }
    
 
}

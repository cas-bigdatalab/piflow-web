package cn.cnic.base.utils;

import cn.cnic.common.constant.ApiConfig;
import cn.cnic.common.constant.MessageConfig;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;
import org.slf4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.ServletOutputStream;
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
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class FileUtils {

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private static Logger logger = LoggerUtil.getLogger();

    public static String CSV_TITLE_KEY = "CSV_TITLE";
    public static String CSV_DATA_KEY = "CSV_DATA";

    public static FileSystem fs;

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
            return ReturnMapUtils.setFailedMsg(MessageConfig.UPLOAD_FAILED_FILE_EMPTY_MSG());
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
        Map<String, Object> rtnMap = new HashMap<>();
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
                if (StringUtils.isBlank(everyLine)) {
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
        String[] csvTitleArray = (String[]) csv_title;
        List<String[]> csvDataList = (List<String[]>) csv_data;
        LinkedHashMap<String, List<String>> csvDataMap = new LinkedHashMap<>();
        Map<Integer, String> csvDataNumber = new HashMap<>();
        for (int i = 0; i < csvTitleArray.length; i++) {
            String string = csvTitleArray[i];
            csvDataMap.put(string, null);
            csvDataNumber.put(i, string);
        }
        for (String[] csvData : csvDataList) {
            if (null == csvData || csvData.length <= 0) {
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

    public static void writeData(String url, String data) throws IOException {
        InputStream in = org.apache.commons.io.IOUtils.toInputStream(data, StandardCharsets.UTF_8.name());
        File file = new File(url);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(url);
        IOUtils.copyBytes(in, out, 4096, true);
        //close stream
        in.close();
        out.close();
    }

    public static String encryptToBase64(String filePath) {
        if (filePath == null) {
            return null;
        }
        try {
            byte[] b = Files.readAllBytes(Paths.get(filePath));
            return Base64.getEncoder().encodeToString(b);
        } catch (IOException e) {
            logger.error("Encrypt to base64 error!! message:{}", e.getMessage());
        }
        return null;
    }

    public static String decryptByBase64(String base64, String filePath) {
        if (base64 == null && filePath == null) {
            return "Create file error! Please give data!!";
        }
        try {
            Files.write(Paths.get(filePath), Base64.getDecoder().decode(base64), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Create success!!";
    }

    public static void deleteData(String url) throws IOException {

        File file = new File(url);
        if (file.exists()) {
            // 删除文件
            boolean success = file.delete();
            if (!success) {
                // 如果文件存在但无法删除，则抛出异常
                throw new IOException("Failed to delete file: " + url);
            }
        } else {
            // 如果文件不存在，也抛出异常
            throw new IOException("File does not exist: " + url);
        }
    }

    /**
     * @param originalFilename:
     * @return String
     * @author tianyao
     * @description 获取不包含路径的文件名  文件名和扩展名
     * @date 2024/2/20 14:59
     */
    public static String getFileName(String originalFilename) {
//        String directory = "";
        String filename = originalFilename;
        filename = filename.replaceAll("\\\\", "/");
        int lastSeparatorIndex = filename.lastIndexOf("/");
        if (lastSeparatorIndex != -1) {
//            directory = originalFilename.substring(0, lastSeparatorIndex);
            filename = originalFilename.substring(lastSeparatorIndex + 1);
        }
        return filename;
    }

    /**
     * @param file:
     * @param fileName:
     * @param fileDirectory:
     * @param defaultFs:
     * @return void
     * @author tianyao
     * @description 将multifile保存至hdfs
     * @date 2024/2/20 15:02
     */
    public static void saveFileToHdfs(MultipartFile file, String fileName, String fileDirectory, String defaultFs) {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", defaultFs);

        try {
            if (ObjectUtils.isEmpty(fs)) {
                fs = FileSystem.get(conf);
            }

            Path hdfsPath = new Path(fileDirectory + fileName);

            // 检查目录是否存在，不存在则创建
            if (!fs.exists(hdfsPath.getParent())) {
                fs.mkdirs(hdfsPath.getParent());
            }
            try (InputStream in = file.getInputStream()) {
                // 使用 Apache Commons IO 工具类来复制文件内容到 HDFS
                FSDataOutputStream outputStream = fs.create(hdfsPath);
                org.apache.commons.io.IOUtils.copy(in, outputStream);
                outputStream.close(); // 关闭输出流
                System.out.println("File saved to HDFS successfully: " + fileName);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveAndUnzipToHdfs(MultipartFile file, String fileName, String fileDirectory, String defaultFs) {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", defaultFs);

        try {
            if (ObjectUtils.isEmpty(fs)) {
                fs = FileSystem.get(conf);
            }
            String[] split = fileName.split("\\.");

            Path hdfsPath = new Path(fileDirectory + fileName.split("\\.")[0] + "/");

            // 检查目录是否存在，不存在则创建
            if (!fs.exists(hdfsPath)) {
                fs.mkdirs(hdfsPath);
            }
            //将压缩流解压缩并将压缩包中的所有文件放到hdfsPath目录下
            try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(file.getInputStream()))) {
                ZipEntry zipEntry;
                while ((zipEntry = zis.getNextEntry()) != null) {
                    Path filePath = new Path(hdfsPath + "/" + zipEntry.getName());

                    if (zipEntry.isDirectory()) {
                        // 如果是目录，创建目录
                        if (!fs.exists(filePath)) {
                            fs.mkdirs(filePath);
                        }
                    } else {
                        // 如果是文件，写入文件
                        try (OutputStream outputStream = fs.create(filePath)) {
                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = zis.read(buffer)) > 0) {
                                outputStream.write(buffer, 0, len);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //删除hdfs中的文件或目录
    public static void deleteHdfsFile(String filePath, String defaultFs) {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", defaultFs);

        try {
            if (ObjectUtils.isEmpty(fs)) {
                fs = FileSystem.get(conf);
            }
            Path hdfsPath = new Path(filePath);

            // 删除文件，第二个参数表示是否递归删除，如果是目录需要设置为true
            boolean isDeleted = fs.delete(hdfsPath, true);
//            if (isDeleted) {
//                System.out.println("File deleted from HDFS successfully: " + hdfsFilePath);
//            } else {
//                System.out.println("File not found or could not be deleted: " + hdfsFilePath);
//            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getDefaultFs() {
        return HttpUtils.doGet(ApiConfig.getTestDataPathUrl(), null, 1000).replace("/user/piflow/testData/", "");
    }

    public static void downloadFileFromHdfs(HttpServletResponse response, String filePath, String fileName, String defaultFs) {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", defaultFs);
        try {
            if (ObjectUtils.isEmpty(fs)) {
                fs = FileSystem.get(conf);
            }
            Path hdfsPath = new Path(filePath);
            FileStatus fileStatus = fs.getFileStatus(hdfsPath);
            if (fileStatus.isDirectory()) {
                int contentLength = 0;
                // 如果是目录，下载为ZIP文件
                String zipFileName = hdfsPath.getName() + ".zip";
                response.reset();
                response.addHeader("Access-Control-Allow-Origin", "*");
                response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
                response.setContentType("application/octet-stream;charset=utf-8");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(zipFileName, StandardCharsets.UTF_8.toString()) + "\"");
                try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
                    contentLength += addDirectoryToZip(zipOut, fs, hdfsPath, "");
                }
                response.setContentLength(contentLength);
            } else {
                response.reset();
                response.addHeader("Access-Control-Allow-Origin", "*");
                response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
                response.setContentType("application/octet-stream;charset=utf-8");
                response.addHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(hdfsPath.getName(), StandardCharsets.UTF_8.toString()) + "\"");
                try (FSDataInputStream in = fs.open(hdfsPath);
                     ServletOutputStream out = response.getOutputStream()) {
                    response.setContentLength(in.available());
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void downloadFilesFromHdfs(HttpServletResponse response, List<cn.cnic.component.system.entity.File> fileList, String zipName, String defaultFs) {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", defaultFs);
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment;filename=" + zipName);
        int contentLength = 0;
        try (FileSystem fs = FileSystem.get(conf);
             ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {

            for (cn.cnic.component.system.entity.File file : fileList) {
                String filePath = file.getFilePath();
                Path hdfsPath = new Path(filePath);

                if (fs.isFile(hdfsPath)) {
                    // 如果是文件，直接添加到ZIP输出流
                    contentLength += addFileToZip(fs, hdfsPath, zipOut);
                    response.setContentLength(contentLength);
                } else if (fs.isDirectory(hdfsPath)) {
                    // 如果是目录，递归添加目录内容到ZIP输出流
                    contentLength += addDirectoryToZip(zipOut, fs, hdfsPath, "");

                }
            }

            zipOut.finish();

        } catch (IOException e) {
            throw new RuntimeException("Error while downloading files from HDFS", e);
        }
    }

    private static int addFileToZip(FileSystem fs, Path filePath, ZipOutputStream zipOut) throws IOException {
        int contentLength = 0;
        try (FSDataInputStream in = fs.open(filePath)) {
            contentLength += in.available();
            ZipEntry entry = new ZipEntry(filePath.getName());
            zipOut.putNextEntry(entry);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                zipOut.write(buffer, 0, bytesRead);
            }
            zipOut.closeEntry();
        }
        return contentLength;
    }
    private static int addDirectoryToZip(ZipOutputStream zipOut, FileSystem fs, Path dir, String base) throws IOException {
        FileStatus[] fileStatuses = fs.listStatus(dir);
        int contentLength = 0;
        for (FileStatus fileStatus : fileStatuses) {
            Path path = fileStatus.getPath();
            String zipFilePath = base + path.getName();

            if (fileStatus.isDirectory()) {
                zipOut.putNextEntry(new ZipEntry(zipFilePath + "/"));
                zipOut.closeEntry();
                contentLength += addDirectoryToZip(zipOut, fs, path, zipFilePath + "/");
            } else {
                zipOut.putNextEntry(new ZipEntry(zipFilePath));
                try (FSDataInputStream in = fs.open(path)) {
                    contentLength += in.available();
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        zipOut.write(buffer, 0, bytesRead);
                    }
                }
                zipOut.closeEntry();
            }
        }
        return contentLength;
    }

    public static Resource loadFileFromHdfs(String fileName, String filePath, String defaultFs) {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", defaultFs);

        try {
            if (ObjectUtils.isEmpty(fs)) {
                fs = FileSystem.get(conf);
            }
            Path hdfsPath = new Path(filePath);
            if (fs.exists(hdfsPath)) {
                return new InputStreamResource(fs.open(hdfsPath));
            } else {
                // 处理文件不存在的情况
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, String> getFileTypeFromHdfs(String filePath, String defaultFs) {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", defaultFs);
        Map<String, String> fileInfo = new HashMap<>();

        try {
            if (ObjectUtils.isEmpty(fs)) {
                fs = FileSystem.get(conf);
            }

            Path hdfsPath = new Path(filePath);
            if (fs.exists(hdfsPath)) {
                if (filePath.contains(".")) {
                    //文件
                    String mimeType = getMimeTypeByExtension(filePath.split("\\.")[1]);
                    fileInfo.put("fileType", mimeType);
                    fileInfo.put("fileName", hdfsPath.getName());
                } else {
                    String mimeType = getMimeTypeByExtension("");
                    fileInfo.put("fileType", mimeType);
                    fileInfo.put("fileName", filePath);
                }
            } else {
                // 处理文件不存在的情况
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileInfo;
    }

//    public static void downloadFilesFromHdfs(HttpServletResponse response, List<cn.cnic.component.system.entity.File> fileList, String zipName, String defaultFs) {
//        Configuration conf = new Configuration();
//        conf.set("fs.defaultFS", defaultFs);
//
//        response.setContentType("multipart/form-data;charset=utf-8");//1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
//        response.setHeader("Content-Disposition", "attachment;filename=" + zipName);
//        FSDataInputStream instream = null;
//        int contentLength = 0;
//        try {
//            ZipOutputStream zipstream = new ZipOutputStream(response.getOutputStream());
//            for (cn.cnic.component.system.entity.File file : fileList) {
//                String filePath = file.getFilePath();
//                String fileName = file.getFileName();
//                try {
//                    if (ObjectUtils.isEmpty(fs)) {
//                        fs = FileSystem.get(conf);
//                    }
//
//                    Path hdfsPath = new Path(filePath);
//                    if (fs.exists(hdfsPath)) {
//                        instream = fs.open(hdfsPath);
//                        contentLength += instream.available();
//                        ZipEntry entry = new ZipEntry(fileName);
//                        zipstream.putNextEntry(entry);
//                        byte[] buffer = new byte[1024];
//                        int len = 0;
//                        while ((len = instream.read(buffer)) != -1) {
//                            zipstream.write(buffer, 0, len);
//                        }
//                        instream.close();
//                        zipstream.closeEntry();
//                        zipstream.flush();
//                    }
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            zipstream.finish();
//            response.setContentLength(contentLength);
//            zipstream.close();
//        } catch (IOException e) {
//            new RuntimeException(e.getMessage());
//        }
//    }


    private static String getMimeTypeByExtension(String extension) {
        switch (extension.toLowerCase()) {
            case "":
                return "dir";
            case "txt":
            case "text":
                return "text/plain";
            case "csv":
                return "text/csv";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "xls":
                return "application/vnd.ms-excel";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "ppt":
                return "application/vnd.ms-powerpoint";
            case "pptx":
                return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "pdf":
                return "application/pdf";
            case "xml":
                return "application/xml";
            case "html":
            case "htm":
                return "text/html";
            case "jpeg":
            case "jpg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "bmp":
                return "image/bmp";
            case "mp3":
                return "audio/mpeg";
            case "wav":
                return "audio/vnd.wave";
            case "mp4":
                return "video/mp4";
            case "avi":
                return "video/x-msvideo";
            case "mov":
                return "video/quicktime";
            case "zip":
                return "application/zip";
            case "rar":
                return "application/x-rar-compressed";
            case "7z":
                return "application/x-7z-compressed";
            // 添加更多文件扩展名和对应的MIME类型
            default:
                return "application/octet-stream";
        }
    }
}

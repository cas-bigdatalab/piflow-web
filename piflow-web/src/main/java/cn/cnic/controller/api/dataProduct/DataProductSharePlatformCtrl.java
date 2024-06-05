package cn.cnic.controller.api.dataProduct;


import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.component.dataProduct.domain.DataProductDomain;
import cn.cnic.component.dataProduct.service.IDataProductService;
import cn.cnic.component.dataProduct.vo.SharePlatformMetadata;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static cn.cnic.base.utils.ExcelUtils.appendIconAndDocumentPathToExcel;
import static cn.cnic.base.utils.FileUtils.getFileName;

@Controller
@RequestMapping("/dataProductSharePlatform")
public class DataProductSharePlatformCtrl {

    private static Logger logger = LoggerUtil.getLogger();


    private final DataProductDomain dataProductDomain;
    private final IDataProductService dataProductServiceImpl;


    public DataProductSharePlatformCtrl(DataProductDomain dataProductDomain, IDataProductService dataProductServiceImpl) {
        this.dataProductDomain = dataProductDomain;
        this.dataProductServiceImpl = dataProductServiceImpl;
    }


    @RequestMapping(value = "/setDataProductStatus", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "setDataProductStatus", notes = "返回url以及审核状态")
    public void setDataProductStatus(HttpServletResponse response, String dataProductId, String releasedDatasetUrl, int status) {
        dataProductDomain.updateDataProductMetaDataStatus(dataProductId, releasedDatasetUrl, status);
    }


    @RequestMapping(value = "/downloadDataset", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "downloadDataset", notes = "数据产品下载")
    public void downloadDataset(HttpServletResponse response, String dataProductId) {
        dataProductServiceImpl.downloadDataset(response, dataProductId);
    }


    @RequestMapping(value = "/downloadMetaDataFile")
    @ResponseBody
    @ApiOperation(value = "getMetaDataFile", notes = "获取元数据和图片,说明文档,并压缩为zip文件")
    public void getMetaDataFile(HttpServletResponse response,String dataProductId) {
        SharePlatformMetadata dataProductMetaData = dataProductDomain.getDataProductMetaDataById(dataProductId);
//        try {
//            DataProductMetaDataView metaDataView = JsonUtils.toObject(dataProductMetaData.getMetaData(), DataProductMetaDataView.class);
//
//           } catch (Exception e) {
//            logger.error("getMetaDataFile failed: " + e.getMessage());
//            e.printStackTrace();
//        }
        String iconPath = dataProductMetaData.getIconPath();
        String documentationPath = dataProductMetaData.getDocumentationPath();
        String filePath = dataProductMetaData.getMetadataFilePath();

        String iconName = getFileName(iconPath);
        String documentationName = getFileName(documentationPath);
        try {
            appendIconAndDocumentPathToExcel(iconName, documentationName, filePath); // 要把两个文件的相对路径存入Excel,都在同一文件夹下,所以直接取文件名
        } catch (Exception e) {
            e.printStackTrace();
        }
        //把vo填到excel中
// 获取文件实体
// 获取缩略图
// 放到文件夹里
// 压缩为zip
// 上传资源共享平台
        try {
            downloadFilesAsZip(response, new String[]{filePath, iconPath, documentationPath}, dataProductId +"_metaData.zip");

        } catch (IOException e) {
            // 处理异常并返回错误响应
            handleException(response, e);
        }
    }


    private static void downloadFilesAsZip(HttpServletResponse response, String[] filePaths, String zipFileName) throws IOException {
        // 创建临时文件夹
        Path tempDir = Files.createTempDirectory("tempFiles");

        try {
            // 复制文件到临时文件夹
            for (String filePath : filePaths) {
                Path srcPath = Paths.get(filePath);
                Path destPath = tempDir.resolve(srcPath.getFileName());
                Files.copy(srcPath, destPath);
            }

            // 设置响应头
            response.reset();
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
            response.setContentType("application/zip;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(zipFileName, StandardCharsets.UTF_8.toString()) + "\"");

            // 创建ZIP输出流
            try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
                // 添加文件到ZIP
                Files.walk(tempDir).filter(Files::isRegularFile).forEach(file -> {
                    try (InputStream in = Files.newInputStream(file)) {
                        String fileName = tempDir.relativize(file).toString();
                        zipOut.putNextEntry(new ZipEntry(fileName));
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = in.read(buffer)) != -1) {
                            zipOut.write(buffer, 0, bytesRead);
                        }
                        zipOut.closeEntry();
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
            }
        } finally {
            // 删除临时文件夹
            Files.walk(tempDir).sorted((path1, path2) -> path2.compareTo(path1)).forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void handleException(HttpServletResponse response, IOException e) {
        try {
            response.reset();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json;charset=utf-8");
        } catch (Exception ex) {
            // 记录日志或者进行其他处理
            ex.printStackTrace();
        }
    }
}

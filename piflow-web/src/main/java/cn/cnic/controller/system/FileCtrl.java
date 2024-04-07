package cn.cnic.controller.system;

import cn.cnic.component.flow.vo.FlowPublishingVo;
import cn.cnic.component.system.service.IFileService;
import cn.cnic.component.system.vo.FileVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Api(value = "file api", tags = "file api")
@Controller
@RequestMapping("/file")
public class FileCtrl {

    private final IFileService fileServiceImpl;

    @Autowired
    public FileCtrl(IFileService fileServiceImpl) {
        this.fileServiceImpl = fileServiceImpl;
    }

    /**
     * @param file:
     * @param unzip:是否解压缩, 上传文件、上传文件夹不解压缩，为false，上传文件夹并解压缩，为true
     * @param associateType:
     * @param associateId:
     * @return String
     * @author tianyao
     * @description 上传文件，分两类文件来上传，数据产品分类封面图和数据产品封面图放到服务器上，其他放在hdfs上
     * @date 2024/2/21 14:35
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "uploadFile", notes = "上传文件")
    public String uploadFile(MultipartFile file, Boolean unzip, Integer associateType, String associateId) {
        return fileServiceImpl.uploadFile(file, unzip, associateType, associateId);
    }

    /**
     * @param file:压缩包
     * @param unzip:是否解压缩
     * @param associateType:关联类型
     * @param associateId:关联id
     * @return String
     * @author tianyao
     * @description 允许上传压缩包，并告知是否需要解压缩 弃用，与上面的接口合二为一
     * @date 2024/2/28 11:05
     */
    @RequestMapping(value = "/uploadFilesZip", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "uploadFilesZip", notes = "上传文件夹压缩包")
    public String uploadFilesZip(MultipartFile file, Boolean unzip, Integer associateType, String associateId) {
        return fileServiceImpl.uploadFilesZip(file, unzip, associateType, associateId);
    }

    /**
     * @param id:
     * @return String
     * @author tianyao
     * @description 文件下载
     * @date 2024/2/21 15:32
     */
    @RequestMapping(value = "/getFileById", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "getFileById", notes = "文件下载")
    public void getFileById(HttpServletResponse response, String id) {
        fileServiceImpl.getFileById(response, id);
    }

    /**
     * @param filePath:
     * @return String
     * @author tianyao
     * @description 根据filePath进行文件下载
     * @date 2024/2/21 15:32
     */
    @RequestMapping(value = "/getFileByFilePath", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "getFileByFilePath", notes = "根据filePath进行文件下载")
    public void getFileByFilePath(HttpServletResponse response, String filePath) {
        fileServiceImpl.getFileByFilePath(response, filePath);
    }

    /**
     * @param response:
     * @param ids:
     * @return void
     * @author tianyao
     * @description 根据文件id列表获取多个文件
     * @date 2024/2/27 11:28
     */
    @RequestMapping(value = "/getFileListByIds", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "getFileListByIds", notes = "根据文件id列表获取多个文件")
    public void getFileListByIds(HttpServletResponse response, String ids) {
        fileServiceImpl.getFileListByIds(response, ids);
    }

}

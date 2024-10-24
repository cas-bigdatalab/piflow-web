package cn.cnic.controller.api.other;

import cn.cnic.controller.requestVo.DockerBaseImageVoRequest;
import cn.cnic.component.stopsComponent.service.IBaseImageService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.component.system.service.ILogHelperService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.component.dashboard.service.IResourceService;
import cn.cnic.component.dashboard.service.IStatisticService;
import io.swagger.annotations.Api;

/**
 * 基础镜像对应的控制层
 */
@Api(value = "docker base image api", tags = "docker base image api")
@RestController
@RequestMapping("/dockerimage")
public class BaseImageCtrl {

    private final ILogHelperService logHelperServiceImpl;
    private final IBaseImageService baseImageServiceImpl;

    @Autowired
    public BaseImageCtrl(ILogHelperService logHelperServiceImpl, IBaseImageService baseImageServiceImpl) {
        this.logHelperServiceImpl = logHelperServiceImpl;
        this.baseImageServiceImpl = baseImageServiceImpl;
    }


    /**
     * 获取Docker基础镜像列表
     *
     * @param page
     * @param limit
     * @return String
     * @author zjcheng
     * @date 2024-05-20
     */
    @RequestMapping(value = "/getBaseImageListByPage", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="getBaseImageListByPage", notes="base image info")
    public String getBaseImageInfoByPage(Integer page, Integer limit) {
        return baseImageServiceImpl.getBaseImageInfoByPage(page, limit);
    }

    /**
     * 上传Docker基础镜像
     *
     * @param imageVo 基础镜像实例属性
     * @return String
     * @author zjcheng
     * @date 2024-05-20
     */
    @RequestMapping(value = "/uploadBaseImage", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="uploadBaseImage", notes="upload base image info")
    public String uploadBaseImage(DockerBaseImageVoRequest imageVo) {
        String username = SessionUserUtil.getCurrentUsername();
        logHelperServiceImpl.logAuthSucceed("upload " + imageVo.getBaseImageName() + " for user ", username);

        return baseImageServiceImpl.uploadBaseImage(username, imageVo.getBaseImageName(),
                imageVo.getBaseImageVersion(), imageVo.getBaseImageDescription(),
                imageVo.getHarborUser(), imageVo.getHarborPassword());
    }

    /**
     * 编辑Docker基础镜像
     *
     * @param imageVo 基础镜像实例属性
     * @return String
     * @author zjcheng
     * @date 2024-05-20
     */
    @RequestMapping(value = "/updateBaseImage", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="updateBaseImage", notes="update base image info")
    public String updateBaseImage(DockerBaseImageVoRequest imageVo) {
        String username = SessionUserUtil.getCurrentUsername();
        logHelperServiceImpl.logAuthSucceed("update " + imageVo.getBaseImageName() + " for user ", username);

        return baseImageServiceImpl.updateBaseImage(username, imageVo.getBaseImageName(),
                imageVo.getBaseImageVersion(), imageVo.getBaseImageDescription(),
                imageVo.getHarborUser(), imageVo.getHarborPassword());
    }

    /**
     * 删除Docker基础镜像
     *
     * @param baseImageName 基础镜像实例名称
     * @return String
     * @author zjcheng
     * @date 2024-05-20
     */
    @RequestMapping(value = "/deleteBaseImage", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="deleteBaseImage", notes="delete base image info")
    public String deleteBaseImage(String baseImageName) {
        String username = SessionUserUtil.getCurrentUsername();
        logHelperServiceImpl.logAuthSucceed("delete " + baseImageName + " for user ", username);

        return baseImageServiceImpl.deleteBaseImage(username, baseImageName);
    }

}

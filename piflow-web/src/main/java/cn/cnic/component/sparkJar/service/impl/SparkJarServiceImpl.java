package cn.cnic.component.sparkJar.service.impl;

import java.io.File;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Date;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import cn.cnic.common.constant.MessageConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.cnic.base.utils.FileUtils;
import cn.cnic.base.utils.PageHelperUtils;
import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.base.utils.UUIDUtils;
import cn.cnic.common.Eunm.SparkJarState;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.sparkJar.domain.SparkJarDomain;
import cn.cnic.component.sparkJar.entity.SparkJarComponent;
import cn.cnic.component.sparkJar.service.ISparkJarService;
import cn.cnic.component.sparkJar.utils.SparkJarUtils;
import cn.cnic.third.service.ISparkJar;
import cn.cnic.third.vo.sparkJar.SparkJarVo;

@Service
public class SparkJarServiceImpl implements ISparkJarService {


    private final SparkJarDomain sparkJarDomain;
    private final ISparkJar sparkJarImpl;

    @Autowired
    public SparkJarServiceImpl(SparkJarDomain sparkJarDomain, ISparkJar sparkJarImpl) {
        this.sparkJarDomain = sparkJarDomain;
        this.sparkJarImpl = sparkJarImpl;
    }


    @Override
    public String uploadSparkJarFile(String username, MultipartFile file) {

        //call piflow server api: sparkJar/path
        String sparkJarPath = sparkJarImpl.getSparkJarPath();

        //upload jar file to sparkJar path
        String sparkJarName = file.getOriginalFilename();

        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (file.isEmpty()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.UPLOAD_FAILED_FILE_EMPTY_MSG());
        }
        Map<String, Object> uploadMap = null;

        if (sparkJarName.endsWith(".zip")) {
            try (InputStream inputStream = file.getInputStream();
                 ZipInputStream zipIn = new ZipInputStream(inputStream)) {
                // 创建临时目录用于解压文件
                Path tempDir = Files.createTempDirectory("spark_jar_upload");

                ZipEntry entry;
                boolean foundJar = false;
                while ((entry = zipIn.getNextEntry()) != null) {
                    if (!entry.isDirectory() && entry.getName().toLowerCase().endsWith(".jar")) {
                        // 获取第一个JAR文件的名称
                        sparkJarName = new File(entry.getName()).getName();

                        // 创建临时文件来保存JAR
                        Path tempFile = tempDir.resolve(sparkJarName);
                        Files.copy(zipIn, tempFile, StandardCopyOption.REPLACE_EXISTING);

                        // 上传JAR文件到sparkJar路径
                        uploadMap = FileUtils.uploadRtnMap(tempFile.toFile(), sparkJarPath, sparkJarName);
                        foundJar = true;
                        break;
                    }
                    zipIn.closeEntry();
                }
                if (!foundJar) {
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("Zip file must contain at least one JAR file");
                }
            } catch (Exception e) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr("Error processing the uploaded file: " + e.getMessage());
            }
        } else {
            uploadMap = FileUtils.uploadRtnMap(file, sparkJarPath, sparkJarName);
        }

        if (null == uploadMap || uploadMap.isEmpty()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.UPLOAD_FAILED_MSG());
        }
        Integer code = (Integer) uploadMap.get("code");
        if (500 == code) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("failed to upload file");
        }

        //save stopsHub to db
        SparkJarComponent sparkJarComponent = SparkJarUtils.sparkJarNewNoId(username);
        sparkJarComponent.setId(UUIDUtils.getUUID32());
        sparkJarComponent.setJarName(sparkJarName);
        sparkJarComponent.setJarUrl(sparkJarPath + sparkJarName);
        sparkJarComponent.setStatus(SparkJarState.UNMOUNT);
        sparkJarDomain.addSparkJarComponent(sparkJarComponent);
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("successful jar upload");
    }

    @Override
    public String mountSparkJar(String username, Boolean isAdmin, String id) {

        SparkJarComponent sparkJarComponent = sparkJarDomain.getSparkJarById(username, isAdmin, id);

        SparkJarVo sparkJarVo = sparkJarImpl.mountSparkJar(sparkJarComponent.getJarName());
        if (sparkJarVo == null) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Mount failed, please try again later");
        }

        sparkJarComponent.setMountId(sparkJarVo.getMountId());
        sparkJarComponent.setStatus(SparkJarState.MOUNT);
        sparkJarComponent.setLastUpdateUser(username);
        sparkJarComponent.setLastUpdateDttm(new Date());
        sparkJarDomain.updateSparkJarComponent(sparkJarComponent);

        return ReturnMapUtils.setSucceededMsgRtnJsonStr("Mount successful");
    }

    @Override
    public String unmountSparkJar(String username, Boolean isAdmin, String id) {

        SparkJarComponent sparkJarComponent = sparkJarDomain.getSparkJarById(username, isAdmin, id);
        if (sparkJarComponent.getStatus() == SparkJarState.UNMOUNT) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Spark jar have been UNMounted already!");
        }

        SparkJarVo sparkJarVo = sparkJarImpl.unmountSparkJar(sparkJarComponent.getMountId());
        if (sparkJarVo == null) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("UNMount failed, please try again later");
        }
        sparkJarComponent.setMountId(sparkJarVo.getMountId());
        sparkJarComponent.setStatus(SparkJarState.UNMOUNT);
        sparkJarComponent.setLastUpdateUser(username);
        sparkJarComponent.setLastUpdateDttm(new Date());
        sparkJarDomain.updateSparkJarComponent(sparkJarComponent);


        return ReturnMapUtils.setSucceededMsgRtnJsonStr("Unmount successful");
    }

    /**
     * sparkJarListPage
     *
     * @param username username
     * @param isAdmin  is admin
     * @param pageNo   Number of pages
     * @param limit    Number each page
     * @param param    Search content
     * @return
     */
    @Override
    public String sparkJarListPage(String username, Boolean isAdmin, Integer pageNo, Integer limit, String param) {
        if (null == pageNo || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
        }
        Page<Process> page = PageHelper.startPage(pageNo, limit, "crt_dttm desc");
        sparkJarDomain.getSparkJarListParam(username, isAdmin, param);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        return PageHelperUtils.setLayTableParamRtnStr(page, rtnMap);
    }

    /**
     * del spark jar
     *
     * @param username username
     * @param id       id
     * @return json
     */
    @Override
    public String delSparkJar(String username, Boolean isAdmin, String id) {
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("id"));
        }
        SparkJarComponent sparkJarComponent = sparkJarDomain.getSparkJarById(username, isAdmin, id);
        if (null == sparkJarComponent) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        SparkJarState status = sparkJarComponent.getStatus();
        if (SparkJarState.MOUNT == status) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The status is MOUNT and deletion is prohibited ");
        }
        int i = sparkJarDomain.deleteSparkJarById(username, id);
        if (i <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.DELETE_ERROR_MSG());
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
    }

}

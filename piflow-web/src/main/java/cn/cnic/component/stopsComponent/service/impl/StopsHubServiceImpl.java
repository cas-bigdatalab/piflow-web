package cn.cnic.component.stopsComponent.service.impl;

import cn.cnic.base.utils.*;
import cn.cnic.common.Eunm.ComponentFileType;
import cn.cnic.common.Eunm.PortType;
import cn.cnic.common.Eunm.StopsHubState;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.stopsComponent.domain.StopsComponentDomain;
import cn.cnic.component.stopsComponent.domain.StopsHubDomain;
import cn.cnic.component.stopsComponent.domain.StopsHubFileRecordDomain;
import cn.cnic.component.stopsComponent.entity.*;
import cn.cnic.component.stopsComponent.service.IStopsHubService;
import cn.cnic.component.stopsComponent.utils.*;
import cn.cnic.component.stopsComponent.vo.PublishComponentVo;
import cn.cnic.component.stopsComponent.vo.StopsComponentPropertyVo;
import cn.cnic.component.stopsComponent.vo.StopsHubInfoVo;
import cn.cnic.component.system.domain.SysUserDomain;
import cn.cnic.component.system.entity.SysUser;
import cn.cnic.third.market.service.IMarket;
import cn.cnic.third.service.IStop;
import cn.cnic.third.vo.stop.StopsHubVo;
import cn.cnic.third.vo.stop.ThirdStopsComponentVo;
import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.dockerjava.api.DockerClient;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class StopsHubServiceImpl implements IStopsHubService {
    private Logger logger = LoggerUtil.getLogger();

    private final StopsComponentDomain stopsComponentDomain;
    private final StopsHubDomain stopsHubDomain;
    private final SysUserDomain sysUserDomain;
    private final IStop stopImpl;
    private final IMarket marketImpl;
    private final StopsHubFileRecordDomain stopsHubFileRecordDomain;

    @Autowired
    public StopsHubServiceImpl(StopsComponentDomain stopsComponentDomain,
                               StopsHubDomain stopsHubDomain,
                               SysUserDomain sysUserDomain, IStop stopImpl, IMarket marketImpl, StopsHubFileRecordDomain stopsHubFileRecordDomain) {
        this.stopsComponentDomain = stopsComponentDomain;
        this.stopsHubDomain = stopsHubDomain;
        this.sysUserDomain = sysUserDomain;
        this.stopImpl = stopImpl;
        this.marketImpl = marketImpl;
        this.stopsHubFileRecordDomain = stopsHubFileRecordDomain;
    }

    /**
     * @param username
     * @param file
     * @param type            Component type:Python/Scala
     * @param languageVersion
     * @return
     */
    @Override
    public String uploadStopsHubFile(String username, MultipartFile file, String type, String languageVersion) {
        logger.info("==============upload stops hub start=============");
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (file.isEmpty()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Upload failed, please try again later");
        }
        String stopsHubName = file.getOriginalFilename();
        List<StopsHub> existStopHub = stopsHubDomain.getStopsHubByJarName("", true, stopsHubName);
        if (CollectionUtils.isNotEmpty(existStopHub)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Stops hub exists!! Please confirm whether the upload is repeated!! If not,please change the file name!!  fileName: " + stopsHubName);
        }
        //call piflow server api: plugin/path
        String stopsHubPath = stopImpl.getStopsHubPath();
        if (!stopsHubPath.endsWith("/")) {
            stopsHubPath = stopsHubPath + "/";
        }
        //upload jar file to plugin path
        ComponentFileType fileType = ComponentFileType.selectGender(type);
        if (fileType == null) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Please select a file type or an incorrect file type");
        }

        Map<String, Object> uploadMap = FileUtils.uploadRtnMap(file, stopsHubPath, stopsHubName);
        if (null == uploadMap || uploadMap.isEmpty()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.UPLOAD_FAILED_MSG());
        }
        Integer code = (Integer) uploadMap.get("code");
        if (500 == code) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("failed to upload file");
        }
        logger.info("==============upload stops hub upload to path result=============" + JSON.toJSONString(uploadMap));

        //save stopsHub to db
        StopsHub stopsHub = StopsHubUtils.stopsHubNewNoId(username);
        stopsHub.setId(UUIDUtils.getUUID32());
        stopsHub.setJarName(stopsHubName);
        stopsHub.setJarUrl(stopsHubPath + stopsHubName);
        stopsHub.setStatus(StopsHubState.UNMOUNT);
        stopsHub.setType(fileType);
        stopsHub.setLanguageVersion(languageVersion);
        stopsHubDomain.addStopHub(stopsHub);
        logger.info("==============upload stops hub finish=============");
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("successful jar upload");
    }

    @Override
    public String mountStopsHub(String username, Boolean isAdmin, String id) {
        logger.info("=====mount stops hub=====start:id :{}", id);
        StopsHub stopsHub = stopsHubDomain.getStopsHubById(username, isAdmin, id);
//        if(stopsHub.getStatus() == StopsHubState.MOUNT){
//            return ReturnMapUtils.setFailedMsgRtnJsonStr("StopsHub have been Mounted already!");
//        }
        if (stopsHub == null) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("no data");
        } else if (stopsHub.getType() == null) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Data type error,Delete it and upload it again or contact the administrator");
        }

        switch (stopsHub.getType()) {
            case SCALA:
                //mount scala stopsHub, parse on server
                return mountScalaStopsHub(stopsHub, username);
            case PYTHON:
                //mount python stopsHub,parse on web
                return mountPythonStopsZip(stopsHub, username);
            default:
                return ReturnMapUtils.setFailedMsgRtnJsonStr("Data type error,Delete it and upload it again or contact the administrator");
        }
    }

    /**
     * mount scala.jar
     *
     * @param stopsHub
     * @param username
     * @return
     * @data 2022-02-03
     * @author leilei
     */
    private String mountScalaStopsHub(StopsHub stopsHub, String username) {
        StopsHubVo stopsHubVo = stopImpl.mountStopsHub(stopsHub.getJarName());
        if (stopsHubVo == null) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Mount failed, please try again later");
        }


        //TODO: remove stops and groups from db
        List<ThirdStopsComponentVo> stops = stopsHubVo.getStops();
        List<String> groupNameList = new ArrayList<>();
        Map<String, StopsComponentGroup> stopsComponentGroupMap = new HashMap<>();
        for (ThirdStopsComponentVo s : stops) {
            groupNameList.addAll(Arrays.asList(s.getGroups().split(",")));
        }
        List<String> distinctGroupNameList = groupNameList.stream().distinct().collect(Collectors.toList());
        List<StopsComponentGroup> stopsComponentGroupList = stopsComponentDomain.getStopGroupByGroupNameList(distinctGroupNameList);
        for (StopsComponentGroup sGroup : stopsComponentGroupList) {
            stopsComponentGroupMap.put(sGroup.getGroupName(), sGroup);
        }
        StringBuffer bundles = new StringBuffer();
        for (ThirdStopsComponentVo s : stops) {

            List<String> stopGroupNameList = Arrays.asList(s.getGroups().split(","));
            for (String groupName : stopGroupNameList) {

                StopsComponentGroup stopsComponentGroup = stopsComponentGroupMap.get(groupName);
                if (stopsComponentGroup == null) {
                    // add group into db
                    stopsComponentGroup = StopsComponentGroupUtils.stopsComponentGroupNewNoId(username);
                    stopsComponentGroup.setId(UUIDUtils.getUUID32());
                    stopsComponentGroup.setGroupName(groupName);
                    stopsComponentDomain.addStopsComponentGroup(stopsComponentGroup);
                    stopsComponentGroupMap.put(groupName, stopsComponentGroup);
                }
            }

            //add stop into db
            List<StopsComponentGroup> stopGroupByName = new ArrayList<>();
            for (String groupName : stopGroupNameList) {
                stopGroupByName.add(stopsComponentGroupMap.get(groupName));
            }
            StopsComponent stopsComponent = stopsComponentDomain.getStopsComponentByBundle(s.getBundle());
            if (stopsComponent == null) {
                stopsComponent = StopsComponentUtils.thirdStopsComponentVoToStopsTemplate(username, s, stopGroupByName);
                stopsComponent.setComponentType(ComponentFileType.SCALA);
                stopsComponent.setStopsHubId(stopsHub.getId());
                stopsComponentDomain.addStopsComponentAndChildren(stopsComponent);
                //stopsComponentMapper.insertStopsComponent(stopsComponent);

            } else {//update stop group
                //stopsComponent.setStopGroupList(stopGroupByName);
                //TODO: Update group info
                logger.info("bundle is already exists, bundle name is " + s.getBundle());
            }
            //add stop and group relationship
            for (StopsComponentGroup sGroup : stopGroupByName) {
                stopsComponentDomain.deleteGroupCorrelationByGroupIdAndStopId(sGroup.getId(), stopsComponent.getId());
                stopsComponentDomain.insertAssociationGroupsStopsTemplate(sGroup.getId(), stopsComponent.getId());
            }
            if (bundles.length() > 0) {
                bundles.append(",");
            }
            bundles.append(s.getBundle());
        }

        stopsHub.setMountId(stopsHubVo.getMountId());
        stopsHub.setStatus(StopsHubState.MOUNT);
        stopsHub.setLastUpdateUser(username);
        stopsHub.setLastUpdateDttm(new Date());
        stopsHub.setBundles(bundles.toString());
        stopsHubDomain.updateStopHub(stopsHub);

        return ReturnMapUtils.setSucceededMsgRtnJsonStr("Mount successful");

    }



    /**
     * mount python.zip
     *
     * @param stopsHub 算法包
     * @param username
     * @return
     * @data 2022-02-03
     * @author leilei
     */
    private String mountPythonStopsZip(StopsHub stopsHub, String username) {
        /*
         * 通过process直接用docker命令的方式打包
         */
        if (StringUtils.isNotEmpty(stopsHub.getJarUrl())) {
            String stopsHubPath = stopImpl.getStopsHubPath();
            if (!stopsHubPath.endsWith("/")) {
                stopsHubPath = stopsHubPath + "/";
            }
            String jarName = stopsHub.getJarName();
            String dockerFileName = "DockerFile-" + stopsHub.getId();
            List<StopsHubFileRecord> insertList = new ArrayList<>();
            try {
                FileSystem fileSystem = FileSystem.get(URI.create(stopsHub.getJarUrl()), new Configuration());
                FSDataInputStream inputStream = fileSystem.open(new Path(stopsHub.getJarUrl()));
                ZipInputStream zipInputStream = new ZipInputStream(inputStream, Charset.forName("GBK"));
                ZipEntry zipEntry = null;

                //构造insertList
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    if (!zipEntry.isDirectory()) {
                        String zipEntryFileName = zipEntry.getName();
                        logger.info("zipEntry Name: " + zipEntryFileName);
                        //没办法判断是python组件还是工具类,在页面让用户自己设置
                        if (zipEntryFileName.endsWith(".py") && !"data_connect.py".equals(zipEntryFileName)) {  //如果是以.py结尾的,生成算法包的具体文件记录,生成记录去除掉基础类data_connect.py
                            //初始化SparkJarFileRecord实体
                            StopsHubFileRecord stopsHubFileRecord = new StopsHubFileRecord();
                            stopsHubFileRecord.setId(UUIDUtils.getUUID32());
                            //只要文件名,不要路径
                            String fileName = zipEntryFileName.contains("/") ? zipEntryFileName.substring(zipEntryFileName.lastIndexOf("/") + 1) : zipEntryFileName;
                            //这里文件名是以 .py 结尾的,去掉 .py; 否则页面组件名后跟个.py不好看
                            String stopName = fileName.endsWith(".py") ? fileName.substring(0, fileName.length() - 3) : fileName;
                            stopsHubFileRecord.setId(UUIDUtils.getUUID32());
                            stopsHubFileRecord.setFileName(stopName);
                            stopsHubFileRecord.setFilePath(zipEntryFileName);
                            stopsHubFileRecord.setStopsHubId(stopsHub.getId());
                            insertList.add(stopsHubFileRecord);
                        }
                    }
                }

//                stopsHub.setMountId();       //不用server端做处理，所以没有mountId
                //状态设置为挂载中
                stopsHub.setStatus(StopsHubState.MOUNTING);
                stopsHub.setLastUpdateUser(username);
                stopsHub.setLastUpdateDttm(new Date());
                stopsHubDomain.updateStopHub(stopsHub);

                //把下载的zip包放在/storage/stopHub/下
                String dstPath = stopsHubPath + "/" + jarName;
                //把dockerFile放在/storage/stopHub/下
                //stopsHubPath：存放算法包路径，跟下载的zip包放在一起
                String dockerFileSavePath = stopsHubPath + "/" + dockerFileName;
                if(!generateDockerfile(stopsHub, dstPath, dockerFileSavePath)) {
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("Create Dockerfile failed, please try again later");
                }

                CompletableFuture<Integer> buildFuture = null; //定义future结构
                String dockerImagesName = generateTagsName(jarName).toLowerCase();   //确定镜像名称
                // 异步执行镜像构建
                buildFuture = CompletableFuture.supplyAsync(() ->
                        buildAndPushDockerImage("/", dockerFileSavePath, dockerImagesName));
                // 当 CompletableFuture 完成时清理dockerFile和zip
                buildFuture.whenComplete((result, throwable) -> {
                    try {
                        // 不管结果如何，首先执行清理文件
                        FileUtils.deleteData(dstPath);
                        FileUtils.deleteData(dockerFileSavePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // 检查是否有异常抛出
                    if (throwable != null) {
                        logger.error("Build Docker image failed: " + throwable.getMessage());
                    } else {
                        //stopsHub前面由Unmount更新为Mounting状态时version已加1，故此处也应加1，否则再次更新状态会因version不匹配失败
                        stopsHub.setVersion(stopsHub.getVersion()+1);
                        // 根据 buildPythonImages 方法的返回值判断操作是否成功
                        if (result == 0) {
                            logger.info("算子镜像构建&推送成功");
                            //向算法包的具体文件记录表中插入数据
                            if (insertList.size() > 0) {
                                for (StopsHubFileRecord stopsHubFileRecord : insertList) {
                                    stopsHubFileRecord.setDockerImagesName(dockerImagesName);
                                    stopsHubFileRecordDomain.addStopsHubFileRecord(stopsHubFileRecord);
                                }
                            }
                            //stopsHub更新为Mount状态
                            stopsHub.setStatus(StopsHubState.MOUNT);
                            stopsHub.setLastUpdateUser(username);
                            stopsHub.setLastUpdateDttm(new Date());
                            stopsHubDomain.updateStopHub(stopsHub);
                        } else {
                            logger.info("python算子镜像构建&推送失败，error code: " + result);
                            //stopsHub更新为UnMount状态
                            stopsHub.setStatus(StopsHubState.UNMOUNT);
                            stopsHub.setLastUpdateUser(username);
                            stopsHub.setLastUpdateDttm(new Date());
                            stopsHubDomain.updateStopHub(stopsHub);
                        }
                    }
                });
            } catch (Exception e) {
                logger.error("init PythonStopsComponent error,error message:" + e.getMessage(), e);
                return ReturnMapUtils.setFailedMsgRtnJsonStr("Mount failed, please try again later");
            }
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Mount failed with empty url, please try again later");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("挂载成功，等待后台自动构建镜像...");
    }


    private Boolean generateDockerfile(StopsHub stopsHub, String dstPath, String dockerFileSavePath){
        String jarName = stopsHub.getJarName();

        try {
            FileSystem fileSystem = FileSystem.newInstance(URI.create(stopsHub.getJarUrl()), new Configuration());
            FSDataInputStream inputStream1 = fileSystem.open(new Path(stopsHub.getJarUrl()));
            ZipInputStream  zipInputStream1 = new ZipInputStream(inputStream1, Charset.forName("GBK"));
            ZipEntry zipEntry = null;

            while ((zipEntry = zipInputStream1.getNextEntry()) != null) {
                //如果是python的依赖文件(requirements.txt结尾),把内容读出来,生成dockerFile并打镜像
                if (!zipEntry.isDirectory() && zipEntry.getName().endsWith("requirements.txt")) {
                    System.out.println("find requirements.txt");
                    StringBuffer dockerFileSb = new StringBuffer();
                    String python_base_image = System.getenv("python_base_image");
                    //指定python算子基础镜像，或下载官方python镜像
                    if (python_base_image != null) {
                        dockerFileSb.append("FROM " + python_base_image + System.lineSeparator());
                    } else {
                        dockerFileSb.append("FROM python:" + stopsHub.getLanguageVersion() + System.lineSeparator());
                    }
                    //dockerFileSb.append("MAINTAINER " + LocalDatacenterInfo.LOCAL_DATACENTER_ID + System.lineSeparator());
//                      dockerFileSb.append("RUN apt update" + System.lineSeparator());
//                      dockerFileSb.append("RUN apt-get install -y zip" + System.lineSeparator());
                    //下面代码需要拉取算法包并解压到docker容器的pythonDir目录下；
                    //把dockerFile放在/storage/stopHub/下，放在hdfs上的Python zip 包也下载到这个目录下"hdfs://master:9000/user/piflow/plugin/RUSLE.zip"

                    dockerFileSb.append("COPY " + dstPath + " /usr/local" + System.lineSeparator());
                    dockerFileSb.append("RUN set -ex \\" + System.lineSeparator());
                    dockerFileSb.append("    && mkdir -p /pythonDir \\" + System.lineSeparator());
                    dockerFileSb.append("    && unzip /usr/local/" + jarName + " -d /pythonDir/ \\" + System.lineSeparator());

                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(zipInputStream1));
                    while ((line = br.readLine()) != null) {
                        if (line.trim().startsWith("#") || line.trim() == null || line.trim().equals("")) {
                            continue;
                        } else if (line.endsWith(".whl")) {
                            if (line.contains("#")) {
                                line = line.substring(0, line.indexOf("#")).trim();
                            }
                            dockerFileSb.append("    && pip install /pythonDir/" + line + " \\" + System.lineSeparator());
                        } else {
                            if (line.contains("#")) {
                                line = line.substring(0, line.indexOf("#")).trim();
                            }
                            dockerFileSb.append("    && pip install -i https://mirrors.aliyun.com/pypi/simple/ " + line + " \\" + System.lineSeparator());
                        }
                    }

                    dockerFileSb.append("    && rm -rf  ~/.cache/pip/* \\" + System.lineSeparator());
                    dockerFileSb.append("    && rm -rf /usr/local/" + jarName + System.lineSeparator());
                    FileUtils.writeData(dockerFileSavePath, dockerFileSb.toString());
                }
            }
        } catch (IOException e) {
            logger.error("write dockerFile failed, error message:" + e.getMessage(), e);
            return false;
        }
        return true;
    }

    private Integer buildAndPushDockerImage(String mountPath, String dockerFileSavePath, String dockerImagesName){
        //通过运行dockerFile的方式以及使用java.lang.Process process = new ProcessBuilder(command).start();来执行docker build  以及docker push
        logger.info("start build python docker image");
        try {
            // 构建 docker commit 命令
            String[] command = {"docker", "-H", "unix:///var/run/docker.sock", "build", mountPath, "-f", dockerFileSavePath,
                    "-t", dockerImagesName};
            System.out.println(joinToString(command, " "));
            // 执行命令
            java.lang.Process process = new ProcessBuilder(command).start();

            // 创建线程读取标准输出
            Thread outputThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            // 创建线程读取错误输出
            Thread errorThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.err.println(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            // 启动线程
            outputThread.start();
            errorThread.start();
            // 等待线程完成
            outputThread.join();
            errorThread.join();
            // 等待命令执行完成
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.out.println("Docker image build failed, please check the command output.");
                return exitCode;
            } else {
                System.out.println("Docker image built successfully.");
            }

            // 构建docker push 命令
            String[] command1 = {"docker", "-H", "unix:///var/run/docker.sock", "push", dockerImagesName};
            System.out.println(joinToString(command1, " "));
            // 执行命令
            java.lang.Process process1 = new ProcessBuilder(command1).start();
            // 创建线程读取标准输出
            Thread outputThread1 = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process1.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            // 创建线程读取错误输出
            Thread errorThread1 = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process1.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.err.println(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            // 启动线程
            outputThread1.start();
            errorThread1.start();
            // 等待线程完成
            outputThread1.join();
            errorThread1.join();
            // 等待命令执行完成
            int exitCode1 = process1.waitFor();
            if (exitCode1 != 0) {
                System.out.println("python算子镜像推送至远程仓库失败，请检查命令输出");
                return exitCode1;
            } else {
                System.out.println("Push docker image to repo successfully.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            return -1;
        }
        return 0;
    }

    public static String generateTagsName(String stopsHubName) {
        //拼成 name-时间戳:tag 格式   registryUrl/projectName/imageName-currentTimeMillis:tags
        String registryUrl = System.getenv("docker_central_warehouse");
        String registryProjectName = "piflow";
        String originProjectPath;
        if (StringUtils.isBlank(registryUrl)) {
            originProjectPath = "";
        } else if (registryUrl.endsWith("/")) {
            originProjectPath = registryUrl + registryProjectName + "/";
        } else {
            originProjectPath = registryUrl + "/" + registryProjectName + "/";
        }
        String tagsName = (originProjectPath + stopsHubName + "-" + System.currentTimeMillis() + ":" + "latest").replace("http://", "");
        return tagsName;
    }

    public static String joinToString(String[] input, String seperator) {
        String output = "";
        for (int i = 0; i < input.length; i++) {
            output += input[i];
            if (i < input.length - 1) {
                output += seperator;
            }
        }
        return output;
    }


    private String buildImageAndPush(DockerClient dockerClient, File dockerFile, String imageName, String tags) throws InterruptedException {
        //拼成 name-时间戳:tag 格式   registryUrl/projectName/imageName-currentTimeMillis:tags
        String registryUrl = DockerClientUtils.getRegistryUrl();
        String registryProjectName = DockerClientUtils.getRegistryProjectName();
        String originProjectPath;
        if (StringUtils.isBlank(registryUrl)) {
            originProjectPath = "";
        } else if (registryUrl.endsWith("/")) {
            originProjectPath = registryUrl + registryProjectName + "/";
        } else {
            originProjectPath = registryUrl + "/" + registryProjectName + "/";
        }
        String tagsName = (originProjectPath + imageName + "-" + System.currentTimeMillis() + ":" + tags).replace("http://", "");
        excImageAsync(dockerClient, dockerFile, tagsName);
        return tagsName;
    }

    public void excImageAsync(DockerClient dockerClient, File dockerFile, String tagsName) throws InterruptedException {
        //TODO 很慢 制作镜像很慢 目前是同步
        try {
            String registryUrl = DockerClientUtils.getRegistryUrl();
            DockerUtils.buildImage(dockerClient, dockerFile, tagsName);
            if (StringUtils.isNotBlank(registryUrl)) {
                DockerUtils.pushImage(dockerClient, tagsName);
            }
        } catch (InterruptedException e) {
            logger.error("====build image error! message:{}", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            //TODO delete dockerfile
            if (dockerFile.exists()) {
                dockerFile.delete();
            }
        }
    }

    @Override
    public String unmountStopsHub(String username, Boolean isAdmin, String id) {
        logger.info("=========unmount stops hub start==id:{}", id);
        StopsHub stopsHub = stopsHubDomain.getStopsHubById(username, isAdmin, id);
        if (stopsHub.getStatus() == StopsHubState.UNMOUNT) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("StopsHub have been UNMounted already!");
        }

        switch (stopsHub.getType()) {
            case SCALA:
                //unmount scala.jar
                return unMountScalaStopsHub(stopsHub, username);
            case PYTHON:
                //unmount python.zip
                return unMountPythonStopsZip(stopsHub, username);
            default:
                return ReturnMapUtils.setFailedMsgRtnJsonStr("UNMount failed, Data type error, contact the administrator");
        }
    }

    /**
     * @Description unmount scala.jar
     * @Param stopsHub
     * @Param username
     * @Return java.lang.String
     * @Author TY
     * @Date 17:03 2023/3/30
     **/

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public String unMountScalaStopsHub(StopsHub stopsHub, String username) {
        logger.info("==========unmount scala stops hub start:stopsHub:{}=================", JSON.toJSONString(stopsHub));
        StopsHubVo stopsHubVo = stopImpl.unmountStopsHub(stopsHub.getMountId());
        if (stopsHubVo == null) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("UNMount failed, please try again later");
        }

        //remove stops and groups into db,
        List<ThirdStopsComponentVo> stops = stopsHubVo.getStops();
        for (ThirdStopsComponentVo s : stops) {
            StopsComponent stopsComponent = stopsComponentDomain.getStopsComponentByBundle(s.getBundle());
            if (stopsComponent != null) {
                stopsComponentDomain.deleteStopsComponent(stopsComponent);
            }
        }

        stopsHub.setMountId(stopsHubVo.getMountId());
        stopsHub.setStatus(StopsHubState.UNMOUNT);
        stopsHub.setLastUpdateUser(username);
        stopsHub.setLastUpdateDttm(new Date());
        stopsHubDomain.updateStopHub(stopsHub);
        logger.info("==========unmount scala stops hub finish=================");
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("UNMount successful");
    }

    /**
     * unMount python.zip
     *
     * @param stopsHub
     * @param username
     * @return
     * @data 2022-02-03
     * @author leilei
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public String unMountPythonStopsZip(StopsHub stopsHub, String username) {
        logger.info("==========unmount python stops hub start:stopsHub:{}=================", JSON.toJSONString(stopsHub));
        //1.search stops_hub_file_record
        List<StopsHubFileRecord> fileRecordList = stopsHubFileRecordDomain.getStopsHubFileRecordByHubId(stopsHub.getId());
        //2.delete flow_stops_template、flow_stops_property_template、flow_stops_groups
        for (StopsHubFileRecord s : fileRecordList) {
            //file path = bundle
            StopsComponent stopsComponent = stopsComponentDomain.getStopsComponentByBundle(s.getFilePath());
            if (stopsComponent != null) {
                stopsComponentDomain.deleteStopsComponent(stopsComponent);
            }
            stopsHubFileRecordDomain.deleteStopsHubFileRecord(s.getId());
        }

//        stopsHub.setMountId(stopsHubVo.getMountId());
        stopsHub.setStatus(StopsHubState.UNMOUNT);
        stopsHub.setLastUpdateUser(username);
        stopsHub.setLastUpdateDttm(new Date());
        stopsHubDomain.updateStopHub(stopsHub);
        logger.info("==========unmount python stops hub finish=================");
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("UNMount successful");
    }

    /**
     * stopsHubListPage
     *
     * @param username username
     * @param isAdmin  is admin
     * @param pageNo   Number of pages
     * @param limit    Number each page
     * @param param    Search content
     * @return
     */
    @Override
    public String stopsHubListPage(String username, Boolean isAdmin, Integer pageNo, Integer limit, String param) {
        if (null == pageNo || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
        }
        Page<Process> page = PageHelper.startPage(pageNo, limit, "crt_dttm desc");
        stopsHubDomain.getStopsHubListParam(username, isAdmin, param);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        return PageHelperUtils.setLayTableParamRtnStr(page, rtnMap);
    }

    /**
     * del stopsHub
     *
     * @param username username
     * @param id       id
     * @return json
     */
    @Override
    public String delStopsHub(String username, Boolean isAdmin, String id) {
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("id"));
        }
        StopsHub stopsHubById = stopsHubDomain.getStopsHubById(username, isAdmin, id);
        if (null == stopsHubById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        StopsHubState status = stopsHubById.getStatus();
        if (StopsHubState.MOUNT == status) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The status is MOUNT and deletion is prohibited ");
        }
        int i = stopsHubDomain.deleteStopsHubById(username, id);
        if (i <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.DELETE_ERROR_MSG());
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
    }

    /**
     * stopsHub publishing
     *
     * @param username username
     * @param id       id
     * @return json
     */
    @Override
    public String stopsHubPublishing(String username, Boolean isAdmin, String id) throws JsonProcessingException {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        SysUser user = sysUserDomain.findUserByUserName(username);
        if (StringUtils.isBlank(user.getDeveloperAccessKey())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PLEASE_BIND_THE_DEVELOPER_ACCOUNT_MSG());
        }
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("id"));
        }
        StopsHub stopsHub = stopsHubDomain.getStopsHubById(username, isAdmin, id);
        if (null == stopsHub) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        if (StopsHubState.MOUNT != stopsHub.getStatus()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        if (StringUtils.isBlank(stopsHub.getBundles())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        String[] bundlesArray = stopsHub.getBundles().split(",");
        //each stops hub must contain only one component
        List<StopsComponent> stopsComponents = stopsComponentDomain.getOnlyStopsComponentByBundles(bundlesArray);
        if (null == stopsComponents || stopsComponents.size() <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        if (stopsComponents.size() > 1) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        PublishComponentVo publishComponentVo = new PublishComponentVo();
        switch (stopsHub.getType()) {
            case DEFAULT:
                break;
            case SCALA:
//                StopsComponent stopsComponentByBundle1 = stopsComponentDomain.getStopsComponentByBundle(bundlesArray[0]);
                ThirdStopsComponentVo thirdStopsComponentVo = stopImpl.getStopInfo(stopsComponents.get(0).getBundel());
                if (null == thirdStopsComponentVo) {
                    return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
                }
                publishComponentVo.setName(thirdStopsComponentVo.getName());
                //base64
                publishComponentVo.setLogo(thirdStopsComponentVo.getIcon());
                publishComponentVo.setDescription(thirdStopsComponentVo.getDescription());
                publishComponentVo.setCategory(thirdStopsComponentVo.getGroups());
                publishComponentVo.setBundle(thirdStopsComponentVo.getBundle());
                publishComponentVo.setAuthorName(thirdStopsComponentVo.getOwner());
                publishComponentVo.setSoftware(Collections.singletonList(SysParamsCache.MARKET_SOFTWARE_FLAG));
                publishComponentVo.setComponentType("algorithm");
                List<Map<String, String>> params = new ArrayList<>();
                thirdStopsComponentVo.getProperties().forEach(pro -> {
                    Map<String, String> param = new HashMap<>();
                    param.put(pro.getName(), pro.getDefaultValue());
                    params.add(param);

                });
                publishComponentVo.setParameters(params);
                break;
            case PYTHON:
                StopsComponent stopsComponentByBundle = stopsComponentDomain.getStopsComponentByBundle(stopsComponents.get(0).getBundel());
                if (null == stopsComponentByBundle) {
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("Please add a component before publishing");
                }
                publishComponentVo.setName(stopsComponentByBundle.getName());
                //base64
                publishComponentVo.setLogo(FileUtils.encryptToBase64(stopsComponentByBundle.getImageUrl().replace("/images", "").replace(SysParamsCache.SYS_CONTEXT_PATH, SysParamsCache.IMAGES_PATH)));
                publishComponentVo.setDescription(stopsComponentByBundle.getDescription());
                publishComponentVo.setCategory(stopsComponentByBundle.getGroups());
                publishComponentVo.setBundle(stopsComponentByBundle.getBundel());
                publishComponentVo.setAuthorName(stopsComponentByBundle.getOwner());
                publishComponentVo.setSoftware(Collections.singletonList(SysParamsCache.MARKET_SOFTWARE_FLAG));
                publishComponentVo.setComponentType("algorithm");
                List<Map<String, String>> params1 = new ArrayList<>();
                stopsComponentByBundle.getProperties().forEach(pro -> {
                    Map<String, String> param = new HashMap<>();
                    param.put(pro.getName(), pro.getDefaultValue());
                    params1.add(param);

                });
                publishComponentVo.setParameters(params1);
                break;
            default:
                return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG() + ",unknown component type!");
        }
        File file = new File(stopsHub.getJarUrl());
        Map<String, Object> rtnMap = marketImpl.publishComponents(user.getDeveloperAccessKey(), publishComponentVo, file);
        String code = rtnMap.get("code").toString();
        if (!"200".equals(code)) {
            return ReturnMapUtils.toJson(rtnMap);
        }
        stopsHub.setIsPublishing(true);
        stopsHubDomain.updateStopHub(stopsHub);
        return ReturnMapUtils.toJson(rtnMap);
    }

    /**
     * @param stopsHubId
     * @return
     */
    @Override
    public String getStopsHubInfoByStopHubId(String username, Boolean isAdmin, String stopsHubId) {
        if (StringUtils.isEmpty(stopsHubId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("id"));
        } else {
            StopsHub stopsHub = stopsHubDomain.getStopsHubById(username, isAdmin, stopsHubId);
            switch (stopsHub.getType()) {
                case PYTHON:
                    //search from stops_hub_file_record
                    List<StopsHubFileRecord> fileRecordList = stopsHubFileRecordDomain.getStopsHubFileRecordByHubId(stopsHubId);
                    //这里根据是否有组件信息,修改isComponent字段的值,然后根据此字段排序,将为true的放在前面,用于页面列表为组件的在前
                    fileRecordList.forEach(a -> {
                        if (a.getStopsComponent() != null) {
                            a.setIsComponent(true);
                        }
                    });
                    fileRecordList.sort(Comparator.comparing(StopsHubFileRecord::getIsComponent).reversed());
                    List<StopsHubInfoVo> list = new ArrayList<>();
                    for (StopsHubFileRecord stopsHubFileRecord : fileRecordList) {
                        StopsHubInfoVo stopsHubInfoVo = new StopsHubInfoVo();
                        stopsHubInfoVo.setId(stopsHubFileRecord.getId());
                        stopsHubInfoVo.setStopName(stopsHubFileRecord.getFileName());
                        stopsHubInfoVo.setStopBundle(stopsHubFileRecord.getFilePath());
                        stopsHubInfoVo.setStopHubId(stopsHubFileRecord.getStopsHubId());
                        list.add(stopsHubInfoVo);
                    }
                    return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("data", list);
                case SCALA:
                    //search from flow_stops_template
                    List<StopsComponent> stopsComponentByStopsHubId = stopsComponentDomain.getStopsComponentByStopsHubId(stopsHubId);
                    List<StopsHubInfoVo> stopsHubInfoVos = stopsComponentByStopsHubId.stream().map(stop -> {
                        StopsHubInfoVo stopsHubInfoVo = new StopsHubInfoVo();
                        stopsHubInfoVo.setId(stop.getId());
                        stopsHubInfoVo.setStopName(stop.getName());
                        stopsHubInfoVo.setStopBundle(stop.getBundel());
                        stopsHubInfoVo.setStopHubId(stop.getStopsHubId());
                        return stopsHubInfoVo;
                    }).collect(Collectors.toList());
                    return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("data", stopsHubInfoVos);
                default:
                    return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
            }
        }
    }

    /**
     * @Description update component info when save or remove a component except scala component
     * @Param stopsHubInfoVo
     * @Param file
     * @Param username
     * @Param isAdmin
     * @Return java.lang.String
     * @Author TY
     * @Date 15:52 2023/4/3
     **/
    @Override
    public String updateComponentInfo(StopsHubInfoVo stopsHubInfoVo, MultipartFile file, String username, Boolean isAdmin) {
        if (stopsHubInfoVo == null) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        } else {
            StopsComponent stopsComponent = stopsComponentDomain.getStopsComponentByBundle(stopsHubInfoVo.getStopBundle());
            if (stopsComponent != null) {
                if (ComponentFileType.PYTHON == stopsComponent.getComponentType()) {
                    //python component
                    return updatePythonStopsHubInfo(stopsHubInfoVo, username, file);
                } else {
                    logger.error("The data type is incorrect. Contact the administrator");
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("The data type is incorrect. Contact the administrator");
                }
            } else {
                StopsHubFileRecord stopsHubFileRecord = stopsHubFileRecordDomain.getStopsHubFileRecordByBundle(stopsHubInfoVo.getStopBundle());
                StopsHub stopsHub = stopsHubDomain.getStopsHubById("", true, stopsHubFileRecord.getStopsHubId());
                if (stopsHub.getType() == ComponentFileType.PYTHON) {
                    return updatePythonStopsHubInfo(stopsHubInfoVo, username, file);
                } else {
                    logger.error("The data type is incorrect. Contact the administrator");
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("The data type is incorrect. Contact the administrator");
                }
            }
        }
    }


    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public String updatePythonStopsHubInfo(StopsHubInfoVo stopsHubInfoVo, String username, MultipartFile file) {
        if (stopsHubInfoVo == null) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        if (!stopsHubInfoVo.getIsPythonComponent()) {
            //delete component, property, component group
            StopsComponent stopsComponent = stopsComponentDomain.getStopsComponentByBundle(stopsHubInfoVo.getStopBundle());
            if (stopsComponent != null) {
                stopsComponentDomain.deleteStopsComponent(stopsComponent);
            }
        } else {
            //update component, property, component group
            List<String> groupNameList = new ArrayList<>();
            Map<String, StopsComponentGroup> stopsComponentGroupMap = new HashMap<>();
            groupNameList.addAll(Arrays.asList(stopsHubInfoVo.getGroups().split(",")));
            List<String> distinctGroupNameList = groupNameList.stream().distinct().collect(Collectors.toList());
            List<StopsComponentGroup> stopsComponentGroupList = stopsComponentDomain.getStopGroupByGroupNameList(distinctGroupNameList);
            for (StopsComponentGroup sGroup : stopsComponentGroupList) {
                stopsComponentGroupMap.put(sGroup.getGroupName(), sGroup);
            }

            for (String groupName : distinctGroupNameList) {
                StopsComponentGroup stopsComponentGroup = stopsComponentGroupMap.get(groupName);
                if (stopsComponentGroup == null) {
                    // add group into db
                    stopsComponentGroup = StopsComponentGroupUtils.stopsComponentGroupNewNoId(username);
                    stopsComponentGroup.setId(UUIDUtils.getUUID32());
                    stopsComponentGroup.setGroupName(groupName);
                    stopsComponentDomain.addStopsComponentGroup(stopsComponentGroup);

                    stopsComponentGroupMap.put(groupName, stopsComponentGroup);
                }
            }

            //add stops-group relation
            List<StopsComponentGroup> stopGroupByName = new ArrayList<>();
            for (String groupName : distinctGroupNameList) {
                stopGroupByName.add(stopsComponentGroupMap.get(groupName));
            }
            //upload image file
            if (file != null && !file.isEmpty()) {
                Map<String, Object> uploadRtnMap = FileUtils.uploadRtnMap(file, SysParamsCache.IMAGES_PATH, null);
                if (null == uploadRtnMap || uploadRtnMap.isEmpty()) {
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("Upload failed, please try again later");
                }
                Integer code = (Integer) uploadRtnMap.get("code");
                if (500 == code) {
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("failed to upload file");
                }
                String path = (String) uploadRtnMap.get("saveFileName");
                String imageUrl = SysParamsCache.SYS_CONTEXT_PATH + "/images/" + path;
                stopsHubInfoVo.setImageUrl(imageUrl);
            }

            StopsHubFileRecord stopsHubFileRecord = stopsHubFileRecordDomain.getStopsHubFileRecordByBundle(stopsHubInfoVo.getStopBundle());
            if (stopsHubFileRecord == null) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.DATA_ERROR_MSG());
            }
            StopsComponent stopsComponent = stopsComponentDomain.getStopsComponentByBundle(stopsHubInfoVo.getStopBundle());
            if (stopsComponent == null) {
                stopsComponent = new StopsComponent();
                stopsComponent.setId(UUIDUtils.getUUID32());
                stopsComponent.setBundel(stopsHubInfoVo.getStopBundle());
                stopsComponent.setName(stopsHubFileRecord.getFileName());
                stopsComponent.setCrtUser(username);
                stopsComponent.setLastUpdateUser(username);
                stopsComponent.setDockerImagesName(stopsHubFileRecord.getDockerImagesName());
                stopsComponent.setComponentType(ComponentFileType.PYTHON);
                stopsComponent.setStopsHubId(stopsHubFileRecord.getStopsHubId());

                stopsComponent.setGroups(stopsHubInfoVo.getGroups());
                stopsComponent.setDescription(stopsHubInfoVo.getBundleDescription());
                stopsComponent.setImageUrl(stopsHubInfoVo.getImageUrl());
                stopsComponent.setOwner(stopsHubInfoVo.getOwner());

                //inports outports
                if (StringUtils.isBlank(stopsHubInfoVo.getInports())) {
                    stopsComponent.setInports("DefaultPort");
                    stopsComponent.setInPortType(PortType.DEFAULT);
                } else {
                    stopsComponent.setInports(stopsHubInfoVo.getInports());
                    stopsComponent.setInPortType(PortType.USER_DEFAULT);
                }
                if (StringUtils.isBlank(stopsHubInfoVo.getOutports())) {
                    stopsComponent.setOutports("DefaultPort");
                    stopsComponent.setOutPortType(PortType.DEFAULT);
                } else {
                    stopsComponent.setOutports(stopsHubInfoVo.getOutports());
                    stopsComponent.setOutPortType(PortType.USER_DEFAULT);
                }
                stopsComponent.setLastUpdateUser(username);
                stopsComponent.setLastUpdateDttm(new Date());

                //init properties
                if (stopsHubInfoVo.getIsHaveParams() && stopsHubInfoVo.getProperties() != null && stopsHubInfoVo.getProperties().size() > 0) {
                    List<StopsComponentProperty> propertyList = new ArrayList<>();
                    for (StopsComponentPropertyVo property : stopsHubInfoVo.getProperties()) {
                        StopsComponentProperty stopsComponentProperty = StopsComponentPropertyUtils.stopsComponentPropertyNewNoId(username);
                        stopsComponentProperty.setId(UUIDUtils.getUUID32());
                        stopsComponentProperty.setDescription(property.getDescription());
                        stopsComponentProperty.setName(property.getName());
                        stopsComponentProperty.setPropertySort(property.getPropertySort());
                        stopsComponentProperty.setExample(property.getExample());
                        stopsComponentProperty.setStopsTemplate(stopsComponent.getId());
                        //TODO
                        stopsComponentProperty.setAllowableValues("[\"\"]");
                        propertyList.add(stopsComponentProperty);
                    }
                    stopsComponent.setProperties(propertyList);
                }
                stopsComponentDomain.addStopsComponentAndChildren(stopsComponent);
            } else {
                //update stop和stopProperties
                stopsComponent.setGroups(stopsHubInfoVo.getGroups());
                stopsComponent.setDescription(stopsHubInfoVo.getBundleDescription());
                stopsComponent.setOwner(stopsHubInfoVo.getOwner());

                if (StringUtils.isBlank(stopsHubInfoVo.getInports())) {
                    stopsComponent.setInports("DefaultPort");
                    stopsComponent.setInPortType(PortType.DEFAULT);
                } else {
                    stopsComponent.setInports(stopsHubInfoVo.getInports());
                    stopsComponent.setInPortType(PortType.USER_DEFAULT);
                }

                if (StringUtils.isBlank(stopsHubInfoVo.getOutports())) {
                    stopsComponent.setOutports("DefaultPort");
                    stopsComponent.setOutPortType(PortType.DEFAULT);
                } else {
                    stopsComponent.setOutports(stopsHubInfoVo.getOutports());
                    stopsComponent.setOutPortType(PortType.USER_DEFAULT);
                }
                stopsComponent.setLastUpdateUser(username);
                stopsComponent.setLastUpdateDttm(new Date());
                stopsComponent.setImageUrl(stopsHubInfoVo.getImageUrl());
                //update flow_stops_template
                stopsComponentDomain.updateStopsComponent(stopsComponent);
                //update properties delete old properties and add new properties
                stopsComponentDomain.deleteStopsComponentProperty(stopsComponent.getId());
                if (stopsHubInfoVo.getIsHaveParams() && stopsHubInfoVo.getProperties() != null && stopsHubInfoVo.getProperties().size() > 0) {
                    List<StopsComponentProperty> propertyList = new ArrayList<>();
                    for (StopsComponentPropertyVo property : stopsHubInfoVo.getProperties()) {

                        StopsComponentProperty stopsComponentProperty = StopsComponentPropertyUtils.stopsComponentPropertyNewNoId(username);
                        stopsComponentProperty.setId(UUIDUtils.getUUID32());
                        stopsComponentProperty.setDescription(property.getDescription());
                        stopsComponentProperty.setName(property.getName());
                        stopsComponentProperty.setPropertySort(property.getPropertySort());
                        stopsComponentProperty.setExample(property.getExample());
                        stopsComponentProperty.setStopsTemplate(stopsComponent.getId());
                        propertyList.add(stopsComponentProperty);
                    }
                    stopsComponentDomain.insertStopsComponentProperty(propertyList);
                    stopsComponent.setProperties(propertyList);
                }
            }
            //add stop and group relationship
            for (StopsComponentGroup sGroup : stopGroupByName) {
                stopsComponentDomain.deleteGroupCorrelationByGroupIdAndStopId(sGroup.getId(), stopsComponent.getId());
                stopsComponentDomain.insertAssociationGroupsStopsTemplate(sGroup.getId(), stopsComponent.getId());
            }
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.UPDATE_SUCCEEDED_MSG());
    }

}

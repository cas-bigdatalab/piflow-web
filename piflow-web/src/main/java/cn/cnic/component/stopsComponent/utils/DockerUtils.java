package cn.cnic.component.stopsComponent.utils;

import com.alibaba.fastjson2.JSON;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DockerUtils {
    private final static Logger logger = LoggerFactory.getLogger(DockerUtils.class);

    /*// docker服务端IP地址
    @Value("DOCKER_HOST")
    public static final String DOCKER_HOST="tcp://10.0.85.137:2375";
    // docker安全证书配置路径
    public static final String DCOEKR_CERT_PATH="";
    // docker是否需要TLS认证
    public static final Boolean DOCKER_TLS_VERIFY=false;
    // Harbor仓库的IP
//    public static final String REGISTRY_URL="192.168.79.131:8443";
    public static final String REGISTRY_URL="";
    // Harbor仓库的名称
//    public static final String REGISTRY_PROJECT_NAME="test";
    public static final String REGISTRY_PROJECT_NAME="";
    // Harbor仓库的登录用户名
//    public static final String REGISTRY_USER_NAME="admin";
    public static final String REGISTRY_USER_NAME="";
    // Harbor仓库的登录密码
//    public static final String REGISTRY_PASSWORD="Harbor12345";
    public static final String REGISTRY_PASSWORD="";
    // docker远程仓库的类型，此处默认是harbor
    public static final String REGISTRY_TYPE="harbor";

    public static final String REGISTRY_PROTOCAL="https://";


    *//**
     * 构建DocekrClient实例
     * @param dockerHost
     * @param tlsVerify
     * @param dockerCertPath
     * @param registryUsername
     * @param registryPassword
     * @param registryUrl
     * @return
     *//*
    public static DockerClient getDocekrClient(String dockerHost,boolean tlsVerify,String dockerCertPath,
                                               String registryUsername, String registryPassword,String registryUrl){
        DefaultDockerClientConfig dockerClientConfig = null;
        if(tlsVerify){
            dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
                    .withDockerHost(DOCKER_HOST)
                    .withDockerTlsVerify(true)
                    .withDockerCertPath(DCOEKR_CERT_PATH)
                    .withRegistryUsername(REGISTRY_USER_NAME)
                    .withRegistryPassword(REGISTRY_PASSWORD)
                    .withRegistryUrl(registryUrl)
                    .build();
        }else {
            dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
                    .withDockerHost(DOCKER_HOST)
                    .withDockerTlsVerify(false)
                    .withDockerCertPath(DCOEKR_CERT_PATH)
                    .withRegistryUsername(REGISTRY_USER_NAME)
                    .withRegistryPassword(REGISTRY_PASSWORD)
                    .withRegistryUrl(registryUrl)
                    .build();
        }
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(dockerClientConfig.getDockerHost())
                .sslConfig(dockerClientConfig.getSSLConfig())
                .build();

        return DockerClientImpl.getInstance(dockerClientConfig,httpClient);
    }

    public static DockerClient getDockerClient(){
        return getDocekrClient(DOCKER_HOST,DOCKER_TLS_VERIFY,DCOEKR_CERT_PATH,REGISTRY_USER_NAME,REGISTRY_PASSWORD,REGISTRY_URL);
    }*/

    /**
     * 获取docker基础信息
     *
     * @param dockerClient
     * @return
     */
    public static String getDockerInfo(DockerClient dockerClient) {
        Info info = dockerClient.infoCmd().exec();
        return JSON.toJSONString(info);
    }

    /**
     * 给镜像打标签
     *
     * @param dockerClient
     * @param imageIdOrFullName
     * @param respository
     * @param tag
     */
    public static void tagImage(DockerClient dockerClient, String imageIdOrFullName, String respository, String tag) {
        TagImageCmd tagImageCmd = dockerClient.tagImageCmd(imageIdOrFullName, respository, tag);
        tagImageCmd.exec();
    }

    /**
     * load镜像
     *
     * @param dockerClient
     * @param inputStream
     */
    public static void loadImage(DockerClient dockerClient, InputStream inputStream) {
        LoadImageCmd loadImageCmd = dockerClient.loadImageCmd(inputStream);
        loadImageCmd.exec();
    }

    /**
     * pull镜像
     *
     * @param dockerClient
     * @param repository
     */
    public static PullImageCmd pullImage(DockerClient dockerClient, String repository) {
        PullImageCmd pullImageCmd = dockerClient.pullImageCmd(repository);
        pullImageCmd.exec(new ResultCallback<PullResponseItem>() {
            @Override
            public void onStart(Closeable closeable) {
                logger.info("开始拉取");
            }

            @Override
            public void onNext(PullResponseItem object) {
                logger.info("拉取下一个");
            }

            @Override
            public void onError(Throwable throwable) {
                logger.error("拉取发生错误:" + throwable.getMessage(), throwable);
            }

            @Override
            public void onComplete() {
                logger.info("拉取成功");
            }

            @Override
            public void close() throws IOException {
                logger.info("拉取结束");
            }
        });
        return pullImageCmd;
    }

    /**
     * 推送镜像
     *
     * @param dockerClient
     * @param imageName
     * @return
     * @throws InterruptedException
     */
    public static Boolean pushImage(DockerClient dockerClient, String imageName) throws InterruptedException {
        logger.info("=========push image start==================");
        final Boolean[] result = {true};
        ResultCallback.Adapter<PushResponseItem> callBack = new ResultCallback.Adapter<PushResponseItem>() {
            @Override
            public void onNext(PushResponseItem pushResponseItem) {
                if (pushResponseItem != null) {
                    ResponseItem.ErrorDetail errorDetail = pushResponseItem.getErrorDetail();
                    if (errorDetail != null) {
                        result[0] = false;
                        logger.error(errorDetail.getMessage(), errorDetail);
                    }
                }
                super.onNext(pushResponseItem);
            }
        };
        dockerClient.pushImageCmd(imageName).exec(callBack).awaitCompletion();
        logger.info("=========push image finish==================");
        return result[0];
    }

    /**
     * 从镜像的tar文件中获取镜像名称
     *
     * @param imagePath
     * @return
     */
    public static String getImageName(String imagePath) {
        try {
            return UnCompressUtils.getImageName(imagePath);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过dockerFile构建镜像
     *
     * @param dockerClient
     * @param dockerFile
     * @param tagsName
     * @return
     */
    public static Map<String, String> buildImage(DockerClient dockerClient, File dockerFile, String tagsName) {
        Set<String> tagsSet = new HashSet<>();
        //tagsSet.add(imageName+"-"+System.currentTimeMillis()+":"+tags);
        tagsSet.add(tagsName);
        BuildImageCmd buildImageCmd = dockerClient.buildImageCmd(dockerFile)
                .withTags(tagsSet);
        BuildImageResultCallback buildImageResultCallback = new BuildImageResultCallback() {
            @Override
            public void onNext(BuildResponseItem item) {
                logger.info("{}", item.getStream());
                super.onNext(item);
            }
        };
        Map<String, String> result = new HashMap<>();
        String imageId = buildImageCmd.exec(buildImageResultCallback).awaitImageId(30, TimeUnit.MINUTES);
        result.put("imageId", imageId);
        result.put("imageName", tagsName);
        logger.info("docker image create:" + JSON.toJSONString(result));
        return result;
    }

    /**
     * 获取镜像列表
     *
     * @param dockerClient
     * @return
     */
    public static List<Image> imageList(DockerClient dockerClient) {
        List<Image> imageList = dockerClient.listImagesCmd().withShowAll(true).exec();
        return imageList;
    }
}
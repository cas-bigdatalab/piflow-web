package cn.cnic.component.stopsComponent.utils;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DockerClientUtils {

    // docker服务端IP地址
    public static String DOCKER_HOST;

    @Value("${DOCKER_HOST}")
    public void setDockerHost(String dockerHost) {
        this.DOCKER_HOST = dockerHost;
    }

    public static String getDockerHost() {
        return DOCKER_HOST;
    }

    // docker安全证书配置路径
    public static String DCOEKR_CERT_PATH;

    @Value("${DCOEKR_CERT_PATH}")
    public void setDcoekrCertPath(String dcoekrCertPath) {
        this.DCOEKR_CERT_PATH = dcoekrCertPath;
    }

    public static String getDcoekrCertPath() {
        return DCOEKR_CERT_PATH;
    }

    // docker是否需要TLS认证
    public static Boolean DOCKER_TLS_VERIFY;

    @Value("${DOCKER_TLS_VERIFY}")
    public void setDockerTlsVerify(String dockerTlsVerify) {
        //因为配置文件中,虽然写的是true/false;但传进来就是String值,所以这里做个转换
        this.DOCKER_TLS_VERIFY = Boolean.valueOf(dockerTlsVerify);
    }

    public static Boolean getDockerTlsVerify() {
        return DOCKER_TLS_VERIFY;
    }

    // Harbor仓库的IP
    public static String REGISTRY_URL;

    @Value("${REGISTRY_URL}")
    public void setRegistryUrl(String registryUrl) {
        this.REGISTRY_URL = registryUrl;
    }

    public static String getRegistryUrl() {
        return REGISTRY_URL;
    }

    // Harbor仓库的名称
    public static String REGISTRY_PROJECT_NAME;

    @Value("${REGISTRY_PROJECT_NAME}")
    public void setRegistryProjectName(String registryProjectName) {
        this.REGISTRY_PROJECT_NAME = registryProjectName;
    }

    public static String getRegistryProjectName() {
        return REGISTRY_PROJECT_NAME;
    }

    // Harbor仓库的登录用户名
    public static String REGISTRY_USER_NAME;
    @Value("${REGISTRY_USER_NAME}")
    public void setRegistryUserName(String registryUserName) {
        this.REGISTRY_USER_NAME = registryUserName;
    }

    public static String getRegistryUserName() {
        return REGISTRY_USER_NAME;
    }
    // Harbor仓库的登录密码
    public static String REGISTRY_PASSWORD;
    @Value("${REGISTRY_PASSWORD}")
    public void setRegistryPassword(String registryPassword) {
        this.REGISTRY_PASSWORD = registryPassword;
    }

    public static String getRegistryPassword() {
        return REGISTRY_PASSWORD;
    }

    // docker远程仓库的类型，此处默认是harbor
    public static String REGISTRY_TYPE;

    @Value("${REGISTRY_TYPE}")
    public void setRegistryType(String registryType) {
        this.REGISTRY_TYPE = registryType;
    }

    public static String getRegistryType() {
        return REGISTRY_TYPE;
    }

    public static String REGISTRY_PROTOCAL;

    @Value("${REGISTRY_PROTOCAL}")
    public void setRegistryProtocal(String registryProtocal) {
        this.REGISTRY_PROTOCAL = registryProtocal;
    }

    public static String getRegistryProtocal() {
        return REGISTRY_PROTOCAL;
    }


    /**
     * 构建DocekrClient实例
     *
     * @param dockerHost
     * @param tlsVerify
     * @param dockerCertPath
     * @param registryUsername
     * @param registryPassword
     * @param registryUrl
     * @return
     */
    public static DockerClient getDockerClient(String dockerHost, boolean tlsVerify, String dockerCertPath,
                                               String registryUsername, String registryPassword, String registryUrl) {
        DefaultDockerClientConfig dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
                    .withDockerHost(dockerHost)
                    .withDockerTlsVerify(tlsVerify)
                    .withDockerCertPath(dockerCertPath)
                    .withRegistryUsername(registryUsername)
                    .withRegistryPassword(registryPassword)
                    .withRegistryUrl(registryUrl)
                    .build();
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(dockerClientConfig.getDockerHost())
                .sslConfig(dockerClientConfig.getSSLConfig())
                .build();

        return DockerClientImpl.getInstance(dockerClientConfig, httpClient);
    }

    public static DockerClient getDockerClient() {
        return getDockerClient(getDockerHost(), getDockerTlsVerify(), getDcoekrCertPath(), getRegistryUserName(), getRegistryPassword(), getRegistryUrl());
    }
}

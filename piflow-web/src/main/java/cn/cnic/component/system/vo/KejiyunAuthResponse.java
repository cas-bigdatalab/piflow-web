package cn.cnic.component.system.vo;

import cn.cnic.base.utils.JsonUtils;
import cn.hutool.system.UserInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 查询科技云用户信息返回值实体类
 */
public class KejiyunAuthResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("userInfo")
    private String userInfo; // 注意，userInfo 是一个 JSON 字符串，所以这里用 String 类型

    // Getter 和 Setter 方法
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    //将 userInfo 的 JSON 字符串转换为 KejiyunAuthUserInfo 对象
    public KejiyunAuthUserInfo getUserInfo() {
        try {
            KejiyunAuthUserInfo authUserInfo = JsonUtils.toObject(userInfo, KejiyunAuthUserInfo.class);
            return authUserInfo;
        } catch (Exception e) {
            return null;

        }
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

}

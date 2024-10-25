package cn.cnic.bundle.util;


import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.ReturnMapUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Service
public class DataSpaceService {

    private static Logger logger = LoggerUtil.getLogger();


    public static void main(String[] args) throws Exception {
        String dsAppId = "53756";
        String dsSign = "MmIzNzliZTcwMWU3NDE0ZTliNmY5OTFkYTA5NzVlNzc=";
        String dsRemote = "http://10.0.90.47";
        String dsEmail = "admin@dataspace.cn";
        String dsSpaceName = "预览测试";


        // 1、根据用户邮箱获取用户ID
        String userInfo = DataSpaceService.getUserIDUtil(dsAppId, dsSign, dsRemote, dsEmail);
        String dsUserId = "";
        try {
            JSONObject jsonObject = JSONObject.fromObject(userInfo);
            dsUserId = jsonObject.getJSONObject("data").getString("id");
        } catch (Exception e) {
            throw new Exception("解析用户信息失败: " + userInfo, e);
        }
        System.out.println(dsUserId);
        // 2、根据空间名称获取空间ID
        JSONArray dataSpaceArray = DataSpaceService.spaceListUtil(dsAppId, dsSign, dsRemote, dsUserId);
        if (dataSpaceArray.size() == 0) {
            throw new Exception("该账号下不存在空间");
        }
        String dsSpaceId = "";
        for (int i = 0; i < dataSpaceArray.size(); i++) {
            JSONObject space = dataSpaceArray.getJSONObject(i);
            if (space.getString("spaceName").equals(dsSpaceName)) {
                dsSpaceId = space.getString("spaceId");
                break;
            }
        }
        if (dsSpaceId.isEmpty()) {
            throw new Exception("该空间《" + dsSpaceName + "》不存在，请核对！");
        }
        System.out.println(dsSpaceId);

        //  获取用户空间文件列表
        String fileListStr = DataSpaceService.fileListUtil(dsAppId, dsSign, dsRemote, dsSpaceId, null);

        JSONArray filesJsonArrayAllInfo = JSONObject.fromObject(fileListStr).getJSONObject("data").getJSONArray("files");

        JSONArray filesJsonArray = new JSONArray();
        for (int i = 0; i < filesJsonArrayAllInfo.size(); i++) {
            if (!filesJsonArrayAllInfo.getJSONObject(i).getString("hash").equals("0")) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("path", filesJsonArrayAllInfo.getJSONObject(i).getString("path"));
                jsonObject.put("dirs", filesJsonArrayAllInfo.getJSONObject(i).getString("dirs"));
                jsonObject.put("hash", filesJsonArrayAllInfo.getJSONObject(i).getString("hash"));
                filesJsonArray.add(jsonObject);
                System.out.println(jsonObject);
            }
        }
        System.out.println(filesJsonArray);

    }

    public String getDataSpaceList(String dsAppId, String dsSign, String dsRemote, String dsEmail) {
        // 1、根据用户邮箱获取用户ID
        String userInfo = DataSpaceService.getUserIDUtil(dsAppId, dsSign, dsRemote, dsEmail);
        String dsUserId = "";
        try {
            JSONObject jsonObject = JSONObject.fromObject(userInfo);
            dsUserId = jsonObject.getJSONObject("data").getString("id");
        } catch (Exception e) {
            logger.error("解析用户信息失败: " + userInfo, e);
            return ("解析用户信息失败: " + userInfo);
        }
        System.out.println(dsUserId);
        // 2、根据空间名称获取空间ID
        JSONArray dataSpaceArray = DataSpaceService.spaceListUtil(dsAppId, dsSign, dsRemote, dsUserId);
        if (dataSpaceArray.size() == 0) {
            logger.error(dsEmail + "账号下不存在空间");
            return dsEmail + "账号下不存在空间";
        }
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("data", dataSpaceArray);


    }

    /**
     * 获取用户空间文件列表
     */
    public String geFileListByDsSpaceId(String dsAppId, String dsSign, String dsRemote, String dsSpaceId, String hash) {
        // 传null获取第一级目录
        if(StringUtils.isBlank(dsSpaceId)) {
            DataSpaceService.fileListUtil(dsAppId, dsSign, dsRemote, dsSpaceId, null);
        }
        // 传hash获取指定目录下的文件列表
        return DataSpaceService.fileListUtil(dsAppId, dsSign, dsRemote, dsSpaceId, hash);
    }


    /**
     * 根据邮箱获取用户ID
     *
     * @param appId
     * @param appSecret
     * @param dsRemote
     * @param email
     * @return
     */
    private static String getUserIDUtil(String appId, String appSecret, String dsRemote, String email) {
        String currentDateTimeString = getCurrentDateTimeString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> paramMap = new HashMap<>(8);
        paramMap.put("email", email);
        paramMap.put("appId", appId);
        paramMap.put("timestamp", currentDateTimeString);
        paramMap.put("version", "1.0");
        final String sign = md5Encrypt(paramSort(paramMap) + appSecret);
        paramMap.put("sign", sign);

        String body = "";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String paramsPath = String.format("%s/api/ds.open/userInfo?appId=%s&email=%s&sign=%s&timestamp=%s&version=%s",
                    dsRemote,
                    encode(appId),
                    encode(email),
                    encode(sign),
                    encode(currentDateTimeString),
                    encode("1.0"));
            HttpGet httpGet = new HttpGet(paramsPath);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                // Get the response body
                body = EntityUtils.toString(response.getEntity());
                // Parse the response JSON
//                JSONObject jsonObject = JSONObject.parseObject(body);
//                if(jsonObject.getJSONObject("data").containsKey("id")){
//                 String  userId = jsonObject.getJSONObject("data").getString("id");
//                }
            }
        } catch (Exception e) {
            throw new RuntimeException("远程请求错误", e);
        }
        return body;
    }


    /**
     * 根据空间ID，获取该空间在服务器上的真实路径
     *
     * @param appId
     * @param appSecret
     * @param dsRemote
     * @param spaceId
     * @return
     */
    private static String getPathUtil(String appId, String appSecret, String dsRemote, String spaceId) {
        String currentDateTimeString = getCurrentDateTimeString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> paramMap = new HashMap<>(8);
        paramMap.put("spaceId", spaceId);
        paramMap.put("appId", appId);
        paramMap.put("timestamp", currentDateTimeString);
        paramMap.put("version", "1.0");
        final String sign = md5Encrypt(paramSort(paramMap) + appSecret);
        paramMap.put("sign", sign);

        String spacePath = "";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//            String paramsPath = dsRemote + "/api/ds.open/space/info?" + "appId=" + appId + "&spaceId=" + spaceId
//                    + "&sign=" + sign + "&timestamp=" + currentDateTimeString + "&version=1.0";
            String paramsPath = String.format("%s/api/ds.open/space/info?appId=%s&spaceId=%s&sign=%s&timestamp=%s&version=%s",
                    dsRemote,
                    encode(appId),
                    encode(spaceId),
                    encode(sign),
                    encode(currentDateTimeString),
                    encode("1.0"));
            HttpGet httpGet = new HttpGet(paramsPath);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                // Get the response body
                String body = EntityUtils.toString(response.getEntity());
                // Parse the response JSON
                JSONObject jsonObject = JSONObject.fromObject(body);
                spacePath = jsonObject.getJSONObject("data").getJSONObject("uploadLink").getString("spacePath");
            }
        } catch (Exception e) {
            throw new RuntimeException("远程请求错误", e);
        }
        return spacePath;
    }


    /**
     * 根据用户ID 获取该用户的空间列表
     *
     * @param appId
     * @param appSecret
     * @param dsRemote
     * @param userId
     * @return
     */
    private static JSONArray spaceListUtil(String appId, String appSecret, String dsRemote, String userId) {
        String currentDateTimeString = getCurrentDateTimeString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> paramMap = new HashMap<>(8);
        paramMap.put("userId", userId);
        paramMap.put("appId", appId);
        paramMap.put("timestamp", currentDateTimeString);
        paramMap.put("version", "1.0");
        final String sign = md5Encrypt(paramSort(paramMap) + appSecret);
        paramMap.put("sign", sign);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String paramsPath = String.format("%s/api//ds.open/space/list?appId=%s&userId=%s&sign=%s&timestamp=%s&version=%s",
                    dsRemote,
                    encode(appId),
                    encode(userId),
                    encode(sign),
                    encode(currentDateTimeString),
                    encode("1.0"));
            HttpGet httpGet = new HttpGet(paramsPath);
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                // Get the response body
                String body = EntityUtils.toString(response.getEntity());
                // Parse the response JSON
                JSONArray jsonArray = JSONObject.fromObject(body).getJSONArray("data");
                return jsonArray;
            }
        } catch (Exception e) {
            throw new RuntimeException("远程请求错误", e);
        }
    }

    /**
     * 根据空间ID ，获取该空间下的所有文件信息，需循环调用
     * 文件夹： dirs 为 1 且 hash 不为 0
     * 文件夹： dirs 为 0
     *
     * @param appId
     * @param appSecret
     * @param dsRemote
     * @param spaceId
     * @param hash
     * @return
     */
    private static String fileListUtil(String appId, String appSecret, String dsRemote, String spaceId, String hash) {
        String currentDateTimeString = getCurrentDateTimeString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> paramMap = new HashMap<>(8);
        paramMap.put("spaceId", spaceId);
        paramMap.put("appId", appId);
        paramMap.put("timestamp", currentDateTimeString);
        paramMap.put("version", "1.0");
        final String sign = md5Encrypt(paramSort(paramMap) + appSecret);
        paramMap.put("sign", sign);

        String body = "";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            String paramsPath = String.format("%s/api/ds.open/space/fileList?appId=%s&spaceId=%s&sign=%s&timestamp=%s&version=%s",
                    dsRemote,
                    encode(appId),
                    encode(spaceId),
                    encode(sign),
                    encode(currentDateTimeString),
                    encode("1.0"));
            if (hash != null) {
                paramsPath = String.format("%s/api/ds.open/space/fileList?appId=%s&spaceId=%s&sign=%s&hash=%s&timestamp=%s&version=%s",
                        dsRemote,
                        encode(appId),
                        encode(spaceId),
                        encode(sign),
                        encode(hash),
                        encode(currentDateTimeString),
                        encode("1.0"));
            }

            HttpGet httpGet = new HttpGet(paramsPath);
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                // Get the response body
                body = EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            throw new RuntimeException("远程请求错误", e);
        }
        return body;
    }


    /**
     * 根据空间ID，进行空间数据同步
     *
     * @param appId
     * @param appSecret
     * @param dsRemote
     * @param spaceId
     * @return
     */
    private static String syncDataSpaceUtil(String appId, String appSecret, String dsRemote, String spaceId) {
        String currentDateTimeString = getCurrentDateTimeString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> paramMap = new HashMap<>(8);
        paramMap.put("spaceId", spaceId);
        paramMap.put("appId", appId);
        paramMap.put("timestamp", currentDateTimeString);
        paramMap.put("version", "1.0");

        final String sign = md5Encrypt(paramSort(paramMap) + appSecret);
        paramMap.put("sign", sign);

        System.out.println(paramMap);

        String body = "";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String paramsPath = String.format("%s/api/ds.open/space/fl.syn?appId=%s&spaceId=%s&sign=%s&timestamp=%s&version=%s",
                    dsRemote,
                    encode(appId),
                    encode(spaceId),
                    encode(sign),
                    encode(currentDateTimeString),
                    encode("1.0"));

            HttpGet httpGet = new HttpGet(paramsPath);
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                // Get the response body
                body = EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            throw new RuntimeException("远程请求错误", e);
        }
        return body;
    }


    // Method for URL encoding
    private static String encode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("URL encoding error: " + value, e);
        }
    }

    public static final String CUSTOM_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * get current date time as string,yyyy-MM-dd HH:mm:ss
     */
    private static String getCurrentDateTimeString() {
        return LocalDateTime.now(TimeZone.getTimeZone("Asia/Shanghai").toZoneId()).format(DateTimeFormatter.ofPattern(CUSTOM_DATETIME_FORMAT));
    }

    /**
     * MD5 encrypt for remote call
     */
    private static String md5Encrypt(String text) {
        StringBuilder sb = new StringBuilder("");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes(StandardCharsets.UTF_8));
            byte[] result = md.digest();
            int i;
            for (byte b : result) {
                i = b;
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * Map 的 key 的 ASCII 码从小到大排序 返回拼接参数
     *
     * @param params
     * @return
     */
    private static String ASCLLSort(Map<String, String> params) {
        String[] sortedKeys = params.keySet().toArray(new String[]{});
        Arrays.sort(sortedKeys);// 排序请求参数
        StringBuilder s2 = new StringBuilder();
        for (String key : sortedKeys) {
            s2.append(key).append("=").append(params.get(key)).append("&");
        }
        s2.deleteCharAt(s2.length() - 1);
        return s2.toString();
    }

    /**
     * request parameter key sort
     */
    private static String paramSort(Map<String, Object> params) {
        String[] sortedKeys = params.keySet().toArray(new String[]{});
        Arrays.sort(sortedKeys);
        StringBuilder s2 = new StringBuilder();
        for (String key : sortedKeys) {
            s2.append(key).append("=").append(params.get(key)).append("&");
        }
        s2.deleteCharAt(s2.length() - 1);
        return s2.toString();
    }

}


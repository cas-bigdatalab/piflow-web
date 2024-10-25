package cn.cnic.base.utils;

import cn.cnic.common.constant.MessageConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.UnsupportedCharsetException;
import java.util.*;

/**
 * Http tool class
 */
public class HttpUtils {

	/**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private static Logger logger = LoggerUtil.getLogger();

	//public static String INTERFACE_CALL_ERROR = "Interface call error";
	
    /**
     * "post" request to transfer "json" data
     *
     * @param url
     * @param json
     * @param timeOutMS (Millisecond)
     * @return
     */
    public static String doPostParmaMap(String url, Map<?, ?> json, Integer timeOutMS) {
    	if (null == json) {
    		return doPost(url, null, timeOutMS);
    	}
        String formatJson = JsonUtils.toFormatJsonNoException(json);
        return doPost(url, formatJson, timeOutMS);
    }

    /**
     * "post" request to transfer "json" data
     *
     * @param url
     * @param json
     * @param timeOutMS (Millisecond)
     * @return
     */
    public static String doPost(String url, String json, Integer timeOutMS) {
        return doPostComCustomizeHeader(url, json, timeOutMS, null);
    }

    /**
     * Get request to transfer data
     *
     * @param url
     * @param map       Request incoming parameters ("key" is the parameter name, "value" is the parameter value) "map" can be empty
     * @param timeOutMS (Millisecond)
     * @return
     */
    public static String doGet(String url, Map<String, String> map, Integer timeOutMS) {
        if (null == map || map.isEmpty()) {
            return doGetComCustomizeHeader(url, null, timeOutMS, null);
        }
        Map<String, Object> mapObject = new HashMap<>();
        for (String key : map.keySet()) {
            mapObject.put(key, map.get(key));
        }
        return doGetComCustomizeHeader(url, mapObject, timeOutMS, null);
    }

    public static String getHtml(String urlStr) {

        // Define links to be accessed
        String url = urlStr;
        // Define a string to store web content
        String result = "";
        // Define a buffered character input stream
        BufferedReader in = null;
        try {
            // Convert string to url object
            URL realUrl = new URL(url);
            // Initialize a link to the "url" link
            URLConnection connection = realUrl.openConnection();
            // Start the actual connection
            connection.connect();
            // Initialize the "BufferedReader" input stream to read the response of the "URL"
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            // Used to temporarily store data for each fetched row
            String line;
            while ((line = in.readLine()) != null) {
                // Traverse each row that is fetched and store it in "result"
                result += line + "\n";
            }
        } catch (Exception e) {
            logger.error("send get request is abnormal!" + e);
            e.printStackTrace();
        } // Use "finally" to close the input stream
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        logger.debug("html info:" + result);
        return result;
    }

    /**
     * "post" request to transfer "json" data
     *
     * @param url
     * @param timeOutMS (Millisecond)
     * @return
     */
    public static String doDelete(String url, Integer timeOutMS) {
        String result = "";

        // Create an "httpclient" object
        CloseableHttpClient httpClient = null;
        // Create a "post" mode request object
        HttpDelete httpDelete = null;
        try {
            // Create an "httpclient" object
            httpClient = HttpClients.createDefault();
            // Create a "post" mode request object
            httpDelete = new HttpDelete(url);
            httpDelete.setProtocolVersion(HttpVersion.HTTP_1_1);
            if (null != timeOutMS) {
                // Set timeout
                RequestConfig requestConfig = RequestConfig.custom()
                        .setConnectTimeout(5000).setConnectionRequestTimeout(1000)
                        .setSocketTimeout(timeOutMS).build();
                httpDelete.setConfig(requestConfig);
            }

            logger.info("call '" + url + "' start");
            // Perform the request operation and get the result (synchronous blocking)
            CloseableHttpResponse response = httpClient.execute(httpDelete);
            logger.info("call succeeded,return msg:" + response.toString());
            // Get result entity
            // Determine whether the network connection status code is normal (0--200 are normal)
            switch (response.getStatusLine().getStatusCode()) {
                case HttpStatus.SC_OK:
                    result = EntityUtils.toString(response.getEntity(), "utf-8");
                    logger.info("call succeeded,return msg:" + result);
                    break;
                case HttpStatus.SC_CREATED:
                    result = EntityUtils.toString(response.getEntity(), "utf-8");
                    logger.info("call succeeded,return msg:" + result);
                    break;
                default:
                    result = MessageConfig.INTERFACE_CALL_ERROR_MSG() + ":" + EntityUtils.toString(response.getEntity(), "utf-8");
                    logger.warn("call failed,return msg:" + result);
                    break;
            }
        } catch (UnsupportedCharsetException e) {
            logger.error(MessageConfig.INTERFACE_CALL_ERROR_MSG(), e);
            result = (MessageConfig.INTERFACE_CALL_ERROR_MSG() + ":UnsupportedCharsetException");
        } catch (ClientProtocolException e) {
            logger.error(MessageConfig.INTERFACE_CALL_ERROR_MSG(), e);
            result = (MessageConfig.INTERFACE_CALL_ERROR_MSG() + ":ClientProtocolException");
        } catch (ParseException e) {
            logger.error(MessageConfig.INTERFACE_CALL_ERROR_MSG(), e);
            result = (MessageConfig.INTERFACE_CALL_ERROR_MSG() + ":ParseException");
        } catch (IOException e) {
            logger.error(MessageConfig.INTERFACE_CALL_ERROR_MSG(), e);
            result = (MessageConfig.INTERFACE_CALL_ERROR_MSG() + ":IOException");
        } finally {
            // Close the connection and release the resource
            httpDelete.releaseConnection();
        }
        return result;
    }

    public static String doPostComCustomizeHeader(String url, String json, Integer timeOutMS, Map<String, String> herderParam) {
        String result = "";

        // Create a "post" mode request object
        HttpPost httpPost = null;
        try {
            // Create a "post" mode request object
            httpPost = new HttpPost(url);
            logger.debug("afferent json param:" + json);
            // Set parameters to the request object
            if (StringUtils.isNotBlank(json)) {
                StringEntity stringEntity = new StringEntity(json.toString(), ContentType.APPLICATION_JSON);
                stringEntity.setContentEncoding("utf-8");
                httpPost.setEntity(stringEntity);
            }
            httpPost.setProtocolVersion(HttpVersion.HTTP_1_1);
            if (null != timeOutMS) {
                // Set timeout
                RequestConfig requestConfig = RequestConfig.custom()
                        .setConnectTimeout(5000).setConnectionRequestTimeout(1000)
                        .setSocketTimeout(timeOutMS).build();
                httpPost.setConfig(requestConfig);
            }
            //add header param
            if (null != herderParam && herderParam.keySet().size() > 0) {
                for (String key : herderParam.keySet()) {
                    if (null == key) {
                        continue;
                    }
                    httpPost.addHeader(key, herderParam.get(key));
                }
            }

            logger.info("call '" + url + "' start");
            result = doPostComCustomizeHttpPost(httpPost);
        } catch (UnsupportedCharsetException e) {
            logger.error(MessageConfig.INTERFACE_CALL_ERROR_MSG(), e);
            result = (MessageConfig.INTERFACE_CALL_ERROR_MSG() + ":UnsupportedCharsetException");
        } catch (ParseException e) {
            logger.error(MessageConfig.INTERFACE_CALL_ERROR_MSG(), e);
            result = (MessageConfig.INTERFACE_CALL_ERROR_MSG() + ":ParseException");
        } finally {
            // Close the connection and release the resource
            httpPost.releaseConnection();
        }
        return result;
    }

    public static String doPostComCustomizeHeaderAndFile(String url, Map<String, String> herderParam, Map<String, String> params, File file, Integer timeOutMS) {
        String result = "";

        // Create an "httpclient" object
        CloseableHttpClient httpClient = null;
        // Create a "post" mode request object
        HttpPost httpPost = null;
        try {
            // Create a "post" mode request object
            httpPost = new HttpPost(url);
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            if (null != file && file.exists()) {
                multipartEntityBuilder.addBinaryBody("file",file);
                //multipartEntityBuilder.addPart("comment", new StringBody("This is comment", ContentType.TEXT_PLAIN));
                multipartEntityBuilder.addTextBody("comment", "this is comment");
            }
            if (null != params && !params.isEmpty()) {
                for (String key : params.keySet()) {
                    multipartEntityBuilder.addTextBody(key, params.get(key));
                }
            }
            HttpEntity httpEntity = multipartEntityBuilder.build();
            httpPost.setEntity(httpEntity);
            httpPost.setProtocolVersion(HttpVersion.HTTP_1_1);
            if (null != timeOutMS) {
                // Set timeout
                RequestConfig requestConfig = RequestConfig.custom()
                        .setConnectTimeout(5000).setConnectionRequestTimeout(1000)
                        .setSocketTimeout(timeOutMS).build();
                httpPost.setConfig(requestConfig);
            }
            //add header param
            if (null != herderParam && herderParam.keySet().size() > 0) {
                for (String key : herderParam.keySet()) {
                    if (null == key) {
                        continue;
                    }
                    httpPost.addHeader(key, herderParam.get(key));
                }
            }
            logger.info("call '" + url + "' start");
            result = doPostComCustomizeHttpPost(httpPost);
        } catch (UnsupportedCharsetException e) {
            logger.error(MessageConfig.INTERFACE_CALL_ERROR_MSG(), e);
            result = (MessageConfig.INTERFACE_CALL_ERROR_MSG() + ":UnsupportedCharsetException");
        } catch (ParseException e) {
            logger.error(MessageConfig.INTERFACE_CALL_ERROR_MSG(), e);
            result = (MessageConfig.INTERFACE_CALL_ERROR_MSG() + ":ParseException");
        } finally {
            // Close the connection and release the resource
            httpPost.releaseConnection();
        }
        return result;
    }

    public static String doPostComCustomizeHttpPost(HttpPost httpPost) {
        if (null == httpPost) {
            return (MessageConfig.INTERFACE_CALL_ERROR_MSG() + ":HttpPost is Null");
        }
        String result = "";
        // Create an "httpclient" object
        CloseableHttpClient httpClient = null;
        try {
            // Create an "httpclient" object
            httpClient = HttpClients.createDefault();
            // Perform the request operation and get the result (synchronous blocking)
            CloseableHttpResponse response = httpClient.execute(httpPost);
            logger.info("call succeeded,return msg:" + response.toString());
            // Get result entity
            // Determine whether the network connection status code is normal (0--200 are normal)
            switch (response.getStatusLine().getStatusCode()) {
                case HttpStatus.SC_OK:
                    result = EntityUtils.toString(response.getEntity(), "utf-8");
                    logger.info("call succeeded,return msg:" + result);
                    break;
                case HttpStatus.SC_CREATED:
                    result = EntityUtils.toString(response.getEntity(), "utf-8");
                    logger.info("call succeeded,return msg:" + result);
                    break;
                default:
                    result = MessageConfig.INTERFACE_CALL_ERROR_MSG() + ":" + EntityUtils.toString(response.getEntity(), "utf-8");
                    logger.warn("call failed,return msg:" + result);
                    break;
            }
        } catch (UnsupportedCharsetException e) {
            logger.error(MessageConfig.INTERFACE_CALL_ERROR_MSG(), e);
            result = (MessageConfig.INTERFACE_CALL_ERROR_MSG() + ":UnsupportedCharsetException");
        } catch (ClientProtocolException e) {
            logger.error(MessageConfig.INTERFACE_CALL_ERROR_MSG(), e);
            result = (MessageConfig.INTERFACE_CALL_ERROR_MSG() + ":ClientProtocolException");
        } catch (ParseException e) {
            logger.error(MessageConfig.INTERFACE_CALL_ERROR_MSG(), e);
            result = (MessageConfig.INTERFACE_CALL_ERROR_MSG() + ":ParseException");
        } catch (IOException e) {
            logger.error(MessageConfig.INTERFACE_CALL_ERROR_MSG(), e);
            result = (MessageConfig.INTERFACE_CALL_ERROR_MSG() + ":IOException");
        } catch (Exception e) {
            logger.error(MessageConfig.INTERFACE_CALL_ERROR_MSG(), e);
            result = (MessageConfig.INTERFACE_CALL_ERROR_MSG() + ":Exception");
        }
        return result;
    }

    public static String doGetComCustomizeHeader(String url, Map<String, Object> map, Integer timeOutMS, Map<String, String> herderParam) {
        String result = "";
        if (StringUtils.isNotBlank(url)) {
            try {
                // Create an "httpclient" object
                CloseableHttpClient httpClient = HttpClients.createDefault();

                // Create a "get" mode request object
                HttpGet httpGet = null;
                // Determine whether to add parameters
                if (null != map && !map.isEmpty()) {
                    // Since the parameters of the "GET" request are all assembled behind the "URL" address, we have to build a "URL" with parameters.

                    URIBuilder uriBuilder = new URIBuilder(url);

                    List<NameValuePair> list = new LinkedList<>();
                    for (String key : map.keySet()) {
                        BasicNameValuePair param = new BasicNameValuePair(key, map.get(key).toString());
                        list.add(param);
                    }

                    uriBuilder.setParameters(list);
                    // Construct a "GET" request object from a "URI" object with parameters
                    httpGet = new HttpGet(uriBuilder.build());
                } else {
                    httpGet = new HttpGet(url);
                }

                // Type of transmission
                httpGet.addHeader("Content-type", "application/json");

                // Add request header information
                // Browser representation
                // httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1;
                // en-US; rv:1.7.6)");
                // Type of transmission
                // httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded");
                //add header param
                if (null != herderParam && herderParam.keySet().size() > 0) {
                    for (String key : herderParam.keySet()) {
                        if (null == key) {
                            continue;
                        }
                        httpGet.addHeader(key, herderParam.get(key));
                    }
                }
                if (null != timeOutMS) {
                    // Set timeout
                    RequestConfig requestConfig = RequestConfig.custom()
                            .setConnectTimeout(5000).setConnectionRequestTimeout(1000)
                            .setSocketTimeout(timeOutMS).build();
                    httpGet.setConfig(requestConfig);
                }

                logger.info("call '" + url + "' start");
                // Get the response object by requesting the object
                CloseableHttpResponse response = httpClient.execute(httpGet);
                logger.info("call succeeded,return msg:" + response.toString());
                // Get result entity
                // Determine whether the network connection status code is normal (0--200 are normal)
                switch (response.getStatusLine().getStatusCode()) {
                    case HttpStatus.SC_OK:
                        result = EntityUtils.toString(response.getEntity(), "utf-8");
                        logger.info("call succeeded,return msg:" + result);
                        break;
                    case HttpStatus.SC_CREATED:
                        result = EntityUtils.toString(response.getEntity(), "utf-8");
                        logger.info("call succeeded,return msg:" + result);
                        break;
                    default:
                        result = MessageConfig.INTERFACE_CALL_ERROR_MSG() + ":" + EntityUtils.toString(response.getEntity(), "utf-8");
                        logger.warn("call failed,return msg:" + result);
                        break;
                }
                // Release link
                response.close();
            } catch (ClientProtocolException e) {
                logger.error(MessageConfig.INTERFACE_CALL_ERROR_MSG(), e);
                result = (MessageConfig.INTERFACE_CALL_ERROR_MSG() + ":ClientProtocolException");
            } catch (ParseException e) {
                logger.error(MessageConfig.INTERFACE_CALL_ERROR_MSG(), e);
                result = (MessageConfig.INTERFACE_CALL_ERROR_MSG() + ":ParseException");
            } catch (IOException e) {
                logger.error(MessageConfig.INTERFACE_CALL_ERROR_MSG(), e);
                result = (MessageConfig.INTERFACE_CALL_ERROR_MSG() + ":IOException");
            } catch (URISyntaxException e) {
                logger.error(MessageConfig.INTERFACE_CALL_ERROR_MSG(), e);
                result = (MessageConfig.INTERFACE_CALL_ERROR_MSG() + ":URISyntaxException");
            }
        }
        return result;
    }

    /**
     * Get request to back response
     *
     * @param url
     * @param map       Request incoming parameters ("key" is the parameter name, "value" is the parameter value) "map" can be empty
     * @param timeOutMS (Millisecond)
     * @return
     */
    public static CloseableHttpResponse doGetReturnResponse(String url, Map<String, String> map, Integer timeOutMS) {
        CloseableHttpResponse response = null;
        if (StringUtils.isNotBlank(url)) {
            try {
                // Create an "httpclient" object
                CloseableHttpClient httpClient = HttpClients.createDefault();

                // Create a "get" mode request object
                HttpGet httpGet = null;
                // Determine whether to add parameters
                if (null != map && !map.isEmpty()) {
                    // Since the parameters of the "GET" request are all assembled behind the "URL" address, we have to build a "URL" with parameters.
                    URIBuilder uriBuilder = new URIBuilder(url);

                    List<NameValuePair> list = new LinkedList<>();
                    for (String key : map.keySet()) {
                        BasicNameValuePair param = new BasicNameValuePair(key, map.get(key));
                        list.add(param);
                    }

                    uriBuilder.setParameters(list);
                    // Construct a "GET" request object from a "URI" object with parameters
                    httpGet = new HttpGet(uriBuilder.build());
                } else {
                    httpGet = new HttpGet(url);
                }
                // Type of transmission
                httpGet.addHeader("Content-type", "application/json");
                if (null != timeOutMS) {
                    // Set timeout
                    RequestConfig requestConfig = RequestConfig.custom()
                            .setConnectTimeout(5000).setConnectionRequestTimeout(1000)
                            .setSocketTimeout(timeOutMS).build();
                    httpGet.setConfig(requestConfig);
                }

                logger.info("call '" + url + "' start");
                // Get the response object by requesting the object
                response = httpClient.execute(httpGet);
                // Get result entity
                // Determine whether the network connection status code is normal (0--200 are normal)
                // Release link
            } catch (ClientProtocolException e) {
                logger.error(MessageConfig.INTERFACE_CALL_ERROR_MSG(), e);
            } catch (ParseException e) {
                logger.error(MessageConfig.INTERFACE_CALL_ERROR_MSG(), e);
            } catch (IOException e) {
                logger.error(MessageConfig.INTERFACE_CALL_ERROR_MSG(), e);
            } catch (URISyntaxException e) {
                logger.error(MessageConfig.INTERFACE_CALL_ERROR_MSG(), e);
            }
        }
        return response;
    }

    public static String doPostFromComCustomizeHeader(String url, Map<String, Object> json, Integer timeOutMS, Map<String, String> headerParam) throws Exception {
        if (org.apache.commons.lang.StringUtils.isBlank(url)) {
            throw new Exception("url is null");
        }
        // Create a "post" mode request object
        HttpPost httpPost = new HttpPost(url);
        // Set parameters to the request object
        if (null != json && json.keySet().size() > 0) {
            //Create parameter queue
            List<NameValuePair> formParams = new ArrayList<>();
            for (String key : json.keySet()) {
                if (null == key || null == json.get(key)) {
                    continue;
                }
                formParams.add(new BasicNameValuePair(key, json.get(key).toString()));
            }
            UrlEncodedFormEntity uefEntity;
            uefEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
            httpPost.setEntity(uefEntity);
        }
        httpPost.setProtocolVersion(HttpVersion.HTTP_1_1);
        if (null != timeOutMS) {
            // Set timeout
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(timeOutMS).setConnectionRequestTimeout(timeOutMS)
                    .setSocketTimeout(timeOutMS).build();
            httpPost.setConfig(requestConfig);
        }
        //add header param
        if (null != headerParam && headerParam.keySet().size() > 0) {
            for (String key : headerParam.keySet()) {
                if (null == key) {
                    continue;
                }
                httpPost.addHeader(key, headerParam.get(key));
            }
        }
        logger.info("call '" + url + "' start");
        String result = doPostComCustomizeHttpPost(httpPost);
        // Close the connection and release the resource
        httpPost.releaseConnection();
        return result;
    }

    public static Map<String, String> setHeaderContentType(String contentType) {
        if (org.apache.commons.lang.StringUtils.isBlank(contentType)) {
            return null;
        }
        Map<String, String> headerParam = new HashMap<>();
        headerParam.put("Content-type", contentType);
        return headerParam;
    }


}

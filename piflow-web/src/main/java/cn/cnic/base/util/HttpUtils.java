package cn.cnic.base.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.UnsupportedCharsetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Http tool class
 */
public class HttpUtils {

	public static String INTERFACE_CALL_ERROR = "Interface call error";
	
    static Logger logger = LoggerUtil.getLogger();

    /**
     * "post" request to transfer "json" data
     *
     * @param url
     * @param json
     * @param timeOutMS (Millisecond)
     * @return
     */
    public static String doPostParmaMap(String url, Map<?, ?> json, Integer timeOutMS) {
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
        String result = "";

        // Create an "httpclient" object
        CloseableHttpClient httpClient = null;
        // Create a "post" mode request object
        HttpPost httpPost = null;
        try {
            // Create an "httpclient" object
            httpClient = HttpClients.createDefault();
            // Create a "post" mode request object
            httpPost = new HttpPost(url);

            logger.debug("afferent json param:" + json);
            // Set parameters to the request object
            StringEntity stringEntity = new StringEntity(json.toString(), ContentType.APPLICATION_JSON);
            stringEntity.setContentEncoding("utf-8");
            httpPost.setProtocolVersion(HttpVersion.HTTP_1_1);
            httpPost.setEntity(stringEntity);
            if (null != timeOutMS) {
                // Set timeout
                RequestConfig requestConfig = RequestConfig.custom()
                        .setConnectTimeout(5000).setConnectionRequestTimeout(1000)
                        .setSocketTimeout(timeOutMS).build();
                httpPost.setConfig(requestConfig);
            }

            logger.info("call '" + url + "' start");
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
                    logger.warn("call failed,return msg:" + EntityUtils.toString(response.getEntity(), "utf-8"));
                    result = INTERFACE_CALL_ERROR;
                    break;
            }
        } catch (UnsupportedCharsetException e) {
            logger.error(INTERFACE_CALL_ERROR, e);
            result = (INTERFACE_CALL_ERROR + ":UnsupportedCharsetException");
        } catch (ClientProtocolException e) {
            logger.error(INTERFACE_CALL_ERROR, e);
            result = (INTERFACE_CALL_ERROR + ":ClientProtocolException");
        } catch (ParseException e) {
            logger.error(INTERFACE_CALL_ERROR, e);
            result = (INTERFACE_CALL_ERROR + ":ParseException");
        } catch (IOException e) {
            logger.error(INTERFACE_CALL_ERROR, e);
            result = (INTERFACE_CALL_ERROR + ":IOException");
        } finally {
            // Close the connection and release the resource
            httpPost.releaseConnection();
        }
        return result;
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
                        BasicNameValuePair param = new BasicNameValuePair(key, map.get(key));
                        list.add(param);
                    }

                    uriBuilder.setParameters(list);
                    // Construct a "GET" request object from a "URI" object with parameters
                    httpGet = new HttpGet(uriBuilder.build());
                } else {
                    httpGet = new HttpGet(url);
                }

                // Add request header information
                // Browser representation
                // httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1;
                // en-US; rv:1.7.6)");
                // Type of transmission
                // httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded");

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
                        logger.warn("call failed,return msg:" + EntityUtils.toString(response.getEntity(), "utf-8"));
                        result = INTERFACE_CALL_ERROR;
                        break;
                }
                // Release link
                response.close();
            } catch (ClientProtocolException e) {
                logger.error(INTERFACE_CALL_ERROR, e);
                result = (INTERFACE_CALL_ERROR + ":ClientProtocolException");
            } catch (ParseException e) {
                logger.error(INTERFACE_CALL_ERROR, e);
                result = (INTERFACE_CALL_ERROR + ":ParseException");
            } catch (IOException e) {
                logger.error(INTERFACE_CALL_ERROR, e);
                result = (INTERFACE_CALL_ERROR + ":IOException");
            } catch (URISyntaxException e) {
                logger.error(INTERFACE_CALL_ERROR, e);
                result = (INTERFACE_CALL_ERROR + ":URISyntaxException");
            }
        }
        return result;
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
     * @param json
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
	                logger.warn("call failed,return msg:" + EntityUtils.toString(response.getEntity(), "utf-8"));
	                result = INTERFACE_CALL_ERROR;
	                break;
	        }
        } catch (UnsupportedCharsetException e) {
            logger.error(INTERFACE_CALL_ERROR, e);
            result = (INTERFACE_CALL_ERROR + ":UnsupportedCharsetException");
        } catch (ClientProtocolException e) {
            logger.error(INTERFACE_CALL_ERROR, e);
            result = (INTERFACE_CALL_ERROR + ":ClientProtocolException");
        } catch (ParseException e) {
            logger.error(INTERFACE_CALL_ERROR, e);
            result = (INTERFACE_CALL_ERROR + ":ParseException");
        } catch (IOException e) {
            logger.error(INTERFACE_CALL_ERROR, e);
            result = (INTERFACE_CALL_ERROR + ":IOException");
        } finally {
            // Close the connection and release the resource
            httpDelete.releaseConnection();
        }
        return result;
    }
}

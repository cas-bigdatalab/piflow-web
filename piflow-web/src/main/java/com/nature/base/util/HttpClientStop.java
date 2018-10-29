package com.nature.base.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;

public class HttpClientStop {

	public String getGroupAndStopInfo(String param, String url) {
		// 请求接口地址
		if (StringUtils.isNotBlank(param)) {
			url = url + "?bundle=" + param;
		}
		// 请求参数
		HttpClient httpclient = null;
		GetMethod post = null;
		String result = "";
		try {
			// 创建连接
			httpclient = new HttpClient();
			post = new GetMethod(url);
			// 设置编码方式
			post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			// 执行请求
			httpclient.executeMethod(post);
			// 判断网络连接状态码是否正常(0--200都数正常)
			if (post.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 接口返回信息
				result = new String(post.getResponseBody(), "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭连接，释放资源
			post.releaseConnection();
			((SimpleHttpConnectionManager) httpclient.getHttpConnectionManager()).shutdown();
		}
		return result;
	}
}

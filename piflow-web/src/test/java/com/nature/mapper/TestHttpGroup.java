package com.nature.mapper;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.HashMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import net.sf.json.JSONObject;

public class TestHttpGroup {

	@Test
	public void test() {
		// 请求接口地址
		String url = "http://10.0.86.98:8001/stop/groups";
		// 请求参数
		// String userid = "";

		HttpClient httpclient = null;
		GetMethod post = null;
		JSONObject jsonResult = null;
		String result = "";
		try {
			// 创建连接
			httpclient = new HttpClient();
			post = new GetMethod(url);
			// 设置编码方式
			post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			// 添加参数
			// post.addParameter("userid", userid);
			// 执行请求
			httpclient.executeMethod(post);
			// 判断网络连接状态码是否正常(0--200都数正常)
			if (post.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 接口返回信息
				result = new String(post.getResponseBody(), "UTF-8");
				System.out.println(result + "...");
			}
			jsonResult = JSONObject.fromObject(result);
			String apiString = jsonResult.getString("groups");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭连接，释放资源
			post.releaseConnection();
			((SimpleHttpConnectionManager) httpclient.getHttpConnectionManager()).shutdown();
		}
	}

	@Test
	public void getStopList() {
		String urlNameString = "http://10.0.86.98:8002/stop/list";
		String result = "";
		String jsonResult = null;
		try {
			// 根据地址获取请求
			HttpGet request = new HttpGet(urlNameString);// 这里发送get请求
			// 获取当前客户端对象
			DefaultHttpClient httpClient = new DefaultHttpClient();
			// 通过请求对象获取响应对象
			HttpResponse response = httpClient.execute(request);
			// 判断网络连接状态码是否正常(0--200都数正常)
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity(), "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void getStopInfo() {
		HashMap<String, Object> map = new HashMap<>();
		// String param = "cn.piflow.bundle.http.UnGZip";
		String param = "cn.piflow.bundle.UserDefineSelectHiveQL";
		// 请求接口地址
		String url = "http://10.0.86.98:8002/stop/info" + "?bundle=" + param;
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
	}

	public static boolean GenerateImages(String imgStr, String name) {
		// 对字节数组字符串进行Base64解码并生成图片
		if (imgStr == null) // 图像数据为空
			return false;
		Decoder decoder = Base64.getDecoder();
		try {
			// Base64解码
			byte[] b = decoder.decode(imgStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
			// 生成jpeg图片 d:\\image\\
			String imgFilePath = "D:\\01.png";// 新生成的图片
			//String property = System.getProperty("user.dir");
			//String path = property + "\\src\\main\\resources\\static\\grapheditor\\stencils\\clipart\\" + name + "_128x128.png";
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}

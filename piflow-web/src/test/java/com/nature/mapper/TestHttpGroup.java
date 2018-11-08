package com.nature.mapper;

import com.nature.common.constant.SysParamsCache;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

@SuppressWarnings({ "deprecation", "restriction" })
public class TestHttpGroup {

	@Test
	public void test() {
		// 请求接口地址
		String url = "http://10.0.86.191:8002/stop/groups";
		// 请求参数
		// String userid = "";

		HttpClient httpclient = null;
		GetMethod post = null;
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
				String result = new String(post.getResponseBody(), "UTF-8");
			}
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
		String urlNameString = "http://10.0.86.191:8002/stop/list";
		try {
			// 根据地址获取请求
			HttpGet request = new HttpGet(urlNameString);// 这里发送get请求
			// 获取当前客户端对象
			@SuppressWarnings("resource")
			DefaultHttpClient httpClient = new DefaultHttpClient();
			// 通过请求对象获取响应对象
			HttpResponse response = httpClient.execute(request);
			// 判断网络连接状态码是否正常(0--200都数正常)
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				
				String result = EntityUtils.toString(response.getEntity(), "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void getStopInfo() {
		String param = "cn.piflow.bundle.microorganism.GenBankParse";
		// 请求接口地址
		String url = "http://10.0.86.191:8002/stop/info" + "?bundle=" + param;
		// 请求参数
		HttpClient httpclient = null;
		GetMethod post = null;
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
				String result = new String(post.getResponseBody(), "UTF-8");
				JSONObject jb1 = JSONObject.fromObject(result);
				JSONArray ja = JSONArray.fromObject(jb1.get("StopInfo"));
				@SuppressWarnings("unchecked")
				Iterator<Object> it = ja.iterator();
				while (it.hasNext()) {
					JSONObject ob = (JSONObject) it.next();
					String icon = ob.get("icon") + "";
					String name = ob.get("name") + "";
					GenerateImages(icon, name + "_128x128.png",SysParamsCache.IMAGES_PATH);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭连接，释放资源
			post.releaseConnection();
			((SimpleHttpConnectionManager) httpclient.getHttpConnectionManager()).shutdown();
		}
	}

	public static boolean GenerateImages(String imgStr, String name,String pathUrl) throws IOException {
		// 对字节数组字符串进行Base64解码并生成图片
		if (imgStr == null) // 图像数据为空
			return false;
		BASE64Decoder decoder = new BASE64Decoder();
		 
		// Base64解码,对字节数组字符串进行Base64解码并生成图片
		byte[] b = decoder.decodeBuffer(imgStr);
		for (int i = 0; i < b.length; ++i) {
			if (b[i] < 0) {// 调整异常数据
				b[i] += 256;
			}
		}
			// 生成jpeg图片 d:\\image\\
			//String imgFilePath = "D:\\01.png";// 新生成的图片
			String path = pathUrl + name + "_128x128.png";
			//String property = System.getProperty("user.dir");
			//String path = property + SysParamsCache.IMAGES_PATH + name + "_128x128.png";
			OutputStream out = new FileOutputStream(path);
			out.write(b);
			out.flush();
			out.close();
			return false;
		 
		 
	}

}

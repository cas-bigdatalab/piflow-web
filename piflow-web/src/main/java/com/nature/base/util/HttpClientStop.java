package com.nature.base.util;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import sun.misc.BASE64Decoder;

public class HttpClientStop {
	
	public String getAllGroupsAndStops(String type){
		   // 请求接口地址
		String url = "";
        String getAllGroups = "http://10.0.86.98:8002/stop/groups";
        String getAllStops = "http://10.0.86.98:8002/stop/list";
        if (StringUtils.isNotBlank(type)) {
			if ("groups".equals(type)) {
				url = getAllGroups;
			}else {
				url = getAllStops;
			}
		}
        HttpClient httpclient = null;
        GetMethod post = null;
        String result = "";
        String jsonResult = null;
        try {
            //创建连接
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
             System.out.println("getAllGroups接口调用成功~"+result);
            }else{
            	System.out.println("返回错误的状态码为："+post.getStatusLine().getStatusCode());
            }
            jsonResult = JSONObject.fromObject(result).getString(type);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接，释放资源
            post.releaseConnection();
            ((SimpleHttpConnectionManager) httpclient.getHttpConnectionManager()).shutdown();
        }
		return jsonResult;
    }
    
    public HashMap<String, Object> getStopInfo(String param){
		HashMap<String, Object> map = new HashMap<>();
		// 请求接口地址
        String url = "http://10.0.86.98:8002/stop/info"+"?bundle="+param;
        // 请求参数
        HttpClient httpclient = null;
        GetMethod post = null;
        String result= "";
        String jsonResult = null;
        try {
            //创建连接
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
                JSONObject jb = new JSONObject();
    			 JSONObject jb1 = jb.fromObject(result);
    			 JSONArray ja =  JSONArray.fromObject(jb1.get("StopInfo"));
    			 Iterator<Object> it = ja.iterator();
    			 while (it.hasNext()) {
    				JSONObject ob = (JSONObject) it.next();
    				String bundle = ob.get("bundle")+"";
    				String owner = ob.get("owner")+"";
    				String inports = ob.get("inports")+"";
    				String outports = ob.get("outports")+"";
    				String groups = ob.get("groups")+"";
    				String properties = ob.get("properties")+"";
    				String name = ob.get("name")+"";
     				String description = ob.get("description")+"";
     				String icon = ob.get("icon")+"";
     				GenerateImage(icon,name);
    				map.put("bundle", bundle);
    				map.put("owner", owner);
    				map.put("inports", inports);
    				map.put("outports", outports);
    				map.put("groups", groups);
    				map.put("properties", properties);
    				map.put("name", name);
     				map.put("description", description);
    				System.out.println("getStopInfo接口调用成功~"+map);
    			}
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接，释放资源
            post.releaseConnection();
            ((SimpleHttpConnectionManager) httpclient.getHttpConnectionManager()).shutdown();
        }
		return map;
    }
     
    public static boolean GenerateImage(String imgStr,String name)  
    {   //对字节数组字符串进行Base64解码并生成图片  
        if (imgStr == null) //图像数据为空  
            return false;  
        BASE64Decoder decoder = new BASE64Decoder();  
        try   
        {  
            //Base64解码  
            byte[] b = decoder.decodeBuffer(imgStr);  
            for(int i=0;i<b.length;++i)  
            {   
                if(b[i]<0)  
                {//调整异常数据  
                	b[i]+=256;  
                }  
            }  
            //设置生成图片的路径
            String property = System.getProperty("user.dir");
            String path = property + "\\src\\main\\resources\\static\\grapheditor\\stencils\\clipart\\"+name+"_128x128.png";
            OutputStream out = new FileOutputStream(path);   
            out.write(b);  
            out.flush();  
            out.close();  
            return true;  
        }   
        catch (Exception e)   
        {  
            return false;  
        }  
    }  
	
	}


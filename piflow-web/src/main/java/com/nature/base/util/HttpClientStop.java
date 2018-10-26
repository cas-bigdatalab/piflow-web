package com.nature.base.util;

import java.io.FileOutputStream;
import java.io.IOException;
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
import org.apache.http.HttpStatus;

import sun.misc.BASE64Decoder;

public class HttpClientStop {
	
    
    public String getGroupAndStopInfo(String param,String url){
		// 请求接口地址
		if (StringUtils.isNotBlank(param)) {
			url = url+"?bundle="+param;
		}
        // 请求参数
        HttpClient httpclient = null;
        GetMethod post = null;
        String result= "";
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
     
    /**
     * base64字符串转换图片
     * @param imgStr
     * @param name
     * @return
     */
    public  boolean GenerateImage(String imgStr,String name)  
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


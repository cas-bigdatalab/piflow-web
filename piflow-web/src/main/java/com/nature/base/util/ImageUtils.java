package com.nature.base.util;

import java.io.FileOutputStream;
import java.io.OutputStream;

import sun.misc.BASE64Decoder;

@SuppressWarnings("restriction")
public class ImageUtils {

	public static boolean generateImage(String imgStr, String name) { // 对字节数组字符串进行Base64解码并生成图片
		if (imgStr == null) // 图像数据为空
			return false;

		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] b = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
			// 设置生成图片的路径
			String property = System.getProperty("user.dir");
			String path = property + "\\src\\main\\resources\\static\\grapheditor\\stencils\\clipart\\" + name
					+ "_128x128.png";
			OutputStream out = new FileOutputStream(path);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}

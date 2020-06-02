package cn.cnic.base.util;

import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.OutputStream;

@SuppressWarnings("restriction")
public class ImageUtils {

    /**
     * Save image
     *
     * @param imgStr  The base64 character of the image
     * @param name    Store image name
     * @param type    Store image type
     * @param pathUrl Storage address
     * @return
     */
    public static boolean generateImage(String imgStr, String name, String type, String pathUrl) { // Base64 decoding of byte array strings and generating images
        if (imgStr == null) // Image data is empty
            return false;

        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64 decoding
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// Adjust abnormal data
                    b[i] += 256;
                }
            }
            CheckPathUtils.isChartPathExist(pathUrl);
            // Set the path to generate the image
            String path = pathUrl + name + "." + type;
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

package cn.cnic.base.utils;

import org.apache.commons.codec.binary.Base64;

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

        try {
            // Base64 decoding
            byte[] b = Base64.decodeBase64(imgStr);
//            for (int i = 0; i < b.length; ++i) {
//                if (b[i] < 0) {// Adjust abnormal data
//                    b[i] += 256;
//                }
//            }
            CheckPathUtils.isChartPathExist(pathUrl);
            // Set the path to generate the image
            String path = pathUrl + name + "." + type;
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

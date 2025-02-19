package cn.cnic.base.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;


@SuppressWarnings("restriction")
public class ImageUtils {

    private static final Logger log = LoggerFactory.getLogger(ImageUtils.class);
    private static final Integer ZERO = 0;
    private static final Integer ONE_ZERO_TWO_FOUR = 1024;
    private static final Integer NINE_ZERO_ZERO = 900;
    private static final Integer THREE_TWO_SEVEN_FIVE = 3275;
    private static final Integer TWO_ZERO_FOUR_SEVEN = 2047;
    private static final Double ZERO_EIGHT_FIVE = 0.85;
    private static final Double ZERO_SIX = 0.6;
    private static final Double ZERO_FOUR_FOUR = 0.44;
    private static final Double ZERO_FOUR = 0.4;
    private static final String JPG = ".jpeg";


    /**
     * 根据指定大小压缩图片
     *
     * @param imageBytes  源图片字节数组
     * @param desFileSize 指定图片大小，单位kb
     * @return 压缩质量后的图片字节数组
     * scale 0~1 缩放倍数，1表示不缩放，但是这个方法指定不缩放，会出现死循环
     */
    public static byte[] compressPicForScale(byte[] imageBytes, long desFileSize) {
        if (imageBytes == null || imageBytes.length <= ZERO || imageBytes.length < desFileSize * ONE_ZERO_TWO_FOUR) {
            return imageBytes;
        }
        long srcSize = imageBytes.length;
        double accuracy = getAccuracy(srcSize / ONE_ZERO_TWO_FOUR);
        try {
            byte[] bytes = imageBytes;
            long start = System.currentTimeMillis();
            while (imageBytes.length > desFileSize * ONE_ZERO_TWO_FOUR) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(imageBytes.length);
                Thumbnails.of(inputStream)
                        .scale(accuracy) // 尺寸压缩
                        .outputQuality(accuracy)  // 质量压缩
                        .toOutputStream(outputStream);
                imageBytes = outputStream.toByteArray();
                long end = System.currentTimeMillis();
                if (end - start > 5 * 1000L){
                    // 如果5秒钟不能完成，则返回原图片，避免死循环。
                    log.warn("压缩时间超过5s,不压缩");
                    return bytes;
                }
            }
            log.info("图片原大小={}kb | 压缩后大小={}kb",
                    srcSize / ONE_ZERO_TWO_FOUR, imageBytes.length / ONE_ZERO_TWO_FOUR);
        } catch (Exception e) {
            log.error("【图片压缩】msg=图片压缩失败!", e);
        }
        return imageBytes;
    }

    /**
     * 自动调节精度(经验数值)
     *
     * @param size 源图片大小
     * @return 图片压缩质量比
     */
    private static double getAccuracy(long size) {
        double accuracy;
        if (size < NINE_ZERO_ZERO) {
            accuracy = ZERO_EIGHT_FIVE;
        } else if (size < TWO_ZERO_FOUR_SEVEN) {
            accuracy = ZERO_SIX;
        } else if (size < THREE_TWO_SEVEN_FIVE) {
            accuracy = ZERO_FOUR_FOUR;
        } else {
            accuracy = ZERO_FOUR;
        }
        return accuracy;
    }

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

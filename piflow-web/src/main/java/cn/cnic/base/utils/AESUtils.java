package cn.cnic.base.utils;

import org.apache.commons.lang.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * @author yaxuan
 * @create 2023/8/30 17:30
 */
public class AESUtils {

    //密钥 (需要前端和后端保持一致)十六位作为密钥
    private static final String KEY = "ABCDEFGHIJKL_key";

    //密钥偏移量 (需要前端和后端保持一致)十六位作为密钥偏移量
    private static final String IV = "ABCDEFGHIJKLM_iv";

    //算法
    private static final String ALGORITHMSTR = "AES/CBC/PKCS5Padding";

    /**
     * base 64 decode
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    public static byte[] base64Decode(String base64Code) throws Exception{
        return StringUtils.isEmpty(base64Code) ? null : Base64.getDecoder().decode(base64Code);
    }

    /**
     * AES解密
     * @param encryptBytes 待解密的byte[]
     * @return 解密后的String
     * @throws Exception
     */
    public static String aesDecryptByBytes(byte[] encryptBytes) throws Exception {

        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);

        byte[] temp = IV.getBytes("UTF-8");
        IvParameterSpec iv = new IvParameterSpec(temp);

        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(KEY.getBytes(), "AES"), iv);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);

        System.out.print(new String(decryptBytes));
        return new String(decryptBytes);
    }

    /**
     * 将base 64 code AES解密
     * @param encryptStr 待解密的base 64 code
     * @return 解密后的string
     * @throws Exception
     */
    public static String aesDecrypt(String encryptStr) throws Exception {
        return StringUtils.isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr));
    }
}


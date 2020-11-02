package cn.cnic.base.util;

import java.util.UUID;

public class UUIDUtils {

    /**
     * uuid(32-bit)
     *
     * @return
     */
    public static String getUUID32() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
}

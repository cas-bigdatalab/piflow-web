package cn.cnic.base.utils;

import java.util.HashMap;
import java.util.Map;

public class PasswordUtils {
    private static Map<String,String> map = new HashMap<>();

    public static void getKeyAndValue(String username, String password){
        map.put(username,password);
    }

    public static String getPassword(String username) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (username.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return username;
    }

    public static void updatePassword(String username,String password) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (username.equals(entry.getKey())) {
                 entry.setValue(password);
            } else {
                map.put(username,password);
            }
        }
    }
}

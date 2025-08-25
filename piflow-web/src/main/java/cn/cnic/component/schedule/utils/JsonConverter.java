package cn.cnic.component.schedule.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.util.ArrayList;

public class JsonConverter {
    private static final Gson GSON = new Gson();

    /**
     * 将 List<T> 对象转换为 JSON 字符串。
     * @param list 要转换的 List 对象
     * @param <T>  List 中元素的类型
     * @return 转换后的 JSON 字符串
     */
    public static <T> String listToJson(List<T> list) {
        return GSON.toJson(list);
    }

    /**
     * 将 JSON 字符串转换为 List<T> 对象。
     * @param json  要转换的 JSON 字符串
     * @param typeOfT List 中元素的类型
     * @param <T>   List 中元素的类型
     * @return 转换后的 List 对象
     */
    public static <T> List<T> jsonToList(String json, Class<T> typeOfT) {
        if (json == null || json.isEmpty()) {
            return new ArrayList<>();
        }
        Type type = TypeToken.getParameterized(List.class, typeOfT).getType();
        return GSON.fromJson(json, type);
    }
}
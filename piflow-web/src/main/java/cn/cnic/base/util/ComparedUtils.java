package cn.cnic.base.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Field;

public class ComparedUtils {

    private static String CLASS_ONE_VALUE_KEY = "class_one_value_key";
    private static String CLASS_TWO_VALUE_KEY = "class_two_value_key";

    /**
     * Get a list of two objects with the same attribute
     *
     * @param class1 object1
     * @param class2 object2
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     */
    public static List<Map<String, Object>> compareTwoClass(Object class1, Object class2) throws ClassNotFoundException, IllegalAccessException {
        List<Map<String, Object>> list = new ArrayList<>();
        //Get the class of the object
        Class<?> clazz1 = class1.getClass();
        Class<?> clazz2 = class2.getClass();
        //Get the property list of an object
        Field[] field1 = clazz1.getDeclaredFields();
        Field[] field2 = clazz2.getDeclaredFields();
        // field1 to map
        Map<String, Field> field1Map = new HashMap<>();
        if (null != field1 && field1.length > 0) {
            //Traverse the attribute list field1
            for (int i = 0; i < field1.length; i++) {
                field1Map.put(field1[i].getName(), field1[i]);
            }
        }
        if (null != field2 && field2.length > 0) {
            //Traverse the attribute list field2
            for (int j = 0; j < field2.length; j++) {
                if (null == field2[j]) {
                    continue;
                }
                Field field1_i = field1Map.get(field2[j].getName());
                if (null == field1_i) {
                    continue;
                }
                field1_i.setAccessible(true);
                field2[j].setAccessible(true);
                //If the field1_i attribute value is different from the field2 [j] attribute value
                if (!compareTwo(field1_i.get(class1), field2[j].get(class2))) {
                    Map<String, Object> map2 = new HashMap<String, Object>();
                    map2.put("name", field1_i.getName());
                    map2.put(CLASS_ONE_VALUE_KEY, field1_i.get(class1));
                    map2.put(CLASS_TWO_VALUE_KEY, field2[j].get(class2));
                    list.add(map2);
                }
            }
        }
        return list;
    }

    //Compare whether the two data have the same content
    public static boolean compareTwo(Object object1, Object object2) {

        if (object1 == null && object2 == null) {
            return true;
        }
        if (object1 == null && object2 != null) {
            return false;
        }
        if (object1.equals(object2)) {
            return true;
        }
        return false;
    }
}

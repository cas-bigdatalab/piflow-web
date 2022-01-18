package cn.cnic.base.utils;

import java.text.DecimalFormat;

public class DecimalFormatUtils {

    public static final String KEEP_TWO_DECIMAL_PLACES = "#.00";

    public static Double formatTwoDecimalPlaces(double num){
        DecimalFormat df = new DecimalFormat(KEEP_TWO_DECIMAL_PLACES);
        String format = df.format(num);
        return Double.parseDouble(format);
    }


}

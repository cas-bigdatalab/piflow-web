package cn.cnic.base.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    static Logger logger = LoggerUtil.getLogger();

    /**
     * yyyyMMdd 20121225
     */
    public static final String DATE_PATTERN_yyyyMMdd = "yyyyMMdd";

    /**
     * yyyyMMddHHmmss 20121225202020
     */
    public static final String DATE_PATTERN_yyyyMMddHHMMss = "yyyyMMddHHmmss";

    /**
     * yyyy-MM-dd 2012-12-25
     */
    public static final String DATE_PATTERN_yyyy_MM_dd = "yyyy-MM-dd";

    /**
     * MM-dd 12月25日
     */
    public static final String DATE_PATTERN_MM_dd = "MM月dd日";

    /**
     * HH:mm:ss 20:20:20
     */
    public static final String DATE_PATTERN_HH_MM_ss = "HH:mm:ss";

    /**
     * HH:mm 20:20
     */
    public static final String DATE_PATTERN_HH_MM = "HH:mm";

    /**
     * yyyy-MM-dd HH:mm:ss 2012-12-25 20:20:20
     */
    public static final String DATE_PATTERN_yyyy_MM_dd_HH_MM_ss = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyy-MM-dd HH:mm:ss 2012-12-25 20:20:20.098
     */
    public static final String DATE_PATTERN_yyyy_MM_dd_HH_MM_ss_S = "yyyy-MM-dd HH:mm:ss.S";

    /**
     * yyyy-MM-dd HH:mm:ss 2012-12-25 20:20
     */
    public static final String DATE_PATTERN_yyyy_MM_dd_HH_MM = "yyyy-MM-dd HH:mm";

    /**
     * Beginning of the day 00:00:00
     */
    public static final String START_OF_DAY = " 00:00:00";

    /**
     * The end of the day 23:59:59
     */
    public static final String END_OF_DAY = " 23:59:59";

    /**
     * :
     */
    public static final String COLON = ":";

    /**
     * 00
     */
    public static final String DEFAULT_SECONDS = "00";

    /**
     * yyyyMMdd 2012
     */
    public static final String DATE_PATTERN_yyyy = "yyyy";

    /**
     * yyyy-MM 2012-12
     */
    public static final String DATE_PATTERN_yyyy_MM = "yyyy-MM";

    /**
     * EEE MMM dd HH:mm:ss zzz yyyy 例如 Thu Oct 25 10:03:25 CST 2018
     */
    public static final String EEE_MMM_dd_HH_mm_ss_zzz_yyyy = "EEE MMM dd HH:mm:ss zzz yyyy";

    /**
     * Calculate the beginning of the next day
     *
     * @throws ParseException
     */
    public static Date startOfNextDay(Date aDay) throws ParseException {
        return getDate(anotherDay(aDay, 1));
    }

    /**
     * Get a date relative to "date"
     *
     * @param i One day after the positive number is obtained, and one day before the negative number is obtained.
     * @throws ParseException
     */
    public static Date anotherDay(Date date, int i) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, i);
        Date result = cal.getTime();
        return result;
    }

    /**
     * Get a date relative to "date"
     *
     * @param i One day after the positive number is obtained, and one day before the negative number is obtained.
     * @throws ParseException
     */
    public static String anotherDateToStr(String sdate, int i) throws ParseException {
        Date date = strToDate(sdate);
        Date newDate = anotherDay(date, i);
        String newDateS = dateToStr(newDate);
        return newDateS;
    }

    public static Date getDate(Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN_yyyy_MM_dd);
        return new SimpleDateFormat(DATE_PATTERN_yyyy_MM_dd).parse(sdf.format(date));
    }

    /**
     * The "Date" time in the format "HH: mm: ss", such as "20:20:20"
     */
    public static Date constructTimeByStrHhmmss(String timeStr) {
        try {
            return new SimpleDateFormat(DateUtils.DATE_PATTERN_HH_MM_ss).parse(timeStr);
        } catch (ParseException e) {
            // logger.warn("【" + timeStr + "】格式化错误！");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Convert short-time format strings to time "yyyy-MM-dd"
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        if (StringUtils.isNotBlank(strDate)) {
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN_yyyy_MM_dd);
            ParsePosition pos = new ParsePosition(0);
            Date strtodate = formatter.parse(strDate, pos);
            return strtodate;
        }
        return null;
    }

    /**
     * Convert short-time format string to time "yyyy-MM-dd HH:mm:ss"
     *
     * @param strDate
     * @return
     */
    public static Date strToTime(String strDate) {
        if (StringUtils.isNotBlank(strDate) && strDate.length() == 19) {
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN_yyyy_MM_dd_HH_MM_ss);
            ParsePosition pos = new ParsePosition(0);
            Date strtodate = formatter.parse(strDate, pos);
            return strtodate;
        }
        return null;
    }

    /**
     * Convert short-time format string to time "yyyy-MM-dd HH:mm"
     *
     * @param strDate
     * @return
     */
    public static Date strToSecTime(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN_yyyy_MM_dd_HH_MM);
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * Convert short format time to string "yyyy-MM-dd"
     *
     * @param dateDate
     * @return
     */
    public static String dateToStr(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN_yyyy_MM_dd);
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * Convert short format time to string "yyyy-MM-dd_HH_MM_ss"
     *
     * @param dateDate
     * @return
     */
    public static String dateTimeToStr(Date dateDate) {
        if (dateDate == null) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN_yyyy_MM_dd_HH_MM_ss);
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * Convert the time format to the string "yyyy-MM-dd_HH_MM_ss"
     *
     * @param dateDate
     * @return
     */
    public static String dateTimesToStr(Date dateDate) {
        if (dateDate == null) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN_yyyy_MM_dd_HH_MM_ss_S);
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * Convert short format time to string "yyyy-MM-dd_HH_MM"
     *
     * @param dateDate
     * @return
     */
    public static String dateTimeSecToStr(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN_yyyy_MM_dd_HH_MM);
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * Convert short format time to string "yyyy-MM-dd"
     *
     * @param dateDate
     * @return
     */
    public static String dateToHhMmStr(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN_HH_MM);
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * Convert short format time to string "yyyyMMdd"
     *
     * @param dateDate
     * @return
     */
    public static String dateToStr_YYYYMMDD(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN_yyyyMMdd);
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * Convert short format time to string "yyyyMMddHmmss"
     *
     * @param dateDate
     * @return
     */
    public static String dateToStr_yyyyMMddHHMMss(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN_yyyyMMddHHMMss);
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 13-bit timestamp forwarding date format
     *
     * @param timeLong
     * @return
     */
    public static String dateToStr_yyyyMMddHHMMss(String timeLong) {
        Long time = Long.parseLong(timeLong);
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN_yyyy_MM_dd_HH_MM_ss);
        Date date;
        try {
            date = formatter.parse(formatter.format(time));
            return formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns a string of days of the week based on a date
     *
     * @param sdate
     * @return
     */
    public static String getWeek(String sdate) {
        // Conversion to Time
        Date date = strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // int hour=c.get(Calendar.DAY_OF_WEEK);
        // What day of the week exists in "hour", which ranges from "1 to 7"
        // 1 = Sunday 7 = Saturday, and so on.
        return new SimpleDateFormat("EEEE").format(c.getTime());
    }

    public static String getWeekStr(String sdate) {
        String str = "";
        str = getWeek(sdate);
        if ("1".equals(str)) {
            str = "星期日";
        } else if ("2".equals(str)) {
            str = "星期一";
        } else if ("3".equals(str)) {
            str = "星期二";
        } else if ("4".equals(str)) {
            str = "星期三";
        } else if ("5".equals(str)) {
            str = "星期四";
        } else if ("6".equals(str)) {
            str = "星期五";
        } else if ("7".equals(str)) {
            str = "星期六";
        }
        return str;
    }

    /**
     * Returns a string of days of the week based on a date
     *
     * @param sdate
     * @return
     */
    public static int getWeeks(String sdate) {
        // Conversion to Time
        Date date = strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // 1 = Monday... 7 = Sunday, and so on.
        int week = c.get(Calendar.DAY_OF_WEEK) - 1;
        return week = week == 0 ? 7 : week;
    }

    /**
     * Calculate two time intervals
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long getDays(String date1, String date2) {
        if (date1 == null || date1.equals(""))
            return 0;
        if (date2 == null || date2.equals(""))
            return 0;
        // Conversion to standard time
        SimpleDateFormat myFormatter = new SimpleDateFormat(DATE_PATTERN_yyyy_MM_dd);
        Date date = null;
        Date mydate = null;
        try {
            date = myFormatter.parse(date1);
            mydate = myFormatter.parse(date2);
        } catch (Exception e) {
        }
        long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        return day;
    }

    /**
     * Month of addition
     *
     * @param startDate
     * @param addnos
     * @return
     */
    public static Date addMonth(Date startDate, int addnos) {
        Calendar cc = Calendar.getInstance();
        if (startDate != null) {
            cc.setTime(startDate);
            cc.add(Calendar.MONTH, addnos);
            return cc.getTime();
        } else {
            return null;
        }
    }

    /**
     * Year added
     *
     * @param date
     * @param addnos
     * @return
     */
    public static Date addYear(Date date, int addnos) {
        Calendar cc = Calendar.getInstance();
        if (date != null) {
            cc.setTime(date);
            cc.add(Calendar.YEAR, addnos);
            return cc.getTime();
        } else {
            return null;
        }
    }

    /**
     * Get the current year
     *
     * @return
     */
    public static int getNowYear() {
        Calendar cc = Calendar.getInstance();
        return cc.get(Calendar.YEAR);
    }

    /**
     * Compare the difference between the two dates. If the start date is earlier than the end date, return the positive number, otherwise return the negative number.
     *
     * @param start
     * @param end
     * @return
     */
    public static long compareDay(Date start, Date end) {
        long day = compareDate(start, end);
        return day / 1000 / 60 / 60 / 24;
    }

    /**
     * Compare the milliseconds difference between the two dates. If the start date is earlier than the end date, the positive number is returned, otherwise the negative number is returned.
     *
     * @param start Start date
     * @param end   End date
     * @return
     */
    public static long compareDate(Date start, Date end) {
        long temp = 0;
        Calendar starts = Calendar.getInstance();
        Calendar ends = Calendar.getInstance();
        starts.setTime(start);
        ends.setTime(end);
        temp = ends.getTime().getTime() - starts.getTime().getTime();
        return temp;
    }

    /**
     * Add N days to a date or subtract N days to get a new date
     *
     * @param startDate Date and time to be added
     * @param addnos    The number of days added, either positive or negative
     * @return Date after operation
     */
    public static Date AddDay(Date startDate, int addnos) {
        if (startDate == null) {
            startDate = new Date();
        }

        Calendar cc = Calendar.getInstance();
        cc.setTime(startDate);
        cc.add(Calendar.DATE, addnos);
        return cc.getTime();
    }

    /**
     * Increase of seconds
     *
     * @param startDate
     * @param addnos
     * @return
     */
    public static Date addSecond(Date startDate, int addnos) {
        if (startDate == null) {
            startDate = new Date();
        }

        Calendar cc = Calendar.getInstance();
        cc.setTime(startDate);
        cc.add(Calendar.SECOND, addnos);
        return cc.getTime();
    }

    /**
     * Year of acquisition date
     *
     * @param date
     * @return
     */
    public static Integer getDateYear(Date date) {
        return getYearMonthOrDay(date, "YEAR");
    }

    /**
     * Date and month of acquisition
     *
     * @param date
     * @return
     */
    public static Integer getDateMonth(Date date) {
        return getYearMonthOrDay(date, "MONTH");
    }

    /**
     * Date of acquisition
     *
     * @param date
     * @return
     */
    public static Integer getDateDay(Date date) {
        return getYearMonthOrDay(date, "DAY");
    }

    private static Integer getYearMonthOrDay(Date date, String type) {
        if (null != date && StringUtils.isNotBlank(type)) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            if ("YEAR".equals(type)) {
                return new Integer(c.get(Calendar.YEAR));
            } else if ("MONTH".equals(type)) {
                return new Integer(c.get(Calendar.MONTH) + 1);
            } else if ("DAY".equals(type)) {
                return new Integer(c.get(Calendar.DAY_OF_MONTH));
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static Date startOfToday() throws ParseException {
        return DateUtils.startOfNextDay(DateUtils.anotherDay(new Date(), -1));
    }

    /**
     * Convert short format time to string "yyyy"
     *
     * @param dateDate
     * @return
     */
    public static String dateToStr_YYYY(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN_yyyy);
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * Convert the time format of year and month to the string "yyyy_MM"
     *
     * @param dateDate
     * @return
     */
    public static String dateToStr_yy_MM(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN_yyyy_MM);
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    public static String dateToChStr(Date dateDate) {
        if (dateDate == null) {
            dateDate = new Date();
        }
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN_yyyy_MM_dd);
        String dateCh = formatter.format(dateDate);
        dateCh = dateCh.replaceFirst("-", "年").replaceFirst("-", "月") + "日";
        String hhmmCh = dateToHhMmStr(dateDate);
        hhmmCh = hhmmCh.replace(":", "时") + "分";
        return dateCh + "  " + hhmmCh;
    }

    public static Date strCstToDate(String cstStr) {
        Date date = null;
        if (StringUtils.isNotBlank(cstStr)) {
            SimpleDateFormat formatter = new SimpleDateFormat(EEE_MMM_dd_HH_mm_ss_zzz_yyyy, Locale.US);
            ParsePosition pos = new ParsePosition(0);
            date = formatter.parse(cstStr, pos);
            logger.debug("Converted values：" + date);
        }
        return date;
    }
}

package com.nature.base.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class DateUtils {
	// static Logger logger = LoggerUtil.getLogger();

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
	 * yyyy-MM-dd HH:mm:ss 2012-12-25 20:20
	 */
	public static final String DATE_PATTERN_yyyy_MM_dd_HH_MM = "yyyy-MM-dd HH:mm";

	/**
	 * 一天的开始时间点 00:00:00
	 */
	public static final String START_OF_DAY = " 00:00:00";

	/**
	 * 一天的结束时间点 23:59:59
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
	 * 计算第二天的开始
	 * 
	 * @throws ParseException
	 */
	public static Date startOfNextDay(Date aDay) throws ParseException {
		return getDate(anotherDay(aDay, 1));
	}

	/**
	 * 获取相对于date前后的某一天
	 * 
	 * @param i 正数获得其后某一天，负数获得之前某一天
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
	 * 获取相对于date前后的某一天
	 * 
	 * @param i 正数获得其后某一天，负数获得之前某一天
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
	 * 构造格式为HH:mm:ss的Date时间，例如20:20:20
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
	 * 将短时间格式字符串转换为时间 yyyy-MM-dd
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
	 * 将短时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
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
	 * 将短时间格式字符串转换为时间 yyyy-MM-dd HH:mm
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
	 * 将短时间格式时间转换为字符串 yyyy-MM-dd
	 * 
	 * @param dateDate
	 * @param k
	 * @return
	 */
	public static String dateToStr(java.util.Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN_yyyy_MM_dd);
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	/**
	 * 将短时间格式时间转换为字符串 yyyy-MM-dd_HH_MM_ss
	 * 
	 * @param dateDate
	 * @param k
	 * @return
	 */
	public static String dateTimeToStr(java.util.Date dateDate) {
		if (dateDate == null) {
			return "";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN_yyyy_MM_dd_HH_MM_ss);
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	/**
	 * 将短时间格式时间转换为字符串 yyyy-MM-dd_HH_MM
	 * 
	 * @param dateDate
	 * @param k
	 * @return
	 */
	public static String dateTimeSecToStr(java.util.Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN_yyyy_MM_dd_HH_MM);
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	/**
	 * 将短时间格式时间转换为字符串 yyyy-MM-dd
	 * 
	 * @param dateDate
	 * @param k
	 * @return
	 */
	public static String dateToHhMmStr(java.util.Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN_HH_MM);
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	/**
	 * 将短时间格式时间转换为字符串 yyyyMMdd
	 * 
	 * @param dateDate
	 * @param k
	 * @return
	 */
	public static String dateToStr_YYYYMMDD(java.util.Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN_yyyyMMdd);
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	/**
	 * 将短时间格式时间转换为字符串 yyyyMMddHHmmss
	 * 
	 * @param dateDate
	 * @param k
	 * @return
	 */
	public static String dateToStr_yyyyMMddHHMMss(java.util.Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN_yyyyMMddHHMMss);
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	/**
	 * 根据一个日期，返回是星期几的字符串
	 * 
	 * @param sdate
	 * @return
	 */
	public static String getWeek(String sdate) {
		// 再转换为时间
		Date date = strToDate(sdate);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// int hour=c.get(Calendar.DAY_OF_WEEK);
		// hour中存的就是星期几了，其范围 1~7
		// 1=星期日 7=星期六，其他类推
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
	 * 根据一个日期，返回是星期几的字符串
	 *
	 * @param sdate
	 * @return
	 */
	public static int getWeeks(String sdate) {
		// 再转换为时间
		Date date = strToDate(sdate);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// 1=星期1 ......... 7=星期日，其他类推
		int week = c.get(Calendar.DAY_OF_WEEK) - 1;
		return week = week == 0 ? 7 : week;
	}

	/**
	 * 计算两个时间间隔
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
		// 转换为标准时间
		SimpleDateFormat myFormatter = new SimpleDateFormat(DATE_PATTERN_yyyy_MM_dd);
		java.util.Date date = null;
		java.util.Date mydate = null;
		try {
			date = myFormatter.parse(date1);
			mydate = myFormatter.parse(date2);
		} catch (Exception e) {
		}
		long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		return day;
	}

	/**
	 * 添加月份
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
	 * 添加年份
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
	 * 得到当前年份
	 * 
	 * @return
	 */
	public static int getNowYear() {
		Calendar cc = Calendar.getInstance();
		return cc.get(Calendar.YEAR);
	}

	/**
	 * 比较两个日期的相差天数,如果开始日期比结束日期早，则返回正数，否则返回负数。
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
	 * 比较两个日期的相差毫秒数,如果开始日期比结束日期早，则返回正数，否则返回负数。
	 * 
	 * @param start 开始日期
	 * @param end   结束日期
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
	 * 给一个日期加上N天或减去N天得到一个新的日期
	 * 
	 * @param startDate 需要增加的日期时间
	 * @param addnos    添加的天数，可以是正数也可以是负数
	 * @return 操作后的日期
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
	 * 增加秒数
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
	 * 获取日期的年
	 * 
	 * @param date
	 * @return
	 */
	public static Integer getDateYear(Date date) {
		return getYearMonthOrDay(date, "YEAR");
	}

	/**
	 * 获取日期月份
	 * 
	 * @param date
	 * @return
	 */
	public static Integer getDateMonth(Date date) {
		return getYearMonthOrDay(date, "MONTH");
	}

	/**
	 * 获取日期天
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
	 * 将短时间格式时间转换为字符串 yyyy
	 * 
	 * @param dateDate
	 * @param k
	 * @return
	 */
	public static String dateToStr_YYYY(java.util.Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN_yyyy);
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	/**
	 * 将年月时间格式时间转换为字符串 yyyy_MM
	 * 
	 * @param dateDate
	 * @param k
	 * @return
	 */
	public static String dateToStr_yy_MM(java.util.Date dateDate) {
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
}

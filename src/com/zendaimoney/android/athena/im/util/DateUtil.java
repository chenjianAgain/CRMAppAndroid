/**   
 * 用一句话描述该文件做什么.
 * @title DateUtil.java
 * @package com.sinsoft.android.util
 * @author shimiso  
 * @update 2012-6-26 上午9:57:56  
 */
package com.zendaimoney.android.athena.im.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.zendaimoney.android.athena.im.comm.Constant;

/**
 * 日期操作工具类.
 * 
 * @author shimiso
 */

public class DateUtil {

	private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static Date str2Date(String str) {
		return str2Date(str, null);
	}

	public static Date str2Date(String str, String format) {
		if (str == null || str.length() == 0) {
			return null;
		}
		if (format == null || format.length() == 0) {
			format = FORMAT;
		}
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			date = sdf.parse(str);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;

	}

	public static Calendar str2Calendar(String str) {
		return str2Calendar(str, null);

	}

	public static Calendar str2Calendar(String str, String format) {

		Date date = str2Date(str, format);
		if (date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		return c;

	}

	public static String date2Str(Calendar c) {// yyyy-MM-dd HH:mm:ss
		return date2Str(c, null);
	}

	public static String date2Str(Calendar c, String format) {
		if (c == null) {
			return null;
		}
		return date2Str(c.getTime(), format);
	}

	public static String date2Str(Date d) {// yyyy-MM-dd HH:mm:ss
		return date2Str(d, null);
	}

	public static String date2Str(Date d, String format) {// yyyy-MM-dd HH:mm:ss
		if (d == null) {
			return null;
		}
		if (format == null || format.length() == 0) {
			format = FORMAT;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String s = sdf.format(d);
		return s;
	}

	public static String getCurDateStr() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-"
				+ c.get(Calendar.DAY_OF_MONTH) + "-"
				+ c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE)
				+ ":" + c.get(Calendar.SECOND);
	}

	/**
	 * 获得当前日期的字符串格式
	 * 
	 * @param format
	 * @return
	 */
	public static String getCurDateStr(String format) {
		Calendar c = Calendar.getInstance();
		return date2Str(c, format);
	}

	// 格式到秒
	public static String getMillon(long time) {

		return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(time);

	}

	// 格式到天
	public static String getDay(long time) {

		return new SimpleDateFormat("yyyy-MM-dd").format(time);

	}

	// 格式到毫秒
	public static String getSMillon(long time) {

		return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(time);

	}
	
	public static long calendarDiff(String messageTime){

		long min = 0;
		long millsDiff = 0;
		long diffCur = 0;
		long diffMessage = 0;
		try{
			Calendar message = str2Calendar(messageTime);
			Calendar cur = str2Calendar(DateUtil.date2Str(Calendar.getInstance(),
					Constant.TIME_FORMART));
			
			diffMessage = message.getTimeInMillis();
			diffCur = cur.getTime().getTime();
		}catch(Exception e){
			e.printStackTrace();
		}
		min = (diffCur -  diffMessage) / (1000 * 60);
		return min;
	}
	
	public static long calendarDiff(String startTime, String endTime){
		long min = 0;
		long diffStart = 0;
		long diffEnd = 0;
		try{
			Calendar start = str2Calendar(startTime);
			Calendar end = str2Calendar(endTime);
			
			diffStart = start.getTimeInMillis();
			diffEnd = end.getTimeInMillis();
		}catch(Exception e){
			e.printStackTrace();
		}
		min = (diffEnd -  diffStart) / (1000 * 60);
		return min;
	}
}

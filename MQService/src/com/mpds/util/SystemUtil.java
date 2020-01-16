
package com.mpds.util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SystemUtil {

	private static final Logger	logger	= Logger.getLogger(SystemUtil.class);
	
	private static final Gson gson = new GsonBuilder().create();

	public static Date addDay(Date aDate, int dayCount){
		if (aDate != null) {
			Calendar cal = new GregorianCalendar();
			cal.setTime(aDate);
			cal.add(Calendar.DAY_OF_YEAR, dayCount);
			return cal.getTime();
		}
		else {
			return null;
		}
	}

	public static String dateToStr(Date date, String format){
		SimpleDateFormat dateFormat = null;
		try {
			dateFormat = new SimpleDateFormat(format);
			return dateFormat.format(date);
		}
		catch (Exception e) {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			return dateFormat.format(date);
		}
	}

	public static Date strToDate(String sDate, String format){
		Date dDate = new Date();
		SimpleDateFormat dateFormat = null;
		try {
			dateFormat = new SimpleDateFormat(format);
			dDate = dateFormat.parse(sDate);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return dDate;
	}

	public static String getEndOfDayID(Date time, String timePoint){
		String eodID = "";
		Date currentTime;
		Date eodTime;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
		currentTime = time;
		eodTime = time;

		// timePoint = String.format("%02d", iHours) + ":" + aTime[1];
		eodTime = strToDate(dateToStr(eodTime, "yyyyMMdd" + timePoint), "yyyyMMddHH:mm");

		if (currentTime.after(eodTime)) {
			eodID = dateFormat.format(SystemUtil.addDay(eodTime, 1));
		}
		else {
			eodID = dateFormat.format(eodTime);
		}

		return eodID;
	}

	public static void setSystemTime(Long timestamp){
		String osName = System.getProperty("os.name");
		String cmd = "";
		Date dateTime = new Date(timestamp);

		SimpleDateFormat dateFormat;

		try {
			if (osName.matches("^(?i)Windows.*$")) {
				dateFormat = new SimpleDateFormat("HH:mm:ss");
				dateFormat.setTimeZone(TimeZone.getDefault());
				cmd = " cmd /c time " + dateFormat.format(dateTime);
				Runtime.getRuntime().exec(cmd);
				logger.info("set time: " + cmd);
				dateFormat = new SimpleDateFormat("MM-dd-yyyy");
				cmd = "cmd /c date " + dateFormat.format(dateTime);
				Runtime.getRuntime().exec(cmd);
				logger.info("set time: " + cmd);
			}
			else {
				dateFormat = new SimpleDateFormat("yyyyMMdd");
				dateFormat.setTimeZone(TimeZone.getDefault());
				cmd = " date -s " + dateFormat.format(dateTime);
				Runtime.getRuntime().exec(cmd);
				logger.info("set time: " + cmd);
				dateFormat = new SimpleDateFormat("HH:mm:ss");
				cmd = " date -s " + dateFormat.format(dateTime);
				Runtime.getRuntime().exec(cmd);
				logger.info("set time: " + cmd);
			}
		}
		catch (IOException e) {
			logger.error("can't set date time to: " + dateTime);
			e.printStackTrace();
		}
	}

	public static String date2Long(String string) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		sdf.setLenient(false);
		return sdf.parse(string).getTime() + "";
	}

	public static String long2Date(String l){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date dt = new Date((new Long(l)).longValue());
		return sdf.format(dt);
	}

	public static String long2Date(Long l){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date dt = new Date(l);
		return sdf.format(dt);
	}
	
	public static String long2Date(Long l, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date dt = new Date(l);
		return sdf.format(dt);
	}

	public static String formatAmount(String string){
		return string;
	}

	public static String auditLogTemplate(String module, String action, String remark){
		return module + " - " + action + " - " + remark;
	}	

	public static String objToJson(Object obj) {
		return gson.toJson(obj);
	}
	
	public static <T> T jsonToObj(String str, Class<T> clazz) {
		return gson.fromJson(str, clazz);
	}
}
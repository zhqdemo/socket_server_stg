package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static String DATE_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";
	public static SimpleDateFormat format = new SimpleDateFormat();
	public static String getNowTime(){
		format = new SimpleDateFormat(DATE_FORMAT_1);
		return format.format(Calendar.getInstance().getTime());
	}
	public static String getNowTime(String formatString){
		format = new SimpleDateFormat(formatString);
		return format.format(Calendar.getInstance().getTime());
	}
	public static String getNextYear(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR)+1);
		format = new SimpleDateFormat(DATE_FORMAT_1);
		return format.format(cal.getTime());
	}
	public static String formatdate(Date date){
		format = new SimpleDateFormat(DATE_FORMAT_1);
		return format.format(date).toString();
	}
	public static String formatTime(Calendar date,String formatString){
		format = new SimpleDateFormat(formatString);
		return format.format(date.getTime());
	}
	public static String formatTime(Date date,String formatString){
		format = new SimpleDateFormat(formatString);
		return format.format(date);
	}
	public static String formatTime(String date,String formatString){
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		format = new SimpleDateFormat(formatString);
		Date d = null;
		try {
			d = format1.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return format.format(d);
	}
}

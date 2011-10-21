package commons.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public abstract class DateUtils {
	
	static {
		TimeZone.setDefault(new SimpleTimeZone(-3 * 60 * 60 * 1000, "GMT-3:00"));
	}
	
	private static final Locale LOCALE = new Locale("pt", "BR");
	
	private static final SimpleDateFormat SQL_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd", LOCALE);
	
	private static final SimpleDateFormat SQL_DATE_TIME_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", LOCALE);
	
	public static final int DOMINGO = 1;
	
	public static final int SABADO = 7;
	
	private static Calendar getCalendar() {
		return Calendar.getInstance(LOCALE);
	}
	
	public static Date addDay(final Date arg0, final int arg1) {
		final Calendar c = getCalendar();
		c.setTime(arg0);
		c.add(Calendar.DAY_OF_MONTH, arg1);
		return c.getTime();
	}
	
	public static Date addDay(final int arg0) {
		final Calendar c = getCalendar();
		c.setTime(today());
		c.add(Calendar.DAY_OF_MONTH, arg0);
		return c.getTime();
	}
	
	public static Date addMonth(final Date arg0, final int months) {
		final Calendar calendar = getCalendar();
		calendar.setTime(arg0);
		calendar.add(Calendar.MONTH, months);
		return calendar.getTime();
	}
	
	public static Date addUsefulDay(final Date arg0, final int arg1) {
		final Calendar calendar = getCalendar();
		calendar.setTime(arg0);
		
		for (int i = 1; i <= arg1; i++) {
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			
			if (calendar.get(Calendar.DAY_OF_WEEK) == DOMINGO) {
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			} else if (calendar.get(Calendar.DAY_OF_WEEK) == SABADO) {
				calendar.add(Calendar.DAY_OF_MONTH, 2);
			}
		}
		
		return calendar.getTime();
	}
	
	public static Date date(final int day, final int month, final int year) {
		final Calendar c = getCalendar();
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.MONTH, month - 1);
		c.set(Calendar.YEAR, year);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	
	public static int day(final Date arg0) {
		final Calendar c = getCalendar();
		c.setTime(arg0);
		return c.get(Calendar.DAY_OF_MONTH);
	}
	
	public static int dayOfWeek(final Date arg0) {
		final Calendar c = getCalendar();
		c.setTime(arg0);
		return c.get(Calendar.DAY_OF_WEEK);
	}
	
	public static Date daysAhead(final int amount) {
		final Calendar c = getCalendar();
		c.add(Calendar.DAY_OF_MONTH, amount);
		return c.getTime();
	}
	
	public static Date firstDayOfMonth() {
		final Calendar c = getCalendar();
		c.setTime(today());
		c.set(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();
	}
	
	public static Date firstDayOfMonth(final int month, final int year) {
		final Calendar c = getCalendar();
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.MONTH, month - 1);
		c.set(Calendar.YEAR, year);
		return time(c.getTime(), "00:00:00");
	}
	
	public static Date firstUsefulDayOfMonth(final Date arg0) {
		final Calendar calendar = parse(firstDayOfMonth(month(arg0), year(arg0)));
		
		while (calendar.get(Calendar.DAY_OF_WEEK) == DOMINGO ||
				calendar.get(Calendar.DAY_OF_WEEK) == SABADO) {
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return parse(calendar);
	}
	
	public static int hours(final Date dataPrevisaoPagamento) {
		final Calendar c = getCalendar();
		c.setTime(dataPrevisaoPagamento);
		return c.get(Calendar.HOUR_OF_DAY);
	}
	
	public static Date lastDayOfMonth() {
		final Calendar c = getCalendar();
		c.setTime(today());
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.add(Calendar.MONTH, 1);
		c.add(Calendar.DAY_OF_MONTH, -1);
		return time(c.getTime(), "23:59:59");
	}
	
	public static Date lastDayOfMonth(final int month, final int year) {
		int _month = month + 1;
		int _year = year;
		
		if (_month > 12) {
			_month = 1;
			_year++;
		}
		
		final Calendar c = getCalendar();
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.MONTH, _month - 1);
		c.set(Calendar.YEAR, _year);
		c.add(Calendar.DAY_OF_MONTH, -1);
		return time(c.getTime(), "23:59:59");
	}
	
	public static int month(final Date arg0) {
		final Calendar calendar = getCalendar();
		calendar.setTime(arg0);
		return calendar.get(Calendar.MONTH) + 1;
	}
	
	public static Date parse(final Calendar arg0) {
		return arg0.getTime();
	}
	
	public static Calendar parse(final Date arg0) {
		final Calendar c = getCalendar();
		c.setTime(arg0);
		return c;
	}
	
	public static Date parseBRST(final Date arg0) {
		if (arg0 != null && arg0.toString().indexOf("23:00:00") > 0) {
			return DateUtils.time(addDay(arg0, 1), "00:00:00");
		}
		
		return arg0;
	}
	
	public static Date removeDay(final Date arg0, final int arg1) {
		return addDay(arg0, arg1 * -1);
	}
	
	public static Date removeDay(final int arg0) {
		final Calendar c = getCalendar();
		c.setTime(today());
		c.add(Calendar.DAY_OF_MONTH, arg0 * -1);
		return c.getTime();
	}
	
	public static Date time(final Date arg0, final String arg1) {
		final Calendar c = getCalendar();
		c.setTime(arg0);
		
		final int hor = Integer.parseInt(arg1.substring(0, 2));
		final int min = Integer.parseInt(arg1.substring(3, 5));
		final int seg = arg1.length() == 8 ? Integer.parseInt(arg1.substring(6,
				8)) : 0;
		
		c.set(Calendar.HOUR_OF_DAY, hor);
		c.set(Calendar.MINUTE, min);
		c.set(Calendar.SECOND, seg);
		c.set(Calendar.MILLISECOND, 0);
		
		return c.getTime();
	}
	
	public static Date today() {
		final Calendar c = getCalendar();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	
	public static String toSQLDateFormat(final Date arg0) {
		return SQL_DATE_FORMAT.format(arg0);
	}
	
	public static String toSQLDateTimeFormat(final Date arg0) {
		return arg0 != null ? SQL_DATE_TIME_FORMAT.format(arg0) : "";
	}
	
	public static Date usefulDayOfMonth(final Date arg0, final int arg1) {
		return addUsefulDay(firstUsefulDayOfMonth(arg0), arg1 - 1);
	}
	
	public static int year(final Date arg0) {
		final Calendar calendar = getCalendar();
		calendar.setTime(arg0);
		return calendar.get(Calendar.YEAR);
	}
}

package commons.datetime;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Use common.utils.DateUtils
 */
@Deprecated
public abstract class AbstractDateTime {
	
	private static final Locale LOCALE = new Locale("pt", "BR");
	
	private static final SimpleDateFormat STRING_DATE_FORMAT = new SimpleDateFormat(
			"dd/MM/yyyy", AbstractDateTime.LOCALE);
	
	private static final SimpleDateFormat STRING_DATE_FORMAT_SHORT = new SimpleDateFormat(
			"dd/MM/yy", AbstractDateTime.LOCALE);
	
	public static final SimpleDateFormat STRING_DATE_TIME_FORMAT = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm:ss", AbstractDateTime.LOCALE);
	
	public static final SimpleDateFormat STRING_TIME_FORMAT = new SimpleDateFormat(
			"HH:mm:ss", AbstractDateTime.LOCALE);
	
	private static final SimpleDateFormat SQL_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd", AbstractDateTime.LOCALE);
	
	private static final SimpleDateFormat SQL_DATE_TIME_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", AbstractDateTime.LOCALE);
	
	public static final TimeZone TIME_ZONE = TimeZone
			.getTimeZone("America/Sao_Paulo");
	
	private static final String shortMonths[] = { "Jan", "Fev", "Mar", "Abr",
			"Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez" };
	
	private static Calendar set(final int arg0, final int arg1, final int arg2) {
		final Calendar calendar = getCalendar();
		calendar.set(Calendar.DAY_OF_MONTH, arg0);
		calendar.set(Calendar.MONTH, arg1 - 1);
		calendar.set(Calendar.YEAR, arg2);
		return calendar;
	}
	
	@Deprecated
	public static Date add(final Date date, final int days) {
		final Calendar calendar = getCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, days);
		return calendar.getTime();
	}
	
	public static Date add(final Date arg0, final long arg1) {
		final Calendar calendar = getCalendar();
		calendar.setTime(arg0);
		calendar.setTimeInMillis(calendar.getTimeInMillis() + arg1);
		return calendar.getTime();
	}
	
	public static Date addDay(final Date arg0, final int arg1) {
		final Calendar calendar = getCalendar();
		calendar.setTime(arg0);
		calendar.add(Calendar.DAY_OF_MONTH, arg1);
		return calendar.getTime();
	}
	
	public static Date addMinute(final Date arg0, final int arg1) {
		final Calendar calendar = getCalendar();
		calendar.setTime(arg0);
		calendar.add(Calendar.MINUTE, arg1);
		return calendar.getTime();
	}
	
	public static Date addMonth(final Date arg0, final int months) {
		final Calendar calendar = getCalendar();
		calendar.setTime(arg0);
		calendar.add(Calendar.MONTH, months);
		return calendar.getTime();
	}
	
	public static Date addUsefulDays(final Date arg0, final int arg1) {
		final Calendar calendar = getCalendar();
		calendar.setTime(arg0);
		
		for (int i = 1; i <= arg1; i++) {
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			
			if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			} else if (calendar.get(Calendar.DAY_OF_WEEK) == 7) {
				calendar.add(Calendar.DAY_OF_MONTH, 2);
			}
		}
		
		return calendar.getTime();
	}
	
	public static long addWeek(final long time, final int weeks) {
		final Calendar calendar = getCalendar();
		calendar.setTimeInMillis(time);
		calendar.add(Calendar.WEEK_OF_YEAR, weeks);
		return calendar.getTimeInMillis();
	}
	
	public static Date addYear(final Date arg0, final int years) {
		final Calendar calendar = getCalendar();
		calendar.setTime(arg0);
		calendar.add(Calendar.YEAR, years);
		return calendar.getTime();
	}
	
	public static Calendar getCalendar() {
		return Calendar.getInstance(AbstractDateTime.LOCALE);
	}
	
	public static Date getClearDate(final int dia, final int mes, final int ano) {
		final Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, dia);
		cal.set(Calendar.MONTH, mes - 1);
		cal.set(Calendar.YEAR, ano);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new Date(cal.getTimeInMillis());
	}
	
	public static Date getDate() {
		return setTime(getCalendar().getTime(), "00:00:00");
	}
	
	public static Date getDate(final int dia, final int mes, final int ano) {
		final Calendar calendar = getCalendar();
		
		calendar.setTime(getNow());
		calendar.set(Calendar.DAY_OF_MONTH, dia);
		calendar.set(Calendar.MONTH, mes - 1);
		calendar.set(Calendar.YEAR, ano);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		return calendar.getTime();
	}
	
	public static long getDate(final String date) {
		final int day = Integer.parseInt(date.substring(0, 2));
		final int month = Integer.parseInt(date.substring(3, 5));
		final int year = Integer.parseInt(date.substring(6, 10));
		final Calendar calendar = getCalendar();
		
		calendar.setTime(getNow());
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		return calendar.getTimeInMillis();
	}
	
	public static int[] getDatePart() {
		final Calendar c = getCalendar();
		final int dia = c.get(Calendar.DAY_OF_MONTH);
		final int mes = c.get(Calendar.MONTH) + 1;
		final int ano = c.get(Calendar.YEAR);
		final int[] result = { dia, mes, ano };
		return result;
	}
	
	public static int[] getDatePart(final Date arg0) {
		final Calendar c = getCalendar();
		c.setTime(arg0);
		final int dia = c.get(Calendar.DAY_OF_MONTH);
		final int mes = c.get(Calendar.MONTH) + 1;
		final int ano = c.get(Calendar.YEAR);
		final int[] result = { dia, mes, ano };
		return result;
	}
	
	public static long getDateTime(final String date) {
		final int day = Integer.parseInt(date.substring(0, 2));
		final int month = Integer.parseInt(date.substring(3, 5));
		final int year = Integer.parseInt(date.substring(6, 10));
		final int hour = Integer.parseInt(date.substring(11, 13));
		final int minute = Integer.parseInt(date.substring(14, 16));
		final int second = Integer.parseInt(date.substring(17, 19));
		final Calendar calendar = getCalendar();
		
		calendar.setTime(getNow());
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, 0);
		
		return calendar.getTimeInMillis();
	}
	
	public static int getDay(final Date arg0) {
		final Calendar calendar = getCalendar();
		calendar.setTime(arg0);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	public static int getDayOfMonth() {
		return getCalendar().get(Calendar.DAY_OF_MONTH);
	}
	
	public static int getDayOfMonth(final Date arg0) {
		final Calendar calendar = getCalendar();
		calendar.setTime(arg0);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	public static int getDayOfWeek() {
		return getCalendar().get(Calendar.DAY_OF_WEEK);
	}
	
	public static int getDayOfWeek(final Date arg0) {
		final Calendar calendar = getCalendar();
		calendar.setTime(arg0);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * Retorna uma String com o dia da semana
	 * 
	 * @param arg0
	 *            Data
	 * @return Dia da Semana
	 */
	public static String getDayOfWeekLongName(final Date arg0) {
		final String daysOfWeek[] = { "Domingo", "Segunda-Feira",
				"Terça-Feira", "Quarta-Feira", "Quinta-Feira", "Sexta-Feira",
				"Sábado" };
		return daysOfWeek[getDayOfWeek(arg0) - 1];
	}
	
	/**
	 * Retorna uma String Abreviada com o dia da semana
	 * 
	 * @param arg0
	 *            Data
	 * @return Dia da Semana
	 */
	public static String getDayOfWeekShortName(final Date arg0) {
		final String daysOfWeek[] = { "Dom", "Seg", "Ter", "Qua", "Qui", "Sex",
				"Sáb" };
		return daysOfWeek[getDayOfWeek(arg0) - 1];
	}
	
	public static int getDaysBetween(final Date arg0, final Date arg1) {
		Double result = 0.0;
		final long diferenca = arg1.getTime() - arg0.getTime();
		final double diferencaEmDias = diferenca / 1000 / 60 / 60 / 24;
		final long horasRestantes = diferenca / 1000 / 60 / 60 % 24;
		result = diferencaEmDias + horasRestantes / 24d;
		return result.intValue();
	}
	
	public static int getDefinedUsefulDayOfMonth(final int arg0,
			final int arg1, final int arg2) {
		int dia = 0;
		final Calendar calendar = set(1, arg1, arg2);
		
		for (int i = 2; i <= 30; i++) {
			if (calendar.get(Calendar.DAY_OF_WEEK) == 1 ||
					calendar.get(Calendar.DAY_OF_WEEK) == 7) {
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			} else {
				dia++;
				
				if (dia == arg0) {
					break;
				}
				
				set(i, arg1, arg2);
			}
		}
		
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * Retorna o primeiro dia do mês/ano em formato long
	 * 
	 * @param arg0
	 *            Mes
	 * @param arg1
	 *            Ano
	 * @return Data do primeiro dia do mês/ano
	 */
	public static long getFirstDay(final int mes, final int ano) {
		final Calendar calendar = getCalendar();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, mes - 1);
		calendar.set(Calendar.YEAR, ano);
		return calendar.getTimeInMillis();
	}
	
	public static Date getFirstDayOfMonth(final Date arg0) {
		final Calendar calendar = getCalendar();
		calendar.setTime(arg0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}
	
	public static long getFirstDayOfMonth(final int mes, final int ano) {
		final Calendar calendar = getCalendar();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, mes - 1);
		calendar.set(Calendar.YEAR, ano);
		return calendar.getTimeInMillis();
	}
	
	public static long getFirstDayOfWeek(final long arg0) {
		final Calendar calendar = getCalendar();
		calendar.setTimeInMillis(arg0);
		final int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
		return removeDay(calendar.getTime(), diaSemana - 1);
	}
	
	public static Date getFirstHourOfToday() {
		final Calendar calendar = getCalendar();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static int getFirstUsefulDayOfMonth(final int arg0, final int arg1) {
		final Calendar calendar = set(1, arg0, arg1);
		
		while (calendar.get(Calendar.DAY_OF_WEEK) == 1 ||
				calendar.get(Calendar.DAY_OF_WEEK) == 7) {
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	public static String getHour(final Date date) {
		final Calendar calendar = getCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY) + ":" +
				calendar.get(Calendar.MINUTE) + ":" +
				calendar.get(Calendar.SECOND);
	}
	
	/**
	 * Retorna o último dia do mês/ano em formato long
	 * 
	 * @param mes
	 * @param ano
	 * @return Data do último dia do mês/ano
	 */
	public static long getLastDay(final int mes, final int ano) {
		final Calendar calendar = getCalendar();
		calendar.set(Calendar.DAY_OF_MONTH, getLastDayOfMonth(mes, ano));
		calendar.set(Calendar.MONTH, mes - 1);
		calendar.set(Calendar.YEAR, ano);
		return calendar.getTimeInMillis();
	}
	
	public static Date getLastDayOfMonth(final Date arg0) {
		final Calendar calendar = getCalendar();
		calendar.setTime(arg0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return setTime(calendar.getTime(), "23:59:59");
	}
	
	public static int getLastDayOfMonth(final int arg0, final int arg1) {
		int mes = arg0 + 1;
		int ano = arg1;
		
		if (mes > 12) {
			mes = 1;
			ano++;
		}
		
		final Calendar calendar = set(1, mes, ano);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	public static int getLastUsefulDayOfMonth(final int arg0, final int arg1) {
		int mes = arg0 + 1;
		int ano = arg1;
		
		if (mes > 12) {
			mes = 1;
			ano++;
		}
		
		final Calendar calendar = set(1, mes, ano);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		
		while (calendar.get(Calendar.DAY_OF_WEEK) == 1 ||
				calendar.get(Calendar.DAY_OF_WEEK) == 7) {
			calendar.add(Calendar.DAY_OF_MONTH, -1);
		}
		
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	public static int getMonth() {
		return getCalendar().get(Calendar.MONTH) + 1;
	}
	
	public static int getMonth(final Date arg0) {
		final Calendar calendar = getCalendar();
		calendar.setTime(arg0);
		return calendar.get(Calendar.MONTH) + 1;
	}
	
	/**
	 * Retorna uma String do mês
	 * 
	 * @param arg0
	 *            Data
	 * @return mês
	 */
	public static String getMonthLongName(final Date arg0) {
		final String months[] = { "Janeiro", "Fevereiro", "Março", "Abril",
				"Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro",
				"Novembro", "Dezembro" };
		return months[getMonth(arg0) - 1];
	}
	
	/**
	 * Retorna uma String Abreviada do mês
	 * 
	 * @param arg0
	 *            Data
	 * @return mês Abreviado
	 */
	public static String getMonthShortName(final Date arg0) {
		return shortMonths[getMonth(arg0) - 1];
	}
	
	/**
	 * Retorna uma String no formato mês/Ano (com o mês abreviado)
	 * 
	 * @param arg0
	 *            Data
	 * @return mês abreviado / Ano <br>
	 *         Exemplo: <br>
	 *         Parâmetro: 01/12/2008 <br>
	 *         Retorno: "Dez/2008"
	 */
	public static String getMonthYearShortName(final Date arg0) {
		return getMonthShortName(arg0).concat("/").concat(
				Integer.toString(getYear(arg0)));
	}
	
	public static Date getNow() {
		return getCalendar().getTime();
	}
	
	public static String getSimpleDate(final long date) {
		final Date data = new Date(date);
		return new SimpleDateFormat("dd/MMM", AbstractDateTime.LOCALE)
				.format(data);
	}
	
	public static String getStringDate(final String arg0) {
		return arg0.substring(0, 2).concat("/").concat(arg0.substring(2, 4))
				.concat("/").concat(arg0.substring(4));
	}
	
	public static String getStringTime(final Date data) {
		final Calendar calendar = getCalendar();
		calendar.setTime(data);
		
		final int hor = calendar.get(Calendar.HOUR_OF_DAY);
		final int min = calendar.get(Calendar.MINUTE);
		final int seg = calendar.get(Calendar.SECOND);
		
		return (hor > 9 ? hor : "0" + hor) + ":" + (min > 9 ? min : "0" + min) +
				":" + (seg > 9 ? seg : "0" + seg);
	}
	
	public static String getStringTime(final long arg0) {
		final long time = arg0 / 1000;
		final long hor = time / 60 / 60;
		final long min = time / 60 - hor * 60;
		final long seg = time % 60;
		
		return (hor > 9 ? hor : "0" + hor) + "h" + (min > 9 ? min : "0" + min) +
				"m" + (seg > 9 ? seg : "0" + seg) + "s";
	}
	
	public static long getTimeInMills(final String arg0) {
		final int hor = Integer.parseInt(arg0.substring(0, 2));
		final int min = Integer.parseInt(arg0.substring(3, 5));
		final int seg = Integer.parseInt(arg0.substring(6, 8));
		
		return seg * 1000 + min * 60 * 1000 + hor * 60 * 60 * 1000;
	}
	
	public static Timestamp getTimestampNow() {
		return new Timestamp(getNow().getTime());
	}
	
	public static Date getToday() {
		return getFirstHourOfToday();
	}
	
	public static int getYear() {
		return getCalendar().get(Calendar.YEAR);
	}
	
	public static int getYear(final Date arg0) {
		final Calendar calendar = getCalendar();
		calendar.setTime(arg0);
		return calendar.get(Calendar.YEAR);
	}
	
	public static Date parseDate(final String arg0) throws Exception {
		if (arg0 == null) {
			return null;
		}
		
		return STRING_DATE_FORMAT.parse(arg0);
	}
	
	public static Date parseDateTime(final String arg0) throws Exception {
		if (arg0 == null) {
			return null;
		}
		
		return STRING_DATE_TIME_FORMAT.parse(arg0);
	}
	
	public static java.sql.Date parseSQL(final Date arg0) {
		return new java.sql.Date(arg0.getTime());
	}
	
	public static Date remove(final Date date, final int days) {
		final Calendar calendar = getCalendar();
		calendar.setTimeInMillis(date.getTime());
		calendar.add(Calendar.DATE, days * -1);
		return calendar.getTime();
	}
	
	public static long removeDay(final Date date, final int days) {
		final Calendar calendar = getCalendar();
		calendar.setTimeInMillis(date.getTime());
		calendar.add(Calendar.DATE, days * -1);
		return calendar.getTimeInMillis();
	}
	
	public static Date removeMonth(final Date arg0, final int months) {
		final Calendar calendar = getCalendar();
		calendar.setTime(arg0);
		calendar.add(Calendar.MONTH, months * -1);
		return calendar.getTime();
	}
	
	public static long removeWeek(final Date date, final int weeks) {
		final Calendar calendar = getCalendar();
		calendar.setTimeInMillis(date.getTime());
		calendar.add(Calendar.WEEK_OF_YEAR, weeks * -1);
		return calendar.getTimeInMillis();
	}
	
	public static Date removeYear(final Date arg0, final int years) {
		final Calendar calendar = getCalendar();
		calendar.setTime(arg0);
		calendar.add(Calendar.YEAR, years * -1);
		return calendar.getTime();
	}
	
	public static Date setDate(final int arg0, final int arg1, final int arg2) {
		final Calendar calendar = getCalendar();
		calendar.set(Calendar.DAY_OF_MONTH, arg0);
		calendar.set(Calendar.MONTH, arg1 - 1);
		calendar.set(Calendar.YEAR, arg2);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		return calendar.getTime();
	}
	
	public static Date setDay(final int arg0, final Date arg1) {
		final Calendar calendar = getCalendar();
		calendar.setTimeInMillis(arg1.getTime());
		calendar.set(Calendar.DAY_OF_MONTH, arg0);
		return calendar.getTime();
	}
	
	public static Date setTime(final Date arg0, final String arg1) {
		final Calendar calendar = getCalendar();
		calendar.setTime(arg0);
		
		final int hor = Integer.parseInt(arg1.substring(0, 2));
		final int min = Integer.parseInt(arg1.substring(3, 5));
		final int seg = arg1.length() == 8 ? Integer.parseInt(arg1.substring(6,
				8)) : 0;
		
		calendar.set(Calendar.HOUR_OF_DAY, hor);
		calendar.set(Calendar.MINUTE, min);
		calendar.set(Calendar.SECOND, seg);
		calendar.set(Calendar.MILLISECOND, 0);
		
		return calendar.getTime();
	}
	
	public static Date setValidDate(final Integer arg0, final Integer arg1,
			final Integer arg2) throws Exception {
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			sdf.setLenient(false);
			sdf.parse(arg0 + "/" + arg1 + "/" + arg2);
		} catch (final Exception e) {
			throw new Exception("Data inválida!");
		}
		
		return setDate(arg0, arg1, arg2);
		
	}
	
	public static Date setYear(final Date arg0) {
		final Calendar calendar = getCalendar();
		calendar.setTime(arg0);
		calendar.set(Calendar.YEAR, getYear());
		
		return calendar.getTime();
	}
	
	public static Date setYear(final Date arg0, final int arg1) {
		final Calendar calendar = getCalendar();
		calendar.setTime(arg0);
		calendar.set(Calendar.YEAR, arg1);
		
		return calendar.getTime();
	}
	
	public static java.sql.Date toSQLDate(final Date arg0) {
		return new java.sql.Date(arg0.getTime());
	}
	
	/**
	 * Retorna a data no formato SQL.
	 * 
	 * @param arg0
	 *            Data.
	 * @return String no formato SQL "yyyy-MM-dd"
	 */
	public static String toSQLDateFormat(final Date arg0) {
		return AbstractDateTime.SQL_DATE_FORMAT.format(arg0);
	}
	
	public static String toSQLDateFormat(final String arg0) {
		return AbstractDateTime.SQL_DATE_FORMAT.format(getDate(arg0));
	}
	
	public static String toSQLDateTimeFormat(final Date arg0) {
		return arg0 != null ? AbstractDateTime.SQL_DATE_TIME_FORMAT
				.format(arg0) : "";
	}
	
	public static String toSQLDateTimeFormat(final String arg0) {
		return AbstractDateTime.SQL_DATE_TIME_FORMAT.format(getDateTime(arg0));
	}
	
	public static String toSQLNumberFormat(final String arg0) {
		return arg0.replaceAll(",", ".");
	}
	
	public static String toStringDateFormat(final Date arg0) {
		return arg0 != null ? AbstractDateTime.STRING_DATE_FORMAT.format(arg0)
				: "";
	}
	
	public static String toStringDateFormat(final long arg0) {
		return AbstractDateTime.STRING_DATE_FORMAT.format(new Date(arg0));
	}
	
	public static String toStringDateFormatShort(final Date arg0) {
		return arg0 != null ? AbstractDateTime.STRING_DATE_FORMAT_SHORT
				.format(arg0) : "";
	}
	
	public static String toStringDateTimeFormat(final Date arg0) {
		return arg0 != null ? AbstractDateTime.STRING_DATE_TIME_FORMAT
				.format(arg0) : "";
	}
	
	public static String toStringTimeFormat(final Date arg0) {
		return arg0 != null ? AbstractDateTime.STRING_TIME_FORMAT.format(arg0)
				: "";
	}
	
	private AbstractDateTime() {
		super();
	}
}

package commons.log;

import java.io.IOException;
import java.util.Locale;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import commons.util.StringUtils;

public abstract class LogBuilder {
	
	private static String appVersion = "";
	
	private static final Logger logger = Logger.getRootLogger();
	
	public static void configure(final String logFileName,
			final String applicationVersion, final boolean debugMode)
			throws IOException {
		Locale.setDefault(new Locale("pt", "BR"));
		
		if (applicationVersion != null) {
			LogBuilder.appVersion = applicationVersion.replaceAll("\\.", "") +
					"] ";
		}
		
		PatternLayout layout = null;
		
		try {
			layout = new PatternLayout("[%d{EEE ddMMyy HHmmss}] %m%n");
		} catch (final Exception e) {
			e.printStackTrace();
		}
		
		logger.removeAllAppenders();
		
		if (debugMode) {
			final ConsoleAppender consoleAppender = new ConsoleAppender(layout);
			consoleAppender.activateOptions();
			logger.addAppender(consoleAppender);
		}
		
		final FileAppender fileAppender = new FileAppender(layout, "logs/" +
				logFileName + ".log", true);
		fileAppender.activateOptions();
		logger.addAppender(fileAppender);
		
		logger.setLevel(Level.INFO);
	}
	
	public static void error(final String arg0) {
		try {
			logger.fatal(appVersion.concat(StringUtils.formatString(arg0)));
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void error(final String arg0, final String arg1) {
		try {
			logger.fatal(appVersion.concat(StringUtils.formatString(arg0))
					.concat(StringUtils.formatString(arg1)));
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void error(final String arg0, final Throwable arg1) {
		try {
			logger.fatal(appVersion.concat(StringUtils.formatString(arg0)),
					arg1);
			arg1.printStackTrace();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void error(final Throwable arg0) {
		try {
			logger.fatal(appVersion, arg0);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void info(final String arg0) {
		try {
			logger.error(appVersion.concat(StringUtils.formatString(arg0)));
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void info(final String arg0, final String arg1) {
		try {
			logger.error(appVersion.concat(StringUtils.formatString(arg0))
					.concat(StringUtils.formatString(arg1)));
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
}

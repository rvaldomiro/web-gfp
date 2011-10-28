package commons.persistence.hibernate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import commons.log.LogBuilder;

public abstract class AbstractHibernateDatabase {
	
	private static Properties setup(final Properties arg0) {
		final String s = arg0.toString().toLowerCase();
		
		if (s.indexOf("jdbc:postgresql") >= 0) {
			arg0.put(HibernateConfigurationKey.DRIVER_CLASS.value(),
					"org.postgresql.Driver");
			arg0.put(HibernateConfigurationKey.DIALECT.value(),
					"org.hibernate.dialect.PostgreSQLDialect");
		} else if (s.indexOf("jdbc:informix-sqli") >= 0) {
			arg0.put(HibernateConfigurationKey.DRIVER_CLASS.value(),
					"com.informix.jdbc.IfxDriver");
			arg0.put(HibernateConfigurationKey.DIALECT.value(),
					"org.hibernate.dialect.InformixDialect");
		} else if (s.indexOf("jdbc:mysql") >= 0) {
			arg0.put(HibernateConfigurationKey.DRIVER_CLASS.value(),
					"com.mysql.jdbc.Driver");
			arg0.put(HibernateConfigurationKey.DIALECT.value(),
					"org.hibernate.dialect.MySQLDialect");
		} else if (s.indexOf("jdbc:hsqldb") >= 0) {
			arg0.put(HibernateConfigurationKey.DRIVER_CLASS.value(),
					"org.hsqldb.jdbcDriver");
			arg0.put(HibernateConfigurationKey.DIALECT.value(),
					"org.hibernate.dialect.HSQLDialect");
		} else {
			throw new IllegalArgumentException("Dialeto não classificado!");
		}
		
		if ("true".equals(arg0.getProperty(HibernateConfigurationKey.USE_POOL
				.value()))) {
			arg0.put("hibernate.c3p0.acquire_increment", "1");
			arg0.put("hibernate.c3p0.idle_test_period", "100");
			arg0.put("hibernate.c3p0.timeout", "25200");
			arg0.put("hibernate.c3p0.max_size", "10");
			arg0.put("hibernate.c3p0.min_size", "1");
			arg0.put("hibernate.c3p0.max_statements", "0");
		}
		
		return arg0;
	}
	
	public static Properties findConfigFile(final String configurationFile)
			throws FileNotFoundException, IOException {
		return findConfigFile(configurationFile, false);
	}
	
	public static Properties findConfigFile(final String configurationFile,
			final boolean showLog) throws FileNotFoundException, IOException {
		final String catalinaBasePath = System.getProperty("catalina.base");
		final boolean isWebProject = catalinaBasePath != null;
		
		final Set<String> fileLocations = new LinkedHashSet<String>();
		fileLocations.add("/home/sync/config/");
		
		if (isWebProject) {
			final File[] webAppDir = new File(catalinaBasePath + "/wtpwebapps/")
					.listFiles();
			
			if (webAppDir != null) {
				for (final File webApp : webAppDir) {
					if (webApp.isDirectory() &&
							!webApp.getName().equals("ROOT")) {
						fileLocations.add(catalinaBasePath + "/../../../../" +
								webApp.getName() + "/");
					}
				}
			}
		}
		
		fileLocations
				.add(isWebProject ? catalinaBasePath +
						"/../../../../resources/config/db/"
						: "../resources/config/db/");
		
		fileLocations.add(System.getProperty("user.home") + "/");
		fileLocations.add("");
		
		String fileNotFoundPath = "";
		
		for (final String location : fileLocations) {
			final String filePath = location.concat("bd-" + configurationFile +
					"-cfg.properties");
			final File f = new File(filePath);
			
			if (showLog) {
				LogBuilder.info("Localizando arquivo de configurações [" +
						f.getAbsolutePath() + "]");
			}
			
			if (f.exists()) {
				final Properties p = new Properties();
				p.load(new FileInputStream(f));
				p.setProperty(
						"hibernate.connection.url",
						p.getProperty("hibernate.connection.url").replace(
								"${basedir}", f.getCanonicalPath() + "/.."));
				return setup(p);
			}
			
			fileNotFoundPath += f.getAbsolutePath() + "\n";
		}
		
		throw new FileNotFoundException("Arquivo de configurações [bd-" +
				configurationFile + "-cfg.properties] não encontrado!\n\n" +
				fileNotFoundPath);
	}
	
}

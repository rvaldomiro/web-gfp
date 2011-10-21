package commons.file;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProperty {
	
	private final Properties properties;
	
	public ConfigProperty(final String fileName) throws IOException {
		super();
		final InputStream isr = getClass().getClassLoader()
				.getResourceAsStream(fileName);
		this.properties = new Properties();
		this.properties.load(isr);
	}
	
	public String getProperty(final String key) {
		return this.properties.getProperty(key);
	}
	
}

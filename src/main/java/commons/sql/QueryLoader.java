package commons.sql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import commons.util.DateUtils;

public class QueryLoader {
	
	private transient String sql;
	
	public QueryLoader(final Class<?> arg0, final String arg1)
			throws IOException {
		super();
		
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		StringBuilder stringBuilder = null;
		String line = null;
		
		try {
			inputStreamReader = new InputStreamReader(
					arg0.getResourceAsStream(arg1.endsWith(".sql") ? arg1
							: arg1.concat(".sql")));
			bufferedReader = new BufferedReader(inputStreamReader);
			stringBuilder = new StringBuilder();
			
			do {
				line = bufferedReader.readLine();
				
				if (line != null) {
					stringBuilder.append(line.concat(System
							.getProperty("line.separator")));
				}
			} while (line != null);
		} finally {
			this.sql = stringBuilder != null ? stringBuilder.toString() : "";
			
			if (bufferedReader != null) {
				bufferedReader.close();
				bufferedReader = null;
			}
			
			if (inputStreamReader != null) {
				inputStreamReader.close();
				inputStreamReader = null;
			}
		}
	}
	
	public QueryLoader(final String sql) {
		this.sql = sql;
	}
	
	public String getSql() {
		return this.sql;
	}
	
	public void setDateTime(final String paramName, final Date date) {
		final String value = DateUtils.toSQLDateTimeFormat(date);
		this.sql = this.sql.replaceAll(":".concat(paramName), value);
	}
	
	public void setParam(final String paramName, final Object paramValue) {
		String value;
		
		if (paramValue instanceof Date) {
			value = DateUtils.toSQLDateFormat((Date) paramValue);
		} else {
			value = paramValue.toString();
		}
		
		final int pos = value.indexOf("\\");
		
		if (pos >= 0) {
			value = value.substring(0, pos).concat("\\\\")
					.concat(value.substring(pos + 1));
		}
		
		this.sql = this.sql.replaceAll(":".concat(paramName), value);
	}
	
}

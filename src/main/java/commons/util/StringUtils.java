package commons.util;

import java.util.List;

public abstract class StringUtils {
	
	private static final String MASK = "ÃÕÀÈÌÒÙÁÉÍÓÚÄËÏÖÜÂÊÎÔÛÇ".toLowerCase();
	
	private static final String CORRECT = "AOAEIOUAEIOUAEIOUAEIOUC"
			.toLowerCase();
	
	public static String capitalize(final String arg0) {
		final char chars[] = arg0.toLowerCase().replaceAll("#", " ")
				.toCharArray();
		
		for (int i = 0; i < arg0.length(); i++) {
			if (i == 0 || chars[i - 1] == " ".toCharArray()[0]) {
				chars[i] = Character.toUpperCase(chars[i]);
			}
		}
		
		return new String(chars);
	}
	
	public static String fillLeft(final String arg0, final String arg1,
			final int arg2) {
		final String value = arg0 != null ? arg0.trim() : "";
		String s = arg1;
		
		if (s == null) {
			s = " ";
		}
		
		final StringBuffer result = new StringBuffer();
		
		while (result.length() < arg2 - value.length()) {
			if (result.length() > 0 &&
					String.valueOf(result.charAt(0)).equals("-")) {
				result.append("0");
			} else {
				result.append(s);
			}
		}
		
		result.append(value);
		
		return result.length() == arg2 ? result.toString() : result.substring(
				0, arg2);
	}
	
	public static String fillRight(final String arg0, final String arg1,
			final int arg2) {
		String value = arg0 != null ? arg0.trim() : "";
		value = formatString(value);
		
		String s = arg1;
		
		if (s == null) {
			s = " ";
		}
		
		final StringBuffer result = new StringBuffer(value);
		
		while (result.length() < arg2) {
			result.append(s);
		}
		
		return result.length() == arg2 ? result.toString() : result.substring(
				0, arg2);
	}
	
	public static String formatString(final String arg0) {
		String result = arg0;
		
		if (result != null) {
			for (int i = 0; i < MASK.length(); i++) {
				result = result.replaceAll(String.valueOf(MASK.charAt(i)),
						String.valueOf(CORRECT.charAt(i)));
			}
			
			return result.replaceAll("[^\\p{ASCII}]", "");
		}
		
		return "";
	}
	
	public static String formatWebFile(String arg0) {
		if (arg0 == null) {
			return "";
		}
		
		arg0 = formatString(arg0);
		arg0 = arg0.replaceAll(" ", "_");
		arg0 = arg0.toLowerCase();
		return arg0;
	}
	
	public static String join(final List<String> collection,
			final String delimiter) {
		final StringBuilder sb = new StringBuilder();
		
		for (final Object item : collection) {
			if (item == null) {
				continue;
			}
			sb.append(item).append(delimiter);
		}
		
		sb.setLength(sb.length() - delimiter.length());
		
		return sb.toString();
	}
	
	public static String quote(final String arg0) {
		return "'".concat(arg0).concat("'");
	}
	
	public static String quoteDouble(final String arg0) {
		return (char) 34 + arg0 + (char) 34;
	}
	
	public static String retirarAlfaNumericos(final String arg0) {
		return arg0.replaceAll("[^0-9]", "");
	}
	
	public static Double toDouble(String arg0) {
		if (arg0 == null) {
			arg0 = "0.0";
		}
		
		arg0 = arg0.replaceAll("\\.", "");
		arg0 = arg0.replaceAll(",", ".");
		return Double.valueOf(arg0);
	}
	
	public static String trim(final String arg0) {
		return arg0 != null ? arg0.trim() : "";
	}
	
}

package common.utils
{
	import mx.formatters.NumberFormatter;
	
	public class NumberUtils
	{
		private static const formatter:NumberFormatter = new NumberFormatter();
		
		public static function formatNumber(value:Object, precision:int = 2):String
		{
			formatter.precision = precision;
			formatter.rounding = "none";
			formatter.decimalSeparatorFrom = ".";
			formatter.decimalSeparatorTo = ",";
			formatter.thousandsSeparatorFrom = ",";
			formatter.thousandsSeparatorTo = ".";
			formatter.useThousandsSeparator = true;
			return formatter.format(value);
		}
	}
}
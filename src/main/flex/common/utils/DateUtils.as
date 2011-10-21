package common.utils
{
	import mx.formatters.DateFormatter;
	import mx.utils.ObjectUtil;
	
	public class DateUtils
	{
		
		public static function get today():Date
		{
			var today:Date = new Date();
			today.setHours(0, 0, 0, 0);
			return today;
		}
		
		private static const formatter:DateFormatter = new DateFormatter();
		
		private static const millisecondsPerDay:int = 1000 * 60 * 60 * 24;
		
		public static function add(date:Date, days:int):Date
		{
			var result:Date = new Date(date.getTime());
			result.date += days;
			return result;
		}
		
		public static function compare(startDate:Date, endDate:Date):int
		{
			var _startDate:Date = ObjectUtil.copy(startDate) as Date;
			_startDate.setHours(0, 0, 0, 0);
			
			var _endDate:Date = ObjectUtil.copy(endDate) as Date;
			_endDate.setHours(0, 0, 0, 0);
			return Math.round(((_startDate.getTime() - _endDate.getTime()) / millisecondsPerDay));
		}
		
		public static function compareToday(startDate:Date):int
		{
			var _startDate:Date = ObjectUtil.copy(startDate) as Date;
			_startDate.setHours(0, 0, 0, 0);
			
			return compare(_startDate, today);
		}
		
		public static function firstDayOfCurrentMonth():Date
		{
			var _today:Date = today;
			return new Date(_today.fullYear, _today.month, 1);
		}
		
		public static function firstDayOfMonth(month:Number, year:Number):Date
		{
			return new Date(year, month, 1);
		}
		
		public static function formatSimple(date:Date):String
		{
			if (!date)
			{
				return null;
			}
			
			var _date:Date = ObjectUtil.copy(date) as Date;
			
			var compare:int = compareToday(_date);
			
			if (compare == 0)
			{
				return "Hoje";
			}
			else if (compare == -1)
			{
				return "Ontem";
			}
			else if (compare == 1)
			{
				return "Amanhã";
			}
			else
			{
				formatter.formatString = "EEE DD/MM";
				return formatter.format(_date);
			}
		}
		
		public static function lastDayOfCurrentMonth():Date
		{
			var _today:Date = today;
			return new Date(_today.fullYear, _today.month + 1, 0);
		}
		
		public static function lastDayOfMonth(month:Number, year:Number):Date
		{
			return new Date(year, month + 1, 0);
		}
		
		public static function remove(date:Date, days:int):Date
		{
			var result:Date = new Date(date.getTime());
			result.date -= days;
			return result;
		}
	}
}

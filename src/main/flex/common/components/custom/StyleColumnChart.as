package common.components.custom
{
	import mx.graphics.GradientEntry;
	import mx.graphics.LinearGradient;
	import mx.graphics.SolidColorStroke;
	import mx.graphics.Stroke;
	
	[Bindable]
	public class StyleColumnChart
	{
		public static function get blueLinearGradient():LinearGradient
		{
			return linearGradientColor(0x0066FF, 0x00CCFF);
		}
		
		public static function get blueStroke():SolidColorStroke
		{
			return strokeColor(0x0066FF);
		}
		
		public static function get greenLinearGradient():LinearGradient
		{
			return linearGradientColor(0x008B00, 0x00FF00);
		}
		
		public static function get greenStroke():SolidColorStroke
		{
			return strokeColor(0x008B00);
		}
		
		public static function get orangeLinearGradient():LinearGradient
		{
			return linearGradientColor(0xFC9C00, 0xFCC048);
		}
		
		public static function get orangeStroke():SolidColorStroke
		{
			return strokeColor(0xFC9C00);
		}
		
		public static function get redLinearGradient():LinearGradient
		{
			return linearGradientColor(0x990000, 0xEE0000);
		}
		
		public static function get redStroke():SolidColorStroke
		{
			return strokeColor(0x990000);
		}
		
		public static function get yellowLinearGradient():LinearGradient
		{
			return linearGradientColor(0xCAC333, 0xF5F30E);
		}
		
		public static function get yellowStroke():SolidColorStroke
		{
			return strokeColor(0xCAC333);
		}
		
		public static function linearGradientColor(colorDark:uint, colorLight:uint):LinearGradient
		{
			var linearGradient:LinearGradient = new LinearGradient();
			var arr:Array = [];
			arr.push(new GradientEntry(colorDark, 0.0, 0.8));
			arr.push(new GradientEntry(colorLight, 1.0, 0.6));
			linearGradient.entries = arr;
			return linearGradient;
		}
		
		public static function strokeColor(color:uint):SolidColorStroke
		{
			return new Stroke(color, 1);
		}
		
		public function StyleColumnChart()
		{
		}
	}
}
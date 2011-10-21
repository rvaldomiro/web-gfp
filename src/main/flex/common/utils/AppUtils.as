package common.utils
{
	import flash.external.ExternalInterface;
	
	public class AppUtils
	{
		public static function urlParams():Object
		{
			var result:Object = new Object;
			var uparam:String = ExternalInterface.call("window.location.search.toString");
			
			if (uparam)
			{
				result = new Object;
				
				var paramArray:Array = uparam.substr(1).split('&');
				
				for (var i:int = 0; i < paramArray.length; i++)
				{
					var splitArray:Array = paramArray[i].split('=');
					var name:String = splitArray[0];
					var value:String = splitArray[1];
					result[name] = value;
				}
			}
			
			return result;
		}
	}
}
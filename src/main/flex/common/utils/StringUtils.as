package common.utils
{
	import mx.collections.ArrayCollection;
	
	public class StringUtils
	{
		
		private static const subs:ArrayCollection = new ArrayCollection([{from: "√°"
																			 , to: "á"}
																		 , {from: "√≠"
																			 , to: "í"}]);
		
		public static function decode(value:String):String
		{
			var result:String = value;
			
			for (var i:int = 0; i < subs.length; i++)
			{
				if (result.search(subs[i].from) >= 0)
				{
					result = value.split(subs[i].from).join(subs[i].to);
				}
			}
			
			return result;
		}
	}
}
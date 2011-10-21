package common.components.imagePlayer
{
	import flash.events.Event;
	import flash.geom.Rectangle;
	
	public class GIFPlayerEvent extends Event
	{
		
		public static const COMPLETE:String = "GIFPlayerEvent_Complete";
		
		public function GIFPlayerEvent(pType:String, pRect:Rectangle)
		{
			super(pType, false, false);
			rect = pRect;
		}
		
		public var rect:Rectangle;
	}
}
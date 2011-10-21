package common.components.imagePlayer
{
	import flash.events.Event;
	
	public class TimeoutEvent extends Event
	{
		public static const TIME_OUT:String = "TimeoutEvent_Timeout";
		
		public function TimeoutEvent(pType:String)
		{
			super(pType, false, false);
		}
	}
}
package common.components.imagePlayer
{
	import flash.events.Event;
	
	public class FileTypeEvent extends Event
	{
		public static const INVALID:String = "FileTypeEvent_Invalid";
		
		public function FileTypeEvent(pType:String)
		{
			super(pType, false, false);
		}
	}
}
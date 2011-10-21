package common.components.imagePlayer
{
	import flash.events.Event;
	
	public class FrameEvent extends Event
	{
		
		public static const FRAME_RENDERED:String = "FrameEvent_Rendered";
		
		public function FrameEvent(pType:String, pFrame:GIFFrame)
		{
			super(pType, false, false);
			
			frame = pFrame;
		}
		
		public var frame:GIFFrame;
	}
}
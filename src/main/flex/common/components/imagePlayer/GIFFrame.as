package common.components.imagePlayer
{
	import flash.display.BitmapData;
	
	public class GIFFrame
	{
		
		public function GIFFrame(pImage:BitmapData, pDelay:int)
		{
			bitmapData = pImage;
			delay = pDelay;
		}
		
		public var bitmapData:BitmapData;
		
		public var delay:int;
	}
}
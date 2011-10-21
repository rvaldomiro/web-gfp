package common.components.imagePlayer
{
	import flash.display.Bitmap;
	import flash.errors.ScriptTimeoutError;
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.events.TimerEvent;
	import flash.net.URLLoader;
	import flash.net.URLLoaderDataFormat;
	import flash.net.URLRequest;
	import flash.utils.ByteArray;
	import flash.utils.Timer;
	
	public class GIFPlayer extends Bitmap
	{
		
		public function GIFPlayer(pAutoPlay:Boolean = true)
		{
			auto = pAutoPlay;
			iIndex = iInc = 0;
			
			myTimer = new Timer(0, 0);
			aFrames = new Array();
			urlLoader = new URLLoader();
			urlLoader.dataFormat = URLLoaderDataFormat.BINARY;
			
			urlLoader.addEventListener(Event.COMPLETE, onComplete);
			urlLoader.addEventListener(IOErrorEvent.IO_ERROR, onIOError);
			
			myTimer.addEventListener(TimerEvent.TIMER, update);
			
			gifDecoder = new GIFDecoder();
		}
		
		private var aFrames:Array;
		
		private var arrayLng:uint;
		
		private var auto:Boolean;
		
		private var gifDecoder:GIFDecoder
		
		private var iInc:int;
		
		private var iIndex:int;
		
		private var myTimer:Timer;
		
		private var urlLoader:URLLoader;
		
		public function get autoPlay():Boolean
		{
			return auto;
		}
		
		public function get currentFrame():int
		{
			return iIndex + 1;
		}
		
		public function dispose():void
		{
			stop();
			var lng:int = aFrames.length;
			
			for (var i:int = 0; i < lng; i++)
				aFrames[int(i)].bitmapData.dispose();
		}
		
		public function get frames():Array
		{
			return aFrames;
		}
		
		public function getDelay(pFrame:int):int
		{
			var delay:int;
			
			if (pFrame >= 1 && pFrame <= aFrames.length)
				delay = aFrames[pFrame - 1].delay;
			
			else
				throw new RangeError("Frame out of range, please specify a frame between 1 and "
									 + aFrames.length);
			
			return delay;
		}
		
		public function getFrame(pFrame:int):GIFFrame
		{
			var frame:GIFFrame;
			
			if (pFrame >= 1 && pFrame <= aFrames.length)
				frame = aFrames[pFrame - 1];
			
			else
				throw new RangeError("Frame out of range, please specify a frame between 1 and "
									 + aFrames.length);
			
			return frame;
		}
		
		public function gotoAndPlay(pFrame:int):void
		{
			if (pFrame >= 1 && pFrame <= aFrames.length)
			{
				if (pFrame == currentFrame)
					return;
				iIndex = iInc = int(int(pFrame) - 1);
				
				switch (gifDecoder.disposeValue)
				{
					case 1:
						bitmapData = aFrames[0].bitmapData.clone();
						bitmapData.draw(aFrames[concat(iInc)].bitmapData);
						break
					case 2:
						bitmapData = aFrames[iInc].bitmapData;
						break;
				}
				
				if (!myTimer.running)
					myTimer.start();
				
			}
			else
				throw new RangeError("Frame out of range, please specify a frame between 1 and "
									 + aFrames.length);
		}
		
		public function gotoAndStop(pFrame:int):void
		{
			if (pFrame >= 1 && pFrame <= aFrames.length)
			{
				if (pFrame == currentFrame)
					return;
				iIndex = iInc = int(int(pFrame) - 1);
				
				switch (gifDecoder.disposeValue)
				{
					case 1:
						bitmapData = aFrames[0].bitmapData.clone();
						bitmapData.draw(aFrames[concat(iInc)].bitmapData);
						break
					case 2:
						bitmapData = aFrames[iInc].bitmapData;
						break;
				}
				
				if (myTimer.running)
					myTimer.stop();
				
			}
			else
				throw new RangeError("Frame out of range, please specify a frame between 1 and "
									 + aFrames.length);
		}
		
		public function load(pRequest:URLRequest):void
		{
			stop();
			urlLoader.load(pRequest);
		}
		
		public function loadBytes(pBytes:ByteArray):void
		{
			readStream(pBytes);
		}
		
		public function get loopCount():int
		{
			return gifDecoder.getLoopCount();
		}
		
		public function play():void
		{
			if (aFrames.length > 0)
			{
				if (!myTimer.running)
					myTimer.start();
				
			}
			else
				throw new Error("Nothing to play");
		}
		
		public function stop():void
		{
			if (myTimer.running)
				myTimer.stop();
		}
		
		public function get totalFrames():int
		{
			return aFrames.length;
		}
		
		private function concat(pIndex:int):int
		{
			bitmapData.lock();
			
			for (var i:int = 0; i < pIndex; i++)
				bitmapData.draw(aFrames[i].bitmapData);
			bitmapData.unlock();
			
			return i;
		}
		
		private function onComplete(pEvt:Event):void
		{
			readStream(pEvt.target.data);
		}
		
		private function onIOError(pEvt:IOErrorEvent):void
		{
			dispatchEvent(pEvt);
		}
		
		private function readStream(pBytes:ByteArray):void
		{
			var gifStream:ByteArray = pBytes;
			
			aFrames = new Array;
			iInc = 0;
			
			try
			{
				gifDecoder.read(gifStream);
				
				var lng:int = gifDecoder.getFrameCount();
				
				for (var i:int = 0; i < lng; i++)
					aFrames[int(i)] = gifDecoder.getFrame(i);
				
				arrayLng = aFrames.length;
				
				auto ? play() : gotoAndStop(1);
				
				dispatchEvent(new GIFPlayerEvent(GIFPlayerEvent.COMPLETE, aFrames[0]
												 .bitmapData.rect));
				
			}
			catch (e:ScriptTimeoutError)
			{
				dispatchEvent(new TimeoutEvent(TimeoutEvent.TIME_OUT));
				
			}
			catch (e:FileTypeError)
			{
				dispatchEvent(new FileTypeEvent(FileTypeEvent.INVALID));
				
			}
			catch (e:Error)
			{
				throw new Error("An unknown error occured, make sure the GIF file contains at least one frame\nNumber of frames : "
								+ aFrames.length);
			}
		
		}
		
		private function update(pEvt:TimerEvent):void
		{
			var delay:int = aFrames[int(iIndex = iInc++ % arrayLng)].delay;
			
			pEvt.target.delay = (delay > 0) ? delay : 100;
			
			switch (gifDecoder.disposeValue)
			{
				case 1:
					if (!iIndex)
						bitmapData = aFrames[0].bitmapData.clone();
					bitmapData.draw(aFrames[iIndex].bitmapData);
					break
				case 2:
					bitmapData = aFrames[iIndex].bitmapData;
					break;
			}
			
			dispatchEvent(new FrameEvent(FrameEvent.FRAME_RENDERED, aFrames[iIndex]));
		}
	}
}
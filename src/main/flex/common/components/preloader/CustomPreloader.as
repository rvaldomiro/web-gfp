package common.components.preloader
{
	import common.components.com.hulstkamp.flex.spark.managers.NiceToolTipManagerImpl;
	
	import flash.display.Loader;
	import flash.display.Sprite;
	import flash.events.Event;
	import flash.events.ProgressEvent;
	import flash.net.URLRequest;
	
	import mx.core.mx_internal;
	import mx.events.FlexEvent;
	import mx.preloaders.DownloadProgressBar;
	import mx.preloaders.IPreloaderDisplay;
	import mx.rpc.AbstractInvoker;
	
	use namespace mx_internal;
	
	public class CustomPreloader extends DownloadProgressBar implements IPreloaderDisplay
	{
		
		public function CustomPreloader()
		{
			super();
			
			if (0)
			{
				var a:AbstractInvoker = null;
			}
			
			registerCustomClasses();
		}
		
		override public function set preloader(preloader:Sprite):void
		{
			preloader.addEventListener(ProgressEvent.PROGRESS, handleProgress);
			preloader.addEventListener(Event.COMPLETE, handleComplete);
			preloader.addEventListener(FlexEvent.INIT_PROGRESS, handleInitProgress);
			preloader.addEventListener(FlexEvent.INIT_COMPLETE, handleInitComplete);
		}
		
		private var dpbImageControl:Loader;
		
		override public function initialize():void
		{
			dpbImageControl = new Loader();
			dpbImageControl.contentLoaderInfo.addEventListener(Event.COMPLETE, loader_completeHandler);
			dpbImageControl.load(new URLRequest("common/components/preloader/LoadingUndefinedSkin.swf"));
		}
		
		protected function draw():void
		{
		}
		
		private function handleComplete(event:Event):void
		{
		}
		
		private function handleInitComplete(event:Event):void
		{
			dispatchEvent(new Event(Event.COMPLETE));
		}
		
		private function handleInitProgress(event:Event):void
		{
		}
		
		private function handleProgress(event:ProgressEvent):void
		{
			var relacion:Number = event.bytesLoaded / event.bytesTotal;
			
			if (dpbImageControl.content)
			{
				dpbImageControl.content["barra"].scaleX = relacion;
				dpbImageControl.content["porcentaje"].text = relacion * 100;
			}
		}
		
		private function loader_completeHandler(event:Event):void
		{
			dpbImageControl.content["barra"].scaleX = 0;
			addChild(dpbImageControl);
			
			this.x = this.stage.stageWidth / 2 - dpbImageControl.contentLoaderInfo
				.width / 2;
			this.y = this.stage.stageHeight / 2 - dpbImageControl.contentLoaderInfo
				.height / 2;
		}
		
		private function registerCustomClasses():void
		{
			mx.core.Singleton.registerClass("mx.managers::IToolTipManager2", NiceToolTipManagerImpl);
		}
	}
}
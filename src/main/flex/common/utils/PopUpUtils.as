package common.utils
{
	import flash.display.DisplayObject;
	import flash.events.IEventDispatcher;
	
	import mx.core.IFlexDisplayObject;
	import mx.managers.PopUpManager;
	
	public class PopUpUtils
	{
		
		public static function createAndCenter(parent:DisplayObject, className:Class
											   , dispatcher:IEventDispatcher = null):void
		{
			var display:IFlexDisplayObject = PopUpManager.createPopUp(parent, className
																	  , true);
			
			if (dispatcher)
			{
				(display as className).dispatcher = dispatcher;
			}
			
			PopUpManager.centerPopUp(display);
		}
	}
}
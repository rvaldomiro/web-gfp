package common.components.service
{
	import common.custom.ICustomEvent;
	import common.utils.MessageUtils;
	import flash.events.IEventDispatcher;
	import mx.collections.ArrayCollection;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import org.swizframework.utils.services.ServiceHelper;
	
	public class AbstractService
	{
		
		[Dispatcher]
		public var dispatcher:IEventDispatcher;
		
		[Bindable]
		public var lista:ArrayCollection;
		
		[Inject]
		public var serviceHelper:ServiceHelper;
		
		protected function executeService(call:AsyncToken, event:ICustomEvent, result:Function
										  , fault:Function = null):void
		{
			serviceHelper.executeServiceCall(call, function(re:ResultEvent):void
			{
				if (result != null)
				{
					result(re);
				}
			}, function(fe:FaultEvent):void
			{
				if (fault != null || event.fault != null)
				{
					fault != null ? fault(fe) : event.fault(fe);
				}
				else
				{
					MessageUtils.error(fe);
				}
			});
		}
	}
}
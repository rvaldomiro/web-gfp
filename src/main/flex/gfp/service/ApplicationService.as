package gfp.service
{
	import common.component.service.AbstractService;
	import common.component.service.IService;
	import common.custom.CustomRemoteObject;
	import common.custom.ICustomEvent;
	import common.util.RemoteUtil;
	
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	public class ApplicationService extends AbstractService
	{
		
		[Bindable]
		public var versaoAtual:String;
		
		private function get service():RemoteObject
		{
			return RemoteUtil.createRemoteObject("applicationService") as RemoteObject;
		}
		
		[EventHandler(event = "AppEvent.OBTER_VERSAO_ATUAL", properties = "event")]
		public function obterVersaoAtual(event:ICustomEvent):void
		{
			executeService(service.obterVersaoAtual(), function(re:ResultEvent):void
			{
				versaoAtual = re.result as String;
				event.result(re);
			});
		}
	}
}

package gfp.service
{
	import common.components.service.AbstractService;
	import common.components.service.IService;
	import common.custom.CustomRemoteObject;
	import common.custom.ICustomEvent;
	
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	public class ApplicationService extends AbstractService implements IService
	{
		
		public function get service():RemoteObject
		{
			return new CustomRemoteObject("applicationService");
		}
		
		[Bindable]
		public var versaoAtual:String;
		
		[EventHandler(event="AppEvent.OBTER_VERSAO_ATUAL")]
		public function obterVersaoAtual(event:ICustomEvent):void
		{
			executeService(service.obterVersaoAtual(), event, function(re:ResultEvent):void
			{
				versaoAtual = re.result as String;
				event.result(re);
			});
		}
	}
}
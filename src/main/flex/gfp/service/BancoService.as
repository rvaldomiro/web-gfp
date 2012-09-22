package gfp.service
{
	import common.component.service.AbstractService;
	import common.component.service.IService;
	import common.custom.CustomRemoteObject;
	import common.custom.ICustomEvent;
	import common.util.RemoteUtil;
	import common.util.SortUtil;
	
	import mx.collections.ArrayCollection;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	public class BancoService extends AbstractService
	{
		
		private function get service():RemoteObject
		{
			return RemoteUtil.createRemoteObject("bancoService") as RemoteObject;
		}
		
		[EventHandler(event = "CategoriaEvent.LISTAR", properties = "event")]
		public function listar(event:ICustomEvent):void
		{
			executeService(service.listarBancos(), function(re:ResultEvent):void
			{
				list = re.result as ArrayCollection;
				SortUtil.sortText(list, "nome");
			});
		}
	}
}


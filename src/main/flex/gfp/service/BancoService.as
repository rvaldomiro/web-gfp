package gfp.service
{
	import common.components.service.AbstractService;
	import common.components.service.IService;
	import common.custom.CustomRemoteObject;
	import common.custom.ICustomEvent;
	import common.utils.SortUtils;
	
	import mx.collections.ArrayCollection;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	public class BancoService extends AbstractService implements IService
	{
		
		public function get service():RemoteObject
		{
			return new CustomRemoteObject("bancoService");
		}
		
		[EventHandler(event="CategoriaEvent.LISTAR")]
		public function listar(event:ICustomEvent):void
		{
			executeService(service.listarBancos(), event, function(re:ResultEvent):void
			{
				lista = re.result as ArrayCollection;
				SortUtils.sortText(lista, "nome");
			});
		}
	}
}
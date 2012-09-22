package gfp.service
{
	import common.component.service.AbstractService;
	import common.component.service.IService;
	import common.custom.CustomRemoteObject;
	import common.custom.ICustomEvent;
	import common.util.RemoteUtil;
	import common.util.SortUtil;
	
	import gfp.model.Conta;
	
	import mx.collections.ArrayCollection;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	import mx.utils.ObjectUtil;
	
	public class ContaService extends AbstractService
	{
		
		[Bindable]
		public var listaCompleta:ArrayCollection;
		
		[Bindable]
		public var selecionada:Conta;
		
		[Inject]
		public var usuarioService:UsuarioService;
		
		private function get service():RemoteObject
		{
			return RemoteUtil.createRemoteObject("contaService") as RemoteObject;
		}
		
		[EventHandler(event = "ContaEvent.EXCLUIR", properties = "event")]
		public function excluir(event:ICustomEvent):void
		{
			executeService(service.excluir(event.object), function(re:ResultEvent):void
			{
				event.result(re);
			});
		}
		
		[EventHandler(event = "ContaEvent.LISTAR", properties = "event")]
		public function listar(event:ICustomEvent):void
		{
			executeService(service.listarContasAtivas(usuarioService.idUsuarioLogado)
						   , function(re:ResultEvent):void
						   {
							   list = re.result as ArrayCollection;
							   SortUtil.sortText(list, "identificacao");
						   });
			
			executeService(service.listarContas(usuarioService.idUsuarioLogado), function(re:ResultEvent):void
			{
				listaCompleta = re.result as ArrayCollection;
				SortUtil.sortText(listaCompleta, "identificacao");
			});
		}
		
		[EventHandler(event = "ContaEvent.EDITAR", properties = "event")]
		public function prepararParaEdicao(event:ICustomEvent):void
		{
			selecionada = event.object ? ObjectUtil.copy(event.object) as Conta :
				new Conta();
			selecionada.usuario ||= usuarioService.usuarioLogado;
		}
		
		[EventHandler(event = "ContaEvent.SALVAR", properties = "event")]
		public function salvar(event:ICustomEvent):void
		{
			executeService(service.salvarConta(selecionada), function(re:ResultEvent):void
			{
				event.result(re);
			});
		}
	}
}


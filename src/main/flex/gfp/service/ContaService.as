package gfp.service
{
	import common.components.service.AbstractService;
	import common.components.service.IService;
	import common.custom.CustomRemoteObject;
	import common.custom.ICustomEvent;
	import common.utils.SortUtils;
	
	import gfp.model.Conta;
	
	import mx.collections.ArrayCollection;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	import mx.utils.ObjectUtil;
	
	public class ContaService extends AbstractService implements IService
	{
		
		[Bindable]
		public var listaCompleta:ArrayCollection;
		
		[Bindable]
		public var selecionada:Conta;
		
		public function get service():RemoteObject
		{
			return new CustomRemoteObject("contaService");
		}
		
		[Inject]
		public var usuarioService:UsuarioService;
		
		[EventHandler(event="ContaEvent.EXCLUIR")]
		public function excluir(event:ICustomEvent):void
		{
			executeService(service.excluir(event.object), event, function(re:ResultEvent):void
			{
				event.result(re);
			});
		}
		
		[EventHandler(event="ContaEvent.LISTAR")]
		public function listar(event:ICustomEvent):void
		{
			executeService(service.listarContasAtivas(usuarioService.idUsuarioLogado)
						   , event, function(re:ResultEvent):void
			{
				lista = re.result as ArrayCollection;
				SortUtils.sortText(lista, "identificacao");
			});
			
			executeService(service.listarContas(usuarioService.idUsuarioLogado), event
						   , function(re:ResultEvent):void
			{
				listaCompleta = re.result as ArrayCollection;
				SortUtils.sortText(listaCompleta, "identificacao");
			});
		}
		
		[EventHandler(event="ContaEvent.EDITAR")]
		public function prepararParaEdicao(event:ICustomEvent):void
		{
			selecionada = event.object ? ObjectUtil.copy(event.object) as Conta : new Conta();
			selecionada.usuario ||= usuarioService.usuarioLogado;
		}
		
		[EventHandler(event="ContaEvent.SALVAR")]
		public function salvar(event:ICustomEvent):void
		{
			executeService(service.salvarConta(selecionada), event, function(re:ResultEvent):void
			{
				event.result(re);
			});
		}
	}
}
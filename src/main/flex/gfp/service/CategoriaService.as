package gfp.service
{
	import common.component.service.AbstractService;
	import common.component.service.IService;
	import common.custom.CustomRemoteObject;
	import common.custom.ICustomEvent;
	import common.util.RemoteUtil;
	import common.util.SortUtil;
	
	import gfp.model.Categoria;
	
	import mx.collections.ArrayCollection;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	import mx.utils.ObjectUtil;
	
	public class CategoriaService extends AbstractService
	{
		
		[Bindable]
		public var selecionada:Categoria;
		
		[Inject]
		public var usuarioService:UsuarioService;
		
		private function get service():RemoteObject
		{
			return RemoteUtil.createRemoteObject("categoriaService") as RemoteObject;
		}
		
		[EventHandler(event = "CategoriaEvent.EXCLUIR", properties = "event")]
		public function excluir(event:ICustomEvent):void
		{
			executeService(service.excluir(event.object), function(re:ResultEvent):void
			{
				event.result(re);
			});
		}
		
		[EventHandler(event = "CategoriaEvent.LISTAR", properties = "event")]
		public function listar(event:ICustomEvent):void
		{
			executeService(service.listarCategorias(usuarioService.idUsuarioLogado)
						   , function(re:ResultEvent):void
						   {
							   list = re.result as ArrayCollection;
							   list.filterFunction = listaFilter;
							   SortUtil.sortText(list, "descricao");
						   });
		}
		
		[EventHandler(event = "CategoriaEvent.EDITAR", properties = "event")]
		public function prepararParaEdicao(event:ICustomEvent):void
		{
			selecionada = event.object ? ObjectUtil.copy(event.object) as Categoria :
				new Categoria();
			selecionada.usuario ||= usuarioService.usuarioLogado;
		}
		
		[EventHandler(event = "CategoriaEvent.SALVAR", properties = "event")]
		public function salvar(event:ICustomEvent):void
		{
			executeService(service.salvarCategoria(event.object), event.result);
		}
		
		private function listaFilter(categoria:Categoria):Boolean
		{
			return !categoria.interna;
		}
	}
}


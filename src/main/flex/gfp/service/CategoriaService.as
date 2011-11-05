package gfp.service
{
	import common.components.service.AbstractService;
	import common.components.service.IService;
	import common.custom.CustomRemoteObject;
	import common.custom.ICustomEvent;
	import common.utils.SortUtils;
	
	import gfp.model.Categoria;
	
	import mx.collections.ArrayCollection;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	import mx.utils.ObjectUtil;
	
	public class CategoriaService extends AbstractService implements IService
	{
		
		[Bindable]
		public var selecionada:Categoria;
		
		public function get service():RemoteObject
		{
			return new CustomRemoteObject("categoriaService");
		}
		
		[Inject]
		public var usuarioService:UsuarioService;
		
		[EventHandler(event = "CategoriaEvent.EXCLUIR")]
		public function excluir(event:ICustomEvent):void
		{
			executeService(service.excluir(event.object), event, function(re:ResultEvent):void
			{
				event.result(re);
			});
		}
		
		[EventHandler(event = "CategoriaEvent.LISTAR")]
		public function listar(event:ICustomEvent):void
		{
			executeService(service.listarCategorias(usuarioService.idUsuarioLogado)
						   , event, function(re:ResultEvent):void
						   {
							   lista = re.result as ArrayCollection;
							   lista.filterFunction = listaFilter;
							   SortUtils.sortText(lista, "descricao");
						   });
		}
		
		[EventHandler(event = "CategoriaEvent.EDITAR")]
		public function prepararParaEdicao(event:ICustomEvent):void
		{
			selecionada = event.object ? ObjectUtil.copy(event.object) as Categoria :
				new Categoria();
			selecionada.usuario ||= usuarioService.usuarioLogado;
		}
		
		[EventHandler(event = "CategoriaEvent.SALVAR")]
		public function salvar(event:ICustomEvent):void
		{
			executeService(service.salvarCategoria(event.object), event, event.result);
		}
		
		private function listaFilter(categoria:Categoria):Boolean
		{
			return !categoria.interna;
		}
	}
}

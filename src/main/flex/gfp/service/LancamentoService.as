package gfp.service
{
	import common.components.service.AbstractService;
	import common.components.service.IService;
	import common.custom.CustomRemoteObject;
	import common.custom.ICustomEvent;
	import common.utils.SortUtils;
	
	import gfp.dto.LancamentoDto;
	import gfp.factory.LancamentoFactory;
	import gfp.model.Lancamento;
	import gfp.type.CategoriaType;
	
	import mx.collections.ArrayCollection;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	import mx.utils.ObjectUtil;
	
	public class LancamentoService extends AbstractService implements IService
	{
		
		[Bindable]
		public var listaDespesaMensal:ArrayCollection;
		
		[Bindable]
		public var listaDespesasVencer:ArrayCollection;
		
		[Bindable]
		public var listaPrevisaoSaldoDiario:ArrayCollection;
		
		[Bindable]
		public var listaReceitaMensal:ArrayCollection;
		
		[Bindable]
		public var listaReceitasVencer:ArrayCollection;
		
		[Bindable]
		public var listaSaldoAtual:ArrayCollection;
		
		[Bindable]
		public var selecionado:Lancamento;
		
		public function get service():RemoteObject
		{
			return new CustomRemoteObject("lancamentoService");
		}
		
		[Inject]
		public var usuarioService:UsuarioService;
		
		[EventHandler(event = "TransacaoEvent.AGENDAR")]
		public function agendar(event:ICustomEvent):void
		{
			executeService(service.agendarLancamentos(event.object), event, function(re:ResultEvent):void
			{
				event.result(re);
			});
		}
		
		[EventHandler(event = "TransacaoEvent.EXCLUIR")]
		public function excluir(event:ICustomEvent):void
		{
			executeService(service.excluir(event.object), event, function(re:ResultEvent):void
			{
				event.result(re);
			});
		}
		
		[EventHandler(event = "TransacaoEvent.LISTAR")]
		public function listar(event:ICustomEvent):void
		{
			var dto:LancamentoDto = event.object as LancamentoDto;
			dto.idUsuario = usuarioService.idUsuarioLogado;
			
			executeService(service.listarLancamentos(event.object), event, function(re:ResultEvent):void
			{
				lista = re.result as ArrayCollection;
				SortUtils.sortDateArray(lista, ["dataPrevisaoPagamento", "dataVencimento"]);
				lista.refresh();
				event.result(re);
			});
		}
		
		[EventHandler(event = "TransacaoEvent.LISTAR_DESPESA_MENSAL")]
		public function listarDespesaMensal(event:ICustomEvent):void
		{
			executeService(service.listarSaldoCategoriaMensal(usuarioService.idUsuarioLogado
															  , (event.object as
															  Date).month + 1, (event
															  .object as Date).fullYear
															  , CategoriaType.DESPESA)
						   , event, function(re:ResultEvent):void
						   {
							   listaDespesaMensal = re.result as ArrayCollection;
							   SortUtils.sortValue(listaDespesaMensal, "valor");
							   event.result(re);
						   });
		}
		
		[EventHandler(event = "TransacaoEvent.LISTAR_DESPESAS_VENCER")]
		public function listarDespesasVencer(event:ICustomEvent):void
		{
			executeService(service.listarDespesasVencer(usuarioService.idUsuarioLogado)
						   , event, function(re:ResultEvent):void
						   {
							   listaDespesasVencer = re.result as ArrayCollection;
							   SortUtils.sortDateArray(listaDespesasVencer, ["dataPrevisaoPagamento"
																			 , "dataVencimento"]);
							   event.result(re);
						   });
		}
		
		[EventHandler(event = "TransacaoEvent.LISTAR_PREVISAO_SALDO_DIARIO")]
		public function listarPrevisaoSaldoDiario(event:ICustomEvent):void
		{
			executeService(service.listarPrevisaoSaldoDiario(usuarioService.idUsuarioLogado)
						   , event, function(re:ResultEvent):void
						   {
							   listaPrevisaoSaldoDiario = re.result as ArrayCollection;
						   });
		}
		
		[EventHandler(event = "TransacaoEvent.LISTAR_RECEITA_MENSAL")]
		public function listarReceitaMensal(event:ICustomEvent):void
		{
			executeService(service.listarSaldoCategoriaMensal(usuarioService.idUsuarioLogado
															  , (event.object as
															  Date).month + 1, (event
															  .object as Date).fullYear
															  , CategoriaType.RECEITA)
						   , event, function(re:ResultEvent):void
						   {
							   listaReceitaMensal = re.result as ArrayCollection;
							   SortUtils.sortValue(listaReceitaMensal, "valor");
							   event.result(re);
						   });
		}
		
		[EventHandler(event = "TransacaoEvent.LISTAR_RECEITAS_VENCER")]
		public function listarReceitasVencer(event:ICustomEvent):void
		{
			executeService(service.listarReceitasVencer(usuarioService.idUsuarioLogado)
						   , event, function(re:ResultEvent):void
						   {
							   listaReceitasVencer = re.result as ArrayCollection;
							   SortUtils.sortDateArray(listaReceitasVencer, ["dataPrevisaoPagamento"
																			 , "dataVencimento"]);
							   event.result(re);
						   });
		}
		
		[EventHandler(event = "TransacaoEvent.LISTAR_SALDO_POR_CONTA")]
		public function listarSaldoPorConta(event:ICustomEvent):void
		{
			executeService(service.listarSaldoPorConta(usuarioService.idUsuarioLogado)
						   , event, function(re:ResultEvent):void
						   {
							   listaSaldoAtual = re.result as ArrayCollection;
						   });
		}
		
		[EventHandler(event = "TransacaoEvent.EDITAR")]
		public function prepararParaEdicao(event:ICustomEvent):void
		{
			selecionado = event.object ? ObjectUtil.copy(event.object) as Lancamento :
				LancamentoFactory.criar();
			selecionado.usuario ||= usuarioService.usuarioLogado;
		}
		
		[EventHandler(event = "TransacaoEvent.SALVAR")]
		public function salvar(event:ICustomEvent):void
		{
			executeService(service.salvarLancamento(selecionado), event, function(re:ResultEvent):void
			{
				event.result(re);
			});
		}
	}
}

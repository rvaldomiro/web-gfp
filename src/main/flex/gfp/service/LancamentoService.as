package gfp.service
{
	import common.component.service.AbstractService;
	import common.component.service.IService;
	import common.custom.CustomRemoteObject;
	import common.custom.ICustomEvent;
	import common.util.RemoteUtil;
	import common.util.SortUtil;
	
	import gfp.dto.LancamentoDto;
	import gfp.factory.LancamentoFactory;
	import gfp.model.Conta;
	import gfp.model.Lancamento;
	import gfp.type.CategoriaType;
	
	import mx.collections.ArrayCollection;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	import mx.utils.ObjectUtil;
	
	public class LancamentoService extends AbstractService
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
		
		[Inject]
		public var usuarioService:UsuarioService;
		
		private function get service():RemoteObject
		{
			return RemoteUtil.createRemoteObject("lancamentoService") as RemoteObject;
		}
		
		[EventHandler(event = "TransacaoEvent.AGENDAR", properties = "event")]
		public function agendar(event:ICustomEvent):void
		{
			executeService(service.agendarLancamentos(event.object), function(re:ResultEvent):void
			{
				event.result(re);
			});
		}
		
		[EventHandler(event = "TransacaoEvent.EXCLUIR", properties = "event")]
		public function excluir(event:ICustomEvent):void
		{
			executeService(service.excluir(event.object), function(re:ResultEvent):void
			{
				event.result(re);
			});
		}
		
		[EventHandler(event = "TransacaoEvent.LISTAR", properties = "event")]
		public function listar(event:ICustomEvent):void
		{
			var dto:LancamentoDto = event.object as LancamentoDto;
			dto.idUsuario = usuarioService.idUsuarioLogado;
			
			executeService(service.listarLancamentos(dto), function(re:ResultEvent):void
			{
				list = re.result as ArrayCollection;
//				SortUtil.sortDateArray(list, ["dataPrevisaoPagamento", "dataVencimento"]);
				SortUtil.sortDateArray(list, ["dataVencimento", "dataPrevisaoPagamento"
											  , "valorOriginal"]);
				list.refresh();
				event.result(re);
			});
		}
		
		[EventHandler(event = "TransacaoEvent.LISTAR_DESPESA_MENSAL", properties = "event")]
		public function listarDespesaMensal(event:ICustomEvent):void
		{
			executeService(service.listarSaldoCategoriaMensal(usuarioService.idUsuarioLogado
															  , (event.object as Date)
															  .month + 1, (event
															  .object as Date).fullYear
															  , CategoriaType.DESPESA)
						   , function(re:ResultEvent):void
						   {
							   listaDespesaMensal = re.result as ArrayCollection;
							   SortUtil.sortValue(listaDespesaMensal, "valor", true);
							   event.result(re);
						   });
		}
		
		[EventHandler(event = "TransacaoEvent.LISTAR_DESPESAS_VENCER", properties = "event")]
		public function listarDespesasVencer(event:ICustomEvent):void
		{
			executeService(service.listarDespesasVencer(usuarioService.idUsuarioLogado)
						   , function(re:ResultEvent):void
						   {
							   listaDespesasVencer = re.result as ArrayCollection;
							   SortUtil.sortDateArray(listaDespesasVencer, ["dataPrevisaoPagamento"
																			, "dataVencimento"]);
							   event.result(re);
						   });
		}
		
		[EventHandler(event = "TransacaoEvent.LISTAR_PREVISAO_SALDO_DIARIO", properties = "event")]
		public function listarPrevisaoSaldoDiario(event:ICustomEvent):void
		{
			executeService(service.listarPrevisaoSaldoDiario(usuarioService.idUsuarioLogado
															 , event.object as Number)
						   , function(re:ResultEvent):void
						   {
							   listaPrevisaoSaldoDiario = re.result as ArrayCollection;
							   event.result(re);
						   });
		}
		
		[EventHandler(event = "TransacaoEvent.LISTAR_RECEITA_MENSAL", properties = "event")]
		public function listarReceitaMensal(event:ICustomEvent):void
		{
			executeService(service.listarSaldoCategoriaMensal(usuarioService.idUsuarioLogado
															  , (event.object as Date)
															  .month + 1, (event
															  .object as Date).fullYear
															  , CategoriaType.RECEITA)
						   , function(re:ResultEvent):void
						   {
							   listaReceitaMensal = re.result as ArrayCollection;
							   SortUtil.sortValue(listaReceitaMensal, "valor", true);
							   event.result(re);
						   });
		}
		
		[EventHandler(event = "TransacaoEvent.LISTAR_RECEITAS_VENCER", properties = "event")]
		public function listarReceitasVencer(event:ICustomEvent):void
		{
			executeService(service.listarReceitasVencer(usuarioService.idUsuarioLogado)
						   , function(re:ResultEvent):void
						   {
							   listaReceitasVencer = re.result as ArrayCollection;
							   SortUtil.sortDateArray(listaReceitasVencer, ["dataPrevisaoPagamento"
																			, "dataVencimento"]);
							   event.result(re);
						   });
		}
		
		[EventHandler(event = "TransacaoEvent.LISTAR_SALDO_POR_CONTA", properties = "event")]
		public function listarSaldoPorConta(event:ICustomEvent):void
		{
			executeService(service.listarSaldoPorConta(usuarioService.idUsuarioLogado)
						   , function(re:ResultEvent):void
						   {
							   listaSaldoAtual = re.result as ArrayCollection;
							   listaSaldoAtual.filterFunction = listaSaldoAtualFilter;
							   listaSaldoAtual.refresh();
							   
							   var contaAnterior:Object;
							   
							   for each (var dto:Object in listaSaldoAtual)
							   {
								   if (!dto.conta)
								   {
									   continue;
								   }
								   
								   if (dto.conta.id != contaAnterior)
								   {
									   dto.contaMestre = dto.conta;
									   contaAnterior = dto.conta.id;
								   }
							   }
						   });
		}
		
		[EventHandler(event = "TransacaoEvent.OBTER_SALDO_REMANESCENTE", properties = "event")]
		public function obterSaldoRemanescente(event:ICustomEvent):void
		{
			executeService(service.obterSaldoRemanescente(selecionado), function(re:ResultEvent):void
			{
				event.result(re);
			});
		}
		
		[EventHandler(event = "TransacaoEvent.EDITAR", properties = "event")]
		public function prepararParaEdicao(event:ICustomEvent):void
		{
			selecionado = event.object ? ObjectUtil.copy(event.object) as Lancamento
				: LancamentoFactory.criar();
			selecionado.usuario ||= usuarioService.usuarioLogado;
		}
		
		[EventHandler(event = "TransacaoEvent.SALVAR", properties = "event")]
		public function salvar(event:ICustomEvent):void
		{
			executeService(service.salvarLancamento(selecionado), function(re:ResultEvent):void
			{
				event.result(re);
			});
		}
		
		private function listaSaldoAtualFilter(o:Object):Boolean
		{
			return Number(o.saldo) != 0.0 || o.situacao == "Saldo Atual" || (o.situacao
				== "Mastercard" && o.conta.operaCartaoMastercard) || (o.situacao
				== "Visa" && o.conta.operaCartaoVisa);
		}
	}
}


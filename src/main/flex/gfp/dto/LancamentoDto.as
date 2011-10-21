package gfp.dto
{
	import common.utils.DateUtils;
	
	import gfp.model.Categoria;
	import gfp.model.Conta;
	
	import mx.collections.ArrayCollection;
	
	[RemoteClass(alias = "gfp.dto.LancamentoDto")]
	[Bindable]
	public class LancamentoDto
	{
		
		public function LancamentoDto():void
		{
		}
		
		public var categoria:Object;
		
		public var conta:Conta;
		
		public var dataFinal:Date = DateUtils.lastDayOfCurrentMonth();
		
		public var dataInicio:Date = DateUtils.today;
		
		public var idUsuario:Number;
		
		public var observacao:String;
		
		public var situacao:int = 1;
		
		public var situacoes:ArrayCollection = new ArrayCollection([{label: 'Todos'}
																	, {label: 'Em Aberto'}
																	, {label: 'Pagos'}]);
		
		public var tipoPeriodo:int = 1;
		
		public var tiposPeriodo:ArrayCollection = new ArrayCollection([{label: 'Vencimento'}
																	   , {label: 'Previsão Pagamento'}
																	   , {label: 'Pagamento'}
																	   , {label: 'Compensação'}]);
	}
}

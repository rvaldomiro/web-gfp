package gfp.dto
{
	import common.util.DateUtil;
	
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
		
		public var dataFinal:Date = DateUtil.lastDayOfCurrentMonth();
		
		[Transient]
		public function get dataInicio():Date
		{
			return DateUtil.toDateTime(dataInicioString);
		}
		
		public function set dataInicio(value:Date):void
		{
			dataInicioString = DateUtil.toDateTimeString(value);
		}
		
//		public var dataInicio:Date = DateUtil.today;
		
		public var dataInicioString:String = DateUtil.toDateTimeString(DateUtil.today);
		
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


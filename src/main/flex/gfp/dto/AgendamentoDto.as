package gfp.dto
{
	import common.util.DateUtil;
	
	import gfp.model.Lancamento;
	
	import mx.collections.ArrayCollection;
	
	[RemoteClass(alias = "gfp.dto.AgendamentoDto")]
	[Bindable]
	public class AgendamentoDto
	{
		
		public static var frequencias:ArrayCollection = new ArrayCollection([{label: 'Mensal'}
																			 , {label: 'Dia Útil do Mês'}]);
		
		public function AgendamentoDto():void
		{
		}
		
		public var anteciparFinaisSemana:Boolean = false;
		
		[Transient]
		public function get dataFinal():Date
		{
			return DateUtil.toDateTime(dataFinalString);
		}
		
		public function set dataFinal(value:Date):void
		{
			dataFinalString = DateUtil.toDateTimeString(value);
		}
		
		public var dataFinalString:String = DateUtil.toDateTimeString(DateUtil.add(DateUtil
																				   .today
																				   , 30));
		
		[Transient]
		public function get dataInicio():Date
		{
			return DateUtil.toDateTime(dataInicioString);
		}
		
		public function set dataInicio(value:Date):void
		{
			dataInicioString = DateUtil.toDateTimeString(value);
		}
		
		public var dataInicioString:String = DateUtil.toDateTimeString(DateUtil.today);
		
		public var dia:int = 1;
		
		public var frequencia:int = 0;
		
		public var lancamento:Lancamento;
		
		public var valor:Number;
	}
}


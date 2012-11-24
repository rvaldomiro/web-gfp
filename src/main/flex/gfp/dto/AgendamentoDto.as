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
			return DateUtil.toDateTime(dataFinalUtc);
		}
		
		public function set dataFinal(value:Date):void
		{
			dataFinalUtc = DateUtil.toDateTimeString(value);
		}
		
		public var dataFinalUtc:String = DateUtil.toDateTimeString(DateUtil.add(DateUtil
																				.today
																				, 30));
		
		[Transient]
		public function get dataInicio():Date
		{
			return DateUtil.toDateTime(dataInicioUtc);
		}
		
		public function set dataInicio(value:Date):void
		{
			dataInicioUtc = DateUtil.toDateTimeString(value);
		}
		
		public var dataInicioUtc:String = DateUtil.toDateTimeString(DateUtil.today);
		
		public var dia:int = 1;
		
		public var frequencia:int = 0;
		
		public var lancamento:Lancamento;
		
		public var valor:Number;
	}

}


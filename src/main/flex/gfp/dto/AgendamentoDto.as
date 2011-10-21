package gfp.dto
{
	import common.utils.DateUtils;
	
	import gfp.model.Lancamento;
	
	import mx.collections.ArrayCollection;
	
	[RemoteClass(alias="gfp.dto.AgendamentoDto")]
	[Bindable]
	public class AgendamentoDto
	{
		
		public static var frequencias:ArrayCollection = new ArrayCollection([{label: 'Mensal'}
																			 , {label: 'Dia Útil do Mês'}]);
		
		public function AgendamentoDto():void
		{
		}
		
		public var anteciparFinaisSemana:Boolean = false;
		
		public var dataFinal:Date = DateUtils.add(DateUtils.today, 30);
		
		public var dataInicio:Date = DateUtils.today;
		
		public var dia:int = 1;
		
		public var frequencia:int = 0;
		
		public var lancamento:Lancamento;
		
		public var valor:Number;
	}
}
package gfp.event
{
	import common.custom.CustomEvent;
	
	public class TransacaoEvent extends CustomEvent
	{
		public static const AGENDAR:String = "TransacaoEvent_Agendar";
		
		public static const ATUALIZAR_CREDITO:String = "TransacaoEvent_AtualizarCredito";
		
		public static const ATUALIZAR_DEBITO:String = "TransacaoEvent_AtualizarDebito";
		
		public static const CALCULAR_TOTAIS:String = "TransacaoEvent_CalcularTotais";
		
		public static const EDITAR:String = "TransacaoEvent_Editar";
		
		public static const EXCLUIR:String = "TransacaoEvent_Excluir";
		
		public static const LISTAR:String = "TransacaoEvent_Listar";
		
		public static const LISTAR_DESPESAS_VENCER:String = "TransacaoEvent_ListarDespesasVencer";
		
		public static const LISTAR_DESPESA_MENSAL:String = "TransacaoEvent_ListarDespesaMensal";
		
		public static const LISTAR_PREVISAO_SALDO_DIARIO:String = "TransacaoEvent_ListarPrevisaoSaldoDiario";
		
		public static const LISTAR_RECEITAS_VENCER:String = "TransacaoEvent_ListarReceitasVencer";
		
		public static const LISTAR_RECEITA_MENSAL:String = "TransacaoEvent_ListarReceitaMensal";
		
		public static const LISTAR_SALDO_POR_CONTA:String = "TransacaoEvent_ListarSaldoPorConta";
		
		public static const SALVAR:String = "TransacaoEvent_Salvar";
		
		public function TransacaoEvent(type:String, object:Object = null, result:Function
									   = null, fault:Function = null):void
		{
			super(type, object, result, fault);
		}
	}
}
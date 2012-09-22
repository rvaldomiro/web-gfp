package gfp.event
{
	import common.custom.CustomEvent;
	
	public class AppEvent extends CustomEvent
	{
		public static const ATUALIZAR_DASHBOARD:String = "AppEvent_AtualizarDashboard";
		
		public static const ATUALIZAR_GRAFICO_MENSAL:String = "AppEvent_AtualizarGraficoMensal";
		
		public static const OBTER_VERSAO_ATUAL:String = "AppEvent_ObterVersaoAtual";
		
		public static const TO_CADASTROS:String = "AppEvent_ToCadastros";
		
		public static const TO_DASHBOARD:String = "AppEvent_ToDashboard";
		
		public static const TO_HOME:String = "AppEvent_ToHome";
		
		public static const TO_TRANSACOES:String = "AppEvent_ToTransacoes";
		
		public static const TO_TRANSACOES_DIA:String = "AppEvent_ToTransacoesDia";
		
		public static const TO_TRANSACOES_ULTIMO_ANO:String = "AppEvent_ToTransacoesUltimoAno";
		
		public function AppEvent(type:String, object:Object = null, result:Function =
								 null, fault:Function = null):void
		{
			super(type, object, result, fault);
		}
	}

}


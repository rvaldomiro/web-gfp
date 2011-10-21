package gfp.helper
{
	import common.utils.DateUtils;
	
	import gfp.model.Lancamento;
	
	public class LancamentoHelper
	{
		public static function intervaloPrevisaoPagamento(o:Lancamento):String
		{
			var diasParaPrevisaoPagamento:int = DateUtils.compareToday(o.dataPrevisaoPagamento);
			
			if (diasParaPrevisaoPagamento < -1)
			{
				return diasParaPrevisaoPagamento * (-1) + (diasParaPrevisaoPagamento
					== -1 ? " dia" : " dias") + " atrás";
			}
			else if (diasParaPrevisaoPagamento > 1)
			{
				return "em " + diasParaPrevisaoPagamento + (diasParaPrevisaoPagamento
					== 1 ? " dia" : " dias");
			}
			else
			{
				return null;
			}
		}
	}
}
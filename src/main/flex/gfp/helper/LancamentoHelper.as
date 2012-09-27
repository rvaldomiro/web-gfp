package gfp.helper
{
	import common.util.DateUtil;
	import common.util.StringUtil;
	
	import gfp.model.Lancamento;
	
	public class LancamentoHelper
	{
		public static function intervaloPrevisaoPagamento(o:Lancamento):String
		{
			var diasParaPrevisaoPagamento:int = DateUtil.compareToday(o.dataPrevisaoPagamento);
			
			if (diasParaPrevisaoPagamento < -1)
			{
				return " - " + diasParaPrevisaoPagamento * (-1) + StringUtil.pluralize(" dia"
																					   , diasParaPrevisaoPagamento) +
					" atrás";
			}
			else if (diasParaPrevisaoPagamento > 1)
			{
				return " - em " + diasParaPrevisaoPagamento + StringUtil.pluralize(" dia"
																				   , diasParaPrevisaoPagamento);
			}
			else
			{
				return "   ";
			}
		}
	}

}


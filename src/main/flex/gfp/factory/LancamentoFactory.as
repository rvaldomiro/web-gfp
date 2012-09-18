package gfp.factory
{
	import common.util.DateUtil;
	
	import gfp.model.Lancamento;
	import gfp.type.FormaPagamentoType;
	
	public class LancamentoFactory
	{
		public static function criar():Lancamento
		{
			var o:Lancamento = new Lancamento();
			o.dataVencimento = DateUtil.today;
			o.dataPrevisaoPagamento = DateUtil.today;
			o.valorOriginal = 0;
//			o.valorPago = 0;
			o.formaPagamento = FormaPagamentoType.DINHEIRO;
			o.parcelaNumero = 1;
			o.parcelaQuantidade = 1;
			return o;
		}
	}
}

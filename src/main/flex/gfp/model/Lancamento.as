package gfp.model
{
	import common.util.DateUtil;
	
	import gfp.type.FormaPagamentoType;
	
	import mx.collections.ArrayCollection;
	
	[RemoteClass(alias = "gfp.model.Lancamento")]
	[Bindable]
	public class Lancamento
	{
		
		public function Lancamento():void
		{
			super();
		}
		
		public var categoria:Categoria;
		
		public var conta:Conta;
		
		public var contaTransferencia:Conta;
		
		public var dataCompensacao:Date;
		
		public var dataPagamento:Date;
		
		public var dataPrevisaoPagamento:Date;
		
		public var dataVencimento:Date;
		
		public var formaPagamento:int;
		
		public var id:Object;
		
		public var observacao:String;
		
		public var original:Lancamento;
		
		public var parcelaNumero:int;
		
		public var parcelaQuantidade:int;
		
		[Transient]
		public var selected:Boolean;
		
		public var usuario:Usuario;
		
		public var valorOriginal:Number;
		
//		public var valorPago:Number;
		
		public var vinculados:ArrayCollection;
		
		public function calcularDataPrevisaoPagamento():void
		{
			if (id != null)
			{
				return;
			}
			
			if (formaPagamento == FormaPagamentoType.CREDITO_MASTERCARD || formaPagamento
				== FormaPagamentoType.CREDITO_VISA)
			{
				var fechamento:Date = new Date(dataVencimento.getFullYear(), dataVencimento
											   .getMonth(), formaPagamento == FormaPagamentoType
											   .CREDITO_MASTERCARD ? conta.fechamentoMastercard
											   : conta.fechamentoVisa);
				var vencimento:Date = new Date(fechamento.getFullYear(), fechamento
											   .getMonth(), formaPagamento == FormaPagamentoType
											   .CREDITO_MASTERCARD ? conta.vencimentoMastercard
											   : conta.vencimentoVisa);
				
				if (DateUtil.compareDate(dataVencimento, fechamento) > 0)
				{
					vencimento = DateUtil.addMonth(vencimento, 1);
				}
				
				var referencia:String = "Fatura " + conta.banco.nome.charAt(0) + (formaPagamento
					== FormaPagamentoType.CREDITO_MASTERCARD ? "M" : "V") + DateUtil
					.formatDate(vencimento).substr(3, 2) + DateUtil.formatDate(vencimento)
					.substr(6, 2);
				var obs:String = observacao ? observacao.substring(referencia.length
																   , observacao.length)
					: "";
				observacao = referencia + obs;
				dataPrevisaoPagamento = vencimento;
			}
			else
			{
				observacao = null;
				dataPrevisaoPagamento = dataVencimento;
			}
		}
	}
}

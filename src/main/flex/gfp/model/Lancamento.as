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
		
		[Transient]
		public function get dataCompensacao():Date
		{
			return DateUtil.toDateTime(dataCompensacaoString);
		}
		
		public function set dataCompensacao(value:Date):void
		{
			dataCompensacaoString = DateUtil.toDateTimeString(value);
		}
		
		public var dataCompensacaoString:String;
		
		[Transient]
		public function get dataPagamento():Date
		{
			return DateUtil.toDateTime(dataPagamentoString);
		}
		
		public function set dataPagamento(value:Date):void
		{
			dataPagamentoString = DateUtil.toDateTimeString(value);
		}
		
		public var dataPagamentoString:String;
		
		[Transient]
		public function get dataPrevisaoPagamento():Date
		{
			return DateUtil.toDateTime(dataPrevisaoPagamentoString);
		}
		
		public function set dataPrevisaoPagamento(value:Date):void
		{
			dataPrevisaoPagamentoString = DateUtil.toDateTimeString(value);
		}
		
		public var dataPrevisaoPagamentoString:String;
		
		[Transient]
		public function get dataVencimento():Date
		{
			return DateUtil.toDateTime(dataVencimentoString);
		}
		
		public function set dataVencimento(value:Date):void
		{
			dataVencimentoString = DateUtil.toDateTimeString(value);
		}
		
		public var dataVencimentoString:String;
		
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
		
		public var vinculados:ArrayCollection;
		
		public function calcularDataPrevisaoPagamento():void
		{
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
			else if (id == null)
			{
				observacao = null;
				dataPrevisaoPagamento = dataVencimento;
			}
		}
	}
}


package gfp.model
{
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
		
		public var valorPago:Number;
		
		public var vinculados:ArrayCollection;
	}
}

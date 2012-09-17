package gfp.model
{
	import gfp.type.ContaType;
	
	[RemoteClass(alias = "gfp.model.Conta")]
	[Bindable]
	public class Conta
	{
		
		public function Conta()
		{
			super();
			tipo = ContaType.CONTA_CORRENTE;
			ativa = true;
			limiteMastercard = 0;
			limiteVisa = 0;
		}
		
		public var ativa:Boolean;
		
		public var banco:Banco;
		
		public var fechamentoMastercard:int;
		
		public var fechamentoVisa:int;
		
		public var id:Object;
		
		public var identificacao:String;
		
		public var limiteMastercard:Number;
		
		public var limiteVisa:Number;
		
		public var operaCartaoDebito:Boolean;
		
		public var operaCartaoMastercard:Boolean;
		
		public var operaCartaoVisa:Boolean;
		
		public var operaCheque:Boolean;
		
		public var tipo:int;
		
		public var usuario:Usuario;
		
		public var vencimentoMastercard:int;
		
		public var vencimentoVisa:int;
		
		public function toString():String
		{
			return (banco ? banco.nome + " " : "") + ContaType.descricao(tipo);
		}
	}
}

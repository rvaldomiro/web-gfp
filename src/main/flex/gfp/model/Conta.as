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
		}
		
		public var ativa:Boolean;
		
		public var banco:Banco;
		
		public var id:Object;
		
		public var identificacao:String;
		
		public var tipo:int;
		
		public var usuario:Usuario;
		
		public function toString():String
		{
			return (banco ? banco.nome + " " : "") + ContaType.descricao(tipo);
		}
	}
}

package gfp.type
{
	
	public class ContaType
	{
		public static const CARTEIRA:int = 3;
		
		public static const CONTA_CORRENTE:int = 2;
		
		public static const POUPANCA:int = 1;
		
		public static function descricao(tipo:int):String
		{
			if (tipo == ContaType.POUPANCA)
			{
				return "Poupança";
			}
			else if (tipo == ContaType.CONTA_CORRENTE)
			{
				return "Conta Corrente";
			}
			else if (tipo == ContaType.CARTEIRA)
			{
				return "Carteira"
			}
			
			return null;
		}
	}
}

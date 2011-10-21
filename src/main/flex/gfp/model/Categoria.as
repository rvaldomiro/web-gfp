package gfp.model
{
	import gfp.type.CategoriaType;
	
	[RemoteClass(alias = "gfp.model.Categoria")]
	[Bindable]
	public class Categoria
	{
		
		public function Categoria()
		{
			super();
			tipo = CategoriaType.DESPESA;
			estatistica = true;
		}
		
		public var descricao:String;
		
		public var estatistica:Boolean;
		
		public var id:Object;
		
		public var interna:Boolean;
		
		public var tipo:int;
		
		public var transferencia:Boolean;
		
		public var usuario:Usuario;
		
		public function toString():String
		{
			return descricao + (transferencia ? " (T)" : "");
		}
	}
}

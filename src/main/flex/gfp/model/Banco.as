package gfp.model
{
	
	[RemoteClass(alias = "gfp.model.Banco")]
	[Bindable]
	public class Banco
	{
		
		public function Banco():void
		{
			super();
		}
		
		public var id:Object;
		
		public var nome:String;
		
		public function toString():String
		{
			return nome;
		}
	}
}


package gfp.model
{
	
	[RemoteClass(alias = "gfp.model.Usuario")]
	[Bindable]
	public class Usuario
	{
		
		public function Usuario():void
		{
			super();
			ativo = true;
		}
		
		public var administrador:Boolean;
		
		public var ativo:Boolean;
		
		public var email:String;
		
		public var id:Object;
		
		public var login:String;
		
		public var nome:String;
		
		public var senha:String;
	}
}


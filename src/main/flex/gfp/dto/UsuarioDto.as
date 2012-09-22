package gfp.dto
{
	
	public class UsuarioDto
	{
		public function UsuarioDto(login:String, senha:String):void
		{
			this.login = login;
			this.senha = senha;
		}
		
		public var login:String;
		
		public var senha:String;
	}
}


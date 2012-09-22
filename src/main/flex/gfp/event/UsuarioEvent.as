package gfp.event
{
	import common.custom.CustomEvent;
	
	public class UsuarioEvent extends CustomEvent
	{
		public static const LOGIN:String = "UsuarioEvent_Login";
		
		public static const SALVAR:String = "UsuarioEvent_Salvar";
		
		public function UsuarioEvent(type:String, object:Object = null, result:Function =
									 null, fault:Function = null):void
		{
			super(type, object, result, fault);
		}
	}
}


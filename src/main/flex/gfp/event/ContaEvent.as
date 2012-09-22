package gfp.event
{
	import common.custom.CustomEvent;
	
	public class ContaEvent extends CustomEvent
	{
		public static const EDITAR:String = "ContaEvent_Editar";
		
		public static const EXCLUIR:String = "ContaEvent_EXCLUIR";
		
		public static const LISTAR:String = "ContaEvent_Listar";
		
		public static const SALVAR:String = "ContaEvent_Salvar";
		
		public function ContaEvent(type:String, object:Object = null, result:Function =
								   null, fault:Function = null):void
		{
			super(type, object, result, fault);
		}
	}
}


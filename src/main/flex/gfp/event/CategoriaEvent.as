package gfp.event
{
	import common.custom.CustomEvent;
	
	public class CategoriaEvent extends CustomEvent
	{
		public static const EDITAR:String = "CategoriaEvent_Editar";
		
		public static const EXCLUIR:String = "CategoriaEvent_Excluir";
		
		public static const LISTAR:String = "CategoriaEvent_Listar";
		
		public static const SALVAR:String = "CategoriaEvent_Salvar";
		
		public function CategoriaEvent(type:String, object:Object = null, result:Function
									   = null, fault:Function = null):void
		{
			super(type, object, result, fault);
		}
	}
}
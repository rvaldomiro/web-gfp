package common.utils
{
	import mx.controls.Alert;
	import mx.core.FlexGlobals;
	import mx.events.CloseEvent;
	import mx.rpc.events.FaultEvent;
	
	public class MessageUtils
	{
		
		public static const PREENCHIMENTO_OBRIGATORIO:String = "Preenchimento obrigatório.";
		
		[Embed(source="/common/assets/images/symbol-delete.png")]
		private static const DELETE:Class;
		
		[Embed(source="/common/assets/images/symbol-error.png")]
		private static const ERROR:Class;
		
		[Embed(source="/common/assets/images/symbol-help.png")]
		private static const HELP:Class;
		
		[Embed(source="/common/assets/images/symbol-information.png")]
		private static const INFORMATION:Class;
		
		private static const main:Object = FlexGlobals.topLevelApplication;
		
		public static function confirm(message:String, confirmFunction:Function):void
		{
			Alert.show(message, "Confirmação", Alert.YES | Alert.NO, null, function close(e:CloseEvent):void
			{
				if (e.detail == Alert.YES)
				{
					confirmFunction();
				}
			}, HELP, Alert.NO);
		}
		
		public static function confirmBoolean(message:String, confirmFunction:Function):void
		{
			Alert.show(message, "Confirmação", Alert.YES | Alert.NO, null, function close(e:CloseEvent):void
			{
				confirmFunction(e.detail == Alert.YES);
			}, HELP, Alert.NO);
		}
		
		public static function confirmDelete(deleteFunction:Function, supplyMessage:String
											 = ""):void
		{
			Alert.show("Deseja realmente excluir este registro?\n" + supplyMessage
					   , "Exclusão", Alert.YES | Alert.NO, null, function close(e:CloseEvent):void
					   {
						   if (e.detail == Alert.YES)
						   {
							   deleteFunction();
						   }
					   }, DELETE, Alert.NO);
		}
		
		public static function error(fe:FaultEvent):void
		{
			fault(fe);
		}
		
		public static function fault(e:FaultEvent):void
		{
			Alert.show(e.fault.faultString + "\n" + e.fault.faultCode + "\n", "Ocorreu um Erro Inesperado"
					   , Alert.OK, null, null, ERROR);
		}
		
		public static function show(message:String, title:String = null):void
		{
			Alert.show(message, title, Alert.OK, null, null, INFORMATION);
		}
	}
}
package common.utils
{
	import flash.events.Event;
	
	import mx.rpc.events.FaultEvent;
	
	public class MessageEvent extends Event
	{
		public static const SHOW:String = "MessageEvent_Show";
		
		public function MessageEvent(message:Object, shake:Boolean = false):void
		{
			super(SHOW, true);
			this.message = message is String ? message as String : decode(message
																		  as FaultEvent);
			this.shake = message is FaultEvent || shake;
		}
		
		public var message:String;
		
		public var shake:Boolean;
		
		override public function clone():Event
		{
			return new MessageEvent(message, shake);
		}
		
		private function decode(fe:FaultEvent):String
		{
			var faultString:String = fe.fault.faultString;
			var faultDetail:String = fe.fault.faultDetail;
			
			if (faultString)
			{
				if (faultString.search(":") >= 0)
				{
					faultString = faultString.split(":")[1].toString().substr(1);
				}
				
				return faultString;
			}
			
			if (faultDetail)
			{
				return faultDetail;
			}
			
			return "Ocorreu um erro inesperado";
		}
	}
}
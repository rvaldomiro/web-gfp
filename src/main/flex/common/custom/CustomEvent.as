package common.custom
{
	import flash.events.Event;
	
	import mx.utils.ObjectUtil;
	
	public class CustomEvent extends Event implements ICustomEvent
	{
		
//		public static function getInstance(type:String, object:Object = null, result:Function
//										   = null, fault:Function = null):CustomEvent
//		{
//			return new CustomEvent(type, object, result, fault);
//		}
		
		public function CustomEvent(type:String, object:Object = null, result:Function
									= null, fault:Function = null)
		{
			super(type, true);
			
			if (object)
			{
				_object = object;
			}
			
			_result = result;
			_fault = fault;
		}
		
		private var _displayStatus:Boolean;
		
		public function get displayStatus():Boolean
		{
			return _displayStatus;
		}
		
		public function set displayStatus(value:Boolean):void
		{
			_displayStatus = value;
		}
		
		private var _fault:Function;
		
		public function get fault():Function
		{
			return _fault;
		}
		
		private var _object:Object;
		
		public function get object():Object
		{
			return _object;
		}
		
		public function get objectCopy():Object
		{
			return ObjectUtil.copy(_object);
		}
		
		private var _result:Function;
		
		public function get result():Function
		{
			return _result;
		}
		
		override public function clone():Event
		{
			return new CustomEvent(type, _object, _result, _fault);
		}
	}
}
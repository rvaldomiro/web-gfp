package common.components.custom
{
	import common.utils.MessageEvent;
	import common.utils.MessageUtils;
	
	import flash.events.Event;
	import flash.events.IEventDispatcher;
	import flash.events.MouseEvent;
	
	import mx.core.UIComponent;
	import mx.managers.ToolTipManager;
	import mx.validators.ValidationResult;
	import mx.validators.Validator;
	
	public class RequiredValidator
	{
		public static const DATE:int = 2;
		
		public static const EMAIL:int = 5;
		
		public static const NUMBER:int = 4;
		
		public static const SELECTION:int = 3;
		
		public static const STRING:int = 1;
		
		private static var _dispatcher:IEventDispatcher;
		
		public static function validate(type:int, sources:Array, dispatcher:IEventDispatcher
										, minValue:Object = null):Boolean
		{
			_dispatcher = dispatcher;
			var validator:Validator;
			
			if (type == STRING)
			{
				validator = new CustomStringValidator();
			}
			else if (type == DATE)
			{
				validator = new CustomDateValidator();
			}
			else if (type == SELECTION)
			{
				validator = new CustomSelectionValidator();
			}
			else if (type == NUMBER)
			{
				validator = new CustomNumberValidator(minValue);
			}
			else if (type == EMAIL)
			{
				validator = new CustomEMailValidator();
			}
			else
			{
				throw new Error("Parâmetro inválido: type");
			}
			
			return new RequiredValidator(validator, sources).validateAll();
		}
		
		public function RequiredValidator(validator:Validator, sources:Array):void
		{
			super();
			_validator = validator;
			_sources = sources;
		}
		
		private var _sources:Array;
		
		[Bindable]
		public function get sources():Array
		{
			return _sources;
		}
		
		public function set sources(value:Array):void
		{
			_sources = value;
			setupSourcesValidation();
		}
		
		private var _validator:Validator;
		
		public function clearAllValidators():void
		{
			if (_validator)
			{
				_validator.source = null;
			}
			
			for each (var source:UIComponent in _sources)
			{
				source.errorString = null;
			}
		}
		
		public function validateAll():Boolean
		{
			clearAllValidators();
			
			var focusComponent:UIComponent = null;
			var result:Boolean = true;
			
			for each (var source:UIComponent in _sources)
			{
				if (source.enabled && source.visible && source.parent.visible)
				{
					_validator.source = source;
					
					var results:Array = _validator.validate().results;
					
					if (results)
					{
						source.errorString = (results[0] as ValidationResult).errorMessage;
						focusComponent ||= source;
						result = false;
					}
					
					_validator.source = null;
				}
			}
			
			if (!result)
			{
				if (focusComponent)
				{
					focusComponent.setFocus();
				}
				
				_dispatcher.dispatchEvent(new MessageEvent("Informe corretamente os campos solicitados"));
			}
			
			return result;
		}
		
		private function setupSourcesValidation():void
		{
			for each (var source:UIComponent in _sources)
			{
				source.addEventListener(_validator.triggerEvent, validatorTriggerEventHandler);
			}
		}
		
		private function validate(target:UIComponent):void
		{
			if (_validator)
			{
				_validator.source = target;
				_validator.validate();
			}
		}
		
		private function validatorTriggerEventHandler(event:Event):void
		{
			var child:UIComponent = UIComponent(event.currentTarget);
			validate(child);
		}
	}
}
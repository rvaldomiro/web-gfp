package common.components.custom
{
	import flash.events.Event;
	import flash.events.FocusEvent;
	
	import mx.formatters.NumberFormatter;
	
	import spark.components.TextInput;
	
	[Event(name="valueChange", type="flash.events.Event")]
	[Event(name="propertiesNumberFormatChange", type="flash.events.Event")]
	public class TextInputNumeric extends TextInput
	{
		
		public static const PROPERTIES_NUMBER_FORMAT_CHANGE:String = "propertiesNumberFormatChange";
		
		public static const VALUE_CHANGE:String = "valueChange";
		
		public function TextInputNumeric()
		{
			super();
			
			this.nf = new NumberFormatter();
			this.nf.precision = this._precision;
			this.nf.rounding = "none";
			this.nf.useNegativeSign = this._useNegativeSign;
			this.nf.useThousandsSeparator = this._useThousandsSeparator;
			
			this.addEventListener(Event.CHANGE, this.formatHandler, false, 0, true);
			this.addEventListener(FocusEvent.FOCUS_IN, this.setCursor, false, 0, true);
			this.resourceManager.addEventListener(Event.CHANGE, this.formatHandler
												  , false, 0, true);
			
			this.width = 95;
			this.maxChars = 20;
			this.restrict = "0-9";
			this.setStyle("textAlign", "right");
			this.value = 0;
		}
		
		[Bindable(event="propertiesNumberFormatChange")]
		public var nf:NumberFormatter;
		
		private var _precision:uint = 2;
		
		public function get precision():uint
		{
			return this._precision;
		}
		
		[Inspectable(defaultValue=2)]
		public function set precision(value:uint):void
		{
			if (this.precision != value)
			{
				this._precision = value;
				this.precisionChanged = true;
				this.invalidateDisplayList();
			}
		}
		
		private var _useNegativeSign:Boolean;
		
		public function get useNegativeSign():Boolean
		{
			return this._useNegativeSign;
		}
		
		[Inspectable(defaultValue=false)]
		public function set useNegativeSign(value:Boolean):void
		{
			if (this.useNegativeSign != value)
			{
				this._useNegativeSign = value;
				this.useNegativeSignChanged = true;
				this.invalidateDisplayList();
			}
		}
		
		private var _useThousandsSeparator:Boolean = true;
		
		public function get useThousandsSeparator():Boolean
		{
			return this._useThousandsSeparator;
		}
		
		[Inspectable(defaultValue=true)]
		public function set useThousandsSeparator(value:Boolean):void
		{
			if (this.useThousandsSeparator != value)
			{
				this._useThousandsSeparator = value;
				this.useThousandsSeparatorChanged = true;
				this.invalidateDisplayList();
			}
		}
		
		private var _value:Number;
		
		public function get value():Number
		{
			return this._value;
		}
		
		[Bindable(event="valueChange")]
		public function set value(value:Number):void
		{
//			this._value = this.toNumber(value);
			this._value = value;
			this.text = this.nf.format(this._value);
			this.dispatchEvent(new Event(TextInputNumeric.VALUE_CHANGE));
		}
		
		private var onlyDigits:RegExp = new RegExp("[^\\d]", "g");
		
		private var precisionChanged:Boolean;
		
		private var useNegativeSignChanged:Boolean;
		
		private var useThousandsSeparatorChanged:Boolean;
		
		public function toNumber(value:Object = null):Number
		{
			if (value == null)
			{
				value = this._value;
			}
			
			if (value is Number)
			{
				return new Number(value);
			}
			
			var retorno:Number = 0;
			
			if (value != null)
			{
				retorno = Number(value.toString().replace(this.onlyDigits, ""));
				
				if (this.useNegativeSign && value.toString().indexOf("-") > -1)
				{
					retorno *= -1;
				}
			}
			
			return (retorno / Math.pow(10, this._precision));
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
		{
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			
			var formatChange:Boolean = (this.precisionChanged || this.useNegativeSignChanged
				|| this.useThousandsSeparatorChanged);
			
			if (this.precisionChanged)
			{
				this.precisionChanged = false;
				this.nf.precision = this.precision;
			}
			
			if (this.useNegativeSignChanged)
			{
				this.useNegativeSignChanged = false;
				this.nf.useNegativeSign = this.useNegativeSign;
				this.restrict = (this.useNegativeSign) ? "0-9\\-" : "0-9";
			}
			
			if (this.useThousandsSeparatorChanged)
			{
				this.useThousandsSeparatorChanged = false;
				this.nf.useThousandsSeparator = this.useThousandsSeparator
			}
			
			if (formatChange)
			{
				this.value = toNumber(this.text);
				this.dispatchEvent(new Event(TextInputNumeric.PROPERTIES_NUMBER_FORMAT_CHANGE));
			}
		}
		
		private function formatHandler(event:Event):void
		{
			this.value = toNumber(this.text);
			this.setCursor(null);
		}
		
		private function setCursor(event:FocusEvent):void
		{
			this.selectRange(this.text.length, this.text.length);
		}
	}
}
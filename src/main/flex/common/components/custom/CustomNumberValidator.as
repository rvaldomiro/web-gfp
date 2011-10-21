package common.components.custom
{
	import mx.validators.NumberValidator;
	import mx.validators.Validator;
	
	public class CustomNumberValidator extends NumberValidator
	{
		public function CustomNumberValidator(minValue:Object = null)
		{
			super();
			this.property = "text";
			this.minValue = minValue;
			this.thousandsSeparator = ".";
			this.decimalSeparator = ",";
			this.precision = 2;
			this.lowerThanMinError = "O preenchimento deste campo é obrigatório.";
		}
	}
}
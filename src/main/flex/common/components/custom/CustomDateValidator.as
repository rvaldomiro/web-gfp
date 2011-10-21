package common.components.custom
{
	import mx.validators.DateValidator;
	import mx.validators.Validator;
	
	public class CustomDateValidator extends DateValidator
	{
		public function CustomDateValidator(property:String = "selectedDate")
		{
			super();
			this.property = property;
		}
	}
}
package common.components.custom
{
	import mx.validators.StringValidator;
	
	public class CustomStringValidator extends StringValidator
	{
		public function CustomStringValidator(property:String = "text")
		{
			super();
			this.property = property;
		}
	}
}
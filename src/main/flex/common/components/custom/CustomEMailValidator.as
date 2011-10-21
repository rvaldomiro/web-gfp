package common.components.custom
{
	import mx.validators.EmailValidator;
	
	public class CustomEMailValidator extends EmailValidator
	{
		public function CustomEMailValidator(property:String = "text")
		{
			super();
			this.property = property;
		}
	}
}
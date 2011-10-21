package common.components.custom
{
	import mx.validators.Validator;
	
	public class CustomSelectionValidator extends Validator
	{
		public function CustomSelectionValidator(property:String = "selectedItem")
		{
			super();
			this.property = property;
		}
	}
}
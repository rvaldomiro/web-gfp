package common.components.custom
{
	import spark.components.Button;
	
	public class CustomButton extends Button
	{
		public function CustomButton()
		{
			super();
			useHandCursor = true;
			buttonMode = true;
		}
	}
}
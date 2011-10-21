package common.components.custom
{
	import spark.components.ButtonBar;
	
	public class CustomButtonBar extends ButtonBar
	{
		public function CustomButtonBar()
		{
			super();
			useHandCursor = true;
			buttonMode = true;
		}
	}
}
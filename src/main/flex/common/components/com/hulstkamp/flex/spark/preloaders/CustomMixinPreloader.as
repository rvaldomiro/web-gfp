package common.components.com.hulstkamp.flex.spark.preloaders
{
	import common.components.com.hulstkamp.flex.spark.managers.NiceToolTipManagerImpl;
	
	import mx.core.mx_internal;
	import mx.preloaders.DownloadProgressBar;
	
	use namespace mx_internal;
	
	/**
	 * Custom preloader to register a custom ToolTipManager as the Manager for tooltips
	 * in the SystemManager.
	 */
	public class CustomMixinPreloader extends DownloadProgressBar
	{
		
		public function CustomMixinPreloader()
		{
			super();
			registerCustomClasses();
		}
		
		protected function registerCustomClasses():void
		{
			mx.core.Singleton.registerClass("mx.managers::IToolTipManager2", NiceToolTipManagerImpl);
		}
	}
}
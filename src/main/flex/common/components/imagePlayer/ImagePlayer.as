package common.components.imagePlayer
{
	
	import flash.net.URLRequest;
	
	import mx.controls.Image;
	import mx.core.UIComponent;
	
	public class ImagePlayer extends Image
	{
		
		public function ImagePlayer()
		{
			super();
			this._gifImage = new UIComponent();
		}
		
		override public function set source(value:Object):void
		{
			if (!value is String)
			{
				throw new ArgumentError("Propriedade source deve ser uma String");
			}
			
			super.source = value;
		}
		
		private var _gifImage:UIComponent;
		
		override protected function createChildren():void
		{
			super.createChildren();
			
			if (this.source)
			{
				var player:GIFPlayer = new GIFPlayer();
				player.load(new URLRequest(this.source as String));
				this._gifImage.addChild(player);
			}
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
		{
			this.addChild(this._gifImage);
			super.updateDisplayList(unscaledWidth, unscaledHeight);
		}
	}
}
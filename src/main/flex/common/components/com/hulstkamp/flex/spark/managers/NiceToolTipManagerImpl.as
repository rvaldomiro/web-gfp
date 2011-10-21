package common.components.com.hulstkamp.flex.spark.managers
{
	import common.components.com.hulstkamp.flex.spark.components.NiceToolTip;
	
	import flash.display.DisplayObject;
	import flash.geom.Point;
	
	import mx.controls.ToolTip;
	import mx.core.IToolTip;
	import mx.core.UIComponent;
	import mx.core.mx_internal;
	import mx.events.EffectEvent;
	import mx.managers.ISystemManager;
	import mx.managers.IToolTipManager2;
	import mx.managers.ToolTipManager;
	import mx.managers.ToolTipManagerImpl;
	import mx.resources.ResourceManager;
	import mx.styles.IStyleClient;
	
	use namespace mx_internal;
	
	/**
	 * Manages a custom NiceToolTip that has an optional title, text, styles and placement.
	 *
	 * Applies title, text, styles and placement to the tooltip using values defined in
	 * resource bundles.
	 *
	 * Takes the value in tooltip or errorString on a component to lookup
	 * values for title, text, styles etc and applies it to the Tooltip.
	 *
	 * Places the Tooltip based on the placement value and passes the placment
	 * to the Tooltip component so that it can draw itself accordingly
	 *
	 * Overrides initializeTip and positionTip to add the required behaviour.
	 *
	 * @TODO: Test that none of the default Flex ToolTip behaviour is broken.
	 * @version 0.1
	 * @author andy hulstkamp. www.hulstkamp.com
	 *
	 */
	public class NiceToolTipManagerImpl extends ToolTipManagerImpl implements IToolTipManager2
	{
		public static var DEFAULT_TOOL_TIP_BUNDLE_NAME:String = "ToolTipBundle";
		
		private static var _instance:IToolTipManager2;
		
		public static function getInstance():IToolTipManager2
		{
			if (!_instance)
			{
				_instance = new NiceToolTipManagerImpl();
			}
			_instance.toolTipClass = NiceToolTip;
			return _instance;
		}
		
		public function NiceToolTipManagerImpl()
		{
			super();
			
			if (_instance)
			{
				throw new Error("Instance already exists.");
			}
		}
		
		private var _useDefaultImplementation:Boolean;
		
		public function get useDefaultImplementation():Boolean
		{
			return _useDefaultImplementation;
		}
		
		public function set useDefaultImplementation(value:Boolean):void
		{
			if (value)
			{
				toolTipClass = ToolTip;
			}
			else
			{
				toolTipClass = NiceToolTip;
			}
			_useDefaultImplementation = value;
		
		}
		
		/**
		 * Assigns values from the resource bundle to the ToolTip.
		 * Uses the value of the tooltip property or the errosString property
		 * on a component as the key to lookup resources.
		 */
		protected function assignResourcesToToolTip():void
		{
			
			if (!currentText || !currentToolTip)
			{
				return;
			}
			
			var toolTipKey:String = "tooltip";
			
			if (isError)
			{
				toolTipKey = "errortip";
			}
			var tokens:Array = currentText.split(".");
			
			//eval the Bundle name to use, check if passed value is a bundle otherwise fallback on default
			var locale:String = ResourceManager.getInstance().localeChain[0];
			var bundleKey:String =  DEFAULT_TOOL_TIP_BUNDLE_NAME;
			
			if (ResourceManager.getInstance().getResourceBundle(locale, tokens[0])
				!= null)
			{
				bundleKey = tokens[0];
			}
			
			//eval the component instance key to use for resource lookup. 
			//Either the second entry when set or the id of the UIComponent. 
			//If neither is set use default
			var targetKey:String = "default";
			
			if (tokens.length > 1)
			{
				targetKey = tokens[1];
			}
			else if (this.currentTarget is UIComponent && UIComponent(currentTarget)
				.id != null)
			{
				targetKey = UIComponent(currentTarget).id;
			}
			
			var lookupKey:String = toolTipKey + "." + targetKey;
			
			//eval and apply color if any
			var color:uint = ResourceManager.getInstance().getUint(bundleKey, lookupKey
																   + ".color");
			
			if (color)
			{
				IStyleClient(currentToolTip).setStyle("color", color);
			}
			
			//eval and apply chromeColor if any
			var chromeColor:uint = ResourceManager.getInstance().getUint(bundleKey
																		 , lookupKey
																		 + ".chromeColor");
			
			if (chromeColor)
			{
				IStyleClient(currentToolTip).setStyle("chromeColor", chromeColor);
			}
			
			//eval and apply styleName if any
			var styleName:String = ResourceManager.getInstance().getString(bundleKey
																		   , lookupKey
																		   + ".styleName");
			
			if (styleName)
			{
				IStyleClient(currentToolTip).styleName = styleName;
			}
			
			//eval and apply placement if any
			var placement:String = ResourceManager.getInstance().getString(bundleKey
																		   , lookupKey
																		   + ".placement");
			
			if (placement)
			{
				IStyleClient(currentToolTip).setStyle("placement", placement);
			}
			
			//eval and apply text. If no text is found add a warning as the tooltip
			var toolTipText:String = ResourceManager.getInstance().getString(bundleKey
																			 , lookupKey
																			 + ".text");
			
			if (toolTipText)
			{
				currentToolTip.text = toolTipText;
			}
			else
			{
//				currentToolTip.text = "warning: " + currentText + " has been set but no entry neither in the specified resource bundle or the default bundle has been found";
				currentToolTip.text = currentText;
			}
			
			//if the ToolTipClass is NiceToolTip eval and apply title if any
			if (currentToolTip is NiceToolTip)
			{
				var toolTipTitle:String = ResourceManager.getInstance().getString(bundleKey
																				  , lookupKey
																				  + ".title");
				
				if (toolTipTitle != null && toolTipTitle.length > 0)
				{
					NiceToolTip(currentToolTip).title = toolTipTitle;
				}
			}
		}
		
		mx_internal override function initializeTip():void
		{
			
			if (useDefaultImplementation)
			{
				super.initializeTip();
				return;
			}
			
			if (isError && currentToolTip is IStyleClient)
			{
				IStyleClient(currentToolTip).setStyle("styleName", "errorTip");
			}
			
			// Set the text of the tooltip.
			if (currentToolTip is IToolTip)
			{
				assignResourcesToToolTip();
			}
			
			sizeTip(currentToolTip);
			
			if (currentToolTip is IStyleClient)
			{
				// Set up its "show" and "hide" effects.
				if (showEffect)
				{
					IStyleClient(currentToolTip).setStyle("showEffect", showEffect);
				}
				
				if (hideEffect)
				{
					IStyleClient(currentToolTip).setStyle("hideEffect", hideEffect);
				}
			}
			
			if (showEffect || hideEffect)
			{
				currentToolTip.addEventListener(EffectEvent.EFFECT_END, effectEndHandler);
			}
		}
		
		mx_internal override function positionTip():void
		{
			
			if (useDefaultImplementation)
			{
				super.positionTip();
				return;
			}
			
			var x:Number;
			var y:Number;
			
			var screenWidth:Number = currentToolTip.screen.width;
			var screenHeight:Number = currentToolTip.screen.height;
			
			var targetPos:Point = currentTarget.localToGlobal(new Point(0, 0));
			var targetWidth:Number = currentTarget.width;
			var targetHeight:Number = currentTarget.height;
			var toolTipWidth:Number = currentToolTip.width;
			var toolTipHeight:Number = currentToolTip.height;
			var toLeft:Boolean;
			var toTop:Boolean;
			
			var sm:ISystemManager = getSystemManager(currentTarget);
			
			var ttPos:String = IStyleClient(currentToolTip).getStyle("placement");
			
			//Use the specified placement value to position the ToolTip but make sure
			//it can be displayed completely
			//If no placement value is set use automatic placement, prefer
			//right over left and bottom over top
			if (ttPos == "topLeft" || ttPos == "bottomLeft" || targetPos.x + targetWidth
				* .75 + toolTipWidth > screenWidth)
			{
				toLeft = true;
			}
			
			if (ttPos == "topLeft" || ttPos == "topRight" || targetPos.y + targetHeight
				/ 2 + toolTipWidth > screenHeight)
			{
				toTop = true;
			}
			
			//Position the ToolTip and let the ToolTip Component know wich placement
			//to use, so that the skin can draw itself accordingly
			if (toLeft && toTop)
			{
				IStyleClient(currentToolTip).setStyle("placement", "topLeft");
				x = targetPos.x + Math.max(20, targetWidth * .1 - 25);
				y = targetPos.y + targetHeight * .25;
			}
			else if (toLeft && !toTop)
			{
				IStyleClient(currentToolTip).setStyle("placement", "bottomLeft");
				x = targetPos.x + Math.max(20, targetWidth * .1 - 25);
				y = targetPos.y + targetHeight * .75;
			}
			else if (!toLeft && toTop)
			{
				IStyleClient(currentToolTip).setStyle("placement", "topRight");
				x = targetPos.x + targetWidth * .9 - 25;
				y = targetPos.y + targetHeight * .25;
			}
			else
			{
				IStyleClient(currentToolTip).setStyle("placement", "bottomRight");
				x = targetPos.x + targetWidth * .9 - 25;
				y = targetPos.y + targetHeight * .75;
			}
			
			var pos:Point = new Point(x, y);
			pos = DisplayObject(sm).localToGlobal(pos);
			pos = DisplayObject(sm.getSandboxRoot()).globalToLocal(pos);
			x = pos.x;
			y = pos.y;
			
			currentToolTip.move(x, y);
		}
	}
}
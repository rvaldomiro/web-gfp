package common.components.serviceFeedback
{
	import flash.display.DisplayObject;
	import flash.events.MouseEvent;
	import flash.ui.Mouse;
	import flash.utils.setTimeout;
	
	import mx.core.UIComponent;
	import mx.messaging.messages.HTTPRequestMessage;
	import mx.messaging.messages.RemotingMessage;
	import mx.messaging.messages.SOAPMessage;
	import mx.rpc.AsyncToken;
	import mx.rpc.IResponder;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	
	import spark.components.supportClasses.SkinnableComponent;
	
	[SkinState("success")]
	[SkinState("fail")]
	[SkinState("processing")]
	[Event(name="result", type="mx.rpc.events.ResultEvent")]
	[Event(name="fault", type="mx.rpc.events.FaultEvent")]
	public class ServiceFeedback extends SkinnableComponent implements IResponder
	{
		
		private static var instances:Object = {};
		
		public static function addResponder(token:AsyncToken):void
		{
			
			// We use the token to populate the watchForCall variable 
			// because the token has the HTTPService URL or
			// the method name
			var watchForCall:String;
			
			if (token.message is SOAPMessage)
			{
				var soapEnvelop:XML = XML(token.message.body);
				var soap:Namespace = soapEnvelop.namespace();
				watchForCall = soapEnvelop.soap::Body.children()[0].name().localName;
			}
			else if (token.message is RemotingMessage)
			{
				watchForCall = RemotingMessage(token.message).operation;
			}
			else if (token.message is HTTPRequestMessage)
			{
				watchForCall = HTTPRequestMessage(token.message).url;
			}
			
			var instances:Array = ServiceFeedback.instances[watchForCall];
			
			// It can have many ServiceFeedbacks listening for 
			// the same call... so we need a loop
			for each (var instance:ServiceFeedback in instances)
			{
				if (instance.isVisible())
				{
					token.addResponder(instance);
					
					with (instance)
					{
						status = PROCESSING;
						invalidateSkinState();
					}
				}
			}
		
		}
		
		public function ServiceFeedback()
		{
			setStyle("skinClass", DefaultLoadingSkin);
		}
		
		[SkinPart(required="true")]
		public var failIcon:UIComponent;
		
		[SkinPart(required="true")]
		public var loadingIcon:UIComponent;
		
		private var _watchForCall:String;
		
		[Bindable]
		
		public function get watchForCall():String
		{
			return _watchForCall;
		}
		
		public function set watchForCall(value:String):void
		{
			_watchForCall = value;
			
			if (ServiceFeedback.instances[watchForCall] == null)
			{
				ServiceFeedback.instances[watchForCall] = new Array();
			}
			ServiceFeedback.instances[watchForCall].push(this);
		}
		
		private const FAIL:String = "fail";
		
		private const PROCESSING:String = "processing";
		
		private const SUCCESS:String = "success";
		
		private var status:String = SUCCESS;
		
		//--------------------------------------------------------------------------
		//
		//  Properties
		//
		//--------------------------------------------------------------------------
		
		private var watchForCallChanged:Boolean;
		
		public function fault(info:Object):void
		{
			
			status = FAIL;
			invalidateSkinState();
			
			// Add detailed information in the Tooltip's icon 
			failIcon.toolTip = info.fault.faultCode + "\n" + info.fault.faultString
				+ "\n" + info.fault.message;
			failIcon.addEventListener(MouseEvent.CLICK, failIconClickHandler);
			
			dispatchEvent(FaultEvent(info));
		}
		
		public function result(data:Object):void
		{
			status = SUCCESS;
			invalidateSkinState();
			dispatchEvent(ResultEvent(data));
		}
		
		//--------------------------------------------------------------------------
		//
		//  Overriden Methods
		//
		//--------------------------------------------------------------------------
		override protected function getCurrentSkinState():String
		{
			return status;
		}
		
		private function failIconClickHandler(event:MouseEvent):void
		{
			status = SUCCESS;
			invalidateSkinState();
			
			failIcon.removeEventListener(MouseEvent.CLICK, failIconClickHandler);
		}
		
		private function isVisible():Boolean
		{
			var currentView:DisplayObject = this;
			var isVisible:Boolean;
			
			// we have to make sure that the view is really visible
			// so, we loop through all it's parents to see it they're visible too
			while (currentView)
			{
				isVisible = currentView.visible;
				
				if (isVisible == false)
				{
					break;
				}
				currentView = currentView.parent;
			}
			return isVisible;
		}
	}
}
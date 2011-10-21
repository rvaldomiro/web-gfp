package common.custom
{
	import mx.rpc.remoting.RemoteObject;
	
	public class CustomRemoteObject extends RemoteObject
	{
		
		public function CustomRemoteObject(destination:String)
		{
			super(destination);
			this.endpoint = "messagebroker/amf";
		}
	}
}
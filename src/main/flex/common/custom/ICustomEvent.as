package common.custom
{
	
	public interface ICustomEvent
	{
		function get displayStatus():Boolean;
		
		function get fault():Function;
		
		function get object():Object;
		
		function get objectCopy():Object;
		
		function get result():Function;
	}
}
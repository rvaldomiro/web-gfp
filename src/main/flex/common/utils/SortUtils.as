package common.utils
{
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	
	public class SortUtils
	{
		
		public static function sortDate(dataProvider:ArrayCollection, fieldName:String
										, descending:Boolean = false):void
		{
			sortDateArray(dataProvider, [fieldName], descending);
		}
		
		public static function sortDateArray(dataProvider:ArrayCollection, fieldNames:Array
											 , descending:Boolean = false):void
		{
			var sort:Sort = new Sort();
			sort.fields = [];
			
			for each (var fieldName:String in fieldNames)
			{
				var sortField:SortField = new SortField(fieldName, false, descending);
				sort.fields.push(sortField);
			}
			
			dataProvider.sort = sort;
			dataProvider.refresh();
		}
		
		public static function sortMultiple(dataProvider:ArrayCollection, fieldNames:Array
											, caseInsensitive:Boolean = false, descending:Boolean
											= false):void
		{
			var sort:Sort = new Sort();
			var fields:Array = new Array();
			
			for each (var fieldName:String in fieldNames)
			{
				var sortField:SortField = new SortField(fieldName, caseInsensitive
														, descending);
				fields.push(sortField);
			}
			
			sort.fields = fields;
			dataProvider.sort = sort;
			dataProvider.refresh();
		}
		
		public static function sortText(dataProvider:ArrayCollection, fieldName:String
										, descending:Boolean = false):void
		{
			var sort:Sort = new Sort();
			var sortField:SortField = new SortField(fieldName, true, descending);
			sort.fields = [sortField];
			dataProvider.sort = sort;
			dataProvider.refresh();
		}
		
		public static function sortValue(dataProvider:ArrayCollection, fieldName:String
										 , descending:Boolean = false):void
		{
			var sort:Sort = new Sort();
			var sortField:SortField = new SortField(fieldName, true, descending, true);
			sort.fields = [sortField];
			dataProvider.sort = sort;
			dataProvider.refresh();
		}
	}
}
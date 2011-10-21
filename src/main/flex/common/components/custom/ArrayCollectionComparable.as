package common.components.custom
{
	import mx.collections.ArrayCollection;
	
	public class ArrayCollectionComparable extends ArrayCollection
	{
		
		public function ArrayCollectionComparable(propertyId:String, source:ArrayCollection)
		{
			super(source ? source.toArray() : []);
			this.propertyId = propertyId;
		}
		
		private var propertyId:String;
		
		override public function contains(item:Object):Boolean
		{
			return getItemIndex(item) >= 0;
		}
		
		public function getItem(item:Object):Object
		{
			var index:int = getItemIndex(item);
			return index >= 0 ? getItemAt(index) : null;
		}
		
		override public function getItemIndex(item:Object):int
		{
			var index:int = 0;
			
			for each (var localItem:Object in this)
			{
				if (localItem[propertyId] == item[propertyId])
				{
					return index;
				}
				
				index++;
			}
			
			return -1;
		}
		
		public function removeItem(item:Object):void
		{
			var index:int = getItemIndex(item);
			
			if (index >= 0)
			{
				removeItemAt(index);
			}
		}
		
		public function update(item:Object):void
		{
			if (!item)
			{
				return;
			}
			
			var index:int = getItemIndex(item);
			
			if (index >= 0)
			{
				this.setItemAt(item, index);
			}
			else
			{
				this.addItem(item);
			}
			
			this.refresh();
		}
	}

}
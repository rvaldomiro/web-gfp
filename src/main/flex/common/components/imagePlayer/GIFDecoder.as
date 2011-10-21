package common.components.imagePlayer
{
	import flash.display.BitmapData;
	import flash.geom.Rectangle;
	import flash.utils.ByteArray;
	
	public class GIFDecoder
	{
		
		private static var MaxStackSize:int = 4096;
		
		private static var STATUS_FORMAT_ERROR:int = 1;
		
		private static var STATUS_OK:int = 0;
		
		private static var STATUS_OPEN_ERROR:int = 2;
		
		private static var frameRect:Rectangle = new Rectangle;
		
		public function GIFDecoder()
		{
			block = new ByteArray;
		}
		
		public function get disposeValue():int
		{
			return dispose;
		}
		
		private var act:Array;
		
		private var bgColor:int;
		
		private var bgIndex:int;
		
		private var bitmap:BitmapData;
		
		private var block:ByteArray;
		
		private var blockSize:int = 0;
		
		private var delay:int = 0;
		
		private var dispose:int = 0;
		
		private var frameCount:int
		
		private var frames:Array
		
		private var gct:Array;
		
		private var gctFlag:Boolean;
		
		private var gctSize:int;
		
		private var height:int;
		
		private var ih:int;
		
		private var image:BitmapData;
		
		private var inStream:ByteArray;
		
		private var interlace:Boolean;
		
		private var iw:int;
		
		private var ix:int;
		
		private var iy:int;
		
		private var lastBgColor:int;
		
		private var lastDispose:int = 0;
		
		private var lastImage:BitmapData;
		
		private var lastRect:Rectangle;
		
		private var lct:Array;
		
		private var lctFlag:Boolean;
		
		private var lctSize:int;
		
		private var loopCount:int = 1;
		
		private var pixelAspect:int;
		
		private var pixelStack:Array;
		
		private var pixels:Array;
		
		private var prefix:Array
		
		private var status:int;
		
		private var suffix:Array;
		
		private var transIndex:int;
		
		private var transparency:Boolean = false;
		
		private var width:int;
		
		public function getDelay(n:int):int
		{
			delay = -1;
			
			if ((n >= 0) && (n < frameCount))
			{
				delay = frames[n].delay;
			}
			return delay;
		}
		
		public function getFrame(n:int):GIFFrame
		{
			var im:GIFFrame = null;
			
			if ((n >= 0) && (n < frameCount))
				
			{
				im = frames[n];
				
			}
			else
			{
				throw new RangeError("Wrong frame number passed");
			}
			
			return im;
		}
		
		public function getFrameCount():int
		{
			return frameCount;
		}
		
		public function getFrameSize():Rectangle
		{
			var rect:Rectangle = GIFDecoder.frameRect;
			
			rect.x = rect.y = 0;
			rect.width = width;
			rect.height = height;
			
			return rect;
		}
		
		public function getImage():GIFFrame
		{
			return getFrame(0);
		}
		
		public function getLoopCount():int
		{
			return loopCount;
		}
		
		public function read(inStream:ByteArray):int
		{
			init();
			
			if (inStream != null)
			{
				this.inStream = inStream;
				readHeader();
				
				if (!hasError())
				{
					readContents();
					
					if (frameCount < 0)
					{
						status = STATUS_FORMAT_ERROR;
					}
				}
			}
			else
			{
				status = STATUS_OPEN_ERROR;
			}
			return status;
		}
		
		private function decodeImageData():void
		{
			var NullCode:int = -1;
			var npix:int = iw * ih;
			var available:int;
			var clear:int;
			var code_mask:int;
			var code_size:int;
			var end_of_information:int;
			var in_code:int;
			var old_code:int;
			var bits:int;
			var code:int;
			var count:int;
			var i:int;
			var datum:int;
			var data_size:int;
			var first:int;
			var top:int;
			var bi:int;
			var pi:int;
			
			if ((pixels == null) || (pixels.length < npix))
			{
				pixels = new Array(npix);
			}
			
			if (prefix == null)
			{
				prefix = new Array(MaxStackSize);
			}
			
			if (suffix == null)
			{
				suffix = new Array(MaxStackSize);
			}
			
			if (pixelStack == null)
			{
				pixelStack = new Array(MaxStackSize + 1);
			}
			
			data_size = readSingleByte();
			clear = 1 << data_size;
			end_of_information = clear + 1;
			available = clear + 2;
			old_code = NullCode;
			code_size = data_size + 1;
			code_mask = (1 << code_size) - 1;
			
			for (code = 0; code < clear; code++)
			{
				prefix[int(code)] = 0;
				suffix[int(code)] = code;
			}
			
			datum = bits = count = first = top = pi = bi = 0;
			
			for (i = 0; i < npix; )
			{
				if (top == 0)
				{
					if (bits < code_size)
					{
						if (count == 0)
						{
							count = readBlock();
							
							if (count <= 0)
							{
								break;
							}
							bi = 0;
						}
						datum += (int((block[int(bi)])) & 0xff) << bits;
						bits += 8;
						bi++;
						count--;
						continue;
					}
					
					code = datum & code_mask;
					datum >>= code_size;
					bits -= code_size;
					
					if ((code > available) || (code == end_of_information))
					{
						break;
					}
					
					if (code == clear)
					{
						code_size = data_size + 1;
						code_mask = (1 << code_size) - 1;
						available = clear + 2;
						old_code = NullCode;
						continue;
					}
					
					if (old_code == NullCode)
					{
						pixelStack[int(top++)] = suffix[int(code)];
						old_code = code;
						first = code;
						continue;
					}
					in_code = code;
					
					if (code == available)
					{
						pixelStack[int(top++)] = first;
						code = old_code;
					}
					
					while (code > clear)
					{
						pixelStack[int(top++)] = suffix[int(code)];
						code = prefix[int(code)];
					}
					first = (suffix[int(code)]) & 0xff;
					
					if (available >= MaxStackSize)
					{
						break;
					}
					pixelStack[int(top++)] = first;
					prefix[int(available)] = old_code;
					suffix[int(available)] = first;
					available++;
					
					if (((available & code_mask) == 0) && (available < MaxStackSize))
					{
						code_size++;
						code_mask += available;
					}
					old_code = in_code;
				}
				
				top--;
				pixels[int(pi++)] = pixelStack[int(top)];
				i++;
			}
			
			for (i = pi; i < npix; i++)
			{
				pixels[int(i)] = 0;
			}
		
		}
		
		private function getPixels(bitmap:BitmapData):Array
		{
			var pixels:Array = new Array(4 * image.width * image.height);
			var count:int = 0;
			var lngWidth:int = image.width;
			var lngHeight:int = image.height;
			var color:int;
			
			for (var th:int = 0; th < lngHeight; th++)
			{
				for (var tw:int = 0; tw < lngWidth; tw++)
				{
					color = bitmap.getPixel(th, tw);
					
					pixels[count++] = (color & 0xFF0000) >> 16;
					pixels[count++] = (color & 0x00FF00) >> 8;
					pixels[count++] = (color & 0x0000FF);
				}
			}
			return pixels;
		}
		
		private function hasError():Boolean
		{
			return status != STATUS_OK;
		}
		
		private function init():void
		{
			status = STATUS_OK;
			frameCount = 0;
			frames = new Array;
			gct = null;
			lct = null;
		}
		
		private function readBlock():int
		{
			blockSize = readSingleByte();
			var n:int = 0;
			
			if (blockSize > 0)
			{
				try
				{
					var count:int = 0;
					
					while (n < blockSize)
					{
						
						inStream.readBytes(block, n, blockSize - n);
						
						if ((blockSize - n) == -1)
						{
							break;
						}
						n += (blockSize - n);
					}
				}
				catch (e:Error)
				{
				}
				
				if (n < blockSize)
				{
					status = STATUS_FORMAT_ERROR;
				}
			}
			return n;
		}
		
		private function readColorTable(ncolors:int):Array
		{
			var nbytes:int = 3 * ncolors;
			var tab:Array = null;
			var c:ByteArray = new ByteArray;
			var n:int = 0;
			
			try
			{
				inStream.readBytes(c, 0, nbytes);
				n = nbytes;
			}
			catch (e:Error)
			{
			}
			
			if (n < nbytes)
			{
				status = STATUS_FORMAT_ERROR;
			}
			else
			{
				tab = new Array(256);
				var i:int = 0;
				var j:int = 0;
				
				while (i < ncolors)
				{
					var r:int = (c[j++]) & 0xff;
					var g:int = (c[j++]) & 0xff;
					var b:int = (c[j++]) & 0xff;
					tab[i++] = (0xff000000 | (r << 16) | (g << 8) | b);
				}
			}
			return tab;
		}
		
		private function readContents():void
		{
			var done:Boolean = false;
			
			while (!(done || hasError()))
			{
				
				var code:int = readSingleByte();
				
				switch (code)
				{
					
					case 0x2C:
						readImage();
						break;
					
					case 0x21:
						code = readSingleByte();
						switch (code)
						{
							case 0xf9:
								readGraphicControlExt();
								break;
							
							case 0xff:
								readBlock();
								var app:String = "";
								for (var i:int = 0; i < 11; i++)
								{
									app += block[int(i)];
								}
								if (app == "NETSCAPE2.0")
								{
									readNetscapeExt();
								}
								else
								{
									skip();
								}
								break;
							
							default:
								skip();
								break;
						}
						break;
					
					case 0x3b:
						done = true;
						break;
					
					case 0x00:
						break;
					
					default:
						status = STATUS_FORMAT_ERROR;
						break;
				}
			}
		}
		
		private function readGraphicControlExt():void
		{
			readSingleByte();
			var packed:int = readSingleByte();
			dispose = (packed & 0x1c) >> 2;
			
			if (dispose == 0)
			{
				dispose = 1;
			}
			transparency = (packed & 1) != 0;
			delay = readShort() * 10;
			transIndex = readSingleByte();
			readSingleByte();
		}
		
		private function readHeader():void
		{
			var id:String = "";
			
			for (var i:int = 0; i < 6; i++)
			{
				id += String.fromCharCode(readSingleByte());
				
			}
			
			if (!(id.indexOf("GIF") == 0))
			{
				status = STATUS_FORMAT_ERROR;
				throw new FileTypeError("Invalid file type");
				return;
			}
			readLSD();
			
			if (gctFlag && !hasError())
			{
				gct = readColorTable(gctSize);
				bgColor = gct[bgIndex];
			}
		}
		
		private function readImage():void
		{
			ix = readShort();
			iy = readShort();
			iw = readShort();
			ih = readShort();
			
			var packed:int = readSingleByte();
			lctFlag = (packed & 0x80) != 0;
			interlace = (packed & 0x40) != 0;
			lctSize = 2 << (packed & 7);
			
			if (lctFlag)
			{
				lct = readColorTable(lctSize);
				act = lct;
			}
			else
			{
				act = gct;
				
				if (bgIndex == transIndex)
				{
					bgColor = 0;
				}
			}
			var save:int = 0;
			
			if (transparency)
			{
				save = act[transIndex];
				act[transIndex] = 0;
			}
			
			if (act == null)
			{
				status = STATUS_FORMAT_ERROR;
			}
			
			if (hasError())
			{
				return;
			}
			
			decodeImageData();
			skip();
			
			if (hasError())
			{
				return;
			}
			
			frameCount++;
			
			bitmap = new BitmapData(width, height);
			
			image = bitmap;
			
			transferPixels();
			
			frames.push(new GIFFrame(bitmap, delay));
			
			if (transparency)
			{
				act[transIndex] = save;
			}
			
			resetFrame();
		
		}
		
		private function readLSD():void
		{
			width = readShort();
			height = readShort();
			
			var packed:int = readSingleByte();
			
			gctFlag = (packed & 0x80) != 0;
			gctSize = 2 << (packed & 7);
			bgIndex = readSingleByte();
			pixelAspect = readSingleByte();
		}
		
		private function readNetscapeExt():void
		{
			do
			{
				readBlock();
				
				if (block[0] == 1)
				{
					var b1:int = (block[1]) & 0xff;
					var b2:int = (block[2]) & 0xff;
					loopCount = (b2 << 8) | b1;
				}
			} while ((blockSize > 0) && !hasError());
		}
		
		private function readShort():int
		{
			return readSingleByte() | (readSingleByte() << 8);
		}
		
		private function readSingleByte():int
		{
			var curByte:int = 0;
			
			try
			{
				curByte = inStream.readUnsignedByte();
			}
			catch (e:Error)
			{
				status = STATUS_FORMAT_ERROR;
			}
			return curByte;
		}
		
		private function resetFrame():void
		{
			lastDispose = dispose;
			lastRect = new Rectangle(ix, iy, iw, ih);
			lastImage = image;
			lastBgColor = bgColor;
			
			var transparency:Boolean = false;
			var delay:int = 0;
			lct = null;
		}
		
		private function setPixels(pixels:Array):void
		{
			var count:int = 0;
			var color:int;
			pixels.position = 0;
			
			var lngWidth:int = image.width;
			var lngHeight:int = image.height;
			bitmap.lock();
			
			for (var th:int = 0; th < lngHeight; th++)
			{
				for (var tw:int = 0; tw < lngWidth; tw++)
				{
					color = pixels[int(count++)];
					bitmap.setPixel32(tw, th, color);
				}
			}
			bitmap.unlock();
		}
		
		private function skip():void
		{
			do
			{
				readBlock();
				
			} while ((blockSize > 0) && !hasError());
		}
		
		private function transferPixels():void
		{
			var dest:Array = getPixels(bitmap);
			
			if (lastDispose > 0)
			{
				if (lastDispose == 3)
				{
					var n:int = frameCount - 2;
					lastImage = n > 0 ? getFrame(n - 1).bitmapData : null;
					
				}
				
				if (lastImage != null)
				{
					var prev:Array = getPixels(lastImage);
					dest = prev.slice();
					
					if (lastDispose == 2)
					{
						var c:Number;
						c = transparency ? 0x00000000 : lastBgColor;
						image.fillRect(lastRect, c);
					}
				}
			}
			
			var pass:int = 1;
			var inc:int = 8;
			var iline:int = 0;
			
			for (var i:int = 0; i < ih; i++)
			{
				var line:int = i;
				
				if (interlace)
				{
					if (iline >= ih)
					{
						pass++;
						
						switch (pass)
						{
							case 2:
								iline = 4;
								break;
							case 3:
								iline = 2;
								inc = 4;
								break;
							case 4:
								iline = 1;
								inc = 2;
								break;
						}
					}
					line = iline;
					iline += inc;
				}
				line += iy;
				
				if (line < height)
				{
					var k:int = line * width;
					var dx:int = k + ix;
					var dlim:int = dx + iw;
					
					if ((k + width) < dlim)
					{
						dlim = k + width;
					}
					var sx:int = i * iw;
					var index:int;
					var tmp:int;
					
					while (dx < dlim)
					{
						index = (pixels[sx++]) & 0xff;
						tmp = act[index];
						
						if (tmp != 0)
						{
							dest[dx] = tmp;
						}
						dx++;
					}
				}
			}
			setPixels(dest);
		}
	}

}
package commons.file;

import java.io.File;
import java.io.FilenameFilter;

public class JarFileFilter implements FilenameFilter {
	
	private final String[] name = { "pooling", "v2" };
	
	private final String extension = "jar";
	
	@Override
	public boolean accept(final File directory, final String filename) {
		boolean fileOK = true;
		fileOK &= filename.toLowerCase().endsWith(".".concat(this.extension));
		fileOK &= filename.toLowerCase().startsWith(this.name[0]) ||
				filename.toLowerCase().startsWith(this.name[1]);
		return fileOK;
	}
	
}

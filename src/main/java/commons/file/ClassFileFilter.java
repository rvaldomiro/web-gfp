package commons.file;

import java.io.File;
import java.io.FilenameFilter;

public class ClassFileFilter implements FilenameFilter {
	
	private final String extension = "class";
	
	@Override
	public boolean accept(final File directory, final String filename) {
		return filename.toLowerCase().endsWith(".".concat(this.extension));
	}
	
}

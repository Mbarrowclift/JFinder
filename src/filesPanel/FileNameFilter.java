package filesPanel;

import java.io.File;
import java.io.FileFilter;

/**
 * Our basic FileFilter class where we filter out UNIX hidden files (any files
 * starting with '.') from being displayed in the files pane.
 *
 * In the future, I will further expand this where it will instead use regexes
 * (or possibly some other form) from JFinder initialization to filter as
 * little or as much as the developer wants for a specific file dialog.
 */
public class FileNameFilter implements FileFilter {
	/**
	 * Determines whether or not to "accept"???or display???a file given the full
	 * path.
	 * 
	 * @param  pathname The full path of the file in question
	 * @return
	 * 		True if we should display, false if not.
	 */
	@Override
	public boolean accept(File pathname) {
		if (pathname.getName().charAt(0) == '.') {
			return false;
		} else {
			return true;
		}
	}
}
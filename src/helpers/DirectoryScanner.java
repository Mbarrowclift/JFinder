package helpers;

import java.io.File;

import filesPanel.FileNameFilter;

/**
 * Provides a simple tool for obtaining Files from a given directory, either
 * based on path or a given File. Can be called statically from wherever they
 * are needed
 *
 * @author Marc Barrowclift
 */
public class DirectoryScanner {
	
	public static String fileExtensionFilter;
	
	/**
	 * Reads a directory from the given path and returns it's contents
	 *
	 * @param path 
	 *        The absolute path to the Directory you want to get the contents of
	 * @return A File Array of the contents of the Directory. Array will be
	 *     empty if no contents are in the directory, null if the given file
	 *     path was not a directory (or didn't have proper permissions, etc.)
	 */
	public static File[] getDirContents(String path) {
		File file = new File(path);
		return file.listFiles(new FileNameFilter());
	}
	
	/**
	 * Reads a directory from the given File and returns it's contents
	 *
	 * @param dir
	 *        The File (directory) you want to get the contents of
	 * @return A File Array of the contents of the Directory. Array will be
	 *     empty if no contents are in the directory, null if the given File
	 *     was not a directory (or didn't have proper permissions, etc.)
	 */
	public static File[] getDirContents(File dir) {
		return dir.listFiles(new FileNameFilter());
	}
}
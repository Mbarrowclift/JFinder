package helpers;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import main.JFinder;

/**
 * Provides a nice means of obtaining any sort of files from the system and
 * returning them in a given form. Can be called statically from wherever they
 * are needed.
 *
 * This class expects MasterControl.RESOURCES_DIR to end with a "/" and for
 * the given name of the file to load to be either:
 * 		- Just the name itself (e.g. "foo.png") if in RESOURCES_DIR root
 * 		- The relative path of the file in RESORUCES_DIR if in subdirectory
 *        (e.g. "test/foo.png")
 *
 * @author  Marc Barrowclift
 */
public class ResourceLoader {
	
	/**
	 * Loads and returns an Icon with the specified name
	 * 
	 * @param name
	 * 		The name of the Icon you want to load (Must be in resources/graphics)
	 * @return
	 * 		The javax.swing.Icon instance of the file you wanted to load
	 */
	public static Icon getIcon(String name) {
		Icon icon = null;

		try {
			icon = new ImageIcon(JFinder.RESOURCES_DIR + name);
		} catch (Exception e) {
			System.err.println("Error loading Icon " + JFinder.RESOURCES_DIR + name);
			e.printStackTrace();
		}

		return icon;
	}
	
	/**
	 * Loads and returns an Image with the specified name
	 * 
	 * @param name
	 * 		The name of the Image to load
	 * @return
	 */
	public static Image getImage(String name) {
		Image image = null;

		try {
			image = ImageIO.read(new File(JFinder.RESOURCES_DIR + name));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error loading Image " + JFinder.RESOURCES_DIR + name);
		}

		return image;
	}
}

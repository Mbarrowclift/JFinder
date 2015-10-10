package favoritesColumn;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import main.JFinder;

/**
 * A custom cell renderer for the favorite list so we can include the file
 * icon images, display a nicer file name (not full path), and include little
 * arrow to the right of directories to indicate we can progress further down
 * with them.
 *
 * We're also adjusting the padding on the list cells a bit so there's a little
 * bit more "breathing room" to the right and left of the content
 * 
 * @author Marc Barrowclift
 */
public class FavoritesListCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 1L;

	// The horizontal cell padding that best mimics OS X
	private final int OS_X_HORIZONTAL_CELL_PADDING = 6;

	// The default cell padding to fall back on if not using OS X
	private final int DEFAULT_HORIZONTAL_CELL_PADDING = 6;

	// The maximum number of pixels we want taken up by a specific file name
	private final int MAX_FILE_NAME_PIXEL_LENGTH = 155;

	// Used for determining the actual pixel length of strings
	private FontMetrics metrics;

	private int horizontalCellPadding;

	/**
	 * CONSTRUCTOR
	 *
	 * Loads the image resources we can ahead of time and sets padding
	 * depending on OS.
	 */
	public FavoritesListCellRenderer() {		
		if (JFinder.IS_OSX) {
			horizontalCellPadding = OS_X_HORIZONTAL_CELL_PADDING;
		} else {
			horizontalCellPadding = DEFAULT_HORIZONTAL_CELL_PADDING;
		}
	}
	
	/**
	 * Called inheritely in DefaultListCelRenderer for each and every cell in
	 * a the given JList. Here we format most of our custom cell attributes
	 * like file icons, text, etc.
	 *
	 * For more difficult/expanded abilities, go see paint().
	 * 
	 * @param  list         
	 *         The list object the cell in question resides in
	 * @param  value        
	 *         The cell's value, in this application's case, a File
	 * @param  index        
	 *         The index of the cell in question
	 * @param  isSelected   
	 *         Whether or not the cell in question is selected
	 * @param  cellHasFocus 
	 *         Whether or not the cell in question has focus
	 * @return
	 * 		The custom formatted JLabel which will replace the default as our cell
	 */
	@Override
	public Component getListCellRendererComponent(JList<?> list,
												  Object value,
												  int index,
												  boolean isSelected,
												  boolean cellHasFocus) {
		// Java documentation says that value CAN be null sometimes, don't do anything if it is
		if (value == null) {
			return null;
		}
		
		// Getting the default cell
		Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		
		JLabel fileLabel = (JLabel)c;
		File file = (File)value;

		// Get the smaller version of the file name (if needed)
		String fileName = shrinkString(file.getName());
		
		fileLabel.setText(fileName);
		fileLabel.setIcon(JFinder.CHOOSER.getIcon(file));

		if ((isSelected)) {
			c.setBackground(new Color(166, 179, 203));
		}
		
		//The highlight still covers this area, just pushes the text over
		setBorder(BorderFactory.createEmptyBorder(0, horizontalCellPadding, 0, horizontalCellPadding));
		
		return fileLabel;
	}

	/**
	 * Takes the given string and "shrinks" it until it's no larger than 155
	 * pixels long (that way it doesn't spill out past our hard coded column
	 * width). It shrinks these file names by omitting characters in the
	 * middle, however much needed, and replacing them with "...", so
	 * "really_long_file_name_la_de_da_my_test.txt" would be something like
	 * "really_long_..._my_test.txt"
	 * 
	 * @param  longString
	 *         The original file name
	 * @return
	 * 		The new "short" version of the file name
	 */
	private String shrinkString(String longString) {
		String shortString = longString;
		if (metrics == null) {
			if (this.getGraphics() != null) {
				metrics = this.getGraphics().getFontMetrics(this.getFont());
			}
		}

		if (metrics != null) { // Sometimes it's null, don't question it.
			int metricLength = metrics.stringWidth(shortString);
			int length = shortString.length();
			int counter = 2;
			String temp = "";
			if (metricLength > MAX_FILE_NAME_PIXEL_LENGTH) {
				while (metricLength > MAX_FILE_NAME_PIXEL_LENGTH) {
					counter += 1;
					String first = shortString.substring(0, (length/2)-counter);
					String second = shortString.substring((length/2)+counter, length);
					temp = first + "..." + second;
					metricLength = metrics.stringWidth(temp);
				}
				shortString = temp;
			}
		}

		return shortString;
	}
}
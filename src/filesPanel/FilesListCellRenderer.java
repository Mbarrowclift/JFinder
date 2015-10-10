package filesPanel;
import helpers.ResourceLoader;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import main.JFinder;

/**
 * A custom cell renderer for the lists so we can include the file icon
 * images, display a nicer file name (not full path), and include little arrow
 * to the right of directories to indicate we can progress further down with
 * them.
 *
 * We're also adjusting the padding on the list cells a bit so there's a little
 * bit more "breathing room" to the right and left of the content
 * 
 * @author Marc Barrowclift
 */
public class FilesListCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 1L;

	// The horizontal cell padding that best mimics OS X
	private final int OS_X_HORIZONTAL_CELL_PADDING = 6;

	// The default cell padding to fall back on if not using OS X
	private final int DEFAULT_HORIZONTAL_CELL_PADDING = 6;

	// The maximum number of pixels we want taken up by a specific file name
	private final int MAX_FILE_NAME_PIXEL_LENGTH = 155;

	// Sometimes the cell width's are randomly larger than usual, so hard
	// coding the usual as a work around
	private final int CELL_WIDTH = 195;

	// The relative path to the arrow resource used to indicate directories
	private final String ARROW = "/arrow.png";

	// The relative path to the selected arrow resource used to indicate directories
	private final String ARROW_SELECTED = "/arrow_selected.png";
	//private final Color LIGHT_BLUE = new Color(92, 164, 223);
	//private final Color DARK_BLUE = new Color(50, 122, 194);

	public static String extensionsToIgnore;

	private Image directoryArrow;
	private Image directoryArrowSelected;
	private int horizontalCellPadding;
	private boolean directory; // Whether or not this given cell is a directory
	private boolean selected; // Whether or not this given cell is selected
	private FontMetrics metrics; // Used for determining the actual pixel length of strings
	private FilesList parentList; // The parent object this renderer is attacted to
	//private boolean focus;

	/**
	 * CONSTRUCTOR
	 *
	 * Loads the image resources we can ahead of time and sets padding
	 * depending on OS.
	 */
	public FilesListCellRenderer(FilesList parentList) {
		this.parentList = parentList;
		directoryArrow = ResourceLoader.getImage(ARROW);
		directoryArrowSelected = ResourceLoader.getImage(ARROW_SELECTED);
		directory = false;

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
		// Java documentation says that value CAN be null sometimes, don't do
		// anything if it is
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

		if (file.isDirectory()) {
			directory = true;
		} else {
			directory = false;

			if (extensionsToIgnore != null) {
				if (!fileName.matches(extensionsToIgnore)) {
					this.setEnabled(false);
				}
			}
		}

		if ((isSelected && !parentList.isDeepestColumn())) {
			c.setBackground(Color.LIGHT_GRAY);
		}

		//The highlight still covers this area, just pushes the text over
		setBorder(BorderFactory.createEmptyBorder(0, horizontalCellPadding, 0, horizontalCellPadding));

		this.selected = isSelected;
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

	/**
	 * Overriding paint so that we can draw the cell any way we want
	 * Anything from background to selection highlights can be done
	 * here.
	 */
	@Override
	public void paint(Graphics g) {
		/*
		if (isSelected && hasFocus) {
			Graphics2D g2d = (Graphics2D) g;
			GradientPaint blueHighlight = new GradientPaint((int)(getWidth()/2 + 0.5), 0, LIGHT_BLUE, (int)(getWidth()/2 + 0.5), getHeight(), DARK_BLUE);
			g2d.setPaint(blueHighlight);

			// Draw a rectangle in the background of the cell
			g2d.fillRect(0, 0, getWidth(), getHeight());
		}
		 */
		
		super.paint(g);

		if (directory) {
			// When a scroll bar's showing, we should move the directory arrows over
			int padding = 0;
			if (parentList.numberOfFiles > 21 || (parentList.numberOfFiles > 20 && parentList.columnIndex > 2)) {
				padding = 6;
			}

			if (selected) {
				g.drawImage(directoryArrowSelected, CELL_WIDTH-padding, 7, null);
			} else {
				g.drawImage(directoryArrow, CELL_WIDTH-padding, 7, null);
			}
		}
	}
}
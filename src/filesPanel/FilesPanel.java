package filesPanel;
import helpers.DirectoryScanner;
import helpers.Scroller;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * A Panel that holds a series of JFilesColumns that denote each respective
 * subdirectory and it's contents (aka "List View"). JFilesColumns are
 * created, added, and removed dynamically from this Panel as needed
 * 
 * @author Marc Barrowclift
 */
public class FilesPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	// The desired width of the columns
	private final int COLUMN_WIDTH = 210;


	// All JFilesColumns currently being displayed to the user. Represents the
	// order in which the columns are displayed to the user as well, the order
	// matters!
	private ArrayList<FilesColumn> directoryColumns;
	
	// The layout used by the Panel to ensure each JFilesColumn is added right
	// next to each other
	private GroupLayout layout;

	// The last displayed JFilesColumn. It's 0 based, so this means if there's
	// 2 columns, curColumn would be 1.
	private int curColumn = -1;
	
	protected boolean fileSelected = false;

	// The JScrollPane used to enclose this JPanel component
	private JScrollPane filesPanelScroller;
	
	public static File[] selectedFiles;
	
	/**
	 * CONSTRUCTOR
	 *
	 * Initializes the JFilesPanel to the default directory/path (home directory)
	 */
	public FilesPanel() {
//		this(System.getProperty("user.home"));
		// Prepare the layout used by the panel
		layout = new GroupLayout(this);
		layout.setAutoCreateGaps(false);
		layout.setAutoCreateContainerGaps(false);
		setLayout(layout);

		// Creating the JScrollPane and adding this panel to it
		filesPanelScroller = new JScrollPane(this);
		filesPanelScroller.setDoubleBuffered(true); // Not sure if this results in any actual improvements, test later
		filesPanelScroller.setBorder(BorderFactory.createEmptyBorder(0,0,0,0)); // Get rid of the ugly, default JScrollPane border
		filesPanelScroller.getHorizontalScrollBar().setUnitIncrement(2); // Make the scroll nicer
		filesPanelScroller.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 7));

		// Various Settings
		setBackground(Color.WHITE);

		filesPanelScroller.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 0, Color.LIGHT_GRAY));
		filesPanelScroller.setPreferredSize(new Dimension(0, 360));
		// Adding the column(s) from the initial path to the panel
		directoryColumns = new ArrayList<FilesColumn>();
	}

	/**
	 * CONSTRUCTOR
	 *
	 * Initializes the JFilesPanel to the given initial path/directory
	 *
	 * XXX NOT ACTUALLY RESPECTING INITIAL PATH IF DEPTH LARGER THAN 1 XXX
	 * 
	 * @param initialPath
	 *        The initial path we want JFinder to display
	 */
	public FilesPanel(String initialPath) {
		// Prepare the layout used by the panel
		layout = new GroupLayout(this);
		layout.setAutoCreateGaps(false);
		layout.setAutoCreateContainerGaps(false);
		setLayout(layout);

		// Creating the JScrollPane and adding this panel to it
		filesPanelScroller = new JScrollPane(this);
		filesPanelScroller.setDoubleBuffered(true); // Not sure if this results in any actual improvements, test later
		filesPanelScroller.setBorder(BorderFactory.createEmptyBorder(0,0,0,0)); // Get rid of the ugly, default JScrollPane border
		filesPanelScroller.getHorizontalScrollBar().setUnitIncrement(2); // Make the scroll nicer
		filesPanelScroller.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 7));

		// Various Settings
		setBackground(Color.WHITE);
		filesPanelScroller.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 0, Color.LIGHT_GRAY));
		filesPanelScroller.setPreferredSize(new Dimension(0, 360));
		
		// Adding the column(s) from the initial path to the panel
		directoryColumns = new ArrayList<FilesColumn>();
		addColumn(DirectoryScanner.getDirContents(initialPath));
	}

	/**
	 * Takes a given array of Files and adds them all to a new JFilesColumn,
	 * then adds that new column to the very right/end of the panel and end of
	 * the directoryColumns ArrayList.
	 * 
	 * @param files
	 *        An Array of Files that you want to add as a new column to the panel
	 */
	public void addColumn(File[] files) {
		// Creates and adds a new Column with the given files
		curColumn++;
		FilesColumn newColumn = new FilesColumn(this, files, curColumn);
		directoryColumns.add(newColumn);
		
		// Refresh the UI
		refreshPanel(-1);
		
		// Scroll to the right (if needed) so the user can see all the content
		// they just clicked and want to see (also makes the program more
		// responsive to the user)
		Scroller.scrollToRight(filesPanelScroller);
	}
	
	/**
	 * Takes a given array of Files and adds them all to a new JFilesColumn, then
	 * adds that new column at a specific index in the directoryColumns ArrayList.
	 * Because of this, all columns to the *right* of the the newly added column will
	 * be removed from the directoryColumns and therefore removed from the panel.
	 *
	 * This happens when the user clicks on a new file or directory in a
	 * *previous* column (say they're on curColumn 3 and click a new directory
	 * in column 0, all columns to the right of column 0 should be removed and
	 * at the end add the new column. In this manner the columns to the right
	 * of the index have been replaced).
	 * 
	 * @param files
	 *        An Array of Files that you want to add as a new column to the given index
	 * @param index
	 *        The index where you want to insert the new column and delete all
	 *        old columns to the right of.
	 */
	public void addColumnAt(File[] files, int index) {
		// If there's no columns to remove in place of this new column, then just append to the end.
		if (curColumn == index) {
			addColumn(files);
		// We're adding it somewhere in between, we have columns we need to remove
		} else {
			// Removing all columns to the right of index
			int size = directoryColumns.size();
			for (int i = index+1; i < size; i++) { // All columns TO THE RIGHT of the selected column 
				directoryColumns.remove(index+1); // MUST BE INDEX, NOT i (size() is changing since we're removing!)
			}
			
			// Adding new column at index
			curColumn = index+1;
			FilesColumn newColumn = new FilesColumn(this, files, index+1);
			directoryColumns.add(newColumn);
			
			// Refresh the UI
			refreshPanel(-1);

			// Scroll to the right (if needed) so the user can see all the
			// content they just clicked and want to see (also makes the
			// program more responsive to the user)
			Scroller.scrollToRight(filesPanelScroller);
		}
		
		updateFocus();
	}
	
	private void updateFocus() {
		directoryColumns.get(directoryColumns.size() - 1).requestFocus();
	}

	/**
	 * Removes all JFilesColumns from this panel, and then adds all the
	 * JFilesColumns in directoryColumns back to the panel.
	 */
	public void refreshPanel(int index) {
		this.removeAll();
		int size = directoryColumns.size();
		
		if (index != -1) {
			for (int i = index+1; i < size; i++) { // All columns TO THE RIGHT of the selected column 
				directoryColumns.remove(index+1); // MUST BE INDEX, NOT i (size() is changing since we're removing!)
			}
			
			curColumn = index;
			size = directoryColumns.size();
		}

		// Adds back in one by one all componetns in directoryColumns
		ParallelGroup pGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
		vGroup.addGroup(pGroup);
		for (int i = 0; i < size; i++) {
			pGroup.addComponent(directoryColumns.get(i).getComponentWithScroller(),
								346,
								346,
								346);
			hGroup.addComponent(directoryColumns.get(i).getComponentWithScroller(),
								COLUMN_WIDTH,
								COLUMN_WIDTH,
								COLUMN_WIDTH);
		}
		layout.setHorizontalGroup(hGroup);
		layout.setVerticalGroup(vGroup);
	}

	/**
	 * Returns the *whole* component, meaning the JScrollPane housing the
	 * actual JPanel.
	 * 
	 * YOU MUST CALL THIS WHEN ADDING THIS COMPONENT TO ANOTHER, DO NOT JUST
	 * ADD THE JFILESPANEL INSTANCE DIRECTLY. This would bypass the
	 * JScrollPane initialized and set up within this component, please use
	 * the returned value from this method for adding to other components.
	 * 
	 * @return The JScrollPane enclosing this component/panel.
	 */
	public JScrollPane getComponentWithScroller() {
		return filesPanelScroller;
	}
	
	public boolean isDeepestColumn(int columnIndex) {
		if (((columnIndex == curColumn-1 || columnIndex == curColumn) && !fileSelected) ||
				(columnIndex == curColumn && fileSelected)) {
			return true;
		} else {
			return false;
		}
	}
}
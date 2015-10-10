package filesPanel;

import helpers.DirectoryScanner;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import main.JFinder;

/**
 * The custom JList we use for the columns in the files panel.
 *
 * @author Marc Barrowclift
 */
public class FilesList extends JList<File> {

	private static final long serialVersionUID = 1L;

	// The cell height that best matches the OS X lists
	private final int OS_X_ROW_HEIGHT = 19;

	// The default cell height to fall back on when on an unrecognized system 
	private final int DEFAULT_ROW_HEIGHT = 19;

	// Our listener to detect list selection events and add a column where
	// needed
	private ListSelectionListener fileSelectionListener;
	
	// Our listener to detect key traversal with the arrow keys and move the
	// selection accordingly
	private KeyListener traversalKeysListener;

	// The files panel instance so we can add columns with ease
	private FilesPanel filesPanelRoot;

	// This instance, so we can access it within the listeners
	private FilesList filesList;

	// The height of the list cells
	private int rowHeight;

	// The index of the parent column in the files panel. DOES NOT CHANGE!
	protected int columnIndex;
	
	// The number of files in the current view
	protected int numberOfFiles;
	
	private FilesListCellRenderer renderer;

	/**
	 * CONSTRUCTOR
	 * @param  model
	 *         The list model to use for this JList
	 * @param  filesPanelRoot
	 *         The files panel instance so we can add columns with ease
	 * @param  columnIndex
	 *         The index of the parent column in the files panel
	 */
	public FilesList(DefaultListModel<File> model, FilesPanel filesPanelRoot, int columnIndex) {
		super(model); // passing the model to the default JList constructor
		
		if (JFinder.IS_OSX) {
			rowHeight = OS_X_ROW_HEIGHT;
		} else {
			rowHeight = DEFAULT_ROW_HEIGHT;
		}
		
		this.columnIndex = columnIndex;
		filesList = this;
		numberOfFiles = model.getSize();
		this.filesPanelRoot = filesPanelRoot;
		renderer = new FilesListCellRenderer(this);

		setCellRenderer(renderer);
		setFixedCellHeight(rowHeight);
		setBackground(Color.WHITE);		
		
//		if (extensionsToIgnore != null) {
//			int size = model.getSize();
//			for (int i = 0; i < size; i++) {
//				if (!model.get(i).getName().matches(extensionsToIgnore)) {
//				}
//			}
//			if (!fileName.matches(extensionsToIgnore)) {
//				fileLabel.setEnabled(false);
//			}
//		}
		
		initListeners();
	}
	
	/**
	 * Initializes all listeners used by the JList
	 */
	private void initListeners() {
		fileSelectionListener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int selectedIndex = filesList.getSelectedIndex();
				if (selectedIndex == -1) {
					return;
				}
				
				Object value = filesList.getModel().getElementAt(selectedIndex);
				Component comp = renderer.getListCellRendererComponent(filesList, value, selectedIndex, true, true);
				if (!comp.isEnabled()) {
					filesList.clearSelection();
					return;
				}
				
				// Get the selected file's path, then add it's contents as a new column to the files panel
				ArrayList<File> files = null;
				try {
					files = ((ArrayList<File>)filesList.getSelectedValuesList());
				} catch (Exception e5) {
					e5.printStackTrace();
				}
				
				FilesPanel.selectedFiles = null;
				int size = files.size();
				if (size > 1) {
					FilesPanel.selectedFiles = DirectoryScanner.getDirContents(files.get(size-1));
				} else {
					FilesPanel.selectedFiles = DirectoryScanner.getDirContents(files.get(0));
				}

				if (FilesPanel.selectedFiles == null) {
					filesPanelRoot.fileSelected = true;
					filesPanelRoot.refreshPanel(columnIndex);
					JFinder.enableSaveOrLoadButton(true);
				} else {
					JFinder.enableSaveOrLoadButton(false);
					filesPanelRoot.fileSelected = false;
					// Only do this code once (while adjusting)
					if (e.getValueIsAdjusting()) {
						return;
					}
					filesPanelRoot.addColumnAt(FilesPanel.selectedFiles, columnIndex);
				}
				
				if (FilesPanel.selectedFiles == null) {
					FilesPanel.selectedFiles = files.toArray(new File[size]);
					
					if (FilesPanel.selectedFiles.length == 0) {
						FilesPanel.selectedFiles = null;
					}
				}
			}
		};
		this.addListSelectionListener(fileSelectionListener);
		
		traversalKeysListener = new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				System.out.println("HI");
			}
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
			@Override
			public void keyReleased(KeyEvent e) {
				
			}		
		};
		this.addKeyListener(traversalKeysListener);
	}
	
	public boolean isDeepestColumn() {
		return filesPanelRoot.isDeepestColumn(columnIndex);
	}
}

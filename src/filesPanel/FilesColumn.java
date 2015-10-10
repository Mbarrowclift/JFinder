package filesPanel;

import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;

/**
 * The columns used in the FilesPanel to hold the contents of directories.
 * This class is a wrapper of sorts for the custom JList "FilesList" and list
 * model where all the actual files are stored and displayed and the
 * JScrollPane which encompasses them.
 *
 * DO NOT ADD THIS COMPONENT TO FRAMES/DIALOGS DIRECTLY! Instead, call
 * getComponentWithScroller() to get a scroller with the component inside.
 *
 * @author Marc Barrowclift
 */
public class FilesColumn {

	private FilesList directoryList;
	private DefaultListModel<File> directoryListModel;
	private FilesListScrollPane directoryScrollPane;
	protected int columnIndex;
	
	/**
	 * CONSTRUCTOR
	 * 
	 * @param  filesPanelRoot
	 *         The filesPanel root so we can add or remove columns in the
	 *         JList selection listeners
	 * @param  files
	 *         The files that should be added to the JFilesList 
	 * @param  columnIndex
	 *         The index of this given column. Never changes!
	 */
	public FilesColumn(FilesPanel filesPanelRoot, File[] files, int columnIndex) {
		this.columnIndex = columnIndex;
		
		directoryListModel = new DefaultListModel<File>();
		for (int i = 0; i < files.length; i++) {
			directoryListModel.add(i, files[i]);
		}
		directoryList = new FilesList(directoryListModel, filesPanelRoot, columnIndex);
		directoryScrollPane = new FilesListScrollPane(directoryList);
	}

	/**
	 * Returns the *whole* component, meaning the JScrollPane housing the
	 * actual JList.
	 * 
	 * YOU MUST CALL THIS WHEN ADDING THIS COMPONENT TO ANOTHER, DO NOT JUST
	 * ADD THE JFILESCOLUMN INSTANCE DIRECTLY. This would bypass the
	 * JScrollPane initialized and set up within this component, please use
	 * the returned value from this method for adding to other components.
	 * 
	 * @return The JScrollPane enclosing this component/JList.
	 */
	public JScrollPane getComponentWithScroller() {
		return directoryScrollPane;
	}
	
	public void requestFocus() {
		directoryList.requestFocus();
	}
}

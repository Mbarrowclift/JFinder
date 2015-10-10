package favoritesColumn;

import filesPanel.FilesPanel;
import helpers.DirectoryScanner;

import java.awt.Color;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import main.JFinder;

/**
 * A custom JList that displays the favorite locations of the user. Clicking
 * on one will clear any existing columns from the files panel and push the
 * contents of the selected favorite directory.
 * 
 * @author Marc Barrowclift
 */
public class FavoritesList extends JList<File> {

	private static final long serialVersionUID = 1L;
	
	// The cell height that best matches the OS X lists
	private final int OS_X_ROW_HEIGHT = 19;

	// The default cell height to fall back on when on an unrecognized system 
	private final int DEFAULT_ROW_HEIGHT = 19;

	// The height of the list cells
	private int rowHeight;
	
	private FavoritesList favoritesList;
	private ListSelectionListener favoriteSelectionListener;
	private FilesPanel filesPanel;
	
	// Keep track of the previous selection so we know if the user clicked a
	// *new* list item instead of the one already selected
	private int previousSelection = -1;
	
	/**
	 * CONSTRUCTOR
	 * 
	 * @param model
	 * 		The model with the contents you want this list to display
	 * @param filesPanel
	 * 		Reference to the files panel so we can remove and add columns
	 * 		based on events
	 */
	public FavoritesList(DefaultListModel<File> model, FilesPanel filesPanel) {
		super(model);
		favoritesList = this;
		this.filesPanel = filesPanel; 
		
		if (JFinder.IS_OSX) {
			rowHeight = OS_X_ROW_HEIGHT;
		} else {
			rowHeight = DEFAULT_ROW_HEIGHT;
		}
		
		FavoritesListCellRenderer renderer = new FavoritesListCellRenderer();
		
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setFixedCellHeight(rowHeight);
		setCellRenderer(renderer);
		setBackground(new Color(224, 227, 232));
		initListener();
	}
	
	/**
	 * Initializes and adds all listeners used by the Favorites List
	 */
	private void initListener() {
		favoriteSelectionListener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				File selectedFile = null;
				if (!e.getValueIsAdjusting()) {
					return;
				}
				
				// Do not use e.getLast/FirstIndex(), straight up does not work
				if (previousSelection == favoritesList.getSelectedIndex()) {
					return;
				} else {
					previousSelection = favoritesList.getSelectedIndex();
				}
				
				try {
					selectedFile = ((File)favoritesList.getSelectedValue());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				File[] files = DirectoryScanner.getDirContents(selectedFile);
				if (files != null) {
					filesPanel.addColumnAt(files, -1);
				}
			}
		};
		this.addListSelectionListener(favoriteSelectionListener);
	}
}

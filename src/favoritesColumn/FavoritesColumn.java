package favoritesColumn;

import filesPanel.FilesPanel;
import helpers.DirectoryScanner;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;

/**
 * The column used for the "Favorites" list of the dialog. This class is a
 * wrapper of sorts for the custom JList "FavoritesList" and list model where
 * all the actual favorite files are stored and displayed as well as the
 * JScrollPane which encompasses them.
 * 
 * DO NOT ADD THIS COMPONENT TO FRAMES/DIALOGS DIRECTLY! Instead, call
 * getComponentWithScroller() to get a scroller with the component inside.
 * 
 * @author Marc Barrowclift
 */
public class FavoritesColumn {
	
	private final int FAVORITE_COLUMN_WIDTH = 205;
	private final int FAVORITE_COLUMN_HEIGHT = 425;
	
	private FavoritesList favoritesList;
	private DefaultListModel<File> favoritesListModel;
	private JScrollPane favoritesScrollPane;
	
	/**
	 * CONSTRUCTOR
	 * 
	 * @param filesPanel
	 *      The FilesPanel reference so that we can push new columns to the
	 * 		main files panel on favorite list item clicks.
	 */
	public FavoritesColumn(FilesPanel filesPanel) {
		String initialPath = System.getProperty("user.home");
		File[] favorites = DirectoryScanner.getDirContents(initialPath);
		
		favoritesListModel = new DefaultListModel<File>();
		for (int i = 0; i < favorites.length; i++) {
			favoritesListModel.add(i, favorites[i]);
		}
		
		favoritesList = new FavoritesList(favoritesListModel, filesPanel);
		favoritesScrollPane = new JScrollPane(favoritesList);
		favoritesScrollPane.setPreferredSize(new Dimension(FAVORITE_COLUMN_WIDTH, FAVORITE_COLUMN_HEIGHT));
		favoritesScrollPane.setMaximumSize(new Dimension(FAVORITE_COLUMN_WIDTH, FAVORITE_COLUMN_HEIGHT));
		favoritesScrollPane.setMinimumSize(new Dimension(FAVORITE_COLUMN_WIDTH, FAVORITE_COLUMN_HEIGHT));
		favoritesScrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
		favoritesScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 7));
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
		return favoritesScrollPane;
	}
}
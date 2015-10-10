package main;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import favoritesColumn.FavoritesColumn;
import filesPanel.FilesListCellRenderer;
import filesPanel.FilesPanel;

/**
 * The main JFinder window where all of JFinder's components are enclosed and
 * displayed
 *
 * NOTE: When going to add custom components such as the filesPanel to the
 * frame, you MUST add the return of the call getComponentWithScroller() on
 * those components. Each of those custom components hold and manage their own
 * scrollers (in this way when these components have to modify the scrollers
 * for autoscrolling they do not have to rely on JFinder and calling it from
 * here.
 * 
 * @author Marc Barrowclift
 */
public class JFinder extends JDialog {
	private static final long serialVersionUID = 1L;
	
	// Determines whether or not this is running on OS X. We will format components differently if on Mac
	public final static boolean IS_OSX = System.getProperty("os.name").toLowerCase().contains("mac");
		
	// The root directory for any external resource files used by the app
	public final static String RESOURCES_DIR = "./res";
		
	// The JFileChooser instance we use ONLY to get the system icons for each file (so we don't use generic icons)
	public final static JFileChooser CHOOSER = new JFileChooser();
	
	// Options for Selection Modes
	public final static int SINGLE_SELECTION = 0;
	public final static int MULTIPLE_INTERVAL_SELECTION = 1;
	public final static int MULTIPLE_SELECTION = 2;
	private final static int DEFAULT_SELECTION_MODE = SINGLE_SELECTION;
//	private int selectionMode;
	
	// Options for File Type Selection Modes
	public final static int FILES_ONLY = 0;
	public final static int DIRECTORIES_ONLY = 1;
	public final static int FILES_AND_DIRECTORIES = 2;
	private final static int DEFAULT_FILES_OR_DIRECTORIES = FILES_ONLY;
//	private int filesOrDirectories;
	
	// Window Titles
	private final String DEFAULT_SAVE_TITLE = "Save";
	private final String DEFAULT_LOAD_TITLE = "Load";
	private String title;
	
	// Is it a save dialog or a load dialog?
	private boolean save;
	
	// Components
	private FilesPanel filesPanel;
	private JFinder jFinder;
	private FavoritesColumn favoritesList;
	private JButton cancel;
	private static JButton saveOrLoad; // Must be static so sub components can disable or enable it when needed
	private ActionListener cancelListener;
	private ActionListener saveOrLoadListener;
	
	/**
	 * The bare-bones needed arguments to create a default JFinder dialog.
	 * 
	 * @param parent
	 * 		The parent component from which this JFinder dialog is being called
	 * @param save
	 * 		Whether or not it's a save or load dialog
	 * @param title
	 * 		The title the window should have
	 */
	public JFinder(JFrame parent,
				   boolean save) {
		this(parent, save, null, DEFAULT_SELECTION_MODE, DEFAULT_FILES_OR_DIRECTORIES, System.getProperty("user.home"), null);
	}
	
	/**
	 * JFinder with all available customizations.
	 * 
	 * @param parent
	 * 		The parent component from which this JFinder dialog is being
	 * 		called
	 * @param save
	 * 		Whether or not it's a save or load dialog
	 * @param title
	 * 		The title the window should have
	 * @param selectionMode
	 * 		JFinder.SINGLE_SELECTION, JFinder.MULTIPLE_SELECTION, or
	 * 		JFinder.MULTIPLE_INTERVAL_SELECTION
	 * @param filesOrDirectories
	 * 		Whether or not we should allow directories, files, or both to be
	 * 		loaded. You can use JFinder.FILES_ONLY, JFinder.DIRECTORIES_ONLY,
	 * 		or JFinder.FILES_AND_DIRECTORIES.
	 * @param startingDirectory
	 * 		The directory path you want JFinder to open to (perhaps an
	 * 		application workspace). Defaults to user home if null.
	 * @param fileExtensionFilter
	 * 		Any string regex that matches files that you want to select. This
	 * 		could be anything from files with names that match a specific
	 * 		pattern you want or files with certain extensions. Can be null, in
	 * 		which case no filtering will be done.
	 */
	public JFinder(JFrame parent,
				   boolean save,
				   String title,
				   int selectionMode,
				   int filesOrDirectories,
				   String startingDirectory,
				   String fileExtensionFilter) {
		super(parent, title, true);
		jFinder = this;
		
		// All the custom arguments passed from the developer
		this.save = save;
		if (title == null) {
			if (save) {
				title = DEFAULT_SAVE_TITLE;
			} else {
				title = DEFAULT_LOAD_TITLE;
			}
		} else {
			this.title = title;
		}
//		this.selectionMode = selectionMode;
//		this.filesOrDirectories = filesOrDirectories;
		FilesListCellRenderer.extensionsToIgnore = fileExtensionFilter;
		if (startingDirectory == null) {
			filesPanel = new FilesPanel();
		} else {
			filesPanel = new FilesPanel(startingDirectory);
		}
		
		initDialog();	
		addComponents();
		initListeners();
	}
	
	/**
	 * Initializes and prepares everything relating to the frame
	 */
	private void initDialog() {
		this.setSize(720, 425);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setTitle(title);
		this.setVisible(false);
		this.setLocationRelativeTo(null);
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
	}
	
	/**
	 * Initializes and adds all components used by JFinder to the dialog
	 */
	private void addComponents() {
		favoritesList = new FavoritesColumn(filesPanel);
		
		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
		cancel = new JButton("Cancel");
		
		if (save) {
			saveOrLoad = new JButton("Save");
		} else {
			saveOrLoad = new JButton("Open");
		}
		
		buttons.add(cancel);
		buttons.add(saveOrLoad);
		saveOrLoad.setEnabled(false);
		
		JPanel rightSide = new JPanel();
		rightSide.setLayout(new BoxLayout(rightSide, BoxLayout.Y_AXIS));
		rightSide.add(filesPanel.getComponentWithScroller());
		rightSide.add(buttons);
		
		add(favoritesList.getComponentWithScroller());
		add(Box.createHorizontalStrut(-1));
		add(rightSide);
		
		this.getRootPane().setDefaultButton(saveOrLoad);
	}
	
	/**
	 * Initializes and adds all listeners to direct children components of this dialog
	 */
	private void initListeners() {
		cancelListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FilesPanel.selectedFiles = null; // The user Cancelled the dialog, therefore there's no selected files
				jFinder.setVisible(false);
			}
		};
		cancel.addActionListener(cancelListener);
		
		saveOrLoadListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jFinder.setVisible(false);
			}
		};
		saveOrLoad.addActionListener(saveOrLoadListener);
	}
	
	/**
	 * Enables or disables the "Open"/"Save" button. Should be called by any
	 * subcomponents that recognize that a file is selected that is acceptable
	 * to be opened or saved to.
	 * 
	 * @param enable
	 * 		true or false, depending on whether or not you want the button to
	 * 		be enabled
	 */
	public static void enableSaveOrLoadButton(boolean enable) {
		saveOrLoad.setEnabled(enable);
	}
	
	/**
	 * Get all selected files from JFinder. This should be used first as the
	 * method to get files to load/save to when JFinder is closed.
	 * 
	 * @return
	 * 		The file(s) the user selected and wants to load/save to. If single
	 * 		selection was specified, it will be an array of length 1 with the
	 * 		single file. Can be null if the user clicked the "Cancel" button
	 * 		instead of actually specifying a location to save or load.
	 */
	public File[] getSelectedFiles() {
		if (FilesPanel.selectedFiles == null) {
			return null;
		}

		return FilesPanel.selectedFiles;
	}
}
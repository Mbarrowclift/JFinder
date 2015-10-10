package main;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * A very simple text loader to demonstrate how to invoke JFinder and
 * access customize it's actions.
 * 
 * @author Marc Barrowclift
 */
public class Example extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JButton load;
	private JTextArea textArea;
	private JScrollPane textAreaScroll;
	private ActionListener loadListener;
	private JFinder loadDialog;

	public static void main(String[] args) {		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Example example = new Example();
				example.setVisible(true);
			}
		});
	}
	
	public Example() {
		initWindow();
		addAllComponents();
		initListeners();
	}
	
	private void initWindow() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500, 470);
		this.setTitle("My Really Simple Text File Reader");
		this.setVisible(false);
		this.setLocationRelativeTo(null);
	}
	
	private void addAllComponents() {
		JPanel container = new JPanel();
		container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textAreaScroll = new JScrollPane(textArea);
		textAreaScroll.setPreferredSize(new Dimension(400, 400));
		textAreaScroll.setMaximumSize(new Dimension(400, 400));
		textAreaScroll.setMinimumSize(new Dimension(400, 400));
		textAreaScroll.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		load = new JButton("Load Text Document");
		load.setAlignmentX(Component.CENTER_ALIGNMENT);

		loadDialog = new JFinder(this,
								 false,
								 "Load Text Document",
								 JFinder.SINGLE_SELECTION,
								 JFinder.FILES_ONLY,
								 null,
								 ".*(txt|rtf)$");
		
		container.add(textAreaScroll);
		container.add(load);
		this.add(container);
	}
	
	private void initListeners() {
		loadListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadDialog.setVisible(true);
				
				File[] selectedFile = loadDialog.getSelectedFiles();
				if (selectedFile != null) {
					String contents = "";

					try {
						BufferedReader reader = new BufferedReader(new FileReader(selectedFile[0]));

						String line = "";
						while ((line = reader.readLine()) != null) {
							contents = contents.concat(line + "\n");
						}
						reader.close();
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
					textArea.setText(contents);
				}
			}
		};
		load.addActionListener(loadListener);
	}
}

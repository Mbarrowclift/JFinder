package helpers;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 * Simple class to ensure that the scroll pane's scroll position is ACTUALLY
 * set (setAutoscrolls(true) doesn't do dick).
 *
 * This is based on the heavily modified original idea of Carl Smotricz on
 * StackOverflow.com. Many thanks to him and his help for understanding how to
 * properly manipulate JScrollPanes. I am sadly unable to find the original
 * post online, but rest assured the credit for the framework I built this
 * class on should go to him.
 * 
 * @author Carl Smotricz (from StackOverflow.com, see above)
 * @author Marc Barrowclift
 */
public class Scroller {
	
	/**
	 * Takes a given scrollPane and scrolls the horizontal scrollbar all the
	 * way to the right.
	 * 
	 * @param scrollPane The scrollPane we want to autoscroll
	 */
	public static void scrollToRight(final JScrollPane scrollPane) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					JScrollBar horizontal = scrollPane.getHorizontalScrollBar();
					horizontal.setValue(horizontal.getMaximum());
				} catch (Exception e) {
					
				}
			}
		});
	}
}

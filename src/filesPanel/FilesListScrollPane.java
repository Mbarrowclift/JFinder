package filesPanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

/**
 * A JScrollPane that will bubble a mouse wheel scroll event to the parent
 * JScrollPane if it's in the scroll direction of the parent scollPane (in
 * this implementation's case, up and down will be processed by the sub
 * JScrollPane, left and right will be passed up instead to the parent
 * JScrollPane).
 *
 * To implement, a parent JScrollPane can be enclosed in the default
 * JScrollPane, but the subcomponent you wish to also scroll should instead be
 * enclosed in this scroll pane. In this way, when the sub scroll pane detects
 * a scroll that should instead be passed to the parent, it can generate the
 * appropriate event for the parent and ignore the event for itself.
 *
 * Without this code, all scrolls will be trapped within the sub JScrollPane
 * if the mouse is hovering hover it (terrible UI).
 *
 * This is a slightly modified version of the Nemi's PDControlScrollPane
 * posted on StackOverflow.com. Many thanks to them for their help in this
 * project. Here's the link to the original post:
 * 
 * http://stackoverflow.com/questions/1377887/jtextpane-prevents-scrolling-in-the-parent-jscrollpane
 *
 * @author  Nemi (from StackOverflow.com, see link above)
 * @author  Marc Barrowclift
 */
public class FilesListScrollPane extends JScrollPane {

	private static final long serialVersionUID = 1L;

	// The value associated with a horizontal scroll (verical is 0)
	private final int HORIZONTAL_DIRECTION = 1;

	// The direction the previous scroll was in (was start off with -1 since
	// there is no previous direction on load)
	private int previousScroll = -1;

	/**
	 * CONSTRUCTOR
	 * 
	 * @param  view
	 *         The component we wish to add to this JScrollPane
	 */
	public FilesListScrollPane(Component view) {
		super(view); // Passing the componet to the default JScrollPane constructor

		addMouseWheelListener(new PDMouseWheelListener());
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
		getVerticalScrollBar().setUnitIncrement(2); // Make the scroll nicer
		// Make the scroll bar not as wide
		getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
	}

	/**
	 * Anonymous inner class to listener for the direction of a scroll for this
	 * component
	 */
	class PDMouseWheelListener implements MouseWheelListener {

		// The JScrollPane enclosing this PDControllScrollPane
		private JScrollPane parentScrollPane; 

		/**
		 * Obtains the parent JScrollPane housing this PDControlScrollPane
		 * instance (so we can pass cloned events to it if necessary).
		 * 
		 * @return The parent JScrollPane instance (if one exists)
		 */
		private JScrollPane getParentScrollPane() {
			if (parentScrollPane == null) {
				Component parent = getParent();
				while (!(parent instanceof JScrollPane) && parent != null) {
					parent = parent.getParent();
				}
				parentScrollPane = (JScrollPane)parent;
			}
			return parentScrollPane;
		}

		/**
		 * Event for touch pad or mouse wheel scrolling.
		 * 
		 * @param e The event currently being processed
		 */
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {

			JScrollPane parent = getParentScrollPane();
			if (parent != null) {
				/*
				 * Only dispatch if we have reached top/bottom on previous scroll
				 */
				if (previousScroll == HORIZONTAL_DIRECTION || e.getModifiers() == HORIZONTAL_DIRECTION) {
					parent.dispatchEvent(cloneEvent(e));
				}

				previousScroll = e.getModifiers();
			}
			/* 
			 * If parent scrollpane doesn't exist, remove this as a listener.
			 * We have to defer this till now (vs doing it in constructor) 
			 * because in the constructor this item has no parent yet.
			 */
			else {
				FilesListScrollPane.this.removeMouseWheelListener(this);
			}
		}

		/**
		 * Dulplicates the event from this scrollPane and passes it instead to
		 * the parent JScrollPane as a "new" event.
		 * 
		 * @param  e
		 *         The event we want to clone and pass up to the parent JScrollPane
		 * @return   The new, cloned event
		 */
		private MouseWheelEvent cloneEvent(MouseWheelEvent e) {
			return new MouseWheelEvent(getParentScrollPane(), e.getID(), e
					.getWhen(), e.getModifiers(), 1, 1, e
					.getClickCount(), false, e.getScrollType(), e
					.getScrollAmount(), e.getWheelRotation());
		}
	}
}
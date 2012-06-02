package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/**
 * Main window of the hymnal. Contains a tool bar and area to display hymns.
 *
 * @author Tim Macdonald <tim.macdonald@my.wheaton.edu>
 */
public class SplitLeafHymnal extends JFrame
{
	private HymnPanel hymnPanel;
	private MainToolBar toolBar;

	/**
	 * Instantiates components, maximizes the window, and performs other bookkeeping.
	 */
	public SplitLeafHymnal() {
		super("JHymnal");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setLayout(new BorderLayout());
		this.hymnPanel = new HymnPanel();
		final JScrollPane hymnScroller = new JScrollPane(this.hymnPanel,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		hymnScroller.getHorizontalScrollBar().setUnitIncrement(20);
		hymnScroller.getVerticalScrollBar().setUnitIncrement(20);

		JPanel toolBarPanel = new JPanel(new BorderLayout());
		this.toolBar = new MainToolBar(this.hymnPanel);
		toolBarPanel.add(this.toolBar, BorderLayout.PAGE_START);

		this.add(toolBarPanel, BorderLayout.NORTH);
		this.add(hymnScroller, BorderLayout.CENTER);
		this.pack();
		this.setVisible(true);
	}
}

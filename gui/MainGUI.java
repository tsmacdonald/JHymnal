package gui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Driver class that instantiates and displays the main frame.
 *
 * @author Tim Macdonald <tim.macdonald@my.wheaton.edu>
 */
public class MainGUI
{
	/**
	 * Instantiates and displays a {@link SplitLeafHymnal} on the EDT.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Thread(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(
						UIManager.getSystemLookAndFeelClassName());
				}
				catch(Exception e) {
					System.err.println("L&F trouble.");
					e.printStackTrace();
				}
				new SplitLeafHymnal();
			}
		}));
	}
}

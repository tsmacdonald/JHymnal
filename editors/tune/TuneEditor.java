package editors.tune;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Driver class that instantiates and displays the main frame.
 *
 * @author Tim Macdonald <tim.macdonald@my.wheaton.edu>
 */
public class TuneEditor extends JFrame
{
	private MusicPanel musicPanel;
	private EditingToolBar toolBar;
	/**
	 * Instantiates components, maximizes the window, and performs other bookkeeping.
	 */
	public TuneEditor() {
		super();
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setLayout(new BorderLayout());

		WizardDialog wizardDialog = new NewTuneDialog(this);
		wizardDialog.setDefaultCloseOperation(wizardDialog.DISPOSE_ON_CLOSE);
		wizardDialog.setVisible(true);
		Wizard w = wizardDialog.getWizard();
		if(w == null) {
			dispose();
		}
		else {
			this.setTitle("TEmacs - " + w.getName());
	
			this.musicPanel = new MusicPanel(w.getName());
			this.musicPanel.setKey(w.getKeySignature());
			this.musicPanel.setMeter(w.getMeter());
			this.musicPanel.setTimeSignature(w.getTimeSignature());
			this.musicPanel.setStartingBeat(w.getStartingBeat());
		
	
	
	
			final JScrollPane musicScroller = new JScrollPane(this.musicPanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
			JPanel toolBarPanel = new JPanel(new BorderLayout());
			this.toolBar = new EditingToolBar(this.musicPanel, w.getKeySignature());
			toolBarPanel.add(this.toolBar, BorderLayout.PAGE_START);
	
	//		NoteCreator noteCreator = toolBar.getNoteCreator(); //Everything
	//		//about this class relationship is awful, and I apologize sincerely
	//		noteCreator.setKeySignature(w.getKeySignature());
	//		noteCreator.setMeter(w.getMeter());
	//		noteCreator.setTimeSignature(w.getTimeSignature());
	//		noteCreator.setStartingBeat(w.getStartingBeat());
	
			this.add(toolBarPanel, BorderLayout.NORTH);
			this.add(musicScroller, BorderLayout.CENTER);
			this.pack();
			this.setVisible(true);
		}
	}

	/**
	 * Instantiates and displays itself on the EDT.
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
				new TuneEditor();
			}
		}));
	}
}

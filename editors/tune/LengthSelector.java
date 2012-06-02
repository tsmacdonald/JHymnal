package editors.tune;

import java.util.ArrayList;
import java.util.List;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import static javax.swing.border.EtchedBorder.LOWERED;

import gui.Utility;

public class LengthSelector implements ToolBarComponent
{
	private List<JToggleButton> buttons;
	private JToggleButton defaultButton;
	private NoteCreator creator;
	private JPanel panel;
	private int defaultDuration;

	public LengthSelector(NoteCreator c) {
		this.creator = c;
		this.panel = new JPanel();
		this.panel.setBorder(BorderFactory.createEtchedBorder(LOWERED));
		this.buttons = new ArrayList<JToggleButton>(5);
		int[] durations = {1, 2, 4, 8, 16};
		final String[] names = {"whole2.png", "half2.png", "quarter2.png",
			"eighth2.png", "sixteenth2.png"};
		assert durations.length == names.length;
		for(int i = durations.length - 1; i >= 0; --i) {
			final int duration = durations[i];
			final int j = i;
			final JToggleButton b = new JToggleButton(Utility.getImage("icons", names[j]));
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(b.getModel().isSelected()) {
						creator.setDuration(duration);
						for(JToggleButton otherButton : buttons) {
							if(b != otherButton) {
								otherButton.setSelected(false);
							}
						}
					}
					else {
						b.doClick();
					}
				}
			});
			this.buttons.add(b);
			this.panel.add(b);
		}
		this.defaultButton = this.buttons.get(2);
		this.defaultDuration = durations[2];
	}

	public JComponent getComponent() {
		return this.panel;
	}

	public void reset() {
		for(JToggleButton b : this.buttons) {	
			if(b.getModel().isSelected()) {
				for(ActionListener l : b.getActionListeners()) {
					l.actionPerformed(new ActionEvent(b,
						ActionEvent.ACTION_PERFORMED, ""));
				}
				return;
			}
		}
		//Else, none have been selected yet
		this.buttons.get(2).doClick();
	}
}

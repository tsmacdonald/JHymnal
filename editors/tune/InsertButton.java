package editors.tune;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;

import gui.Utility;

public class InsertButton implements ToolBarComponent
{
	private JButton button;
	private NoteCreator creator;
	public InsertButton(final NoteCreator creator) {
		this.creator = creator;
		this.button = new JButton(Utility.getImage("icons", "add2.png"));
		this.button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				creator.insert();
			}
		});
	}

	public JComponent getComponent() {
		return this.button;
	}

	public void reset() { }
}

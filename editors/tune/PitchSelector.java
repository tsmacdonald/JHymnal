package editors.tune;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicArrowButton;

import model.Accidental;
import model.Cleff;
import model.KeySignature;

public class PitchSelector implements ToolBarComponent
{
	private NoteCreator creator;
	private JPanel panel;
	private SingleNotePanel notePanel;
	private List<JToggleButton> accidentalButtons;

	public PitchSelector(NoteCreator creator, KeySignature key) {
		this.creator = creator;
		this.panel = new JPanel();
		this.notePanel = new SingleNotePanel(key);
		this.accidentalButtons = new ArrayList<JToggleButton>(3);

//		JButton up = new JButton("▲");
		JButton up = new BasicArrowButton(SwingConstants.NORTH);
		up.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				noteUp();
			}
		});

//		JButton down = new JButton("▼");
		JButton down = new BasicArrowButton(SwingConstants.SOUTH);
		down.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				noteDown();
			}
		});

		final JToggleButton flat = new JToggleButton("b");
		final JToggleButton natural = new JToggleButton("N");
		final JToggleButton sharp = new JToggleButton("#");
		this.accidentalButtons.add(flat);
		this.accidentalButtons.add(natural);
		this.accidentalButtons.add(sharp);
		flat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(flat.getModel().isSelected()) {
					setAccidental(Accidental.FLAT);
					natural.setSelected(false);
					sharp.setSelected(false);
				}
				else {
					flat.doClick();
				}
			}
		});
		natural.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(natural.getModel().isSelected()) {
					setAccidental(Accidental.NATURAL);
					flat.setSelected(false);
					sharp.setSelected(false);
				}
				else {
					natural.doClick();
				}
			}
		});
		sharp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(sharp.getModel().isSelected()) {
					setAccidental(Accidental.SHARP);
					flat.setSelected(false);
					natural.setSelected(false);
				}
				else {
					sharp.doClick();
				}
			}
		});
		JPanel accidentalPanel = new JPanel();
		accidentalPanel.add(flat);
		accidentalPanel.add(natural);
		accidentalPanel.add(sharp);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		for(javax.swing.JComponent c : new JComponent[] {up, accidentalPanel, down}) {
			c.setAlignmentX(.5f);
			buttonPanel.add(c);
		}

		this.panel.setLayout(new BorderLayout());
		this.panel.add(this.notePanel, BorderLayout.CENTER);
		this.panel.add(buttonPanel, BorderLayout.EAST);
	}

	public void noteUp() {
		try {
			this.notePanel.increment();
			creator.setPitch(this.notePanel.getPitch());
		}
		catch(NoteOutOfRangeException e) { }
	}

	public void noteDown() {
		try {
			this.notePanel.decrement();
			creator.setPitch(this.notePanel.getPitch());
		}
		catch(NoteOutOfRangeException e) { }
	}

	public void setAccidental(Accidental a) {
		this.notePanel.setAccidental(a);
		creator.setPitch(this.notePanel.getPitch());
	}

	public void setCleff(Cleff c) {
		this.notePanel.setCleff(c);
		this.creator.setPitch(this.notePanel.getPitch());
	}

	public String getPitch() {
		return this.notePanel.getPitch();
	}

	public JComponent getComponent() {
		return this.panel;
	}

	public void reset() {
		JToggleButton natural = this.accidentalButtons.get(1);
		if(natural.getModel().isSelected()) {
			for(ActionListener l : natural.getActionListeners()) {
				l.actionPerformed(new ActionEvent(natural,
					ActionEvent.ACTION_PERFORMED, ""));
			}
		}
		else {
			natural.doClick();
		}

		this.notePanel.reset();
		creator.setPitch(this.notePanel.getPitch());
	}
}

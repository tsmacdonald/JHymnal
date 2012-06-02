package editors.tune;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToolBar;

import gui.Utility;
import model.KeySignature;

/**
 *
 * @author Tim Macdonald <tim.macdonald@my.wheaton.edu>
 */
public class EditingToolBar extends JToolBar
{
	private MusicPanel musicPanel;

	public EditingToolBar(final MusicPanel musicPanel, KeySignature key) {
		super();
		List<ToolBarComponent> components = new LinkedList<ToolBarComponent>();
		final NoteCreator creator = new NoteCreator(musicPanel, components);

		//
		// Open
		//
		JButton open = new JButton(Utility.getImage("icons", "folder2.png"));
		open.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
			}
		});
//		this.add(open);

		//
		// Save
		//
		JButton save = new JButton(Utility.getImage("icons", "saver.png"));
		save.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				musicPanel.saveTune();
			}
		});
		this.add(save);
		
		//
		// Parts
		//
		PartSelector satb = new PartSelector(creator);
		components.add(satb);
		this.add(satb.getComponent());

		//
		// Syllable Specifier
		//
		SyllableSpecifier syllables = new SyllableSpecifier(creator);
		components.add(syllables);
		this.add(syllables.getComponent());

		//
		// Note Length
		//
		LengthSelector noteLength = new LengthSelector(creator);
		components.add(noteLength);
		this.add(noteLength.getComponent());

		//
		// Modifiers
		//
		ModifierSelector modifiers = new ModifierSelector(creator);
		components.add(modifiers);
		this.add(modifiers.getComponent());

		//
		// Pitch
		//
		PitchSelector pitch = new PitchSelector(creator, key);
		components.add(pitch);
		satb.setPitchSelector(pitch);
		this.add(pitch.getComponent());

		//
		// Undo
		//
		UndoButton undo = new UndoButton(creator);
		components.add(undo);
		this.add(undo.getComponent());

		//
		// Insert
		//
		InsertButton insert = new InsertButton(creator);
		components.add(insert);
		this.add(insert.getComponent());

		this.musicPanel = musicPanel;

		creator.resetAll();
	}

//	public NoteCreator getNoteCreator() {
//		return this.noteCreator;
//	}
}

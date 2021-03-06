// Copyright 2012 Jason Petersen and Timothy Macdonald
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package editors.tune;

import gui.Utility;

import java.util.ArrayList;
import java.util.List;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import static javax.swing.border.EtchedBorder.LOWERED;

public class SyllableSpecifier implements ToolBarComponent
{
	private JPanel panel;
	private List<JToggleButton> buttons;

	public SyllableSpecifier(final NoteCreator creator) {
		this.panel = new JPanel();
		this.panel.setBorder(BorderFactory.createEtchedBorder(LOWERED));

		this.buttons = new ArrayList<JToggleButton>(2);

		final JToggleButton singleNote = new JToggleButton(Utility.getImage("icons", "onenote.png"));
		singleNote.setToolTipText("Only add one note per syllable");

		final JToggleButton multiNote = new JToggleButton(Utility.getImage("icons", "manynotes.png"));
		multiNote.setToolTipText("Add many notes per syllable");
		multiNote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JToggleButton self = (JToggleButton)(e.getSource());
				if(self.getModel().isSelected()) {
					creator.setContinousSyllable(false);
					singleNote.setSelected(false);
				}
				else {
					creator.setContinousSyllable(true);
					singleNote.setSelected(true);
				}
			}
		});
		singleNote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JToggleButton self = (JToggleButton)(e.getSource());
				if(self.getModel().isSelected()) {
					creator.setContinousSyllable(true);
					multiNote.setSelected(false);
				}
				else {
					creator.setContinousSyllable(false);
					multiNote.setSelected(true);
				}
			}
		});
		singleNote.setEnabled(false);
		multiNote.setEnabled(false);
		this.buttons.add(singleNote);
		this.panel.add(singleNote);
		this.buttons.add(multiNote);
		this.panel.add(multiNote);
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
			}
		}
	}
}

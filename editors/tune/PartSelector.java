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

import static javax.swing.border.EtchedBorder.LOWERED;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import model.Cleff;
import model.Part;
import gui.Utility;

public class PartSelector implements ToolBarComponent
{
	private List<JToggleButton> buttons; 
	private NoteCreator creator;
	private JPanel panel;
	private PitchSelector pitchSelector;

	public PartSelector(NoteCreator c) {
		this.creator = c;
		this.buttons = new ArrayList<JToggleButton>(4);
		this.panel = new JPanel();
		this.panel.setBorder(BorderFactory.createEtchedBorder(LOWERED));
		
		for(final Part p : Part.values()) {
			final JToggleButton b = new JToggleButton(Utility.getImage("icons",
				p.toString().toLowerCase() + ".png"));
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(b.getModel().isSelected()) {
						creator.setPart(p);
						pitchSelector.setCleff(getCleff(p));
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
	}
	
	private Cleff getCleff(Part p) {
		switch(p) {
			case SOPRANO:
			case ALTO: return Cleff.TREBLE;
			case TENOR:
			case BASS: return Cleff.BASS;
			default: assert false; return null;
		}
	}

	public JComponent getComponent() {
		return this.panel;
	}

	public void reset() {
		int i=0;
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
		this.buttons.get(0).doClick();
	}

	public void setPitchSelector(PitchSelector pitch) {
		this.pitchSelector = pitch;		
	}
}

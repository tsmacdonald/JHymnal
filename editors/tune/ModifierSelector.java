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

public class ModifierSelector implements ToolBarComponent
{
	private List<JToggleButton> buttons; 
	private NoteCreator creator;
	private JPanel panel;

	public ModifierSelector(final NoteCreator creator) {
		this.creator = creator;
		this.buttons = new ArrayList<JToggleButton>(4);
		this.panel = new JPanel();
		this.panel.setBorder(BorderFactory.createEtchedBorder(LOWERED));
		
		JToggleButton dot = new JToggleButton(Utility.getImage("icons", "dotted.png"));
		dot.setToolTipText("Dot the note");
		dot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JToggleButton self = (JToggleButton)(e.getSource());
				if(self.getModel().isSelected()) {
					creator.setDotted(true);
				}
				else {
					creator.setDotted(false);
				}
			}
		});
		this.buttons.add(dot);
		this.panel.add(dot);

		JToggleButton slur = new JToggleButton(Utility.getImage("icons", "slurred3.png"));
		slur.setToolTipText("Slur the note");
		slur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JToggleButton self = (JToggleButton)(e.getSource());
				if(self.getModel().isSelected()) {
					creator.setSlurred(true);
				}
				else {
					creator.setSlurred(false);
				}
			}
		});
		this.buttons.add(slur);
//		this.panel.add(slur);

		JToggleButton tie = new JToggleButton(Utility.getImage("icons", "tied.png"));
		tie.setToolTipText("Tie the note");
		tie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JToggleButton self = (JToggleButton)(e.getSource());
				if(self.getModel().isSelected()) {
					creator.setTied(true);
				}
				else {
					creator.setTied(false);
				}
			}
		});
		this.buttons.add(tie);
		//this.panel.add(tie);

		JToggleButton rest = new JToggleButton(Utility.getImage("icons", "rest.png"));
		rest.setToolTipText("Indicate that the note is a rest");
		rest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JToggleButton self = (JToggleButton)(e.getSource());
				if(self.getModel().isSelected()) {
					creator.setRest(true);
				}
				else {
					creator.setRest(false);
				}
			}
		});
		this.buttons.add(rest);
		//this.panel.add(rest);

	}

	public JComponent getComponent() {
		return this.panel;
	}

	public void reset() {
		for(JToggleButton b : this.buttons) {
			for(ActionListener l : b.getActionListeners()) {
				l.actionPerformed(new ActionEvent(b,
					ActionEvent.ACTION_PERFORMED, ""));
			}
		}
	}
}

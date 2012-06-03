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

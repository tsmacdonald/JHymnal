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

package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import model.Hymn;
import model.Text;
import model.Tune;

/**
 * Swing implementation of the AbstractVerseSelector interface. Contains and
 * coordinates n toggle buttons, where n is the number of verses of the active
 * hymn. If there is no active hymn, does not display and buttons.
 *
 * @author Tim Macdonald <tim.macdonald@my.wheaton.edu>
 */
public class SwingVerseSelector implements AbstractVerseSelector, HymnChangeListener
{
	private HymnPanel panel;
	private JPanel container;
	private boolean[] map;
	private Hymn hymn;
	private MainToolBar toolBar;

	public SwingVerseSelector(MainToolBar toolBar, HymnPanel panel) {
		this.panel = panel;
		this.hymn = null;
		this.container = new JPanel();
		this.toolBar = toolBar;
		setButtons();
	}

	/**
	 * Creates and adds the appropriate number of buttons for teh current hymn.
	 */
	private void setButtons() {
		this.container.removeAll();
		if(this.hymn == null) return;
		int verseCount = this.hymn.numberOfVerses();
		this.map = new boolean[verseCount];
		for(int i = 0; i < verseCount; ++i) {
			final int v= i + 1;
			final JToggleButton b = new JToggleButton(String.format("[%d]", v));
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					toggleVerse(v);
				}
			});
			this.container.add(b);
			this.map[i] = true;
		}
		this.toolBar.revalidate();
	}

	/**
	 * Called to indicate that the given verse should be toggled. Renders and
	 * displays the new hymn.
	 */
	public void toggleVerse(int verseNumber) {
		if(this.hymn == null) return;
		if(this.map[verseNumber - 1]) {
			this.hymn.removeVerse(verseNumber);
		}
		else {
			this.hymn.restoreVerse(verseNumber);
		}
		this.map[verseNumber - 1] = ! this.map[verseNumber - 1];
		this.panel.setHymn(this.hymn); //TODO Double check
		this.panel.render();
	}

	/**
	 * Called to indicate that the given verse should be added. Renders and
	 * displays the new hymn.
	 */
	public void addVerse(int verseNumber) {
		this.map[verseNumber - 1] = false;
		this.toggleVerse(verseNumber);
	}

	/**
	 * Called to indicate that the given verse should be removed. Renders and
	 * displays the new hymn.
	 */
	public void removeVerse(int verseNumber) {
		this.map[verseNumber - 1] = true;
		this.toggleVerse(verseNumber);
	}

	/**
	 * Returns the component that should be displayed by the tool bar.
	 */
	public JComponent getComponent() {
		return this.container;
	}

	/**
	 * Do nothing.
	 */
	public void textChanged(Text t) { }

	/**
	 * Do nothing.
	 */
	public void tuneChanged(Tune t) { }

	/**
	 * Change the active hymn and make new buttons for it.
	 */
	public void hymnChanged(Hymn h) {
		this.hymn = h;
		setButtons();
	}
}

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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

//import editors.text.TextEditor;
import editors.tune.TuneEditor;
import io.Database;

import model.Hymn;
import model.Text;
import model.Tune;

/**
 * Tool bar containing widgets to open the tune or text editors, import other
 * hymns, and select the text, tune, and verses to display.
 *
 * @author Tim Macdonald <tim.macdonald@my.wheaton.edu>
 */
public class MainToolBar extends JToolBar implements AbstractToolBar
{
	private HymnPanel panel;
	private Set<HymnChangeListener> listeners;
	private TuneSelector tuneSelector;
	private TextSelector textSelector;

	/**
	 * Instantiates the tool bar and adds all of its components.
	 */
	public MainToolBar(final HymnPanel panel) {
		super();

//		JButton repaint = new JButton("R");
//		repaint.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				System.out.println("Repainting");
//				panel.repaint();
//			}
//		});
//		this.add(repaint);
				
		//
		// Clear Cache
		//
		JButton clearCache = new JButton(Utility.getImage("icons", "clearcache.png"));
		clearCache.setToolTipText("Clear the cache of rendered hymns.");
		clearCache.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Database.getInstance().clearCache();
			}
		});
		this.add(clearCache);

//		//
//		// Tune Creator
//		//
//		JButton tuneCreator = new JButton(Utility.getImage("icons", "addtune.png"));
//		tuneCreator.setToolTipText("Open the tune creator");
//		tuneCreator.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				TuneEditor t = new TuneEditor();
//				t.addWindowListener(new WindowAdapter() {
//					public void windowClosed(WindowEvent e) {
//						refreshTunes();
//					}
//				});
//			}
//		});
//		this.add(tuneCreator);
//
//		//
//		// Text Creator
//		//
//		JButton textCreator = new JButton(Utility.getImage("icons", "addtext.png"));
//		textCreator.setToolTipText("Open the text creator");
//		textCreator.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent e){
//				TextEditor t = new TextEditor();
//				t.frame().addWindowListener(new WindowAdapter() {
//					public void windowClosed(WindowEvent e) {
//						refreshText();
//					}
//				});
//			}			
//		});
//		this.add(textCreator);

		//
		// Text Dropdown
		//
		this.listeners = new HashSet<HymnChangeListener>();
		SwingDropdown textDropdown = new SwingDropdown(this, panel);
		this.textSelector = new TextSelector(textDropdown);
		textDropdown.setSelector(textSelector); 
		this.add(textDropdown.getComponent());
		this.listeners.add(textSelector);

		//
		// Tune Dropdown
		//
		SwingDropdown tuneDropdown = new SwingDropdown(this, panel);
		this.tuneSelector = new TuneSelector(tuneDropdown);
		tuneDropdown.setSelector(tuneSelector); 
		this.add(tuneDropdown.getComponent());
		this.listeners.add(tuneSelector);

		//
		// Verse-selection buttons
		//
		SwingVerseSelector verses = new SwingVerseSelector(this, panel);
		this.listeners.add(verses);
		this.add(verses.getComponent());

				

		this.panel = panel;
	}

	private void refreshText() {
		System.out.println("Refreshing the text and stuff.");
		this.textSelector.reset();
	}

	private void refreshTunes() {
		this.tuneSelector.reset();
	}

	/**
	 * Notifies all the HymnChangeListeners that a new Tune has been
	 * selected.
	 */
	public void fireTuneChangeEvent(Tune t) {
		for(HymnChangeListener l : this.listeners) {
			l.tuneChanged(t);
		}
	}

	/**
	 * Notifies all the HymnChangeListeners that a new Tune has been
	 * selected.
	 */
	public void fireTextChangeEvent(Text t) {
		for(HymnChangeListener l : this.listeners) {
			l.textChanged(t);
		}
	}

	/**
	 * Notifies all the HymnChangeListeners that a new Hymn has been selected.
	 */
	public void fireHymnChangeEvent(Hymn h) {
		for(HymnChangeListener l : this.listeners) {
			l.hymnChanged(h);
		}
	}
}


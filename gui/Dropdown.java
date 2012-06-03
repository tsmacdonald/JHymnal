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

import model.Hymn;
import model.HymnComponent;

/**
 * Abstract supertype for dropdown menus. All use of graphical components by
 * the controller must adhere to the API defined here, meaning that other GUI
 * systems can be used easily.
 *
 * @author Tim Macdonald <tim.macdonald@my.wheaton.edu>
 */
public abstract class Dropdown
{
	private AbstractHymnComponentSelector controller;

	/**
	 * Sets the controller (hymn selector) to be used. Note that the
	 * dropdown will have <strong>no</strong> logic if this is not set.
	 */
	public void setSelector(AbstractHymnComponentSelector s) {
		this.controller = s;
	}

	protected AbstractHymnComponentSelector getSelector() {
		return this.controller;
	}

	/**
	 * Add all of the given hymn components to the dropdown.
	 */
	public abstract void add(Iterable<? extends HymnComponent> i);

	/**
	 * Add a separator to the dropdown, which should in some way distinguish
	 * the items before it from the items after it.
	 */
	public abstract void addSeparator();

	/**
	 * Remove all hymn components from the dropdown.
	 */
	public abstract void clear();

	/**
	 * Set the currently-selected item.
	 */
	public abstract void setSelectedItem(HymnComponent h);

	/**
	 * Cause the given hymn to be rendered and displayed.
	 */
	public abstract void renderHymn(Hymn h);

	/**
	 * Should notify all listeners that the active hymn has changed.
	 */
	public abstract void fireHymnChangeEvent(Hymn h);

	/**
	 * Called when something about the display has changed and should be
	 * redrawn on-screen.
	 */
	public abstract void repaint();
}

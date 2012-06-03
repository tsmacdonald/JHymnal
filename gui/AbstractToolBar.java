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
import model.Text;
import model.Tune;

/**
 * Defines the responsiblities of the tool bar-like object in the GUI (note
 * that it may actually be a menu, a cluster of buttons, or something else--
 * this exists outside of the precise graphical representation). Specifically,
 * tool bars are expected to notify their actions (buttons) about changes in
 * the program state.
 *
 * @author Tim Macdonald <tim.macdonald@my.wheaton.edu>
 */
public interface AbstractToolBar
{
	/**
	 * Notify all components that the active tune has changed.
	 *
	 * @param t The new Tune.
	 */
	public void fireTuneChangeEvent(Tune t);

	/**
	 * Notify all components that the active text has changed.
	 *
	 * @param t The new Text.
	 */
	public void fireTextChangeEvent(Text t);

	/**
	 * Notify all components that the active hymn has changed.
	 *
	 * @param h The new Hymn.
	 */
	public void fireHymnChangeEvent(Hymn h);
}

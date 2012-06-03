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
 * The listener interface for receiving events related to changing the active
 * hymn component or hymn.
 *
 * @author Tim Macdonald <tim.macdonald@my.wheaton.edu>
 */
public interface HymnChangeListener
{
	/**
	 * Called when the active text has changed.
	 *
	 * @param t The new active text.
	 */
	public void textChanged(Text t);

	/**
	 * Called when the active tune has changed.
	 *
	 * @param t The new active tune.
	 */
	public void tuneChanged(Tune t);

	/**
	 * Called when the active hymn has changed.
	 *
	 * @param h The new active hymn.
	 */
	public void hymnChanged(Hymn h);
}

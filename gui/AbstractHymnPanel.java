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

/**
 * Interface for hymn panels--guarantees that hymn panels are able to
 * accept a Hymn to be rendered.
 *
 * @author Tim Macdonald <tim.macdonald@my.wheaton.edu>
 */
public interface AbstractHymnPanel
{
	/**
	 * Sets the hymn to be displayed (but does not actually display it).
	 */
	public void setHymn(Hymn h);

	/**
	 * Returns the hymn that will be displayed (but might not be currently
	 * displayed.
	 */
	public Hymn getHymn();

	/**
	 * Renders the hymn object and displays it.
	 */
	public void render();
}

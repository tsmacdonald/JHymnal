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

/**
 * HymnComponent.java
 * 
 * Supertype for Texts and Tunes.
 * 
 * @author Tim Macdonald, Jason Petersen
 */

package model;

public interface HymnComponent {

    /**
     * Get the Meter for this HymnComponent.
     * @return The Meter for this HymnComponent.
     * @author Spring 2010
     */
	public Meter getMeter();

	/**
	 * Returns the human-readable title.
	 * @author Tim Macdonald
	 */
	public String getName();
	
	/**
	 * Get the filename for this HymnComponent,
	 * minus any file extensions.
	 * @return The filename for this HymnComponent.
	 * @author Jason Petersen
	 */
	public String getFilename();
	
	/**
	 * Determine if this HymnComponent is valid,
	 * i.e. is a final, ready-for-rendering product
	 * @return True if the HymnComponent is ready for
	 * rendering, otherwise false.
	 * @author Jason Petersen
	 */
	public boolean isValid();
}

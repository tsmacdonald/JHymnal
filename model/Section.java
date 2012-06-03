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
 * Section.java
 * 
 * Supertype for Lyrics
 * and Voices.
 * 
 * @author Jason Petersen
 */

package model;

public interface Section {
	
	/**
	 * Get the number of Lines
	 * in this Section.
	 * @return The number of Lines
	 * in this Section.
	 * @author Jason Petersen
	 */
	public int numberOfLines();
	
	/**
	 * Get the number of Syllables
	 * in this Section.
	 * @return The number of Syllables
	 * in this Section.
	 * @author Jason Petersen
	 */
	public int numberOfSyllables();
}

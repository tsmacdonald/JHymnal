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
 * InvalidHymnException.java
 * 
 * Exception class for when an attempt to
 * create a Hymn goes awry.
 * 
 * @author Jason Petersen
 */

package model;

public class InvalidHymnException extends Exception {

	/** Useful if we were going to serialize this class, but this mostly just keeps Eclipse from complaining */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * @param message The error message
	 * @author Jason Petersen
	 */
	InvalidHymnException(String message) {
		super(message);
	}
}

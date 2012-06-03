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

import java.io.File;

import javax.swing.ImageIcon;

/**
 * Contains useful general-purpose functions that shouldn't really be
 * associated with a particular class.
 *
 * @author Tim Macdonald <tim.macdonald@my.wheaton.edu>
 */
public class Utility
{
	/**
	 * Returns the image at the given relative path. All images should be in
	 * the resources directory, which should not be explicitly passed in. That
	 * is, to get the file <code>resources/icons/Foo.png</code>, call
	 * <code>getImage("icons", "Foo.png")</code>.
	 */
	public static ImageIcon getImage(String... path) {
		String finalPath = "resources" + File.separator;
		for(int i = 0; i < path.length - 1; ++i) {
			finalPath += path[i] + File.separator;
		}
		finalPath += path[path.length - 1];
		return new ImageIcon(finalPath);
	}
}


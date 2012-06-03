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

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Driver class that instantiates and displays the main frame.
 *
 * @author Tim Macdonald <tim.macdonald@my.wheaton.edu>
 */
public class MainGUI
{
	/**
	 * Instantiates and displays a {@link SplitLeafHymnal} on the EDT.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Thread(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(
						UIManager.getSystemLookAndFeelClassName());
				}
				catch(Exception e) {
					System.err.println("L&F trouble.");
					e.printStackTrace();
				}
				new SplitLeafHymnal();
			}
		}));
	}
}

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

package editors.tune;

import javax.swing.JFrame;

public class NewTuneDialog extends WizardDialog
{
	private NewTuneWizard wizard;
	public NewTuneDialog(JFrame parent) {
		super(parent);
		this.wizard = new NewTuneWizard(this);
		this.add(this.wizard);
		super.setSize(new java.awt.Dimension(500, 400));
	}

	public Wizard getWizard() {
		if(this.wizard.shouldContinue()) {
			return this.wizard;
		}
		return null;
	}
}

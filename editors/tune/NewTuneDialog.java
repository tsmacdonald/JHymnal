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

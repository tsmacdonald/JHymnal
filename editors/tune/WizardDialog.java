package editors.tune;

import javax.swing.JDialog;
import javax.swing.JFrame;

public abstract class WizardDialog extends JDialog
{
	public WizardDialog(JFrame parent) {
		super(parent, true);
	}

	public abstract Wizard getWizard();
}

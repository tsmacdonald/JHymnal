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
/**
 * Panel to be put in a dialog box. Allows the user to set initial tune
 * information.
 *
 * @author Tim Macdonald <tim.macdonald@my.wheaton.edu>, Payton Gibson
 */

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;

import io.BadInputException;

import model.KeySignature;
import model.Meter;
import model.TimeSignature;

public class NewTuneWizard extends JPanel implements Wizard
{
	private String name;
	private Meter meter;
	private KeySignature key;
	private TimeSignature timeSignature;
	private String startBeat;
	private boolean valid;
	private JDialog dialog;

	private JLabel error;

	public NewTuneWizard(JDialog d) {
		super();
		this.dialog = d;
		this.valid = false;

		JPanel name = new JPanel();
		JLabel nameLabel = new JLabel("Name: ");
		final JTextField nameField =new JTextField(15);
		name.add(nameLabel);
		name.add(nameField);
		this.add(name);

		JPanel meter = new JPanel();
		JLabel meterLabel = new JLabel("Meter: ");
		final JTextField meterField = new JTextField(15);
		meter.add(meterLabel);
		meter.add(meterField);
		this.add(meter);

		JPanel key = new JPanel();
		JLabel keyLabel = new JLabel("Key Signature:");
		final JComboBox keyDropdown = new JComboBox();
		key.add(keyLabel);
		key.add(keyDropdown);
		this.add(key);

		JPanel time = new JPanel();
		JLabel timeLabel = new JLabel("Time Signature:");
		final JTextField timeField =new JTextField(5);
		time.add(timeLabel);
		time.add(timeField);
		this.add(time);

		JPanel startBeat = new JPanel();
		JLabel startLabel = new JLabel("Start beat:");
		final JTextField startField = new JTextField(5);
		startBeat.add(startLabel);
		startBeat.add(startField);
//		this.add(startBeat);

		String keys[]={" ","C","D","G","A","E", "B","Fs","Cs","F","Bf","Ef","Af","Df","Gf","Cf"};
		for(char n = 'A'; n <= 'G'; ++n) {
			for(String k : new String[] {"", "f", "s"}) {
				try {
					keyDropdown.addItem(KeySignature.getKeySignatureFor((n + k)));
				} catch (BadInputException e) {
					//jk lolz  e.printStackTrace();
				}
			}
		}
		for(int i=0; i<keys.length; i++){
			keyDropdown.addItem(keys[i]);
		}

		JButton create = new JButton("Create");
		create.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = nameField.getText();
				if(name.isEmpty()) {
					flag("Invalid name!");
					return;
				}
				NewTuneWizard.this.name = name;

				try {
					NewTuneWizard.this.meter = makeMeter(meterField.getText());
				}
				catch(InvalidInputException ex) {
					flag("Invalid meter!");
					return;
				}

				NewTuneWizard.this.key = (KeySignature)(keyDropdown.getSelectedItem());

				String rawTime = timeField.getText();
				try {
					NewTuneWizard.this.timeSignature = makeTimeSignature(rawTime);
				}
				catch(InvalidInputException ex) {
					flag("Invalid time signature!");
					return;
				}

//				String startingBeat = startField.getText();
//				if(startingBeat.isEmpty()) {
//					//TODO
//					flag("Invalid starting beat!");
//					return;
//				}
//				NewTuneWizard.this.startBeat = startingBeat;
				flag("");
				if(shouldContinue()) {
					dialog.dispose();
				}
			}
		});
		this.add(create);

		this.error = new JLabel("");
		this.add(this.error);
		setSize(new java.awt.Dimension(620, 480));
	}

	private void flag(String msg) {
		this.error.setText(msg);
		this.revalidate();
		if(msg.isEmpty()) {
			this.valid = true;
		}
		else {
			this.valid = false;
		}
	}

	private Meter makeMeter(String text) throws InvalidInputException {
		if(text.isEmpty()) throw new InvalidInputException();
		String[] parts = text.split("[^\\d]+");
		ArrayList<Integer> ints = new ArrayList<Integer>();
		if(parts.length < 1) throw new InvalidInputException();
		for(int i = 0; i < parts.length; ++i) {
			if(parts[i].isEmpty()) continue;
			else ints.add(new Integer(parts[i]));
		}
		return new Meter("", ints);
	}

	private TimeSignature makeTimeSignature(String time)
	  throws InvalidInputException {
		if(time.isEmpty()) throw new InvalidInputException();
		String[] parts = time.split("[^\\d]+");
		if(parts.length != 2) throw new InvalidInputException();
		int numBeats = Integer.parseInt(parts[0]);
		int beatLength = Integer.parseInt(parts[1]);
		try {
			return new TimeSignature(numBeats, beatLength);
		}
		catch(BadInputException e) {
			throw new InvalidInputException();
		}
	}

	private class InvalidInputException extends Exception { }

	public boolean shouldContinue() {
		return this.valid;
	}

	public String getName() {
		return this.name;
	}

	public KeySignature getKeySignature() {
		return this.key;
	}

	public Meter getMeter() {
		return this.meter;
	}

	public TimeSignature getTimeSignature() {
		return this.timeSignature;
	}

	public String getStartingBeat() {
		return "1";//return this.startBeat;
	}
}


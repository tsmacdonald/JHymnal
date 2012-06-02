package editors.tune;

import java.util.ArrayList;
import java.util.Arrays;

import io.BadInputException;

import model.KeySignature;
import model.Meter;
import model.TimeSignature;

public class DummyWizard implements Wizard
{
	public String getName() {
		return "A Happy Tune";
	}

	public KeySignature getKeySignature() {
		try {
			return KeySignature.getKeySignatureFor("D");
		}
		catch(BadInputException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Meter getMeter() {
		return new Meter(new ArrayList<Integer>(Arrays.asList(new Integer[] {8, 6, 8, 6})));
	}

	public TimeSignature getTimeSignature() {
		try {
		return new TimeSignature(4, 4);
		}
		catch(BadInputException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getStartingBeat() {
		return "1";
	}
}

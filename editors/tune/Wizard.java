package editors.tune;

import model.KeySignature;
import model.Meter;
import model.TimeSignature;

public interface Wizard
{
	public String getName();
	public KeySignature getKeySignature();
	public Meter getMeter();
	public TimeSignature getTimeSignature();
	public String getStartingBeat();
}

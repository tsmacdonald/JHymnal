package editors.tune;

import java.util.List;

import model.KeySignature;
import model.Note;
import model.Part;
import model.Slur;

public class NoteCreator
{
	private MusicPanel out;
	private NoteBuilder builder;
	private boolean isLastNoteSlurred;
	private boolean shouldCurrentNoteBeSlurred;
	private Note lastNote;
	private List<ToolBarComponent> components;
	private KeySignature keySignature;
	
	private boolean lastNoteTied;

	public NoteCreator(MusicPanel musicPanel, List<ToolBarComponent> components) {
		this.out = musicPanel;
		this.components = components;
		this.builder = new NoteBuilder();
		this.isLastNoteSlurred = false;
		this.shouldCurrentNoteBeSlurred = false;
		this.lastNote = null;
		this.lastNoteTied = false;
	}

	public void insert() {
		try {
			Note newNote = builder.makeNote();
			out.add(newNote);
			this.lastNote = newNote;
			resetAll();
		}
		catch(IncompleteInformationException e) {
			System.err.println("Tried to insert an incomplete note.");
		}
	}

	public void resetAll() {
		for(ToolBarComponent c : this.components) {
			c.reset();
		}
	}

	public void undo() {
		out.removeLastNote();
	}

	public void setDotted(boolean isDotted) {
		this.builder.setDotted(isDotted);
	}

	public void setDuration(int duration) {
		this.builder.setDuration(duration);
	}

	public void setSlurred(boolean isNewNoteSlurred) {
//		this.shouldCurrentNoteBeSlurred = isNewNoteSlurred;
		this.builder.setSlurStatus(Slur.NONE);
		//TODO
	}

	public void setPart(Part p) {
		this.out.setPart(p);
	}

	public void setPitch(String p) {
		this.builder.setPitch(p);
	}

	public void setRest(boolean rest) {
		this.builder.setRest(rest);
	}

	public void setTied(boolean isTied) {
		if(isTied && this.lastNote != null) {
			this.lastNote.startsTie(this.lastNoteTied);
		}
		this.lastNoteTied = isTied;
	}

	public void setContinousSyllable(boolean isContSyl) {
		//
	}

	private class IncompleteInformationException extends Exception { }

	private class NoteBuilder
	{
		private String pitch;
		private int duration;
		private boolean dotted;
		private boolean startsTie;
		private Slur slurStatus;
		private boolean rest;

		private boolean[] isSet;

		public NoteBuilder() {
			this.isSet = new boolean[6];
			this.setStartsTie(false);
			this.handleSlurring();
		}

		public void setPitch(String pitch) {
			this.pitch = pitch;
			this.isSet[0] = true;
		}

		public void setDuration(int duration) {
			this.duration = duration;
			this.isSet[1] = true;
		}

		public void setDotted(boolean dotted) {
			this.dotted = dotted;
			this.isSet[2] = true;
		}

		public void setStartsTie(boolean startsTie) {
			this.startsTie = startsTie;
			this.isSet[3] = true;
		}

		public void handleSlurring() {
			setSlurStatus(Slur.NONE);
			//TODO
		}

		public void setSlurStatus(Slur slurStatus) {
			this.slurStatus = slurStatus;
			this.isSet[4] = true;
		}

		public void setRest(boolean rest) {
			this.rest = rest;
			this.isSet[5] = true;
		}

		public Note makeNote() throws IncompleteInformationException {
			boolean valid = true;
			int i = 0;
			for(boolean b : this.isSet) {
				if(! b) {
					valid = false;
					break;
				}
				i++;
			}
			if(! valid) throw new IncompleteInformationException();

			try {
				return new Note(this.pitch, this.duration, this.dotted,
					this.startsTie, this.slurStatus, this.rest);
			}
			catch(io.BadInputException e) {
				System.err.println("Invalid Note configuration...this should never happen.");
				e.printStackTrace();
				throw new IncompleteInformationException();
			}
			catch(model.UnsupportedNoteException e) {
				System.err.println("Unsupported note...this should never happen.");
				e.printStackTrace();
				throw new IncompleteInformationException();
			}
		}
	}
	
	public void setKeySignature(KeySignature key) {
		this.keySignature = key;
	}

//	public void setMeter(Meter m) {
//		//
//	}
//
//	public void setTimeSignature(TimeSignature ts) {
//		//
//	}
//
//	public void setStartingBeat(String beat) {
//		//
//	}
}

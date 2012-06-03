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

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.FileInputStream;

import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Accidental;
import model.Cleff;
import model.KeySignature;
import model.Note;

/**
 * Displays a single note whose pitch can be adjusted.
 *
 * @author nbliss, Tim Macdonald <tim.macdonald@my.wheaton.edu>
 */
public class SingleNotePanel extends JPanel
{
	private static final Pitch DEFAULT_TREBLE = new Pitch('B', 4);
	private static final Pitch DEFAULT_BASS = new Pitch('C', 3);
	private Pitch currentPitch;
	private KeySignature key;
	Graphics g;
	private Font font;
	private Cleff cleff;
	
	public SingleNotePanel(KeySignature key) {
		super();
		this.key = key;
		this.currentPitch = DEFAULT_TREBLE;
		cleff=Cleff.TREBLE;
		setPreferredSize(new java.awt.Dimension(100, 100));
		try {
			FileInputStream fis;
			fis = new FileInputStream("resources/fonts/sonata.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, fis);
		}catch (Exception e) {
			System.err.println("trouble with font:");
			System.err.println(e.getMessage());
		}		
	}

	private void update() {
		repaint();
	}
	
	/**
	 * paints the graphics
	 * @author nbliss
	 */
	public void paint(Graphics g){
		this.g=g;
	    super.paintComponent(g);
	    ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    for(int i=0;i<5;i++){
	    	g.drawLine(10, 30+i*10, 90, 30+i*10);	    
	    }
	    g.drawLine(10,30,10,70);
	    g.drawLine(90,30,90,70);
		g.setFont(font.deriveFont(44.0f));
		if(cleff==Cleff.TREBLE)
			g.drawString(cleffString(),15,70);
		else
			g.drawString(cleffString(),15,75);
		int yval;
		if(cleff==Cleff.TREBLE){
			yval=(38-pitchInt(currentPitch))*5+31;
		}else{yval=(26-pitchInt(currentPitch))*5+31;}	
		int xval;
		if(key.getAccidentals()[pitchInt(currentPitch)%7]!=currentPitch.accidental) {
			String toDraw="";
			switch(currentPitch.accidental){
			case FLAT: toDraw="b";break;
			case SHARP: toDraw="#";break;
			case NATURAL: toDraw="n";break;					
			}
			g.drawString(toDraw,50,yval);
			xval=65;
		}
		else xval=60;
		g.drawString("w", xval, yval);
		if((cleff==Cleff.TREBLE && pitchInt(currentPitch)>=40) ||
				(cleff==Cleff.BASS && pitchInt(currentPitch)>=28)){
			for(int i=30;i>=yval-1;i-=10){
				g.drawLine(xval-2,i,xval+19,i);
			}
		}else if((cleff==Cleff.TREBLE && pitchInt(currentPitch)<=28) ||
				(cleff==Cleff.BASS && pitchInt(currentPitch)<=24)){
			for(int i=70; i<=yval+1;i+=10){
				g.drawLine(xval-2,i,xval+19,i);
			}
		}
	}
	
	/**
	 * Moves the note up by one, unless the note is already as high as it can
	 * be. Can't go higher than being on the 2nd ledger line above the staff.
	 */
	public void increment() throws NoteOutOfRangeException {
		int noteNum=pitchInt(currentPitch);
		if((cleff==Cleff.TREBLE && noteNum>=41)||(cleff==Cleff.BASS && noteNum>=30))
				throw new NoteOutOfRangeException();
		if(this.currentPitch.note == 'B') {
			this.currentPitch.octave++;
		}

		if(this.currentPitch.note == 'G') {
			this.currentPitch.note = 'A';
		}
		else {
			this.currentPitch.note += 1;
		}

		update();

	}

	/**
	 * Moves the note down by one, unless the note is already as low as it can
	 * be. Can't go lower than being on the 2nd ledger line below the staff.
	 */
	 public void decrement() throws NoteOutOfRangeException {
		int noteNum=pitchInt(currentPitch);
		if((cleff==Cleff.TREBLE && noteNum<=26)||(cleff==Cleff.BASS && noteNum<=14))
				throw new NoteOutOfRangeException();
		if(this.currentPitch.note == 'C') {
			this.currentPitch.octave--;
		}

		if(this.currentPitch.note == 'A') {
			this.currentPitch.note = 'G';
		}
		else {
			this.currentPitch.note -= 1;
		}

		update();

	}

	/**
	 * Returns the index of the given note in a C-based ordering.
	 * So <code>'C'=&lt;0</code>, <code>'A'=&lt;5</code>, etc.
	 */
	private static int getIndex(char n) {
		if(n <= 'B') {
			return n - 'A' + 5;
		}
		else {
			return n - 'C';
		}
	}

	/**
	 * Sets whether the note is flat, natural, or sharp.
	 */
	public void setAccidental(Accidental a) {
		this.currentPitch.accidental = a;
		update();
	}

	/**
	 * Sets the cleff of the note. If the cleff changes, the note will be reset
	 * to the cleff's default note value.
	 */
	public void setCleff(Cleff cleff) {
		this.cleff=cleff;
		if(cleff==Cleff.TREBLE)
			currentPitch=DEFAULT_TREBLE;
		else
			currentPitch=DEFAULT_BASS;
		update();
	}

	/**
	 * Returns the pitch of the note in Scientific Pitch Notation.
	 */
	public String getPitch() {
		return currentPitch.toString();
	}

	/**
	 * Sets everything to its default value.
	 */
	public void reset() {
		//this.currentPitch = DEFAULT_TREBLE;
		//this.cleff=Cleff.TREBLE;
		update();
	}

	/**
	 * Get an integer value of the absolute pitch for this Pitch.
	 * @return An integer value of the absolute pitch in order to make rendering
	 * easier.  The 'C' four octaves below middle C is 0.  For every octave add
	 * 7 (8 possible pitches, but zero-based).  Also add number of pitches above
	 * middle C in that octave.  Example: middle C = 28.
	 * @author Spring 2010
	 */
	private int pitchInt(Pitch pitch) {
		return (((pitch.note + 5) - 'A') % 7) + 
		    (7 * pitch.octave);
	}
	
	private String cleffString(){
		if(cleff==Cleff.TREBLE) return "&";
		else return "?";
	}
	
	private static class Pitch
	{
		char note;
		int octave;
		Accidental accidental;

		public Pitch(char note, int octave) {
				
			this(note, Accidental.NATURAL, octave);//FIXME
		}

		public Pitch(char note, Accidental accidental, int octave) {
			assert 'A' <= note && note <= 'G';
			this.note = note;
			this.octave = octave;
			this.accidental = accidental;
		}
		



		
		private static String accidentalToString(Accidental a) {
			if(a == null) return "";
			switch(a) {
				case FLAT: return "f";
				case NATURAL: return "n";
				case SHARP: return "s";
				default: assert false;
			}
			return null;
		}
		
		public String toString(){
			return this.note + accidentalToString(this.accidental) + this.octave;
		}
	}
}

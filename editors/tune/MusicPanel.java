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

/**
 * @author Tim Macdonald
 */

package editors.tune;

import java.io.IOException;

import javax.swing.JPanel;

import io.BadInputException;
import io.Database;

import model.Cleff;
import model.KeySignature;
import model.Line;
import model.Meter;
import model.Music;
import model.Note;
import model.Part;
import model.TimeSignature;
import model.Tune;
import model.Unit;
import model.UnsupportedNoteException;
import model.Voice;

public class MusicPanel extends JPanel {
	public MusicPanel(String title) {
		super();
	}

	public void saveTune() {
//		try {
//			Tune tune = new Tune(this.title, this.meter, this.key, this.sig);
//			ArrayList<Integer> lineCounts = new ArrayList<Integer>();
//			for(Integer i : this.meter) {
//				lineCounts.add(i);
//			}
//			int[] currentLines = {0, 0, 0, 0};
//			int currStart = lineCounts.get(currentLines[0]);
//			int[] currents = {currStart, currStart, currStart, currStart};
//			Line[] lines = {new Line(), new Line(), new Line(), new Line()};
//
//			Voice soprano = new Voice(Part.SOPRANO);
//			Voice alto = new Voice(Part.ALTO);
//			Voice tenor = new Voice(Part.TENOR);
//			Voice bass = new Voice(Part.BASS);
//			for(NoteP np : this.notes) {
//				int i = np.part.ordinal();
//				Unit unit = new Unit();
//				unit.addNote(np.note);
//				if(currents[i] > 0) {
//					lines[i].addSyllable(unit);
//					currents[i]--;
//				}
//				else {
//					if(currentLines[i] < lineCounts.size() - 1) {
//						currentLines[i]++;
//						currents[i] = lineCounts.get(currentLines[i]);
//						switch(np.part) {
//							case SOPRANO: soprano.addLine(lines[i]); break;
//							case ALTO: alto.addLine(lines[i]); break;
//							case TENOR: tenor.addLine(lines[i]); break;
//							case BASS: bass.addLine(lines[i]); break;
//						}
//						lines[i] = new Line();
//					}
//					lines[i].addSyllable(unit);
//				}
//			}
//			 soprano.addLine(lines[0]);
//			 alto.addLine(lines[1]);
//			 tenor.addLine(lines[2]);
//			 bass.addLine(lines[3]);
//
//			Music music = new Music();
//			music.setSoprano(soprano);
//			music.setAlto(alto);
//			music.setTenor(tenor);
//			music.setBass(bass);
//			tune.setMusic(music);
//			try {
//				Database.getInstance().add(tune);
//			}
//			catch(IOException e) {
//				System.err.println("Error saving tune.");
//				e.printStackTrace();
//			}
//		}
//		catch(BadInputException e) {
//			System.err.println("Invalid values in tune.");
//			e.printStackTrace();
//		}
	}

	public void add(Note n) {
	}

	public void removeLastNote() {
	}

	public void setPart(Part part){}

	public void setKey(KeySignature key){}

	public void setTimeSignature(TimeSignature sig) {}

	public void setMeter(Meter meter){}

	public void setStartingBeat(String sb) {
	}
}

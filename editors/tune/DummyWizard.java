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

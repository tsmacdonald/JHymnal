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


package gui;

import io.Database;

import model.InvalidHymnException;
import model.Hymn;
import model.HymnComponent;
import model.Text;
import model.Tune;

/**
 * Controller for the tune selector. {@see AbstractHymnComponentSelector}
 *
 * @author Tim Macdonald <tim.macdonald@my.wheaton.edu>
 */
public class TuneSelector extends AbstractHymnComponentSelector
{
	/** The last Tune that was selected */
	private Tune lastTune = null;

	public TuneSelector(Dropdown dropdown) {
		super(dropdown);
	}

	/**
	 * Returns all the hymn components of the appropriate type. Presumably
	 * reads them from the database.
	 */
	@Override
	public Iterable<? extends HymnComponent> getAll() {
		return Database.getInstance().validTuneIterator();
	}

	/**
	 * Called when the selected text is changed. Resets the Text dropdown.
	 */
	public void tuneChanged(Tune tune) {
		this.lastTune = tune;
		if(! Hymn.canCombine(tune, this.getCurrent())) {
			this.reset();
		}
		this.getDropdown().setSelectedItem(tune);
	}


	/**
	 * Called when the selected text is changed. Updates the current tune and
	 * rearranges the dropdown.
	 */
	public void textChanged(Text text) {
		this.changeCurrent(text);
		if(Hymn.canCombine(this.getCurrent(), this.lastTune)) {
			try {
				Hymn h = new Hymn((Text)(this.getCurrent()), this.lastTune);
				getDropdown().fireHymnChangeEvent(h);
				getDropdown().renderHymn(h);
			}
			catch(InvalidHymnException e) {
				System.err.println(e.getMessage());
			}
		}
		else {
			this.sortAll();
		}
	}

	/**
	 * Wrapper for getting the tunes/text that fit the current meter from the database.
	 */
	@Override
	public Iterable<? extends HymnComponent> getAccompanyingHymnComponents(HymnComponent current) {
		assert current instanceof Tune;
		return Database.getInstance().relevantTunes((Text)current);
	}
}

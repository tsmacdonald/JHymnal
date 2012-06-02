package gui;

import io.Database;

import model.InvalidHymnException;
import model.Hymn;
import model.HymnComponent;
import model.Text;
import model.Tune;

/**
 * Controller for the text selector. {@see AbstractHymnComponentSelector}
 *
 * @author Tim Macdonald <tim.macdonald@my.wheaton.edu>
 */
public class TextSelector extends AbstractHymnComponentSelector
{
	/** The last Text that was selected */
	private Text lastText = null;

	public TextSelector(Dropdown dropdown) {
		super(dropdown);
	}

	/**
	 * Returns all the hymn components of the appropriate type. Presumably
	 * reads them from the database.
	 */
	@Override
	public Iterable<? extends HymnComponent> getAll() {
		return Database.getInstance().validTextIterator();
	}

	/**
	 * Called when the selected tune is changed. Updates the current tune and
	 * rearranges the dropdown.
	 */
	public void tuneChanged(Tune tune) { 
		this.changeCurrent(tune);
		if(Hymn.canCombine(this.getCurrent(), this.lastText)) {
			try {
				Hymn h = new Hymn(this.lastText, (Tune)(this.getCurrent()));
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
	 * Called when the selected text is changed. Resets the Text dropdown.
	 */
	public void textChanged(Text text) {
		this.lastText = text;
		if(! Hymn.canCombine(this.getCurrent(), text)) {
			this.reset();
		}
		this.getDropdown().setSelectedItem(text);
	}

	/**
	 * Wrapper for getting the tunes/text that fit the current meter from the database.
	 */
	@Override
	public Iterable<? extends HymnComponent> getAccompanyingHymnComponents(HymnComponent current) {
		assert current instanceof Text;
		return Database.getInstance().relevantTexts((Tune)current);
	}
}

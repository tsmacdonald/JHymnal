package gui;

import model.Hymn;
import model.Text;
import model.Tune;

/**
 * The listener interface for receiving events related to changing the active
 * hymn component or hymn.
 *
 * @author Tim Macdonald <tim.macdonald@my.wheaton.edu>
 */
public interface HymnChangeListener
{
	/**
	 * Called when the active text has changed.
	 *
	 * @param t The new active text.
	 */
	public void textChanged(Text t);

	/**
	 * Called when the active tune has changed.
	 *
	 * @param t The new active tune.
	 */
	public void tuneChanged(Tune t);

	/**
	 * Called when the active hymn has changed.
	 *
	 * @param h The new active hymn.
	 */
	public void hymnChanged(Hymn h);
}

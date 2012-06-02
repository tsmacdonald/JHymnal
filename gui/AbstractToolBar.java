package gui;

import model.Hymn;
import model.Text;
import model.Tune;

/**
 * Defines the responsiblities of the tool bar-like object in the GUI (note
 * that it may actually be a menu, a cluster of buttons, or something else--
 * this exists outside of the precise graphical representation). Specifically,
 * tool bars are expected to notify their actions (buttons) about changes in
 * the program state.
 *
 * @author Tim Macdonald <tim.macdonald@my.wheaton.edu>
 */
public interface AbstractToolBar
{
	/**
	 * Notify all components that the active tune has changed.
	 *
	 * @param t The new Tune.
	 */
	public void fireTuneChangeEvent(Tune t);

	/**
	 * Notify all components that the active text has changed.
	 *
	 * @param t The new Text.
	 */
	public void fireTextChangeEvent(Text t);

	/**
	 * Notify all components that the active hymn has changed.
	 *
	 * @param h The new Hymn.
	 */
	public void fireHymnChangeEvent(Hymn h);
}

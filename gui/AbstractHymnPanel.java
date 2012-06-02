package gui;

import model.Hymn;

/**
 * Interface for hymn panels--guarantees that hymn panels are able to
 * accept a Hymn to be rendered.
 *
 * @author Tim Macdonald <tim.macdonald@my.wheaton.edu>
 */
public interface AbstractHymnPanel
{
	/**
	 * Sets the hymn to be displayed (but does not actually display it).
	 */
	public void setHymn(Hymn h);

	/**
	 * Returns the hymn that will be displayed (but might not be currently
	 * displayed.
	 */
	public Hymn getHymn();

	/**
	 * Renders the hymn object and displays it.
	 */
	public void render();
}

package gui;

/**
 * Defines the responsibilities of the graphical objects used to pick the
 * verses to be displayed.
 *
 * @author Tim Macdonald <tim.macdonald@my.wheaton.edu>
 */
public interface AbstractVerseSelector
{
	/**
	 * Called to indicate that the given verse has been toggled. It is
	 * expected that the verse selector will cause the verse to be displayed
	 * or hidden as appropriate.
	 *
	 * @param verseNumber The verse number to toggle. Starts at 1. Counts
	 *                    verses that <em>could</em> be displayed, not that
	 *                    <em>are</em> displayed.
	 */
	public void toggleVerse(int verseNumber);

	/**
	 * Called to indicate that the given verse has been added. It is expected
	 * that the verse selector will cause the verse to be displayed.
	 *
	 * @param verseNumber The verse number to toggle. Starts at 1. Counts
	 *                    verses that <em>could</em> be displayed, not that
	 *                    <em>are</em> displayed.
	 */
	public void addVerse(int verseNumber);

	/**
	 * Called to indicate that the given verse has been removed. It is expected
	 * that the verse selector will cause the verse to be displayed.
	 *
	 * @param verseNumber The verse number to toggle. Starts at 1. Counts
	 *                    verses that <em>could</em> be displayed, not that
	 *                    <em>are</em> displayed.
	 */
	public void removeVerse(int verseNumber);
}


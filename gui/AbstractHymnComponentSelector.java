package gui;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import model.Hymn;
import model.HymnComponent;
import io.Database;

/**
 * Serves as the model for the tune and text selectors. Stores data and provides logic.
 *
 * @author Tim Macdonald
 */
public abstract class AbstractHymnComponentSelector implements HymnChangeListener
{
	/** Holds all the hymn components read from the database. */
	private Set<HymnComponent> allHymnComponents = new HashSet<HymnComponent>();

	/** Adaptor for the view. */
	private Dropdown dropdown;


	/** The currently selected tune/text. */
	private HymnComponent current;

	public AbstractHymnComponentSelector(Dropdown dropdown) {
		this.dropdown = dropdown;
		this.allHymnComponents = new HashSet<HymnComponent>();
		this.populateSet();
		this.current = null;
		this.sortAll();
	}

	protected Dropdown getDropdown() {
		return this.dropdown;
	}

	/**
	 * Rebuilds the set of all hymn components (in case new ones are added
	 * while the program is running).
	 */
	public void populateSet() {
		allHymnComponents.clear();
		for(HymnComponent hc : getAll()) {
			allHymnComponents.add(hc);
		}
	}

	/**
	 * Breaks the hymn components into two sorted lists--one for the ones that
	 * fit the meter, another for the remainder, and adds them to the dropdown
	 * in an appropriate order.
	 */
	public void sortAll() {
		Database db = Database.getInstance();
		SortedSet<HymnComponent> relevantHymnComponents = new TreeSet<HymnComponent>(new HymnComparator());
		SortedSet<HymnComponent> otherHymnComponents = new TreeSet<HymnComponent>(new HymnComparator());

		for(HymnComponent hc : this.allHymnComponents) {
			otherHymnComponents.add(hc);
		}

		for(HymnComponent hc : getAccompanyingHymnComponents(this.current)) {
			relevantHymnComponents.add(hc);
			otherHymnComponents.remove(hc);
		}

		this.dropdown.clear();
		if(! relevantHymnComponents.isEmpty()) {
			this.dropdown.add(relevantHymnComponents);
			this.dropdown.addSeparator();
		}
		this.dropdown.add(otherHymnComponents);
	}

	/**
	 * Sets the hymn components whose meter must be matched.
	 */
	public void changeCurrent(HymnComponent newCurrent) {
		this.current = newCurrent;
	}

	/**
	 * Returns the hymn component whose meter must be matched.
	 */
	public HymnComponent getCurrent() {
		return this.current;
	}

	/**
	 * Returns all the hymn components of the appropriate type. Presumably
	 * reads them from the database.
	 */
	public abstract Iterable<? extends HymnComponent> getAll();

	/**
	 * Restores the hymn list to its original state. That is, it
	 * clears current and removes the "relevant hymns" list.
	 */
	public void reset() {
		this.populateSet();
		this.changeCurrent(null);
		this.sortAll();
		this.dropdown.repaint();
	}

	/**
	 * Literally does nothing.
	 */
	public void hymnChanged(Hymn h) { }

	/**
	 * Useful for debugging.
	 */
	private static int echo(int x) {
		System.out.println(x);
		return x;
	}

	/**
	 * Wrapper for getting the tunes/text that fit the current meter from the database.
	 */
	public abstract Iterable<? extends HymnComponent> getAccompanyingHymnComponents(HymnComponent current);

	/**
	 * Comparator used for sorting hymn components alphabetically by title.
	 */
	private class HymnComparator implements Comparator<HymnComponent>
	{
		public int compare(HymnComponent a, HymnComponent b) {
			return a.getName().compareTo(b.getName());
		}
		
		public boolean equals(HymnComponent a, HymnComponent b) {
			return a.equals(b);
		}
	}
}

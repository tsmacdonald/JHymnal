/**
 * HymnComponent.java
 * 
 * Supertype for Texts and Tunes.
 * 
 * @author Tim Macdonald, Jason Petersen
 */

package model;

public interface HymnComponent {

    /**
     * Get the Meter for this HymnComponent.
     * @return The Meter for this HymnComponent.
     * @author Spring 2010
     */
	public Meter getMeter();

	/**
	 * Returns the human-readable title.
	 * @author Tim Macdonald
	 */
	public String getName();
	
	/**
	 * Get the filename for this HymnComponent,
	 * minus any file extensions.
	 * @return The filename for this HymnComponent.
	 * @author Jason Petersen
	 */
	public String getFilename();
	
	/**
	 * Determine if this HymnComponent is valid,
	 * i.e. is a final, ready-for-rendering product
	 * @return True if the HymnComponent is ready for
	 * rendering, otherwise false.
	 * @author Jason Petersen
	 */
	public boolean isValid();
}

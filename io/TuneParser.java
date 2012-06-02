/**
 * TuneParser.java
 * 
 * Interface for a TuneParser.
 * Provides methods to parse a file.
 * 
 * @author Jason Petersen
 */

package io;

import java.io.File;
import java.io.IOException;

import model.Tune;

interface TuneParser {
	
	/**
	 * Build a complete instance of an Tune from the given File.
	 * The returned Tune should have both real data and metadata associated with it.
	 * @param file The File from which to build the Tune.
	 * @return The Tune parsed from the given File.
	 * @throws IOException An error occurred when reading from the File.
	 * @author Jason Petersen
	 */
	public Tune parse(File file) throws IOException;
	
}

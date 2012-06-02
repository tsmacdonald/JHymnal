/**
 * TextParser.java
 * 
 * Interface for a TextParser.
 * Provides methods to parse a file.
 * 
 * @author Jason Petersen
 */

package io;

import java.io.File;
import java.io.IOException;

import model.Text;

interface TextParser {
	
	/**
	 * Build a complete instance of an Text from the given File.
	 * The returned Text should have both real data and metadata associated with it.
	 * @param file The File from which to build the Text.
	 * @return The Text parsed from the given File.
	 * @throws IOException An error occurred when reading from the File.
	 * @author Jason Petersen
	 */
	public Text parse(File file) throws IOException;

}

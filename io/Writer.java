/**
 * Writer.java 
 * 
 * Interface for a file writer
 * which creates Texts, Tunes and
 * Songs.
 * 
 * @author Jason Petersen
 */

package io;

import java.io.File;
import java.io.IOException;

import model.Hymn;
import model.Text;
import model.Tune;

interface Writer {
	
	/**
	 * Create a file from the given Tune.
	 * @param tune The Tune to write out to a file.
	 * @throws InputFileException An error occurred when writing the file.
	 * @throws UnsupportedOperationException The operation is not
	 * supported by this Writer.
	 * @author Jason Petersen
	 */
	public void write(Tune tune, File file) throws IOException, UnsupportedOperationException;
	
	/**
	 * Create a file from the given Text.
	 * @param text The Text to write out to a file.
	 * @throws InputFileException An error occurred when writing the file.
	 * @throws UnsupportedOperationException The operation is not
	 * supported by this Writer.
	 * @author Jason Petersen
	 */
	public void write(Text text, File file) throws IOException, UnsupportedOperationException;
	
	/**
	 * Create a file from the given Hymn.
	 * @param hymn The Hymn to write out to a file.
	 * @throws InputFileException An error occurred when writing the file.
	 * @throws UnsupportedOperationException The operation is not
	 * supported by this Writer.
	 * @author Jason Petersen
	 */
	public void write(Hymn hymnm, File file) throws IOException, UnsupportedOperationException;

}

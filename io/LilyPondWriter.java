/**
 * LilyPondWriter.java
 * 
 * Class to write out Texts, Tunes
 * and Songs to a LilyPond file.
 * 
 * @author Jason Petersen
 */

package io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import model.Hymn;
import model.Text;
import model.Tune;

public final class LilyPondWriter implements Writer {
	
	/** Used to create the LilyPond-formatted Strings */
	private static LilyPondUtility util = LilyPondUtility.getInstance();
	
	/** Singleton */
	private static LilyPondWriter instance = new LilyPondWriter();
	
	/**
	 * Constructor for Singleton.
	 * @author Jason Petersen
	 */
	private LilyPondWriter() { }
	
	/**
	 * Accessor method for the singleton of LilyPondWriter
	 * @return The singleton of LilyPondWriter
	 * @author Jason Petersen
	 */
	public static LilyPondWriter getInstance(){
		return instance;
	}

	@Override
	public void write(Tune tune, File file) throws IOException {
		String toWrite = util.createString(tune);
		createFileFrom(toWrite, file);
	}

	@Override
	public void write(Text text, File file) throws IOException {
		String toWrite = util.createString(text);
		createFileFrom(toWrite, file);
	}
	
	@Override
	public void write(Hymn hymn, File file) throws IOException {
		String toWrite = util.createString(hymn);
		createFileFrom(toWrite, file);
	}

	/* ---------- Helper methods ---------- */
	
	/**
	 * Helper method to write a LilyPond-formatted String to a file.
	 * @param asLilyPond The Text, Tune or Song as a LilyPond-formatted String.
	 * @throws InputFileException There was an IOException when creating the file.
	 * @author Jason Petersen
	 */
	private void createFileFrom(String asLilyPond, File file) throws IOException {
		if(file == null)
			throw new IOException("File cannot be null");
		
		if(file.exists()) file.delete();
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.write(asLilyPond);
		bw.close();
		
		System.out.println("Created file " + file.getAbsolutePath());
	}
}

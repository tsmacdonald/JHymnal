// Copyright 2012 Jason Petersen and Timothy Macdonald
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * ParserEngine.java 
 * 
 * Class to parse Tunes and Texts
 * from Files.
 * 
 * @author Jason Petersen
 */

package io;

import java.io.File;
import java.io.IOException;

import model.Text;
import model.Tune;

public class ParserEngine {
	
	/** The list of possible TextParsers */
	private static TextParser[] textParsers = { XMLTextParser.getInstance(), FlatFileTextParser.getInstance() };
	
	/** The list of possible TuneParsers */
	private static TuneParser[] tuneParsers = { XMLTuneParser.getInstance(), FlatFileTuneParser.getInstance() };  
	
	/**
	 * Get the Text stored in the given File.
	 * @param file The File from which to get the Text.
	 * @return The Text parsed from the given File.
	 * @throws UnsupportedFileException The given File does not
	 * have an associated TextParser.
	 * @author Jason Petersen
	 */
	public static Text parseText(File file) throws UnsupportedFileException {
		if(file == null)
			throw new UnsupportedFileException("File cannot be null");
		
		for(TextParser p : textParsers){
			try {
				return p.parse(file);
			}
			catch (IOException ioe){
				/* System.err.println("ParserEngine.parseText - " + ioe.getMessage()); */
			}
		}
		throw new UnsupportedFileException("The file " + file.getName() + " does not have an associated TextParser.");
	}
	
	/**
	 * Get the Tune stored in the given File.
	 * @param file The File from which to get the Tune.
	 * @return The Tune parsed from the given File.
	 * @throws UnsupportedFileException The given File does not
	 * have an associated TuneParser.
	 * @author Jason Petersen
	 */
	public static Tune parseTune(File file) throws UnsupportedFileException {
		if(file == null)
			throw new UnsupportedFileException("File cannot be null");
		
		for(TuneParser p : tuneParsers){
			try {
				return p.parse(file);
			}
			catch (IOException ioe){
				System.err.println("ParserEngine.parseTune - " + ioe.getMessage());
			}
		}
		throw new UnsupportedFileException("The file " + file.getName() + " does not have an associated TuneParser.");
	}
}

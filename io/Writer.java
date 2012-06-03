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

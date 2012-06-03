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

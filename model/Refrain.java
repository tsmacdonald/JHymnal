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
 * Refrain.java
 * 
 * Class to model a specific portion of
 * music which needs to contain lyrics,
 * used for choruses and alleluias.
 * 
 * @author Jason Petersen
 */

package model;

import io.BadInputException;

public final class Refrain {
	
	/** The lyrics for this Refrain */
	private Lyric lyric;
	
	/** The music for this Refrain */
	private Music music;
	
	/** 
	 * Constructor.
	 * @param soprano The Soprano Voice for this Refrain.
	 * @param alto The Alto Voice for this Refrain.
	 * @param tenor The Tenor Voice for this Refrain.
	 * @param bass The Bass Voice for this Refrain.
	 * @param lyric The Lyric for this Refrain.
	 * @author Jason Petersen
	 */
	public Refrain(Music music, Lyric lyric) throws BadInputException {
		if(music == null || lyric == null)
			throw new BadInputException("Input for Refrain cannot be null");
			
		if(!music.isValid() || music.numberOfSyllables() != lyric.numberOfSyllables())
			throw new BadInputException("The number of Music and Lyric syllables must match for Refrains");
		
		this.lyric = lyric;
		this.music = music;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other instanceof Refrain) {
			Refrain refrain = (Refrain) other;
			return refrain.music.equals(this.music) && refrain.lyric.equals(this.lyric);
		}
		return false;
	}
	
	/**
	 * Get the Lyric for this Refrain.
	 * @return The Lyric for this Refrain.
	 * @author Jason Petersen
	 */
	public Lyric getLyric() {
		return lyric;
	}
	
	/**
	 * Get the Music for this Refrain.
	 * @return The Music for this Refrain.
	 * @author Jason Petersen
	 */
	public Music getMusic() {
		return music;
	}
	
}

package io;

import java.io.IOException;

public class UnsupportedFileException extends IOException {
	
	/** Useful if we were going to serialize this class, but this mostly just keeps Eclipse from complaining */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * @param message The error message
	 * @author Jason Petersen
	 */
	UnsupportedFileException(String message) {
		super(message);
	}
}

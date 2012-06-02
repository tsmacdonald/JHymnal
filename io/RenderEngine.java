/**
 * RenderEngine.java
 * 
 * Class responsible for rendering
 * Hymns and HymnComponents.
 * Currently this is done via LilyPond,
 * though it could easily be changed.
 * 
 * @author Jason Petersen
 */

package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import model.Hymn;
import model.Text;
import model.Tune;

public final class RenderEngine {
	
	/** Used to create the LilyPond compliant Strings for rendering */
	private static LilyPondUtility util = LilyPondUtility.getInstance();
	
	/** The location of lilypond on the user's computer */
	private static final String cmd = "etc/bin/lilypond";
	
	/**
	 * Create an image file from the given Hymn.
	 * @param hymn The Hymn from which to make an image files.
	 * @return The image files created from the Hymn.
	 * @throws IOException An error occurred when creating the file.s
	 * @author Jason Petersen
	 */
	public static ArrayList<File> render(Hymn hymn) throws IOException {
		return createFiles(util.createString(hymn), hymn.getFilename());
	}
	
	/**
	 * Create a image files from the given Tune.
	 * @param tune The Tune from which to make an image files.
	 * @return The image files created from the Tune.
	 * @throws IOException An error occurred when creating the files.
	 * @author Jason Petersen
	 */
	public static ArrayList<File> render(Tune tune) throws IOException {
		return createFiles(util.createString(tune), tune.getFilename());
	}
	
	/**
	 * Create a image files from the given Text.
	 * @param text The Text from which to make an image files.
	 * @return The image files created from the Text.
	 * @throws IOException An error occurred when creating the files.
	 * @author Jason Petersen
	 */
	public static ArrayList<File> render(Text text) throws IOException {
		return createFiles(util.createString(text), text.getFilename());
	}
	
	/**
	 * Fetch the existing images rendered for the given filename.
	 * @param filename The filename, minus any extensions, for which
	 * to search for matching images.
	 * @return The ArrayList of Files that are the images. May be empty.
	 * @author Jason Petersen
	 */
	public static ArrayList<File> fetch(String filename) {
		ArrayList<File> fetched = new ArrayList<File>();
		
		File directory = new File(Database.imgCache);
		if(directory.isDirectory()) {
			for(File file : directory.listFiles()) {
				if(file.isFile()) {
					String image = file.getAbsolutePath();
					if(image.contains(filename + ".png")) { // Grab the single image
						fetched.add(file);
						break;
					}
					else if(image.contains(filename + "-page")) { // Grab multiple images
						fetched.add(file);
					}
				}
			}
		}
		return fetched;
	}
	
	/* ---------- Private methods ---------- */
	
	private static ArrayList<File> createFiles(String asString, String filename) throws IOException {
		if(supportedOS()) {
			renderPNGs(asString, filename);
			ArrayList<File> toReturn = fetch(filename);
			if(toReturn.isEmpty())
	        	throw new IOException("Failed to render " + filename);
			return toReturn;
		}
		throw new IOException("Unsupported operating system: " + System.getProperty("os.name"));
	}
	
	/**
	 * Create the PNGs from the given LilyPond compliant String.
	 * @param asString A LilyPond compliant String.
	 * @param filename The name of the file to create.
	 * @throws IOException An error occurred when creating the file.
	 * @return The image created from the given String.
	 * @author Jason Petersen
	 */
	private static void renderPNGs(String asString, String filename) throws IOException {
		filename = Database.imgCache + "/" + filename;
		
		// Call echo on the Hymn as a String
		ProcessBuilder p = new ProcessBuilder("/bin/echo", asString);
		p.redirectErrorStream(true);
		Process echo = p.start();
        
        // Call lilypond
		ProcessBuilder pb = new ProcessBuilder(cmd, "-fpng", "-o", filename, "-");
		pb.redirectErrorStream(true);
		Process lily = pb.start();
		
		// Pipe the output from echo to lilypond
		pipe(echo.getInputStream(), lily.getOutputStream());

		// Wait for LilyPond to finish its process
		try {
			lily.waitFor();
		} catch (InterruptedException e) {
			System.err.println("RenderEngine.createPNG - LilyPond was interrupted");
		}
		
		// Process the output from lilypond
		String s = null;
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(lily.getInputStream()));
        while((s = stdInput.readLine()) != null) {
        	//System.out.println(s); 
        }
	}

	/**
	 * Helper function to pipe data from the input stream of one process to
	 * the output stream of another. Specifically, this is used to send the
	 * stdin from echo to lilypond for conversion. This is done to prevent
	 * writing .ly files to the disk, thereby cutting the number of disk writes
	 * for hymn creation in half. Under the hood, a new thread is spawned and
	 * started to do all the copying of the bytes.
	 * 
	 * This was not a solution I reached on my own, though the final product is. 
	 * The code is modeled after that presented on the following two websites:
	 * http://stackoverflow.com/questions/60302/starting-a-process-with-inherited-stdin-stdout-stderr-in-java-6
	 * http://blog.bensmann.com/piping-between-processes
	 * 
	 * @param input The InputStream to copy to output.
	 * @param output The OutputStream into which input will be copied.
	 * @author Jason Petersen 
	 */
	private static void pipe(final InputStream input, final OutputStream output) {
		new Thread(new Runnable(){
			public void run(){
				try {
					int read;
					byte[] b = new byte[512];
					for(;;){
						read = input.read(b, 0, b.length);
						if(read > -1) output.write(b, 0, read);
						else break;
					}
				} catch (IOException e) {
					System.err.println("RenderEngine.pipe - Failed to pipe data from input to output");
				} finally {
					try {
						input.close();
					} catch (IOException e) {
						System.err.println("RenderEngine.pipe - Failed to close input");
					}
					try {
						output.close();
					} catch (IOException e){
						System.err.println("RenderEngine.pipe - Failed to close output");
					}
				}	
			}
		}).start();
	}
	
	/**
	 * Helper function to test if the user's
	 * operating system is supported - currently, the only
	 * supported OS is Linux.
	 * @return True if the operating system is supported,
	 * otherwise false.
	 * @author Jason Petersen
	 */
	private static boolean supportedOS() {
		return System.getProperty("os.name").equals("Linux");
	}
	
}

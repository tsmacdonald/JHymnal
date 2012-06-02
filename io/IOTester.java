/**
 * Driver.java
 * 
 * Simple class to test input and
 * output of texts, tunes and files.
 * 
 * @author Spring 2010
 * @author Jason Petersen
 */

package io;

import java.io.File;
import java.io.IOException;

import model.InvalidHymnException;
import model.Hymn;
import model.Text;
import model.Tune;

final class IOTester {

	private static Database db = Database.getInstance();
	
	public static void main(String[] args) {
		int counter = 1;
		System.out.println("--- TEXTS ---");
		for(Text t : db.validTextIterator()){
			System.out.println(counter + ". " + t.getName());
			counter++;
		}
		
		counter = 1;
		System.out.println("--- TUNES ---");
		for(Tune t : db.validTuneIterator()){
			System.out.println(counter + ". " + t.getName());
//			if(t.getName().contains("REPEAT")) {
//				try {
//					Text txt = null;
//					for(Text tx : db.validTextIterator()) {
//						if(tx.getName().contains("Amazing")) {
//							txt = tx;
//							break;
//						}
//					}
//					Hymn hymn = new Hymn(txt, t);
//					hymn.render();
//					LilyPondWriter.getInstance().write(hymn, new File("data/bad/" + hymn.getFilename() + ".ly"));
//				} catch (IOException e) {
//					// Auto-generated catch block
//					e.printStackTrace();
//				} catch (InvalidHymnException e) {
//					// Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
			counter++;
		}
		
		//testLilyPondTunes();
		//testLilyPondTexts();
		//remakeXMLFromFlatFiles();
		rebuildLilyPondAndPNGs();
		//testRenderCaching();
		//printMeters();
	}
	
	private static void testLilyPondTunes() {
		for(Tune tune : db.validTuneIterator()) {
			try {
				io.RenderEngine.render(tune);
			} catch(IOException ioe) {
				System.err.println("IOTester.testLilyPondTunes() - " + ioe.getMessage());
				
			}
		}
	}
	
	private static void testLilyPondTexts() {
		for(Text text : db.validTextIterator()) {
			try {
				io.RenderEngine.render(text);
			} catch(IOException ioe) {
				System.err.println("IOTester.testLilyPondTunes() - " + ioe.getMessage());
			}
		}
	}
	
	private static void printMeters() {
		for(Tune tune : db.validTuneIterator()){
			System.out.println(tune.getName() + " - " + tune.getMeter());
		}
	}
	
	private static void testRenderCaching() {
		for(Text text : db.validTextIterator()){
			for(Tune tune : db.validTuneIterator()){
				try {
					Hymn hymn = new Hymn(text, tune);
					hymn.render();
				}
				catch(InvalidHymnException ihe){
					// Do nothing
				}
				catch(IOException ioe){ // Failed to render the image
					System.err.println("IOTester.databaseTest() - " + ioe.getMessage());
				}
			}
		}
	}
	
	private static void remakeXMLFromFlatFiles() {
		XMLWriter writer = XMLWriter.getInstance();
		FlatFileTuneParser tuneParser = FlatFileTuneParser.getInstance();
		FlatFileTextParser textParser = FlatFileTextParser.getInstance();
		
		File folder = new File("data/flatfile");
		if(folder.isDirectory()){
			for(File file : folder.listFiles()){
				String name = file.getName();
				if(name.contains(".tune")){
					try {
						Tune tune = tuneParser.parse(file);
						File xml = new File("data/xml/" + name.substring(0, name.length()-5).toUpperCase() + ".xml");
						writer.write(tune, xml);
					} catch(IOException ioe){
						System.err.println("IOTester.remakeXMLFromFlatFiles() - " + ioe.getMessage());
					}
				}
				if(name.contains(".text")){
					try {
						Text text = textParser.parse(file);
						File xml = new File("data/xml/" + name.substring(0, name.length()-5) + ".xml");
						writer.write(text, xml);
					} catch(IOException ioe){
						System.err.println("IOTester.remakeXMLFromFlatFiles() - " + ioe.getMessage());
					}
				}
			}
		}
	}

	private static void rebuildLilyPondAndPNGs(){		
		LilyPondWriter w = LilyPondWriter.getInstance();

		for(Text text : db.validTextIterator()){
			for(Tune tune : db.validTuneIterator()){
				Hymn hymn;
				try {
					hymn = new Hymn(text, tune);
					io.RenderEngine.render(hymn);
					try {
						w.write(hymn, new File("data/lilypond/" + hymn.getFilename() + ".ly"));
					} catch(IOException e){
						// Do nothing
					}
				}
				catch(InvalidHymnException ihe){
					// Do nothing
				}
				catch(IOException ioe){ // Failed to render the image
					try {
						hymn = new Hymn(text, tune);
						w.write(hymn, new File("data/bad/" + hymn.getFilename() + ".ly"));
					} catch (InvalidHymnException e) {
						// Do nothing
					} catch(IOException e) {
						System.err.println("IOTester.rebuildLilyPondAndPNGs() - " + e.getMessage());
					}
					System.err.println("IOTester.rebuildLilyPondAndPNGs() - " + ioe.getMessage());
				}
			}
		}
	}
	
}

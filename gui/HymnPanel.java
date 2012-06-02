package gui;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import model.Hymn;

/**
 * Responsible for converting Hymn objects into a displayable, graphical
 * component.
 *
 * @author Tim Macdonald <tim.macdonald@my.wheaton.edu>
 */
public class HymnPanel extends JPanel implements AbstractHymnPanel
{
	private Hymn hymn;
	private JComponent displayed;

	public HymnPanel() {
		super();
		this.displayed = null;
		try
		{
			this.displayed = new JLabel(new ImageIcon(new File("default.png").getAbsolutePath()));
			this.add(this.displayed);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		this.hymn = null;
	}

	public void setHymn(Hymn h) {
		this.hymn = h;
	}

	/**
	 * Set the component that's being displayed. This needs to be an explicit
	 * method because it's used in anonymous inner classes.
	 */
	private synchronized void setDisplayed(JComponent displayed) {
		this.displayed = displayed;
	}

	/**
	 * Return the component that's being displayed. This needs to be an explicit
	 * method because it's used in anonymous inner classes.
	 */
	private synchronized JComponent getDisplayed() {
		return this.displayed;
	}

	/**
	 * Displays the current hymn as an image in the panel. {@see model.Hymn#render}
	 */
	public void render() {
		new Thread(new Runnable() {
			public void run() {
				try {
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					File[] imageFiles = null;
					try {
						imageFiles = (getHymn().render().toArray(new File[] {}));
					}
					catch(IOException e) {
						System.err.println("Problem rendering hymn: " + getHymn());
					}
					final File[] finalImageFiles = imageFiles;
					JComponent old = getDisplayed();
					remove(getDisplayed());
					setDisplayed(null);
					try {
						SwingUtilities.invokeLater(new Thread(
						  new Runnable() {
							public void run() {
								setDisplayed(new JLabel(mergeImages(mapImage(finalImageFiles))));
								HymnPanel.this.add(getDisplayed());
								getDisplayed().setVisible(true);
								HymnPanel.this.revalidate();
							}
						}));
					}
					catch(Exception e) {
						System.err.println("Error displaying rendered hymn.");
						System.err.println(e.getMessage());
					}
				}
				finally {
					setCursor(Cursor.getDefaultCursor());
				}
			}
		}).start();
	}

	/**
	 * Returns an array of <code>ImageIcon</code>s, where the <em>i</em>th
	 * element of the returned array is equal to the <code>ImageIcon</code>
	 * constructed from the <em>i</em>th element of the given array.
	 */
	private static ImageIcon[] mapImage(File[] files) {
		ImageIcon[] images = new ImageIcon[files.length];
		for(int i = 0; i < files.length; ++i) {
			images[i] = new ImageIcon(files[i].getAbsolutePath());
		}
		return images;
	}

	/**
	 * Returns an <code>ImageIcon</code> created by concatenating the given
	 * images together.
	 */
	private static ImageIcon mergeImages(ImageIcon[] images) {
		int currentHeight = 0;
		int totalHeight = 0;
		for(ImageIcon i : images) {
			totalHeight += i.getIconHeight();
		}
		int width = images[0].getIconWidth();
		BufferedImage merged = new BufferedImage(width, totalHeight,
		                                         BufferedImage.TYPE_INT_ARGB);
		Graphics g = merged.getGraphics();
		for(ImageIcon i : images) {
			g.drawImage(i.getImage(), 0, currentHeight, null);
			currentHeight += i.getIconHeight();
		}
		return new ImageIcon(merged);
	}

	/**
	 * Returns the current hymn, which will be displayed upon the next call to
	 * render();
	 */
	public Hymn getHymn() {
		return this.hymn;
	}
}

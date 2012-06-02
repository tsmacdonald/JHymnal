package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import model.Hymn;
import model.HymnComponent;
import model.Text;
import model.Tune;

/**
 * Swing implementation of the Dropdown interface.
 *
 * @author Tim Macdonald <tim.macdonald@my.wheaton.edu>
 */
public class SwingDropdown extends Dropdown
{
	private JComboBox comboBox;
	private boolean actionListenerSet;
	private AbstractToolBar toolBar;
	private AbstractHymnPanel panel;

	public SwingDropdown(AbstractToolBar toolBar, AbstractHymnPanel ahp) {
		super();
		this.toolBar = toolBar;
		this.panel = ahp;
		this.comboBox = new JComboBox();
		this.actionListenerSet = false;
	}

	@Override
	public void setSelector(AbstractHymnComponentSelector s) {
		super.setSelector(s);
		setActionListener();
	}

	private void setActionListener() {
		if(this.actionListenerSet) return;
		this.comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.paramString().indexOf("Button1") == -1) {
					return; //TODO le hack
				}
				final JComboBox box = (JComboBox)e.getSource();
				Object selected = box.getSelectedItem();
				if(!(selected instanceof HymnComponent)) return;
				final HymnComponent hc = (HymnComponent)selected;
				new Thread(new Runnable() {
					public void run() {
						if(hc == null) return;
						else if(hc instanceof Text) {
							SwingDropdown.this.toolBar.fireTextChangeEvent((Text)hc);
						}
						else if(hc instanceof Tune) {
							SwingDropdown.this.toolBar.fireTuneChangeEvent((Tune)hc);
						}
						else {
							System.err.println("Unexpected item in ComboBox: " + hc);
						}
					}
				}).start();
			}
		});
		this.actionListenerSet = true;
	}

	/**
	 * Adds everything in i, in order, to the dropdown.
	 */
	@Override
	public void add(Iterable<? extends HymnComponent> i) {
		for(HymnComponent c : i) {
			this.comboBox.addItem(c);
		}
	}

	/**
	 * Adds a horizontal line to the dropdown to separate groups of items.
	 */
	@Override
	public void addSeparator() {
		this.comboBox.addItem(new Separator());
	}

	/**
	 * Removes all items from the dropdown.
	 */
	@Override
	public void clear() {
		this.comboBox.removeAllItems();
	}

	/**
	 * Returns the JComponent that should be displayed.
	 */
	public JComponent getComponent() {
		return this.comboBox;
	}

	public void renderHymn(Hymn h) {
		this.panel.setHymn(h);
		this.panel.render();
	}

	/**
	 * Forward the event to the tool bar.
	 */
	public void fireHymnChangeEvent(Hymn h) {
		this.toolBar.fireHymnChangeEvent(h);
	}

	/**
	 * Sets the selected item in the JComboBox.
	 */
	@Override
	public void setSelectedItem(HymnComponent h) {
		this.comboBox.setSelectedItem(h);
		this.comboBox.repaint();
	}

	@Override
	public void repaint() {
		this.comboBox.repaint();
	}

	/**
	 * Used to separate relevant HymnComponents from the rest of them. In the
	 * future this should be redone to work with a ListCellRenderer and provide
	 * a prettier line.
	 */ //TODO ^
	private class Separator {
		@Override
		public String toString() {
			return "--------";
		}
	}
}

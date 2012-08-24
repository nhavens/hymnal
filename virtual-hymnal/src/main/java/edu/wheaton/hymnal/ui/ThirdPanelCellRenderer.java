package edu.wheaton.hymnal.ui;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import java.awt.Component;
import java.awt.Color;
import javax.swing.ToolTipManager;

public class ThirdPanelCellRenderer extends JLabel implements
		ListCellRenderer {
	private static final long serialVersionUID = 1L;
	private final Object defaultObject;
	private static final String DEFAULT_TOOLTIP_TEXT = "Default Selection";

	public ThirdPanelCellRenderer(Object defaultObject) {
		setOpaque(true);
		this.defaultObject = defaultObject;
	}

	public Component getListCellRendererComponent(JList list,
			Object value, int index, boolean isSelected, boolean cellHasFocus) {

		setText(value.toString());

		Color background;
		Color foreground;

		// check if this cell represents the current DnD drop location
		JList.DropLocation dropLocation = list.getDropLocation();
		if (dropLocation != null && !dropLocation.isInsert()
				&& dropLocation.getIndex() == index) {

			background = Color.BLUE;
			foreground = Color.WHITE;

			// check if this cell is selected
		} else if (isSelected) {
			background = new Color(184, 207, 229);
			foreground = Color.BLACK;

			// unselected, and not the DnD drop location
		} else {
			if (value.equals(defaultObject)) {
				background = Color.YELLOW;
				foreground = Color.BLACK;
			} else {
				background = Color.WHITE;
				foreground = Color.BLACK;
			}
		}

		setBackground(background);
		setForeground(foreground);
		if (value.equals(defaultObject)) {
			ToolTipManager ttm = ToolTipManager.sharedInstance();
			ttm.setInitialDelay(100);
			addMouseMotionListener(ttm);
			setToolTipText(DEFAULT_TOOLTIP_TEXT);
		}
		return this;
	}
}

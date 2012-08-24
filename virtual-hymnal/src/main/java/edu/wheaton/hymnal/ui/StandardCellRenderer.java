package edu.wheaton.hymnal.ui;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import java.awt.Component;
import java.awt.Color;
import java.awt.Graphics;

class StandardCellRenderer extends JLabel implements ListCellRenderer {
	private static final long serialVersionUID = 1L;

	public StandardCellRenderer() {
		setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

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
			background = Color.WHITE;
			foreground = Color.BLACK;
		}

		setBackground(background);
		setForeground(foreground);

		return this;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Color orig = g.getColor();
		g.setColor(Color.GRAY);
		g.setXORMode(Color.BLACK);
		int[] xcoords = { getWidth() - 10, getWidth() - 10, getWidth() - 3 };
		int[] ycoords = { 3, getHeight() - 3, (getHeight() - 6) / 2 + 3 };
		g.fillPolygon(xcoords, ycoords, 3);
		g.setColor(orig);
	}
}

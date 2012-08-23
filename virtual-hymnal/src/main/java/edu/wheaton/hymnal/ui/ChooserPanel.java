package edu.wheaton.hymnal.ui;

import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public abstract class ChooserPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 266;
    private static final int HEIGHT = 750;
    private static final Dimension PREFERRED_SIZE = new Dimension(WIDTH, HEIGHT);
    
    public ChooserPanel() { super(); }
    
    public ChooserPanel(LayoutManager layout) {
        super(layout);
    }
    
    public Dimension getPreferredSize() {
        return PREFERRED_SIZE;
    }
}

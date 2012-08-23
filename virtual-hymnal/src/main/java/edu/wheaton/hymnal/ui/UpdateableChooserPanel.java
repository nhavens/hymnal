package edu.wheaton.hymnal.ui;

import java.awt.LayoutManager;

public abstract class UpdateableChooserPanel extends ChooserPanel {
	private static final long serialVersionUID = 1L;

	public UpdateableChooserPanel() { super(); }

    public UpdateableChooserPanel(LayoutManager layout) {
        super(layout);
    }

    public abstract void setSelectedItem();
}

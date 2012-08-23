package edu.wheaton.hymnal.ui;

import java.awt.BorderLayout;
import javax.swing.JLabel;

public class ChooserPanelProxy extends UpdateableChooserPanel {
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "Waiting for selection...";
    private JLabel messageLabel;
    
    public ChooserPanelProxy() {
        super(new BorderLayout());        
        messageLabel = new JLabel(MESSAGE);
        this.add(messageLabel);
    }

    public void setSelectedItem() { return; }
}

package edu.wheaton.hymnal.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Chooser extends JFrame implements Observer {
    
	private static final long serialVersionUID = 1L;

	private static String title = "Chooser";
    
    private JPanel selectionPanel;
    /*
     * One of these will be a TextChooserPanel and the other will be a
     * TuneChooserPanel depending on the selection made in the 
     * TopLevelChooserPanel.
     */
    private UpdateableChooserPanel secondPanel;
    private UpdateableChooserPanel thirdPanel;
    private MusicPage musicPage;
    
	private Model model;

	/**
	 * The render button
	 */
	private JButton renderButton;    

    public Chooser() {
        model = new Model();

        // setup music page (will not be displayed initially)
        musicPage = new MusicPage(model, this);
        model.addObserver(musicPage);
        
        // setup window
        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        model.addObserver(this);

        // add panels
        selectionPanel = new JPanel();
        selectionPanel.setLayout(new GridLayout(1,3));
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        selectionPanel.add(new TopLevelChooserPanel(model));
		secondPanel = new ChooserPanelProxy();
        selectionPanel.add(secondPanel);
        thirdPanel = new ChooserPanelProxy();
        selectionPanel.add(thirdPanel);
        add(selectionPanel, BorderLayout.CENTER);
        
        JPanel renderButtonPanel = new JPanel();
		renderButtonPanel.setLayout(new BoxLayout(renderButtonPanel, BoxLayout.LINE_AXIS));
		renderButtonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
		renderButton = new JButton("Render");
        renderButton.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    render();                    
                }
            });
        renderButton.setEnabled(false);
		renderButtonPanel.add(Box.createHorizontalGlue(), BorderLayout.PAGE_END);
		renderButtonPanel.add(renderButton);
		add(renderButtonPanel, BorderLayout.PAGE_END);
        
        pack();
    }
    
    public void update(Observable o, Object arg) {
    	if (isVisible()) {
            renderButton.setEnabled(false);
            if (arg instanceof String) { // selection was a top level selection
                if (model.getTopLevelSelection().equals(TopLevelChooserPanel.TEXT_STRING)) {
                    replaceSecondPanel(new TextChooserPanel(true, model));
                    replaceThirdPanel(new TuneChooserPanel(false, model));
                } else {
                    replaceSecondPanel(new TuneChooserPanel(true, model));
                    replaceThirdPanel(new TextChooserPanel(false, model));
                }
                validate();
            } else { // selection was a Text or a Tune
            	thirdPanel.setSelectedItem();
                if (model.isRenderReady()) renderButton.setEnabled(true);
            }
        }
    }    

    private void replaceSecondPanel(UpdateableChooserPanel newPanel) {
        selectionPanel.remove(secondPanel);
        this.secondPanel = newPanel;
        selectionPanel.add(secondPanel, 1);
    }
    
    private void replaceThirdPanel(UpdateableChooserPanel newPanel) {
        selectionPanel.remove(thirdPanel);
        this.thirdPanel = newPanel;
        selectionPanel.add(thirdPanel, 2);
    }

	public void render() {
		if(model.isRenderReady()){
            model.render();
            musicPage.render();
            this.setVisible(false);
		}
	}

    public void returnToChooser() {
        model.returnToChooser();
        model.printModel();
        secondPanel.setSelectedItem();
        thirdPanel.setSelectedItem();
        this.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Chooser().setVisible(true);
			}
        });
    }
}

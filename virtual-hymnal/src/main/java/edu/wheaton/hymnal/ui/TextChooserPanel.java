package edu.wheaton.hymnal.ui;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.wheaton.hymnal.data.Db;
import edu.wheaton.hymnal.data.H2Db;
import edu.wheaton.hymnal.data.Text;
import edu.wheaton.hymnal.data.Tune;

public class TextChooserPanel extends UpdateableChooserPanel implements
		ListSelectionListener, Observer {

	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "Waiting for selection...";
	private static final String TOOLTIP_TEXT = "Please select a TEXT";
	private static final Db db = H2Db.getInstance();

	private JLabel messageLabel;
	private JList list;
	private JScrollPane scrollPane;
	private Model model;
	private State st;

	public TextChooserPanel(boolean populate, Model model) {
		super(new BorderLayout());
		this.model = model;
		model.addObserver(this);
		if (populate) {
			populateWithAll();
			st = new SecondPanelState();
		} else {
			messageLabel = new JLabel(MESSAGE);
			this.add(messageLabel);
			st = new ThirdPanelState();
		}
	}

	/**
	 * Populate the list with all available Texts in the DB.
	 * 
	 * This should be called when this panel will be the second panel in a
	 * chooser.
	 */
	private void populateWithAll() {
		setupList(db.selectAllTexts());
		list.setCellRenderer(new StandardCellRenderer());
		scrollPane = new JScrollPane(list);
		scrollPane.setPreferredSize(this.getPreferredSize());

		this.add(scrollPane);
	}

	private void setupList(Text[] textArray) {
		list = new JList(textArray);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addListSelectionListener(this);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		list.setToolTipText(TOOLTIP_TEXT);
	}

	public void valueChanged(ListSelectionEvent lse) {
		if (!list.isSelectionEmpty() && lse.getValueIsAdjusting()) {
			Text selection = (Text) list.getSelectedValue();
			model.setText(selection);
		}
	}

	public void update(Observable o, Object arg) {
		st.update(o, arg);
	}

	/**
	 * Make sure that the view matches the model. This should only be necessary
	 * when returning from the MusicPage to the Chooser.
	 */
	public void setSelectedItem() {
		ListModel lm = list.getModel();
		if (lm.getSize() == 0) {
			System.out
					.println("TextChooserPanel: cannot set selected item on an empty list.");
			return;
		}
		for (int i = 0; i < lm.getSize(); i++) {
			if (lm.getElementAt(i).equals(model.getText())) {
				list.setSelectedIndex(i);
				break;
			}
		}
	}

	private interface State {
		public void update(Observable o, Object arg);
	}

	private class SecondPanelState implements State {
		public void update(Observable o, Object arg) {
			/*
			 * cases: - arg is top level selection -> ignore, b/c this panel
			 * goes away - arg is text -> ignore changes in text selection - arg
			 * is tune -> should not happen, b/c no tune list available
			 */
		}
	}

	private class ThirdPanelState implements State {
		public void update(Observable o, Object arg) {
			if (arg instanceof Tune) {
				removeAll();
				setupList(H2Db.getInstance().selectTextsFor((Tune) arg));

				list.setCellRenderer(new ThirdPanelCellRenderer(db
						.selectDefaultTextFor((Tune) arg)));

				scrollPane = new JScrollPane(list);
				scrollPane.setPreferredSize(getPreferredSize());

				add(scrollPane);
				validate();
			}
		}
	}
}

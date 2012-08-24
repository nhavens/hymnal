package edu.wheaton.hymnal.ui;

import java.awt.BorderLayout;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TopLevelChooserPanel extends ChooserPanel implements
		ListSelectionListener {

	private static final long serialVersionUID = 1L;
	public static final String TEXT_STRING = "TEXT";
	public static final String TUNE_STRING = "TUNE";

	private JList list;
	private static final String LIST_TOOLTIP_TEXT = "Select either "
			+ TEXT_STRING + " or " + TUNE_STRING;

	private Model model;

	public TopLevelChooserPanel(Model model) {
		super(new BorderLayout());

		this.model = model;

		String[] data = { TEXT_STRING, TUNE_STRING };
		list = new JList(data);
		list.setCellRenderer(new StandardCellRenderer());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addListSelectionListener(this);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		list.setToolTipText(LIST_TOOLTIP_TEXT);

		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(this.getPreferredSize());

		this.add(listScroller);
	}

	/*
	 * Switch (or update... haven't decided yet) SecondaryPanel based on
	 * selection. Note: This method is called whenever the value of the
	 * selection changes.
	 */
	public void valueChanged(ListSelectionEvent lse) {
		if (!list.isSelectionEmpty() && lse.getValueIsAdjusting()) {
			Object value = list.getSelectedValue();
			model.setTopLevelSelection(value.toString());
		}
	}
}

package presentation;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import entity.CardSet;

public class Menu extends Screen {

	private JList<CardSet> setList;
	private JButton playBut, editBut, deleteBut;

	public Menu(ContentPanel mainPanel) {
		super(mainPanel);
	}

	@Override
	protected JPanel createContent() {

		JPanel panel = new JPanel(new GridBagLayout());

		JPanel innerPanel = new JPanel(new BorderLayout(20, 0));

		setList = gui.createList(dp.getAllSets(), new CardSet[0]);
		setList.setFixedCellWidth(150);
		setList.addListSelectionListener(new ListHasSelectionListener());
		JScrollPane scrollPane = gui.createScrollPane(setList);

		JButton newBut = gui.createButton("New");
		newBut.addActionListener(e -> newSet());
		playBut = gui.createButton("Play");
		playBut.addActionListener(e -> playSet(setList.getSelectedValue()));
		playBut.setEnabled(false);
		editBut = gui.createButton("Edit");
		editBut.addActionListener(e -> editSet(setList.getSelectedValue()));
		editBut.setEnabled(false);
		deleteBut = gui.createButton("Delete");
		deleteBut.addActionListener(e -> deleteSet(setList.getSelectedValue()));
		deleteBut.setEnabled(false);

		JPanel buttonPanel = new JPanel(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 0.5;
		c.insets = new Insets(5, 0, 5, 0);
		buttonPanel.add(newBut, c);
		c.gridy++;
		buttonPanel.add(playBut, c);
		c.gridy++;
		buttonPanel.add(editBut, c);
		c.gridy++;
		buttonPanel.add(deleteBut, c);

		innerPanel.add(scrollPane, BorderLayout.CENTER);
		innerPanel.add(buttonPanel, BorderLayout.EAST);

		panel.add(innerPanel);

		return panel;
	}

	private void playSet(CardSet set) {
		mainPanel.openScreen(new CardViewer(mainPanel, set));
	}

	private void newSet() {
		mainPanel.openScreen(new CardEditor(mainPanel, null));
	}

	private void editSet(CardSet set) {
		mainPanel.openScreen(new CardEditor(mainPanel, set));
	}

	private void deleteSet(CardSet set) {
		if (set != null) {
			dp.deleteSet(set);
		}
		reload();
	}

	private class ListHasSelectionListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			boolean isSelected = !setList.isSelectionEmpty();
			playBut.setEnabled(isSelected);
			editBut.setEnabled(isSelected);
			deleteBut.setEnabled(isSelected);
		}
	}

	@Override
	protected void reload() {
		setList.setListData(dp.getAllSets().toArray(new CardSet[0]));
		super.reload();
	}

	@Override
	protected String getHeader() {
		return "Menu";
	}
}

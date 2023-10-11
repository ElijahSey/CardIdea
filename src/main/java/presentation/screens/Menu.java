package presentation.screens;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import entity.CardSet;
import presentation.basic.ContentPanel;
import presentation.basic.Screen;
import presentation.inner.StatisticPanel;

public class Menu extends Screen {

	private JList<CardSet> setList;
	private JButton playBut, editBut, deleteBut;
	private JPanel statisticsPanel;

	public Menu(ContentPanel mainPanel) {
		super(mainPanel);
	}

	@Override
	protected JPanel createContent() {

		JPanel panel = new JPanel(new GridBagLayout());

		JPanel innerPanel = new JPanel(new BorderLayout(20, 10));

		setList = gui.createList(dp.getAllSets(), new CardSet[0]);
		setList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setList.addListSelectionListener(new ListHasSelectionListener());
		JScrollPane scrollPane = gui.createScrollPane(setList);
		scrollPane.setPreferredSize(new Dimension(200, 0));

		JButton newBut = gui.createButton(lm.getString("new"));
		newBut.setToolTipText(lm.getString("Menu.new.tooltip"));
		newBut.addActionListener(e -> newSet());
		playBut = gui.createButton(lm.getString("start"));
		playBut.setToolTipText(lm.getString("Menu.start.tooltip"));
		playBut.addActionListener(e -> playSet(setList.getSelectedValue()));
		playBut.setEnabled(false);
		editBut = gui.createButton(lm.getString("edit"));
		editBut.setToolTipText(lm.getString("Menu.edit.tooltip"));
		editBut.addActionListener(e -> editSet(setList.getSelectedValue()));
		editBut.setEnabled(false);
		deleteBut = gui.createButton(lm.getString("delete"));
		deleteBut.setToolTipText(lm.getString("Menu.delete.tooltip"));
		deleteBut.addActionListener(e -> deleteSet(setList.getSelectedValue()));
		deleteBut.setEnabled(false);

		JPanel buttonPanel = new JPanel(new GridBagLayout());
		statisticsPanel = new JPanel(new BorderLayout());
		statisticsPanel.add(new StatisticPanel(setList.getSelectedValue(), 20));

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
		innerPanel.add(statisticsPanel, BorderLayout.SOUTH);

		panel.add(innerPanel);

		return panel;
	}

	@Override
	protected void afterOpening() {
		revalidate();
	}

	private void playSet(CardSet set) {
		mainPanel.addScreen(new CardViewer(mainPanel, set));
	}

	private void newSet() {
		mainPanel.addScreen(new CardEditor(mainPanel, null));
	}

	private void editSet(CardSet set) {
		mainPanel.addScreen(new CardEditor(mainPanel, set));
	}

	private void deleteSet(CardSet set) {
		if (set != null) {
			dp.deleteSet(set);
		}
		revalidate();
	}

	private class ListHasSelectionListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			boolean isSelected = !setList.isSelectionEmpty();
			playBut.setEnabled(isSelected);
			editBut.setEnabled(isSelected);
			deleteBut.setEnabled(isSelected);
			statisticsPanel.removeAll();
			statisticsPanel.add(new StatisticPanel(setList.getSelectedValue(), 20));
			statisticsPanel.revalidate();
			statisticsPanel.repaint();
		}
	}

	@Override
	protected void revalidate() {
		setList.setListData(dp.getAllSets().toArray(new CardSet[0]));
		super.revalidate();
	}
}

package presentation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

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

	public Menu(JPanel mainPanel) {
		super(mainPanel);
		addContent();
	}

	@Override
	protected JPanel createContent() {

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		List<CardSet> cardSets = dp.getAllSets();
		setList = new JList<>(cardSets.toArray(new CardSet[] {}));
		setList.addListSelectionListener(new ListHasSelectionListener());
		JScrollPane pane = new JScrollPane(setList);

		JButton newBut = gui.createButton("New");
		newBut.addActionListener(e -> newSet());
		playBut = gui.createButton("Play");
		playBut.addActionListener(e -> playSet());
		playBut.setEnabled(false);
		editBut = gui.createButton("Edit");
		editBut.addActionListener(e -> editSet());
		editBut.setEnabled(false);
		deleteBut = gui.createButton("Delete");
		deleteBut.addActionListener(e -> deleteSet());
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
		c.insets = new Insets(0, 0, 0, 20);
		c.gridy = 0;
		c.fill = GridBagConstraints.NONE;
		panel.add(pane, c);
		c.gridx++;
		panel.add(buttonPanel, c);

		return panel;
	}

	private void playSet() {
		new CardViewer(mainPanel, setList.getSelectedValue());
	}

	private void newSet() {
		new CardEditor(mainPanel, null);
	}

	private void editSet() {
		new CardEditor(mainPanel, setList.getSelectedValue());
	}

	private void deleteSet() {

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
}

package presentation;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import entity.Card;
import entity.CardSet;

public class CardEditor extends Screen {

	private CardSet cardSet;
	private List<Card> cards;
	private JList<Card> list;
	private JComboBox<String> topic;
	private JTextField cardSetName, question;
	private JTextArea solution, hint;
	private JButton delBut;

	private boolean cardListChanged;

	public CardEditor(ContentPanel mainPanel, CardSet cardSet) {
		super(mainPanel);
		cardListChanged = false;
		if (cardSet == null) {
			this.cardSet = new CardSet();
			this.cardSet.setName("");
			dp.insert(this.cardSet);
			cards = new ArrayList<>();
		} else {
			this.cardSet = cardSet;
			cards = dp.getCardsOfSet(cardSet);
		}
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}

	@Override
	protected JPanel createContent() {

		initializeGuiElements();

		cardSetName = gui.createTextField();
		cardSetName.setText(cardSet.getName());
		JScrollPane scroll = gui.createScrollPane(list);

		JPanel cardHeader = createGridPanel(new GridLayout(0, 1, 0, 10), createNCPanel(new JLabel("Topic"), topic),
				createNCPanel(new JLabel("Question"), question));
		JPanel cardBody = createGridPanel(new GridLayout(0, 1, 0, 10), createNCPanel(new JLabel("Solution"), solution),
				createNCPanel(new JLabel("Hint"), hint));
		JPanel west = createNCPanel(createNCPanel(new JLabel("Cardset Name"), cardSetName),
				createNCPanel(new JLabel("Cards"), scroll));
		JPanel center = createNCPanel(cardHeader, cardBody);
		JPanel south = createButtonPanel();

		updateTextFields(null);

		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.add(west, BorderLayout.WEST);
		panel.add(center, BorderLayout.CENTER);
		panel.add(south, BorderLayout.SOUTH);
		return panel;
	}

	@Override
	protected void executeExitAction() {
		if (cardSet.getName().isBlank() && cardSetName.getText().isBlank()) {
			dp.deleteSet(cardSet);
		}
	}

	private void initializeGuiElements() {
		list = gui.createList(cards, new Card[0]);
		list.setFixedCellWidth(150);
		list.addListSelectionListener(e -> updateTextFields(list.getSelectedValue()));
		topic = gui.createComboBox(dp.getTopicsOfSet(cardSet), new String[0]);
		topic.setEditable(true);
		question = gui.createTextField();
		solution = gui.createTextArea();
		hint = gui.createTextArea();
	}

	private void updateTextFields(Card card) {
		if (card == null) {
			card = new Card();
			delBut.setEnabled(false);
		} else {
			delBut.setEnabled(true);
		}

		topic.setSelectedItem(card.getTopic());
		question.setText(card.getQuestion());
		solution.setText(card.getSolution());
		hint.setText(card.getHint());
	}

	private static JPanel createGridPanel(GridLayout grid, JComponent... comps) {
		JPanel panel = new JPanel(grid);
		for (JComponent comp : comps) {
			panel.add(comp);
		}
		return panel;
	}

	private static JPanel createNCPanel(JComponent north, JComponent center) {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.add(north, BorderLayout.NORTH);
		panel.add(center, BorderLayout.CENTER);
		return panel;
	}

	private JPanel createButtonPanel() {

		JPanel panel = new JPanel();
		delBut = gui.createButton("Delete");
		delBut.addActionListener(e -> deleteCard(list.getSelectedValue()));
		JButton importBut = gui.createButton("Import");
		importBut.setToolTipText("Import Cards from a File");
		importBut.addActionListener(e -> importCards());
		JButton clearBut = gui.createButton("Clear");
		clearBut.addActionListener(e -> list.clearSelection());
		JButton addBut = gui.createButton("Add");
		addBut.addActionListener(e -> saveCard(list.getSelectedValue()));
		JButton saveBut = gui.createButton("Save");
		saveBut.addActionListener(e -> saveSet());

		panel.add(delBut);
		panel.add(importBut);
		panel.add(clearBut);
		panel.add(addBut);
		panel.add(saveBut);
		return panel;
	}

	public void saveCard(Card card) {
		if (card == null) {
			card = new Card(cardSet, (String) topic.getSelectedItem(), question.getText(), solution.getText(),
					hint.getText());
			dp.insert(card);
		} else {
			dp.update(card, e -> {
				Card c = (Card) e;
				c.setTopic((String) topic.getSelectedItem());
				c.setQuestion(question.getText());
				c.setSolution(solution.getText());
				c.setHint(hint.getText());
			});
		}
		reload();
		cardListChanged = true;
	}

	public void deleteCard(Card card) {
		if (card != null) {
			dp.deleteCard(card);
			reload();
		}
		cardListChanged = true;
	}

	public void saveSet() {
		String name = cardSetName.getText();
		if (cardSet.getName().equals(name) && !cardListChanged) {
			return;
		}
		if (name.isBlank()) {
			super.error("Der Name des Sets ist leer!");
		} else if (!dp.isUniqueName(cardSet, name)) {
			super.error("Dieser Name existiert bereits!");
		} else {
			dp.update(cardSet, e -> {
				((CardSet) e).setName(name);
			});
			cardListChanged = false;
		}
	}

	private void importCards() {
		mainPanel.addScreen(new CardSetImport(mainPanel, cardSet));
	}

	@Override
	protected void reload() {
		cards = dp.getCardsOfSet(cardSet);
		list.setListData(cards.toArray(new Card[0]));
		updateTextFields(null);
		super.reload();
	}

	@Override
	protected String getHeader() {
		if (cardSet.getName().isBlank()) {
			return "New";
		}
		return cardSet.getName();
	}
}

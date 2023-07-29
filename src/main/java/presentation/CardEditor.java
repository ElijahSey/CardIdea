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
	private JButton delBut, newBut, saveBut;

	public CardEditor(ContentPanel mainPanel, JLabel header, CardSet cardSet) {
		super(mainPanel, header);
		if (cardSet == null) {
			this.cardSet = new CardSet();
			this.cardSet.setName("");
			cards = new ArrayList<>();
			dp.insert(this.cardSet);
			header.setText("New");
		} else {
			this.cardSet = cardSet;
			cards = dp.getCardsOfSet(cardSet);
			header.setText(cardSet.getName());
		}
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		addContent();
		updateCard(null);
	}

	@Override
	public JPanel createContent() {

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

		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.add(west, BorderLayout.WEST);
		panel.add(center, BorderLayout.CENTER);
		panel.add(south, BorderLayout.SOUTH);
		return panel;
	}

	@Override
	protected void executeExitAction() {
		dp.update(cardSet, e -> {
			((CardSet) e).setName(cardSetName.getText());
		});
	}

	private void initializeGuiElements() {
		list = gui.createList(cards, new Card[0]);
		list.setFixedCellWidth(150);
		list.addListSelectionListener(e -> updateCard(list.getSelectedValue()));
		topic = gui.createComboBox(dp.getTopicsOfSet(cardSet), new String[0]);
		topic.setEditable(true);
		question = gui.createTextField();
		solution = gui.createTextArea();
		hint = gui.createTextArea();
	}

	private void updateCard(Card card) {
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
		newBut = gui.createButton("New");
		newBut.addActionListener(e -> list.clearSelection());
		saveBut = gui.createButton("Save");
		saveBut.addActionListener(e -> saveCard(list.getSelectedValue()));

		panel.add(delBut);
		panel.add(newBut);
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
	}

	public void deleteCard(Card card) {
		if (card != null) {
			dp.deleteCard(card);
			reload();
		}
	}

	@Override
	protected void reload() {
		cards = dp.getCardsOfSet(cardSet);
		list.setListData(cards.toArray(new Card[0]));
		updateCard(null);
		super.reload();
	}
}

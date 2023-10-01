package presentation.screens;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InvalidNameException;
import javax.naming.NameAlreadyBoundException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import entity.Card;
import entity.CardSet;
import entity.Topic;
import presentation.basic.ContentPanel;
import presentation.basic.Screen;
import presentation.util.VisualException;

public class CardEditor extends Screen {

	private CardSet cardSet;
	private List<Card> cards;
	private List<Topic> topics;
	private JList<Card> cardList;
	private JList<Topic> topicList;
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
			topics = new ArrayList<>();
			cards = new ArrayList<>();
		} else {
			this.cardSet = cardSet;
			topics = dp.getTopicsOfSet(cardSet);
			cards = dp.getCardsOfSet(cardSet);
		}
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}

	@Override
	protected JPanel createContent() {

		initializeGuiElements();

		cardSetName = gui.createTextField();
		cardSetName.setText(cardSet.getName());

		JScrollPane topicScroll = gui.createScrollPane(topicList);
		topicScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		JScrollPane cardScroll = gui.createScrollPane(cardList);
		cardScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		JPanel topics = createNCPanel(new JLabel("Topics"), topicScroll);
		JPanel cards = createNCPanel(new JLabel("Cards"), cardScroll);

		JSplitPane lists = gui.createSplitPane(JSplitPane.VERTICAL_SPLIT, topics, cards);

		JPanel cardHeader = createNCPanel(new JLabel("Question"), question);
		JSplitPane cardBody = gui.createSplitPane(JSplitPane.VERTICAL_SPLIT,
				createNCPanel(new JLabel("Solution"), solution), createNCPanel(new JLabel("Hint"), hint));

		JPanel west = createNCPanel(createNCPanel(new JLabel("Cardset Name"), cardSetName), lists);
		JPanel center = createNCPanel(cardHeader, cardBody);
		JPanel south = createButtonPanel();

		JSplitPane mainSplit = gui.createSplitPane(JSplitPane.HORIZONTAL_SPLIT, west, center);

		updateTextFields(null);

		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.add(mainSplit, BorderLayout.CENTER);
		panel.add(south, BorderLayout.SOUTH);

		SwingUtilities.invokeLater(() -> {
			lists.setDividerLocation(0.35);
			cardBody.setDividerLocation(0.6);
			mainSplit.setDividerLocation(0.2);
		});

		return panel;
	}

	@Override
	protected void afterOpening() {
		revalidate();
	}

	@Override
	protected boolean afterClosing() {
		if (cardSet.getName().isBlank() && cardSetName.getText().isBlank() && topics.isEmpty() && cards.isEmpty()) {
			dp.deleteSet(cardSet);
		} else if (cardListChanged) {
			int opt = JOptionPane.showConfirmDialog(mainPanel, "Do you want to save the card set before exiting?",
					"Save", JOptionPane.YES_NO_CANCEL_OPTION);
			switch (opt) {
			case 0:
				saveSet();
				break;
			case 2:
				return false;
			}
		}
		return true;
	}

	private void initializeGuiElements() {
		cardList = gui.createList(cards, new Card[0]);
		cardList.addListSelectionListener(e -> updateTextFields(cardList.getSelectedValue()));
		topicList = gui.createList(dp.getTopicsOfSet(cardSet), new Topic[0]);
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

		topicList.setSelectedValue(card.getTopic(), true);
		question.setText(card.getQuestion());
		solution.setText(card.getSolution());
		hint.setText(card.getHint());
	}

	private static JPanel createNCPanel(JComponent north, JComponent center) {
		JPanel panel = new JPanel(new BorderLayout(0, 5));
		panel.add(north, BorderLayout.NORTH);
		panel.add(center, BorderLayout.CENTER);
		return panel;
	}

	private JPanel createButtonPanel() {

		JPanel panel = new JPanel();
		delBut = gui.createButton(i18n.getString("delete"));
		delBut.setToolTipText(i18n.getString("CardEditor.delete.tooltip"));
		delBut.addActionListener(e -> deleteCard(cardList.getSelectedValue()));
		JButton importBut = gui.createButton(i18n.getString("import"));
		importBut.setToolTipText(i18n.getString("CardEditor.import.tooltip"));
		importBut.addActionListener(e -> importCards());
		JButton clearBut = gui.createButton(i18n.getString("clear"));
		clearBut.setToolTipText(i18n.getString("CardEditor.clear.tooltip"));
		clearBut.addActionListener(e -> cardList.clearSelection());
		JButton addBut = gui.createButton(i18n.getString("add"));
		addBut.setToolTipText(i18n.getString("CardEditor.add.tooltip"));
		addBut.addActionListener(e -> addCard(cardList.getSelectedValue()));
		JButton saveBut = gui.createButton(i18n.getString("save"));
		saveBut.setToolTipText(i18n.getString("CardEditor.save.tooltip"));
		saveBut.addActionListener(e -> saveSet());

		panel.add(delBut);
		panel.add(importBut);
		panel.add(clearBut);
		panel.add(addBut);
		panel.add(saveBut);
		return panel;
	}

	public void addCard(Card card) {
		if (topicList.isSelectionEmpty()) {
			throw new VisualException(mainPanel, "Please select a topic when adding or updating a card.");
		}
		if (card == null) {
			cards.add(new Card(topicList.getSelectedValue(), question.getText(), solution.getText(), hint.getText()));
		} else {
			card.setTopic(topicList.getSelectedValue());
			card.setQuestion(question.getText());
			card.setSolution(solution.getText());
			card.setHint(hint.getText());
		}
		revalidate();
		cardListChanged = true;
	}

	public void deleteCard(Card card) {
		cards.remove(card);
		revalidate();
		cardListChanged = true;
	}

	public void saveSet() {

		String name = cardSetName.getText();
		try {
			if (name.isBlank()) {
				throw new InvalidNameException("The name of the card set cannot be empty.");
			}
			if (name.equals(cardSet.getName()) && !cardListChanged) {
				return;
			}
			if (!dp.isUniqueName(cardSet, name)) {
				throw new NameAlreadyBoundException("The name of the card set already exists.");
			}
			cardSet.setName(name);
			dp.updateSet(cardSet, topics, cards);
			cardListChanged = false;
		} catch (Exception e) {
			throw new VisualException(mainPanel, e);
		}
	}

	private void importCards() {
		mainPanel.addScreen(new CardImport(mainPanel, cardSet, topics, cards));
	}

	@Override
	protected void revalidate() {
		topicList.setListData(topics.toArray(new Topic[0]));
		cardList.setListData(cards.toArray(new Card[0]));
		updateTextFields(null);
		super.revalidate();
	}

	@Override
	protected String getHeader() {
		if (cardSet.getName() == null || cardSet.getName().isBlank()) {
			return i18n.getString("new");
		}
		return cardSet.getName();
	}
}

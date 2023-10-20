package presentation.screens;

import entity.Card;
import entity.CardSet;
import entity.Topic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import presentation.basic.Screen;

public class CardEditor extends Screen {

	private CardSet cardSet;
	private ObservableList<Card> cards;
	private ObservableList<Topic> topics;

	@FXML
	private ListView<Card> cardList;

	@FXML
	private ListView<Topic> topicList;

	@FXML
	private TextField question;

	@FXML
	private TextArea solution;

	@FXML
	private TextArea hint;

	@FXML
	private Button deleteButton;

	@FXML
	private Button clearButton;

	@FXML
	private Button addOrUpdateButton;

	@FXML
	private Button saveButton;

//	private boolean changed;

//	public CardEditor(ContentPanel mainPanel, CardSet cardSet) {
//		super(mainPanel);
//		if (cardSet == null) {
//			this.cardSet = new CardSet();
//			topics = new ArrayList<>();
//			cards = new ArrayList<>();
//		} else {
//			this.cardSet = cardSet;
//			topics = dp.getTopicsOfSet(cardSet);
//			cards = dp.getCardsOfSet(cardSet);
//		}
//		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//	}

	public void initialize() {

		topics = FXCollections.observableArrayList();
		cards = FXCollections.observableArrayList();

		topicList.setItems(topics);
		cardList.setItems(cards);
	}

	public void setCardSet(CardSet cardSet) {

		if (cardSet == null) {
			this.cardSet = new CardSet();
		} else {
			this.cardSet = cardSet;
			topics.addAll(dp.getTopicsOfSet(cardSet));
			cards.addAll(dp.getCardsOfSet(cardSet));

			if (cardSet.getName() != null && !cardSet.getName().isBlank()) {
				header.setText(cardSet.getName());
			}
		}
	}

//	protected Node createContent() {
//
//		initializeGuiElements();
//
//		JScrollPane topicScroll = gui.createScrollPane(topicList);
//		topicScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//		JScrollPane cardScroll = gui.createScrollPane(cardList);
//		cardScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//
//		JPanel topics = createNCPanel(new JLabel(lm.getString("Topic", 2)), topicScroll);
//		JPanel cards = createNCPanel(new JLabel(lm.getString("Card", 2)), cardScroll);
//
//		JSplitPane west = gui.createSplitPane(JSplitPane.VERTICAL_SPLIT, topics, cards);
//
//		JPanel cardHeader = createNCPanel(new JLabel(lm.getString("question")), question);
//		JSplitPane cardBody = gui.createSplitPane(JSplitPane.VERTICAL_SPLIT,
//				createNCPanel(new JLabel(lm.getString("solution")), solution),
//				createNCPanel(new JLabel(lm.getString("hint")), hint));
//
//		JPanel center = createNCPanel(cardHeader, cardBody);
//		JPanel south = createButtonPanel();
//
//		JSplitPane mainSplit = gui.createSplitPane(JSplitPane.HORIZONTAL_SPLIT, west, center);
//
//		updateTextFields(null);
//
//		JPanel panel = new JPanel(new BorderLayout(10, 10));
//		panel.add(mainSplit, BorderLayout.CENTER);
//		panel.add(south, BorderLayout.SOUTH);
//
//		SwingUtilities.invokeLater(() -> {
//			west.setDividerLocation(0.35);
//			cardBody.setDividerLocation(0.6);
//			mainSplit.setDividerLocation(0.2);
//		});
//
//		SwingNode node = new SwingNode();
//		node.setContent(panel);
//
//		return node;
//	}

//	@Override
//	public void createMenuItems() {
//		super.createMenuItems();
//		addMenuItem(CommandBar.FILE, lm.getString("export"), lm.getString("CardEditor.export.tooltip"), null,
//				e -> new CardExport(mainPanel, cardSet).show());
//		addMenuItem(CommandBar.FILE, lm.getString("import"), lm.getString("CardEditor.import.tooltip"), null,
//				e -> new CardImport(mainPanel, cardSet, topics, cards).show());
//		addMenuItem(CommandBar.FILE, lm.getString("save"), null,
//				KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK), e -> saveSet());
//		addMenuItem(CommandBar.EDIT, lm.getString("rename"), null, KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0),
//				e -> renameSet("CardEditor.rename.dialog"));
//	}

//	@Override
//	protected void afterOpening() {
//		super.afterOpening();
//		if (cardSet.getName() == null) {
//			if (!renameSet("CardEditor.new.dialog")) {
//				mainPanel.back();
//			}
//		}
//		setChanged(false);
//		revalidate();
//	}
//
//	@Override
//	protected boolean afterClosing() {
//
//		if (changed) {
//			int opt = JOptionPane.showConfirmDialog(mainPanel, lm.getString("CardEditor.save.dialog"),
//					lm.getString("save"), JOptionPane.YES_NO_CANCEL_OPTION);
//			switch (opt) {
//			case 0:
//				saveSet();
//				break;
//			case 2:
//				return false;
//			}
//		}
//		super.afterClosing();
//		return true;
//	}

//	private void updateTextFields(Card card) {
//		if (card == null) {
//			card = new Card();
//			delBut.setEnabled(false);
//			addOrUpdateBut.setText(lm.getString("add"));
//		} else {
//			delBut.setEnabled(true);
//			addOrUpdateBut.setText(lm.getString("update"));
//		}
//		topicList.setSelectedValue(card.getTopic(), true);
//		question.setText(card.getQuestion());
//		solution.setText(card.getSolution());
//		hint.setText(card.getHint());
//	}

//	private static JPanel createNCPanel(JComponent north, JComponent center) {
//		JPanel panel = new JPanel(new BorderLayout(0, 5));
//		panel.add(north, BorderLayout.NORTH);
//		panel.add(center, BorderLayout.CENTER);
//		return panel;
//	}

//	private JPanel createButtonPanel() {
//
//		JPanel panel = new JPanel(new BorderLayout());
//
//		JPanel center = new JPanel();
//		JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//		delBut = gui.createButton(lm.getString("delete"));
//		delBut.setToolTipText(lm.getString("CardEditor.delete.tooltip"));
//		delBut.addActionListener(e -> deleteCard(cardList.getSelectedValue()));
//		JButton clearBut = gui.createButton(lm.getString("clear"));
//		clearBut.setToolTipText(lm.getString("CardEditor.clear.tooltip"));
//		clearBut.addActionListener(e -> cardList.clearSelection());
//		addOrUpdateBut = gui.createButton(lm.getString("add"));
//		addOrUpdateBut.setToolTipText(lm.getString("CardEditor.add.tooltip"));
//		addOrUpdateBut.addActionListener(e -> addOrUpdateCard(cardList.getSelectedValue()));
//		saveBut = gui.createButton(lm.getString("save"));
//		saveBut.setToolTipText(lm.getString("CardEditor.save.tooltip"));
//		saveBut.addActionListener(e -> saveSet());
//
//		center.add(delBut);
//		center.add(clearBut);
//		center.add(addOrUpdateBut);
//		right.add(saveBut);
//		panel.add(Box.createHorizontalStrut(100), BorderLayout.WEST);
//		panel.add(center, BorderLayout.CENTER);
//		panel.add(right, BorderLayout.EAST);
//		return panel;
//	}

//	public void addOrUpdateCard(Card card) {
//		Topic topic = topicList.getSelectedValue();
//		if (topic == null) {
//			showUserInfo(lm.getString("CardEditor.notopic.message"));
//			return;
//		}
//		if (card == null) {
//			cards.add(new Card(topic, question.getText(), solution.getText(), hint.getText()));
//		} else {
//			card.setTopic(topic);
//			card.setQuestion(question.getText());
//			card.setSolution(solution.getText());
//			card.setHint(hint.getText());
//		}
//		revalidate();
//		setChanged(true);
//	}
//
//	public void deleteCard(Card card) {
//		cards.remove(card);
//		revalidate();
//		setChanged(true);
//	}
//
//	private boolean renameSet(String message) {
//		String newName = JOptionPane.showInputDialog(mainPanel, lm.getString(message), cardSet.getName());
//		if (newName == null) {
//			return false;
//		}
//		if (newName.isBlank()) {
//			showUserInfo(lm.getString("CardEditor.blankname.message"));
//			return false;
//		}
//		cardSet.setName(newName);
//		dp.update(cardSet);
//		mainPanel.setHeader(getHeader());
//		return true;
//	}
//
//	public void saveSet() {
//
//		dp.updateSet(cardSet, topics, cards);
//		setChanged(false);
//	}
//
//	public void setChanged(boolean flag) {
//		changed = flag;
//		saveBut.setEnabled(flag);
//	}
//
//	@Override
//	protected void revalidate() {
//		topicList.setListData(topics.toArray(new Topic[0]));
//		cardList.setListData(cards.toArray(new Card[0]));
//		updateTextFields(null);
//		super.revalidate();
//	}
}

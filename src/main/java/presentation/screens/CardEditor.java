package presentation.screens;

import entity.Card;
import entity.CardSet;
import entity.Topic;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import presentation.basic.MainFrame;
import presentation.basic.Screen;

public class CardEditor extends Screen {

	private CardSet cardSet;
	private ObservableList<Card> cards;
	private ObservableList<Topic> topics;
	private boolean changed;

	@FXML
	private ListView<Card> cardList;

	@FXML
	private ListView<Topic> topicList;

	@FXML
	private TextField question;

	@FXML
	private TextArea solution, hint;

	@FXML
	private Button deleteButton, addOrUpdateButton, saveButton;

	public CardEditor(CardSet set) {
		cardSet = set;
		topics = FXCollections.observableArrayList();
		cards = FXCollections.observableArrayList();
	}

	@Override
	public void initialize() {

		topicList.setItems(topics);
		cardList.setItems(cards);

		cardList.getSelectionModel().selectedItemProperty()
				.addListener((ChangeListener<Card>) (observable, oldValue, newValue) -> {
					if (newValue != null) {
						updateInputFields(newValue);
					}
				});

		if (cardSet == null) {
			cardSet = new CardSet();
		} else {
			topics.addAll(dp.getTopicsOfSet(cardSet));
			cards.addAll(dp.getCardsOfSet(cardSet));
		}
	}

	@FXML
	private void handleDelete() {
		Card card = cardList.getSelectionModel().getSelectedItem();
		cards.remove(card);
		setChanged(true);
	}

	@FXML
	private void handleClear() {
		deleteButton.setDisable(true);
		addOrUpdateButton.setText(lm.getString(getClass(), "add"));
		topicList.getSelectionModel().clearSelection();
		cardList.getSelectionModel().clearSelection();
		question.clear();
		solution.clear();
		hint.clear();
	}

	@FXML
	private void handleAddOrUpdate() {
		Topic topic = topicList.getSelectionModel().getSelectedItem();
		if (topic == null) {
			MainFrame.getInstance().showAlert(getClass(), "notopic", AlertType.WARNING);
			return;
		}
		Card card = cardList.getSelectionModel().getSelectedItem();
		if (card == null) {
			cards.add(new Card(topic, question.getText(), solution.getText(), hint.getText()));
		} else {
			card.setTopic(topic);
			card.setQuestion(question.getText());
			card.setSolution(solution.getText());
			card.setHint(hint.getText());
		}
		setChanged(true);
	}

	@FXML
	private void handleSave() {

		dp.updateSet(cardSet, topics, cards);
		setChanged(false);
	}

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

	@Override
	public void afterLoad() {
		super.afterLoad();
		if (cardSet.getName() != null && !cardSet.getName().isBlank()) {
			header.setText(cardSet.getName());
		}
	}

	private void updateInputFields(Card card) {

		deleteButton.setDisable(false);
		addOrUpdateButton.setText(lm.getString(getClass(), "update"));
		topicList.getSelectionModel().select(card.getTopic());
		question.setText(card.getQuestion());
		solution.setText(card.getSolution());
		hint.setText(card.getHint());
	}

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

	@Override
	public boolean beforeClose() {
		if (!changed) {
			return true;
		}
		ButtonType response = MainFrame.getInstance().showAlert(getClass(), "save", AlertType.CONFIRMATION,
				ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		return switch (response.getButtonData()) {
		case YES -> {
			handleSave();
			yield true;
		}
		case NO -> true;
		default -> false;
		};
	}

	public void setChanged(boolean flag) {
		changed = flag;
		saveButton.setDisable(!flag);
	}
}

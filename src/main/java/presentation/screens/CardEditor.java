package presentation.screens;

import java.util.List;
import java.util.Optional;

import entity.Card;
import entity.CardSet;
import entity.Topic;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import presentation.basic.MainFrame;
import presentation.basic.Screen;
import presentation.dialog.CardExport;
import presentation.dialog.CardImport;
import presentation.menuBar.MenuBar;

public class CardEditor extends Screen {

	private CardSet cardSet;
	private ObservableList<Card> cards;
	private ObservableList<Topic> topics;
	private boolean changed;

	@FXML
	private ListView<Topic> topicList;

	@FXML
	private Button deleteTopic, editTopic;

	@FXML
	private ListView<Card> cardList;

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

		topicList.getSelectionModel().selectedItemProperty()
				.addListener((ChangeListener<Topic>) (observable, oldValue, newValue) -> {
					boolean isEmpty = newValue == null;
					deleteTopic.setDisable(isEmpty);
					editTopic.setDisable(isEmpty);
				});

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
		topics.addListener((ListChangeListener<Topic>) c -> setChanged(true));
		cards.addListener((ListChangeListener<Card>) c -> setChanged(true));
	}

	@FXML
	private void handleDeleteTopic() {

		Topic topic = topicList.getSelectionModel().getSelectedItem();
		List<Card> cardsOfTopic = dp.getCardsOfTopic(topic);
		if (cards.size() > 0) {
			ButtonType type = MainFrame.showAlert(getClass(), "deleteTopic", AlertType.WARNING, ButtonType.OK,
					ButtonType.CANCEL);
			if (!type.getButtonData().equals(ButtonData.OK_DONE)) {
				return;
			}
		}
		cards.removeAll(cardsOfTopic);
		topics.remove(topic);
	}

	@FXML
	private void handleEditTopic() {

		Topic topic = topicList.getSelectionModel().getSelectedItem();
		Optional<String> nameOpt = MainFrame.showDialog(new TextInputDialog(topic.getName()), getClass(), "addTopic");
		nameOpt.ifPresent(name -> {
			topic.setName(name);
			topicList.refresh();
		});
	}

	@FXML
	private void handleAddTopic() {

		Optional<String> nameOpt = MainFrame.showDialog(new TextInputDialog(), getClass(), "addTopic");
		nameOpt.ifPresent(name -> topics.add(new Topic(name, cardSet)));
	}

	@FXML
	private void handleDelete() {

		Card card = cardList.getSelectionModel().getSelectedItem();
		cards.remove(card);
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
			MainFrame.showAlert(getClass(), "notopic", AlertType.WARNING);
			return;
		}
		Card card = cardList.getSelectionModel().getSelectedItem();
		if (card == null) {
			cards.add(new Card(topic, question.getText(), solution.getText(), hint.getText()));
			cardList.getSelectionModel().select(card);
		} else {
			updateCard(topic, card);
		}
	}

	@FXML
	private void handleSave() {

		dp.updateSet(cardSet, topics, cards);
		setChanged(false);
	}

	@Override
	public void addMenuItems(MenuBar menuBar) {

		menuBar.addSeparator(MenuBar.FILE);
		menuBar.addMenuItem(MenuBar.FILE, lm.getString("export"), null, e -> new CardExport(cardSet));
		menuBar.addMenuItem(MenuBar.FILE, lm.getString("import"), null, e -> new CardImport(cardSet, topics, cards));
		menuBar.addSeparator(MenuBar.FILE);
		menuBar.addMenuItem(MenuBar.FILE, lm.getString("save"),
				new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN), e -> handleSave());
		menuBar.addMenuItem(MenuBar.EDIT, lm.getString("rename"), new KeyCodeCombination(KeyCode.F2),
				e -> renameSet("rename"));
	}

	@Override
	public boolean beforeOpen() {

		if (cardSet.getName() == null && !renameSet("create")) {
			return false;
		}
		return super.beforeOpen();
	}

	@Override
	public boolean beforeClose() {

		if (changed) {
			ButtonType response = MainFrame.showAlert(getClass(), "save", AlertType.CONFIRMATION, ButtonType.YES,
					ButtonType.NO, ButtonType.CANCEL);
			switch (response.getButtonData()) {
			case YES:
				handleSave();
				break;
			case NO:
				break;
			default:
				return false;
			}
		}
		return super.beforeClose();
	}

	@Override
	public String getHeader() {

		if (cardSet.getName() != null && !cardSet.getName().isBlank()) {
			return cardSet.getName();
		}
		return super.getHeader();
	}

	private void updateInputFields(Card card) {

		deleteButton.setDisable(false);
		addOrUpdateButton.setText(lm.getString(getClass(), "update"));
		topicList.getSelectionModel().select(card.getTopic());
		question.setText(card.getQuestion());
		solution.setText(card.getSolution());
		hint.setText(card.getHint());
	}

	private void updateCard(Topic topic, Card card) {

		card.setTopic(topic);
		card.setQuestion(question.getText());
		card.setSolution(solution.getText());
		card.setHint(hint.getText());
		setChanged(true);
	}

	private boolean renameSet(String key) {

		Optional<String> newNameOpt = MainFrame.showDialog(new TextInputDialog(cardSet.getName()), getClass(), key);
		if (newNameOpt.isEmpty()) {
			return false;
		}
		String newName = newNameOpt.get();
		if (newName.isBlank()) {
			MainFrame.showAlert(getClass(), "invalidName", AlertType.WARNING);
			return false;
		}
		cardSet.setName(newName);
		header.setText(getHeader());
		setChanged(true);
		return true;
	}

	private void setChanged(boolean flag) {

		changed = flag;
		saveButton.setDisable(!flag);
	}
}

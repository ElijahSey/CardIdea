package presentation.screens;

import java.util.Optional;

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
			MainFrame.showAlert(getClass(), "notopic", AlertType.WARNING);
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

	@Override
	public void addMenuItems(MenuBar menuBar) {
		menuBar.addMenuItem(MenuBar.FILE, lm.getString("export"), null,
				e -> MainFrame.showDialog(new CardExport(cardSet)));
		menuBar.addMenuItem(MenuBar.FILE, lm.getString("import"), null,
				e -> MainFrame.showDialog(new CardImport(cardSet, topics, cards)));
		menuBar.addMenuItem(MenuBar.FILE, lm.getString("save"),
				new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN), e -> handleSave());
		menuBar.addMenuItem(MenuBar.EDIT, lm.getString("rename"), new KeyCodeCombination(KeyCode.F2), e -> renameSet());
	}

	@Override
	public boolean beforeClose() {
		if (!changed) {
			return true;
		}
		ButtonType response = MainFrame.showAlert(getClass(), "save", AlertType.CONFIRMATION, ButtonType.YES,
				ButtonType.NO, ButtonType.CANCEL);
		return switch (response.getButtonData()) {
		case YES -> {
			handleSave();
			yield true;
		}
		case NO -> true;
		default -> false;
		};
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

	private boolean renameSet() {
		Optional<String> newNameOpt = MainFrame.showDialog(new TextInputDialog(cardSet.getName()), getClass(),
				"rename");
		if (newNameOpt.isEmpty()) {
			return false;
		}
		String newName = newNameOpt.get();
		if (newName.isBlank()) {
			MainFrame.showAlert(getClass(), "invalidName", AlertType.WARNING);
			return false;
		}
		cardSet.setName(newName);
		dp.update(cardSet);
		header.setText(getHeader());
		return true;
	}

	private void setChanged(boolean flag) {
		changed = flag;
		saveButton.setDisable(!flag);
	}
}

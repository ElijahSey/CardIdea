package presentation.screens;

import java.util.Optional;

import entity.Card;
import entity.CardSet;
import entity.Topic;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import presentation.basic.MainFrame;
import presentation.basic.Screen;
import presentation.dialog.CardExport;
import presentation.dialog.CardImport;
import presentation.menuBar.MenuBar;
import presentation.util.CardTree;
import presentation.util.Util;

public class CardEditor extends Screen {

	private CardSet cardSet;
	private boolean editMode;
	private boolean changed;

	private static final Image CREATE_IMAGE = Util.loadImage("create.png");
	private static final Image EDIT_IMAGE = Util.loadImage("edit.png");
	private static final Image CONFIRM_IMAGE = Util.loadImage("tick.png");
	private static final Image CANCEL_IMAGE = Util.loadImage("cross.png");
	private static final Image DELETE_IMAGE = Util.loadImage("delete.png");

	private static final double BUTTON_SIZE = 20;

	@FXML
	private Button deleteButton, editButton, addButton;

	@FXML
	private CardTree cardTree;

	@FXML
	private TextField question;

	@FXML
	private TextArea solution, hint;

	@FXML
	private Button saveButton;

	public CardEditor(CardSet set) {

		if (set == null) {
			cardSet = new CardSet();
		} else {
			cardSet = set;
		}
		editMode = false;
	}

	@Override
	public void initialize() {

		cardTree.init(cardSet, () -> setChanged(true));

		cardTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				if (newValue.getValue() instanceof Topic) {
					addButton.setDisable(false);
					clearFields();
				} else if (newValue.getValue() instanceof Card card) {
					addButton.setDisable(true);
					updateFields(card);
				}
				editButton.setDisable(false);
			} else {
				addButton.setDisable(true);
				editButton.setDisable(true);
			}
		});
	}

	@FXML
	private void handleAdd() {

		TreeItem<Object> selectedItem = cardTree.getSelectionModel().getSelectedItem();
		if (editMode) {
			cardTree.addCard(selectedItem, question.getText(), solution.getText(), hint.getText());
			setChanged(true);
			exitEditMode();
		} else {
			enterEditMode(true);
		}
	}

	@FXML
	private void handleEdit() {

		boolean changed = false;
		TreeItem<Object> selectedItem = cardTree.getSelectionModel().getSelectedItem();
		if (editMode) {
			changed = cardTree.editCard(selectedItem, question.getText(), solution.getText(), hint.getText());
			exitEditMode();
		} else if (selectedItem.getValue() instanceof Topic) {
			changed = cardTree.editTopic(selectedItem);
		} else {
			enterEditMode(false);
		}
		setChanged(changed || this.changed);
	}

	@FXML
	private void handleDelete() {

		boolean changed = false;
		TreeItem<Object> selectedItem = cardTree.getSelectionModel().getSelectedItem();
		if (selectedItem == null) {
			clearFields();
		} else if (editMode) {
			exitEditMode();
			updateFields((Card) selectedItem.getValue());
		} else if (selectedItem.getValue() instanceof Topic topic) {
			changed = cardTree.deleteTopic(selectedItem);
		} else if (selectedItem.getValue() instanceof Card card) {
			changed = cardTree.deleteCard(selectedItem);
		}
		setChanged(changed || this.changed);
	}

	@FXML
	private void handleCreateTopic() {

		boolean changed = cardTree.addTopic(cardSet);
		setChanged(changed || this.changed);
	}

	@FXML
	private void handleSave() {

		cardSet.update(cardTree.getAll());
		setChanged(false);
	}

	@Override
	public void addMenuItems(MenuBar menuBar) {

		menuBar.addSeparator(MenuBar.FILE);
		menuBar.addMenuItem(MenuBar.FILE, lm.getString("export"), null, e -> new CardExport(cardSet));
		menuBar.addMenuItem(MenuBar.FILE, lm.getString("import"), null, e -> {
			new CardImport(cardSet, map -> cardTree.addAll(map));
			setChanged(true);
		});
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

	private void enterEditMode(boolean add) {

		if (add) {
			addButton.setGraphic(Util.imageViewOf(CONFIRM_IMAGE, BUTTON_SIZE, BUTTON_SIZE));
			editButton.setDisable(true);
			clearFields();
		} else {
			editButton.setGraphic(Util.imageViewOf(CONFIRM_IMAGE, BUTTON_SIZE, BUTTON_SIZE));
			addButton.setDisable(true);
		}
		deleteButton.setGraphic(Util.imageViewOf(CANCEL_IMAGE, BUTTON_SIZE, BUTTON_SIZE));
		enableFields(true);
		cardTree.setDisable(true);
		editMode = true;
	}

	private void exitEditMode() {

		addButton.setGraphic(Util.imageViewOf(CREATE_IMAGE, BUTTON_SIZE, BUTTON_SIZE));
		editButton.setGraphic(Util.imageViewOf(EDIT_IMAGE, BUTTON_SIZE, BUTTON_SIZE));
		deleteButton.setGraphic(Util.imageViewOf(DELETE_IMAGE, BUTTON_SIZE, BUTTON_SIZE));
		addButton.setDisable(false);
		editButton.setDisable(false);

		enableFields(false);
		cardTree.setDisable(false);
		editMode = false;
		cardTree.getSelectionModel().clearAndSelect(cardTree.getSelectionModel().getSelectedIndex());
	}

	private void enableFields(boolean flag) {

		question.setEditable(flag);
		solution.setEditable(flag);
		hint.setEditable(flag);
	}

	private void clearFields() {

		question.clear();
		solution.clear();
		hint.clear();
	}

	private void updateFields(Card card) {

		enableFields(false);
		question.setText(card.getQuestion());
		solution.setText(card.getSolution());
		hint.setText(card.getHint());
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

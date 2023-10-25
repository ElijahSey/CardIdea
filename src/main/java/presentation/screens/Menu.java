package presentation.screens;

import entity.CardSet;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import presentation.basic.Screen;
import presentation.menuBar.MenuBar;

public class Menu extends Screen {

	private ObservableList<CardSet> sets;

	@FXML
	private ListView<CardSet> setList;

	@FXML
	private Button startButton, editButton, deleteButton;

	public Menu() {

	}

	@Override
	public void initialize() {

		setList.getSelectionModel().selectedItemProperty()
				.addListener((ChangeListener<CardSet>) (observable, oldValue, newValue) -> {
					boolean isEmpty = newValue == null;
					startButton.setDisable(isEmpty || newValue.getSize() < 1);
					editButton.setDisable(isEmpty);
					deleteButton.setDisable(isEmpty);
				});
	}

	@FXML
	private void handleNew() {

		mainFrame.addScreen(new CardEditor(null));
	}

	@FXML
	private void handleStart() {

		mainFrame.addScreen(new CardViewer(setList.getSelectionModel().getSelectedItem()));
	}

	@FXML
	private void handleEdit() {

		mainFrame.addScreen(new CardEditor(setList.getSelectionModel().getSelectedItem()));
	}

	@FXML
	private void handleDelete() {

		CardSet set = setList.getSelectionModel().getSelectedItem();
		sets.remove(set);
		dp.deleteSet(set);
	}

	@Override
	public void addMenuItems(MenuBar menuBar) {

		menuBar.addSeparator(MenuBar.FILE);
		menuBar.addMenuItem(MenuBar.FILE, lm.getString("new"),
				new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN), e -> handleNew());
		menuBar.addMenuItem(MenuBar.EDIT, lm.getString("delete"), new KeyCodeCombination(KeyCode.DELETE),
				e -> handleDelete());
	}

	@Override
	public void onDisplay() {

		sets = FXCollections.observableArrayList(dp.getAllSets());
		setList.setItems(sets);
	}
}

package presentation.screens;

import entity.CardSet;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import presentation.basic.Screen;

public class Menu extends Screen {

	private ObservableList<CardSet> sets;

	@FXML
	private ListView<CardSet> setList;

	@FXML
	private Button startButton, editButton, deleteButton;

	public Menu() {
		sets = FXCollections.observableArrayList(dp.getAllSets());
	}

	@Override
	public void initialize() {

		setList.setItems(sets);
		setList.getSelectionModel().selectedItemProperty()
				.addListener((ChangeListener<CardSet>) (observable, oldValue, newValue) -> {
					boolean isSelected = newValue == null;
					startButton.setDisable(isSelected);
					editButton.setDisable(isSelected);
					deleteButton.setDisable(isSelected);
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
		if (set != null) {
			dp.deleteSet(set);
		}
	}
}

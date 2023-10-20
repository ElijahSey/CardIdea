package presentation.screens;

import javax.swing.JPanel;

import entity.CardSet;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import presentation.basic.Screen;

public class Menu extends Screen {

	private JPanel statisticsPanel;

	private ObservableList<CardSet> sets;

	@FXML
	private ListView<CardSet> setList;

	@FXML
	private Button newButton;

	@FXML
	private Button startButton;

	@FXML
	private Button editButton;

	@FXML
	private Button deleteButton;

	public void initialize() {
		sets = FXCollections.observableArrayList(dp.getAllSets());
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
	private void newSet(ActionEvent event) {
		CardEditor controller = mainFrame.addScreen(CardEditor.class);
		controller.setCardSet(null);
	}

	@FXML
	private void startSet(ActionEvent event) {
		mainFrame.addScreen(CardViewer.class);
	}

	@FXML
	private void editSet(ActionEvent event) {
		CardEditor controller = mainFrame.addScreen(CardEditor.class);
		controller.setCardSet(setList.getSelectionModel().getSelectedItem());
	}

	@FXML
	private void deleteSet(ActionEvent event) {

		CardSet set = setList.getSelectionModel().getSelectedItem();
		if (set != null) {
			dp.deleteSet(set);
		}
	}

//	private class ListHasSelectionListener implements ListSelectionListener {
//
//		@Override
//		public void valueChanged(ListSelectionEvent e) {
//			statisticsPanel.removeAll();
//			statisticsPanel.add(new StatisticPanel(setList.getSelectedValue(), 20));
//			statisticsPanel.revalidate();
//			statisticsPanel.repaint();
//		}
//	}
}

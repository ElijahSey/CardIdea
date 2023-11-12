package presentation.screens;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import entity.CardSet;
import entity.Result;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import presentation.basic.Screen;
import presentation.menuBar.MenuBar;
import presentation.util.LanguageManager;

public class Menu extends Screen {

	private ObservableList<CardSet> sets;

	@FXML
	private ListView<CardSet> setList;

	@FXML
	private Button startButton, editButton, deleteButton;

	@FXML
	private StackedAreaChart<String, Number> resultsChart;

	private MenuItem deleteMenu;

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
					deleteMenu.setDisable(isEmpty);
					resultsChart.getData().clear();
					if (!isEmpty) {
						resultsChart.getData().addAll(getChartData(newValue));
					}
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
		set.remove();
	}

	@Override
	public void addMenuItems(MenuBar menuBar) {

		menuBar.addSeparator(MenuBar.FILE);
		menuBar.addMenuItem(MenuBar.FILE, lm.getString("new"),
				new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN), e -> handleNew());

		deleteMenu = menuBar.addMenuItem(MenuBar.EDIT, lm.getString("delete"), new KeyCodeCombination(KeyCode.DELETE),
				e -> handleDelete());
		deleteMenu.setDisable(true);
	}

	@Override
	public void onDisplay() {

		sets = FXCollections.observableArrayList(CardSet.all());
		setList.setItems(sets);
	}

	public static ObservableList<Series<String, Number>> getChartData(CardSet set) {

		ObservableList<Series<String, Number>> data = FXCollections.observableArrayList();

		String[] legend = LanguageManager.getInstance().getArray(Menu.class, "legend");
		List<Result> resultList = Result.ofSet(set);
		for (int i = 0; i < 4; i++) {
			Series<String, Number> series = new Series<>(legend[i], FXCollections.observableArrayList());
			for (Result res : resultList) {
				String time = res.getCreatedOn()
						.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.MEDIUM));
				series.getData().add(new XYChart.Data<>(time, res.getScore(i)));
			}
			data.add(series);
		}
		return data;
	}
}

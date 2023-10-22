package presentation.menuBar;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import presentation.basic.FXController;
import presentation.basic.MainFrame;

public class MenuBar implements FXController {

	public static final int FILE = 0;
	public static final int EDIT = 1;
	public static final int VIEW = 2;

	@FXML
	private Button backButton;

	@FXML
	private Button homeButton;

	@FXML
	private Button settingsButton;

	@FXML
	private Label headerLabel;

	@FXML
	private ArrayList<Menu> menuList;

	public MenuBar() {

	}

	@Override
	public void initialize() {

	}

	@FXML
	private void handleBack() {
		MainFrame.getInstance().back();
	}

	@FXML
	private void handleHome() {
		MainFrame.getInstance().home();
	}

	public Label getHeaderLabel() {
		return headerLabel;
	}

	public void add(int menu, MenuItem item) {
		menuList.get(menu).getItems().add(0, item);
	}

	public MenuItem addMenuItem(int menu, String text, KeyCombination key, EventHandler<ActionEvent> handler) {
		MenuItem item = new MenuItem(text);
		item.setOnAction(handler);
		item.setAccelerator(key);
		add(menu, item);
		return item;
	}
}

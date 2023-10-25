package presentation.menuBar;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;
import presentation.basic.FXController;
import presentation.basic.MainFrame;
import presentation.dialog.Preferences;

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

	@FXML
	private void handlePreferences() {

		new Preferences();
	}

	@FXML
	private void handleClose() {

		MainFrame.getInstance().onWindowClose();
	}

	public Label getHeaderLabel() {

		return headerLabel;
	}

	public void addMenuItem(int menu, int index, String text, KeyCombination key, EventHandler<ActionEvent> handler) {

		MenuItem item = new MenuItem(text);
		item.setOnAction(handler);
		item.setAccelerator(key);
		menuList.get(menu).getItems().add(index, item);
	}

	public void addMenuItem(int menu, String text, KeyCombination key, EventHandler<ActionEvent> handler) {

		addMenuItem(menu, 0, text, key, handler);
	}

	public void addSeparator(int menu, int index) {

		SeparatorMenuItem sep = new SeparatorMenuItem();
		menuList.get(menu).getItems().add(index, sep);
	}

	public void addSeparator(int menu) {

		addSeparator(menu, 0);
	}
}

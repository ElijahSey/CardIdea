package presentation.menuBar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import presentation.basic.MainFrame;

public class MenuBar {

	@FXML
	private Button backButton;

	@FXML
	private Button homeButton;

	@FXML
	private Button settingsButton;

	@FXML
	private Label header;

	@FXML
	private void handleBack(ActionEvent event) {
		MainFrame.getInstance().back();
	}

	@FXML
	private void handleHome(ActionEvent event) {
		MainFrame.getInstance().home();
	}

	@FXML
	private void handleSettings(ActionEvent event) {
//		new Settings();
	}

	public Label getHeader() {
		return header;
	}
}

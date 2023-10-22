package presentation.menuBar;

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
	private Label headerLabel;

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
}

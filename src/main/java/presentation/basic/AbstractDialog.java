package presentation.basic;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import logic.data.DataPreperator;
import logic.data.PropertyManager;
import presentation.util.LanguageManager;

public abstract class AbstractDialog implements FXController {

	protected final LanguageManager lm;
	protected final DataPreperator dp;
	protected final PropertyManager pm;

	private final Stage stage;

	public AbstractDialog() {

		lm = LanguageManager.getInstance();
		dp = DataPreperator.getInstance();
		pm = PropertyManager.getInstance();

		FXMLLoader loader = MainFrame.loadClass(this);
		stage = new Stage();
		stage.setTitle(getHeader());
		stage.getIcons().add(MainFrame.cardImage);

		BorderPane layout = new BorderPane();
		Scene scene = new Scene(layout);
		stage.setScene(scene);
		layout.setCenter(loader.getRoot());
	}

	@Override
	public void initialize() {
	}

	public void show(boolean modal) {
		if (modal) {
			stage.showAndWait();
		} else {
			stage.show();
		}
	}

	public void close() {
		stage.close();
	}

	protected String getHeader() {
		return lm.getString(getClass(), "header");
	}
}

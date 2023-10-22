package presentation.basic;

import java.awt.SplashScreen;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import logic.data.DataPreperator;
import presentation.menuBar.MenuBar;
import presentation.screens.Menu;
import presentation.util.LanguageManager;

public class MainFrame extends Application {

	protected DataPreperator dp;
	protected LanguageManager lm;

	private Stage primaryStage;
	private BorderPane layout;
	private Deque<Display> screens;

	public static Image cardImage;

	private static MainFrame instance;

	@Override
	public void start(Stage primaryStage) {

		instance = this;
		SplashScreen splash = showSplashScreen();

		this.primaryStage = primaryStage;
		dp = DataPreperator.getInstance();
		lm = LanguageManager.getInstance();
		screens = new LinkedList<>();
		cardImage = new Image(getClass().getResourceAsStream("/images/card_logo.png"));

		createMainFrame();
		primaryStage.show();

		if (splash != null) {
			splash.close();
		}
	}

	private SplashScreen showSplashScreen() {

		SplashScreen splash = SplashScreen.getSplashScreen();
		if (splash == null) {
			System.err.println("Image for splashscreen not found");
		}
		return splash;
	}

	private void createMainFrame() {

		primaryStage.setTitle("cardIDEA");
		primaryStage.getIcons().add(cardImage);
		primaryStage.setMaximized(true);
		primaryStage.setOnCloseRequest(e -> closeWindow());

		layout = new BorderPane();
		Scene scene = new Scene(layout);
		primaryStage.setScene(scene);
		initMenu();
	}

	private Display loadScreen(Screen controller) {

		FXMLLoader loader = loadClass(controller);
		FXMLLoader menuBar = loadClass(new MenuBar());
		return new Display(loader, menuBar);
	}

	private static FXMLLoader loadClass(Object controller) {
		String className = controller.getClass().getSimpleName();
		FXMLLoader loader = new FXMLLoader();
		loader.setResources(LanguageManager.getInstance().getBundle(className));
		loader.setController(controller);
		try {
			loader.load(MainFrame.class.getResourceAsStream("/fxml/" + className + ".fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loader;
	}

	public void initMenu() {
		addScreen0(new Menu());
	}

	public void addScreen(Screen screen) {

		if (!screens.peek().getController().beforeClose()) {
			return;
		}
		addScreen0(screen);
	}

	public void addScreen0(Screen screen) {
		Display loader = loadScreen(screen);
		screens.push(loader);
		displayScreen(loader);
	}

	public void back() {
		if (!screens.peek().getController().beforeClose()) {
			return;
		}
		if (screens.size() < 2) {
			return;
		}
		screens.pop();
		displayScreen(screens.peek());
	}

	public void home() {
		if (!screens.peek().getController().beforeClose()) {
			return;
		}
		screens.clear();
		initMenu();
	}

	private void displayScreen(Display display) {
		layout.setTop(display.getMenuBarNode());
		layout.setCenter(display.getNode());
		display.getController().onDisplay();
	}

	public static void showDialog(AbstractDialog dialog) {

		FXMLLoader loader = loadClass(dialog);
		Stage stage = new Stage();
		stage.setTitle("cardIDEA");
		stage.getIcons().add(cardImage);

		BorderPane layout = new BorderPane();
		Scene scene = new Scene(layout);
		stage.setScene(scene);
		layout.setCenter(loader.getRoot());
	}

	public static <R> Optional<R> showDialog(Dialog<R> dialog, Class<?> clazz, String key) {
		String className = clazz.getSimpleName();
		ResourceBundle bundle = LanguageManager.getInstance().getBundle(className);
		dialog.setTitle(bundle.getString(key + ".title"));
		dialog.setHeaderText(bundle.getString(key + ".header"));
		dialog.setContentText(bundle.getString(key + ".message"));
		return dialog.showAndWait();
	}

	public static ButtonType showAlert(Class<?> clazz, String key, AlertType alertType, ButtonType... buttonTypes) {
		String className = clazz.getSimpleName();
		ResourceBundle bundle = LanguageManager.getInstance().getBundle(className);
		String message = bundle.getString(key + ".message");
		Alert alert = new Alert(alertType, message, buttonTypes);
		((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(cardImage);
		alert.setHeaderText(bundle.getString(key + ".header"));
		alert.showAndWait();
		return alert.getResult();
	}

	private void closeWindow() {
		if (!screens.peek().getController().beforeClose()) {
			return;
		}
		showAlert(getClass(), "close", AlertType.CONFIRMATION);
		dp.close();
	}

	public Window getWindow() {
		return primaryStage;
	}

	public static MainFrame getInstance() {
		return instance;
	}
}

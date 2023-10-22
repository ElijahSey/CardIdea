package presentation.basic;

import java.awt.SplashScreen;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import logic.data.DataPreperator;
import logic.data.PropertyManager;
import presentation.menuBar.MenuBar;
import presentation.screens.Menu;
import presentation.util.LanguageManager;

public class MainFrame extends Application {

	protected PropertyManager pm;
	protected DataPreperator dp;
	protected LanguageManager lm;

	private Stage primaryStage;
	private BorderPane layout;
	private FXMLLoader menuBar;
	private Deque<FXMLLoader> screens;

	private static MainFrame instance;

	@Override
	public void start(Stage primaryStage) {

		instance = this;
		SplashScreen splash = showSplashScreen();

		this.primaryStage = primaryStage;
		screens = new LinkedList<>();
		pm = PropertyManager.getInstance();
		dp = DataPreperator.getInstance();
		lm = LanguageManager.getInstance();

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

		primaryStage.setTitle("cardIdea");
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/card_logo.png")));
		primaryStage.setMaximized(true);
		primaryStage.setOnCloseRequest(e -> closeWindow());

		menuBar = loadClass(new MenuBar());

		layout = new BorderPane();
		Scene scene = new Scene(layout);
		primaryStage.setScene(scene);
		layout.setTop(menuBar.getRoot());
		initMenu();
	}

	private void closeWindow() {
		dp.close();
	}

	private <T extends Screen> FXMLLoader loadScreen(Screen controller) {

		FXMLLoader loader = loadClass(controller);
		controller.setHeaderLabel(((MenuBar) menuBar.getController()).getHeaderLabel());
		controller.afterLoad();
		return loader;
	}

	private FXMLLoader loadClass(Object controller) {
		String className = controller.getClass().getSimpleName();
		FXMLLoader loader = new FXMLLoader();
		loader.setResources(lm.getBundle(className));
		loader.setController(controller);
		try {
			loader.load(getClass().getResourceAsStream("/fxml/" + className + ".fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loader;
	}

	public void initMenu() {
		FXMLLoader loader = loadScreen(new Menu());
		screens.push(loader);
		displayScreen(loader);
	}

	public void addScreen(Screen screen) {

		if (!((Screen) screens.peek().getController()).beforeClose()) {
			return;
		}
		FXMLLoader loader = loadScreen(screen);
		screens.push(loader);
		displayScreen(loader);
	}

	public void back() {
		if (!((Screen) screens.peek().getController()).beforeClose()) {
			return;
		}
		if (screens.size() < 2) {
			return;
		}
		screens.pop();
		displayScreen(screens.peek());
	}

	public void home() {
		if (!((Screen) screens.peek().getController()).beforeClose()) {
			return;
		}
		screens.clear();
		initMenu();
	}

	private void displayScreen(FXMLLoader newLoader) {
		Node root = newLoader.getRoot();
		layout.setCenter(root);
	}

	public ButtonType showAlert(Class<?> clazz, String key, AlertType alertType, ButtonType... buttonTypes) {
		String className = clazz.getSimpleName();
		ResourceBundle bundle = lm.getBundle(className);
		String message = bundle.getString(key + ".message");
		Alert alert = new Alert(alertType, message, buttonTypes);
		alert.setHeaderText(bundle.getString(key + ".header"));
		alert.showAndWait();
		return alert.getResult();
	}

	public static MainFrame getInstance() {
		return instance;
	}
}

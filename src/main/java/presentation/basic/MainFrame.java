package presentation.basic;

import java.awt.SplashScreen;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import logic.data.DataPreperator;
import presentation.menuBar.MenuBar;
import presentation.screens.CardEditor;
import presentation.screens.Menu;
import presentation.util.GuiFactory;
import presentation.util.LanguageManager;

public class MainFrame extends Application {

	protected DataPreperator dp;
	protected GuiFactory gui;
	protected LanguageManager lm;

	private Stage primaryStage;
	private BorderPane layout;
	private FXMLLoader menuBar;
	private Map<Class<?>, FXMLLoader> loaders;
	private Deque<Class<?>> screens;
	private List<ReloadListener> reloadListeners;

	private static MainFrame instance;

	@Override
	public void start(Stage primaryStage) {

		instance = this;
		SplashScreen splash = showSplashScreen();

		this.primaryStage = primaryStage;
		screens = new ArrayDeque<>();
		dp = DataPreperator.getInstance();
		gui = GuiFactory.createDefaultGuiFactory();
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

		loadAllClasses();

		layout = new BorderPane();
		Scene scene = new Scene(layout);
		primaryStage.setScene(scene);
		layout.setTop(menuBar.getRoot());
		addScreen(Menu.class);
	}

	private void closeWindow() {
		dp.close();
	}

	private void loadAllClasses() {
		loaders = new HashMap<>();
		menuBar = loadClass(MenuBar.class);
		loadScreen(Menu.class);
		loadScreen(CardEditor.class);
	}

	private <T extends Screen> void loadScreen(Class<T> clazz) {

		FXMLLoader loader = loadClass(clazz);
		Screen controller = loader.getController();
		controller.setHeader(((MenuBar) menuBar.getController()).getHeader());
		loaders.put(clazz, loader);
	}

	private FXMLLoader loadClass(Class<?> clazz) {
		String className = clazz.getSimpleName();
		FXMLLoader loader = new FXMLLoader();
		loader.setResources(lm.getBundle(className));
		try {
			loader.load(getClass().getResourceAsStream("/fxml/" + className + ".fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loader;
	}

	public <T extends Screen> T addScreen(Class<?> clazz) {

		screens.push(clazz);
		FXMLLoader loader = loaders.get(clazz);
		displayScreen(loader);
		return loader.getController();
	}

	public void back() {
		if (screens.size() < 2) {
			return;
		}
		screens.pop();
		displayScreen(loaders.get(screens.peek()));
	}

	public void home() {
		Class<?> home = screens.peekLast();
		screens.clear();
		addScreen(home);
	}

	private void displayScreen(FXMLLoader loader) {
		Node root = loader.getRoot();
		layout.setCenter(root);
	}

//	public void rebuildActiveScreen() {
//		Screen screen = getActiveScreen();
//		screen.rebuild();
//		displayScreen(screen);
//	}
//
//	public void rebuildEverything() {
////		rebuildCommandBar();
//		for (ReloadListener r : reloadListeners) {
//			r.reload();
//		}
//		for (Screen s : screens) {
//			s.rebuild();
//		}
//		displayScreen(screens.peek());
//	}

	public void addReloadListener(ReloadListener l) {
		reloadListeners.add(l);
	}

	public interface ReloadListener {
		void reload();
	}

	public static MainFrame getInstance() {
		return instance;
	}
}

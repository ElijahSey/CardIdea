package presentation.basic;

import javafx.application.Preloader;
import javafx.application.Preloader.StateChangeNotification.Type;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import presentation.util.Util;

public class SplashScreenLoader extends Preloader {

	private Stage stage;
	private static final int SPLASH_WIDTH = 150;
	private static final int SPLASH_HEIGHT = 150;

	@Override
	public void start(Stage primaryStage) throws Exception {

		stage = primaryStage;
		stage.initStyle(StageStyle.TRANSPARENT);

		BorderPane pane = new BorderPane();
		pane.setCenter(Util.imageViewOf(Util.loadImage("card_logo.png"), SPLASH_WIDTH, SPLASH_HEIGHT));
		Scene scene = new Scene(pane, SPLASH_WIDTH, SPLASH_HEIGHT);
		scene.setFill(Color.TRANSPARENT);
		Rectangle2D bounds = Screen.getPrimary().getBounds();
		stage.setScene(scene);
		stage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
		stage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);

		stage.show();
	}

	@Override
	public void handleStateChangeNotification(StateChangeNotification stateChangeNotification) {

		if (stateChangeNotification.getType() == Type.BEFORE_START) {
			stage.hide();
		}
	}
}

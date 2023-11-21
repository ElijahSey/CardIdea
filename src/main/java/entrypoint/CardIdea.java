package entrypoint;

import javafx.application.Application;
import presentation.basic.MainFrame;
import presentation.basic.SplashScreenLoader;

public class CardIdea {

	public static void main(String[] args) {

		System.setProperty("javafx.preloader", SplashScreenLoader.class.getCanonicalName());
		Application.launch(MainFrame.class, args);
	}
}

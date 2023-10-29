package entrypoint;

import dataAccess.PropertyManager;
import javafx.application.Application;
import presentation.basic.MainFrame;

public class CardIdea {

	public static void main(String[] args) {

		PropertyManager.initializeProperties();
		Application.launch(MainFrame.class, args);
	}
}

package presentation.basic;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import presentation.menuBar.MenuBar;

public class Display {

	private Screen screen;
	private Node node;
	private Node menuBarNode;

	public Display(FXMLLoader loader, FXMLLoader menuBarLoader) {

		screen = loader.getController();
		node = loader.getRoot();
		menuBarNode = menuBarLoader.getRoot();

		MenuBar menuBar = menuBarLoader.getController();
		screen.setHeaderLabel((menuBar).getHeaderLabel());
		screen.addMenuItems(menuBar);
	}

	public Screen getController() {

		return screen;
	}

	public Node getNode() {

		return node;
	}

	public Node getMenuBarNode() {

		return menuBarNode;
	}
}

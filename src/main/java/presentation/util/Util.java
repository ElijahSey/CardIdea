package presentation.util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public final class Util {

	private static final String IMAGE_PATH = "/images/";

	public static Image loadImage(String name) {

		return new Image(Util.class.getResourceAsStream(IMAGE_PATH + name));
	}

	public static ImageView imageViewOf(Image image, double width, double height) {

		ImageView view = new ImageView();
		view.setImage(image);
		view.setFitWidth(width);
		view.setFitHeight(height);
		return view;
	}
}

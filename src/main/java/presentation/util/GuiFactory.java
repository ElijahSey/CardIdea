package presentation.util;

import javax.swing.JButton;

public abstract class GuiFactory {

	private static GuiFactory instance;

	public abstract JButton createButton(String text);

	public static GuiFactory createDefaultGuiFactory() {
		instance = new DefaultGuiFactory();
		return instance;
	}

	public static GuiFactory getInstance() {
		if (instance == null) {
			return createDefaultGuiFactory();
		}
		return instance;
	}
}

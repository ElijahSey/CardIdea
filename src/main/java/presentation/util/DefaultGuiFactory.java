package presentation.util;

import javax.swing.JButton;

public class DefaultGuiFactory extends GuiFactory {

	@Override
	public JButton createButton(String text) {
		JButton b = new JButton(text);
		b.setFocusPainted(false);
		return b;
	}
}

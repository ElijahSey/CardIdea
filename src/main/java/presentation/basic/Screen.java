package presentation.basic;

import javafx.scene.control.Label;
import logic.data.DataPreperator;
import presentation.menuBar.MenuBar;
import presentation.util.LanguageManager;

public abstract class Screen implements FXController {

	protected final LanguageManager lm;
	protected final DataPreperator dp;
	protected final MainFrame mainFrame;

	protected Label header;

	public Screen() {

		lm = LanguageManager.getInstance();
		dp = DataPreperator.getInstance();
		mainFrame = MainFrame.getInstance();
	}

	public void setHeaderLabel(Label header) {

		this.header = header;
	}

	public boolean beforeOpen() {

		header.setText(getHeader());
		return true;
	}

	public void onDisplay() {

	}

	public boolean beforeClose() {

		return true;
	}

	public void addMenuItems(MenuBar menuBar) {

	}

	public String getHeader() {

		return lm.getString(getClass(), "header");
	}
}

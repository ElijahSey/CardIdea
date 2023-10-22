package presentation.basic;

import logic.data.DataPreperator;
import presentation.util.LanguageManager;

public abstract class AbstractDialog implements FXController {

	protected final LanguageManager lm;
	protected final DataPreperator dp;

	public AbstractDialog(boolean modal) {
		lm = LanguageManager.getInstance();
		dp = DataPreperator.getInstance();
	}

	public void beforeDisplay() {

	}

	public void beforeClose() {

	}

	protected String getHeader() {
		return lm.getString(getClass(), getClass().getSimpleName() + ".header");
	}
}

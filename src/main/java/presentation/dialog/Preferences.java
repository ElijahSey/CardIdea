package presentation.dialog;

import java.util.Locale;

import dataAccess.PropertyManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;
import presentation.basic.AbstractDialog;

public class Preferences extends AbstractDialog {

	@FXML
	private ChoiceBox<Locale> languages;

	public Preferences() {

		show(false);
	}

	@Override
	public void initialize() {

		languages.setConverter(new StringConverter<>() {

			@Override
			public String toString(Locale object) {

				return object.getDisplayLanguage(object);
			}

			@Override
			public Locale fromString(String string) {

				return new Locale(string);
			}
		});
		languages.setItems(FXCollections.observableList(PropertyManager.getAvailableLanguages()));
		languages.getSelectionModel().select(lm.getLocale());
	}

	@FXML
	private void handleCancel() {

		close();
	}

	@FXML
	private void handleSave() {

		Locale loc = languages.getValue();
		lm.setLocale(loc);
		PropertyManager.setProperty(PropertyManager.LANGUAGE_KEY, loc.getLanguage());
		close();
	}
}
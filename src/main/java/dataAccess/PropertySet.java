package dataAccess;

import java.io.IOException;
import java.util.Properties;

public enum PropertySet {

	;

	private Properties prop;

	PropertySet() {
		prop = new Properties();
		try {
			prop.load(getClass().getResourceAsStream(PropertyManager.PROPERTIES_FOLDER + name() + ".properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String get(String key) {
		return prop.getProperty(key);
	}
}

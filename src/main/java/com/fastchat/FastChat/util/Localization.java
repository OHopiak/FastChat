package com.fastchat.FastChat.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Localization {

	private static final ResourceBundle DEFAULT_BUNDLE = ResourceBundle.getBundle("default", new UTF8Control());
	private static String bundleName = "default";
	private static ResourceBundle bundle = ResourceBundle.getBundle(bundleName, new UTF8Control());

	public static String get(String key) {
		String result = "";
		try {
			result = bundle.getString(key);
		} catch (MissingResourceException ex) {
			try {
				result = DEFAULT_BUNDLE.getString(key);
			} catch (MissingResourceException ignore) {
			}
		}
		return result;
	}

	private static void setBundle(String bundleNewName) {
		if (!bundleNewName.equals(bundleName)) {

			if (Localization.class.getResource(String.format("/%s.properties", bundleNewName)) != null) {
				bundleName = bundleNewName;
				bundle = ResourceBundle.getBundle(bundleName, new UTF8Control());
			} else {
				bundleName = "default";
				bundle = DEFAULT_BUNDLE;
			}
		}
	}

	public static void autoSetLocale() {
		String language = System.getProperty("user.language");
		setLocale(new Locale(language));
	}

	private static void setLocale(Locale locale) {
		switch (locale.getLanguage()) {
			case "uk":
				setBundle("ukrainian");
				break;
			case "nb":
				setBundle("norwegian");
				break;
			default:
				setBundle("default");
		}
	}
}

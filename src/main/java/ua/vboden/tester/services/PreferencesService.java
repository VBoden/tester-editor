package ua.vboden.tester.services;

import java.util.prefs.Preferences;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PreferencesService {

	public static final String AUTO_LOAD_LAST_SELECTED_DB = "auto.load.last.selected.db";

	public static final String LAST_DB = "db.last.opened";

	@Autowired
	private SessionService sessionService;

	private Preferences preferences;

	public static Preferences getPreferences() {
		return Preferences.userRoot().node("tester.fxeditor");
	}

	public static void setLastDB(String dbFile) {
		getPreferences().put(LAST_DB, dbFile);
	}

	public static String getLastDB() {
		return getPreferences().get(LAST_DB, null);
	}

	public static boolean isSelectDbOnStart() {
		return !getPreferences().getBoolean(AUTO_LOAD_LAST_SELECTED_DB, false);
	}

	public void loadPreferences() {
		preferences = getPreferences();
		sessionService.setAutoLoadLastSelectedDb(preferences.getBoolean(AUTO_LOAD_LAST_SELECTED_DB, false));
	}
	
	public void saveAutoLoadLastSelectedDb(boolean autoLoadLastDb) {
		preferences.putBoolean(AUTO_LOAD_LAST_SELECTED_DB, autoLoadLastDb);
		sessionService.setAutoLoadLastSelectedDb(autoLoadLastDb);
	}
}

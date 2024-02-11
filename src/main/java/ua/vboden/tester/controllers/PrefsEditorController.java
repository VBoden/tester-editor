package ua.vboden.tester.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import ua.vboden.tester.dto.CodeString;
import ua.vboden.tester.services.PreferencesService;
import ua.vboden.tester.services.SessionService;

@Component
public class PrefsEditorController extends AbstractController {

	@FXML
	private CheckBox autoLoadLastSelectedDb;

	@FXML
	private CheckBox displayDefaultsOnlyCheck;

	@FXML
	private ComboBox<CodeString> languageFrom;

	@FXML
	private ComboBox<CodeString> languageTo;

	@FXML
	private CheckBox useDefaultCheck;

	@FXML
	private CheckBox showTranscription;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private PreferencesService preferencesService;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		preferencesService.loadPreferences();
		initView();
	}

	private void initView() {		
		autoLoadLastSelectedDb.setSelected(sessionService.isAutoLoadLastSelectedDb());
	}

	@Override
	String getFXML() {
		return "/fxml/testerPrefsEditor.fxml";
	}

	@Override
	String getTitle() {
		return getResources().getString("menu.file.preferences");
	}

	@FXML
	void onCancel(ActionEvent event) {
		getStage().close();
	}

	@FXML
	void onSave(ActionEvent event) {
		preferencesService.saveAutoLoadLastSelectedDb(autoLoadLastSelectedDb.isSelected());
		getStage().close();
	}

}

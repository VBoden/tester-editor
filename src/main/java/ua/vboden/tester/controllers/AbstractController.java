package ua.vboden.tester.controllers;

import java.io.IOException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import ua.vboden.tester.services.SessionService;

public abstract class AbstractController implements ApplicationContextAware, Initializable {

	Logger logger = LoggerFactory.getLogger(AbstractController.class);

	@Autowired
	private SessionService sessionService;

	private ApplicationContext applicationContext;

	private ResourceBundle resources;

	private Stage stage;

	abstract String getFXML();

	abstract String getTitle();

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (this.applicationContext != null) {
			return;
		}
		this.applicationContext = applicationContext;
	}

	public void showStage(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(getFXML()));
		resources = ResourceBundle.getBundle("bundles/localization");
		fxmlLoader.setResources(resources);
		fxmlLoader.setController(this);
		Parent parent = fxmlLoader.load();
//		Parent parent = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));

		Scene scene = new Scene(parent);
//		Scene scene = new Scene(parent, 510, 325);
		if (stage == null) {
			stage = new Stage();
		}
		this.stage = stage;
		stage.setTitle(getTitle());
		stage.setScene(scene);

		stage.show();
	}

	public Stage getStage() {
		return stage;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	public ResourceBundle getResources() {
		return resources;
	}

	protected void showInformationAlert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION, message, ButtonType.OK);
		alert.setTitle(resources.getString("error.message.title"));
		alert.setHeaderText(resources.getString("error.message.header"));
		alert.showAndWait();
	}

}

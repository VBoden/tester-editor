package ua.vboden.tester.controllers;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import ua.vboden.tester.entities.Answer;
import ua.vboden.tester.services.AnswerService;
import ua.vboden.tester.services.EntityService;

@Component
@Scope(value = "prototype")
public class AnswerEditorController extends AbstractEditorController<Answer, Answer> {

	@FXML
	private TextArea answerText;

	@FXML
	private CheckBox checkCorrect;

	@Autowired
	private AnswerService answerService;
	private List<Answer> answers = new ArrayList<>();
	private TableView<Answer> answersTable;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initView();
	}

	public void showStage(List<Answer> answers, Answer selected, TableView<Answer> answersTable) throws IOException {
		setCurrent(selected);
		this.answers = answers;
		this.answersTable = answersTable;
		super.showStage((Stage)null);
		populateFields(getCurrent());
	}

	public void showStage(List<Answer> answers) throws IOException {
		this.answers = answers;
		super.showStage((Stage)null);
	}

	@Override
	protected void initView() {

	}

	@Override
	String getFXML() {
		return "/fxml/answerEditor.fxml";
	}

	@Override
	String getTitle() {
		return getResources().getString("menu.manage.categories");
	}

	@Override
	protected void resetEditing() {
		answerText.setText("");
		checkCorrect.setSelected(false);
	}

	@Override
	protected EntityService<Answer, Answer> getService() {
		return answerService;
	}

	@Override
	protected ObservableList<Answer> getSelected() {
		return null;// categoriesList.getSelectionModel().getSelectedItems();
	}

	@Override
	protected void populateEntity(Answer entity) {
		String newValue = answerText.getText();
		entity.setValue(newValue);
		entity.setCorrect(checkCorrect.isSelected());
		if (getCurrent() == null) {
			answers.add(entity);
		}else {
			int index = answers.indexOf(entity);
			answers.remove(entity);
			answers.add(index, entity);
			answersTable.refresh();
		}
	}

	@Override
	protected Answer createNew() {
		return new Answer();
	}

	@Override
	protected String checkFilledFields() {
		String newTitle = answerText.getText();
		return StringUtils.isBlank(newTitle)
				? MessageFormat.format(getResources().getString("error.message.fill"),
						getResources().getString("answer.text"))
				: null;
	}

	@Override
	protected void populateFields(Answer current) {
		answerText.setText(current.getValue());
		checkCorrect.setSelected(current.isCorrect());
	}

	@Override
	protected TableView<Answer> getTable() {
		return null;
	}
}

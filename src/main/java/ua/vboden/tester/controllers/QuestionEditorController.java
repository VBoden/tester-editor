package ua.vboden.tester.controllers;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import ua.vboden.tester.dto.IdString;
import ua.vboden.tester.entities.Answer;
import ua.vboden.tester.entities.Category;
import ua.vboden.tester.entities.Question;
import ua.vboden.tester.services.CategoryService;
import ua.vboden.tester.services.EntityService;
import ua.vboden.tester.services.QuestionService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

@Component
@Scope(value = "prototype")
public class QuestionEditorController extends AbstractEditorController<Question, Question> {

	@FXML
	private TableColumn<Answer, String> answerTextColumn;

	@FXML
	private TableView<Answer> answersTable;

	@FXML
	private Button cleanFields;

	@FXML
	private Button closeButton;

	@FXML
	private TableColumn<Answer, Boolean> correctAnswerColumn;

	@FXML
	private TextArea questionText;

	@FXML
	private Button saveAsNewQuestion;

	@FXML
	private Button saveQuestion;

	@FXML
	private TreeView<Category> themesTree;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private ObjectFactory<AnswerEditorController> answerEditorController;

	private Map<Category, TreeItem<Category>> nodes = new HashMap<>();

//	private List<Answer> answers = new ArrayList<>();

	private ObservableList<Answer> answerItems;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initView();
	}

	@Override
	protected void initView() {
		categoryService.loadCategories();
		nodes = CategoryUtils.fillCategoryView(themesTree, getSessionService().getCategoryModels());

		answerTextColumn.setCellValueFactory(new PropertyValueFactory<Answer, String>("value"));
//		correctAnswerColumn.setCellValueFactory(new PropertyValueFactory<Answer, Boolean>("correct"));
//		correctAnswerColumn.setCellFactory(CheckBoxTableCell.forTableColumn(correctAnswerColumn));  
		
		correctAnswerColumn.setCellFactory(col -> {
            CheckBoxTableCell<Answer, Boolean> cell = new CheckBoxTableCell<>(index -> {
                BooleanProperty active = new SimpleBooleanProperty(answersTable.getItems().get(index).isCorrect());
                active.addListener((obs, wasActive, isNowActive) -> {
                	Answer item = answersTable.getItems().get(index);
                    item.setCorrect(isNowActive);
                });
                return active ;
            });
            return cell ;
        });
		
		answerItems = FXCollections.observableArrayList(new ArrayList<>());
		answersTable.setItems(answerItems);
	}

	public void showStage(Object object, Question selected) throws IOException {
		setCurrent(selected);
		super.showStage(null);
	}

	@Override
	String getFXML() {
		return "/fxml/questionEditor.fxml";
	}

	@Override
	String getTitle() {
		return getResources().getString("menu.edit.dictionaryEntries");
	}

	@Override
	protected void resetEditing() {
		questionText.setText("");
		themesTree.getSelectionModel().clearSelection();
		answerItems.clear();
	}

	@Override
	protected EntityService<Question, Question> getService() {
		return questionService;
	}

	@Override
	protected ObservableList<Question> getSelected() {
		return null;// categoriesList.getSelectionModel().getSelectedItems();
	}

	@Override
	protected void populateEntity(Question entity) {
		String newText = questionText.getText();
		entity.setText(newText);
		TreeItem<Category> selectedItem = themesTree.getSelectionModel().getSelectedItem();
		if (selectedItem != null) {
			entity.setCategories(List.of(selectedItem.getValue()));
		}
		entity.setAnswers(answerItems);
	}

	@Override
	protected Question createNew() {
		return new Question();
	}

	@Override
	protected String checkFilledFields() {
//		TreeItem<Category> selectedItem = themesTree.getSelectionModel().getSelectedItem();
//		if (selectedItem != null) {
//			Category value = selectedItem.getValue();
//			if (getCurrent() != null) {
//				Optional<Category> current = getCategoryById(getCurrent().getId());
//				if (current.isPresent()) {
//					Category sup = value;
//					do {
//						if (current.get().equals(sup)) {
//							return "cycle!";
//						}
//						sup = sup.getSupperCategory();
//					} while (sup != null);
//				}
//			}
//		}
		String newTitle = questionText.getText();
		return StringUtils.isBlank(newTitle)
				? MessageFormat.format(getResources().getString("error.message.fill"),
						getResources().getString("category.name"))
				: null;
	}

	private Optional<Category> getCategoryById(Integer id) {
		Optional<Category> current = getSessionService().getCategoryModels().stream()
				.filter(c -> c.getId() == id.intValue()).findAny();
		return current;
	}

	@Override
	protected void populateFields(Question current) {
		questionText.setText(current.getText());
		if (current.getCategories() != null && current.getCategories().size() > 0) {
			themesTree.getSelectionModel().select(nodes.get(current.getCategories().get(0)));
		} else {
			themesTree.getSelectionModel().clearSelection();
		}
		answerItems.clear();
		answerItems.addAll(current.getAnswers());
	}

	@Override
	protected TableView<Question> getTable() {
		return null;
	}

	@FXML
	void answersClicked(MouseEvent event) throws IOException {
		if (event.getClickCount() == 2) {
			Answer selected = answersTable.getSelectionModel().getSelectedItem();
//			populateFields(getCurrent());
			answerEditorController.getObject().showStage(answerItems, selected, answersTable);
		}
	}

	@FXML
	void addAnswer(MouseEvent event) throws IOException {
		answerEditorController.getObject().showStage(answerItems);
	}
//
//    @FXML
//    void cleanFields(ActionEvent event) {
//
//    }
//
//    @FXML
//    void close(ActionEvent event) {
//
//    }
//
//    @FXML
//    void save(ActionEvent event) {
//
//    }
//
//    @FXML
//    void saveNew(ActionEvent event) {
//
//    }
}

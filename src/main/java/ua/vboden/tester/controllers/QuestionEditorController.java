package ua.vboden.tester.controllers;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
import ua.vboden.tester.dto.IdString;
import ua.vboden.tester.entities.Category;
import ua.vboden.tester.services.CategoryService;
import ua.vboden.tester.services.EntityService;
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
public class QuestionEditorController extends AbstractEditorController<IdString, Category> {


    @FXML
    private TableColumn<?, ?> answerTextColumn;

    @FXML
    private TableView<?> answersTable;

    @FXML
    private Button cleanFields;

    @FXML
    private Button closeButton;

    @FXML
    private TableColumn<?, ?> correctAnswerColumn;

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

	Map<Category, TreeItem<Category>> nodes = new HashMap<>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		generalInit(resources);
//		categoriesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		initView();
	}

	@Override
	protected void initView() {
		categoryService.loadCategories();
		nodes = CategoryUtils.fillCategoryView(themesTree, getSessionService().getCategoryModels());
	}


	@Override
	String getFXML() {
		return "/fxml/testerCategoryEditor.fxml";
	}

	@Override
	String getTitle() {
		return getResources().getString("menu.manage.categories");
	}

	@Override
	protected void resetEditing() {
//		categoryName.setText("");
	}

	@Override
	protected EntityService<IdString, Category> getService() {
		return categoryService;
	}

	@Override
	protected ObservableList<IdString> getSelected() {
		return null;// categoriesList.getSelectionModel().getSelectedItems();
	}

	@Override
	protected void populateEntity(Category entity) {
//		String newTitle = categoryName.getText();
//		entity.setName(newTitle);
//		TreeItem<Category> selectedItem = themesTree.getSelectionModel().getSelectedItem();
//		if (selectedItem != null) {
//			entity.setSupperCategory(selectedItem.getValue());
//		}
	}

	@Override
	protected Category createNew() {
		return new Category();
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
//		String newTitle = categoryName.getText();
//		return StringUtils.isBlank(newTitle)
//				? MessageFormat.format(getResources().getString("error.message.fill"),
//						getResources().getString("category.name"))
//				: null;
		return null;
	}

	private Optional<Category> getCategoryById(Integer id) {
		Optional<Category> current = getSessionService().getCategoryModels().stream()
				.filter(c -> c.getId() == id.intValue()).findAny();
		return current;
	}

	@Override
	protected void populateFields(IdString current) {
//		categoryName.setText(current.getValue());
//		Category currentEntity = getCategoryById(current.getId()).get();
//		if (currentEntity.getSupperCategory() != null) {
//			themesTree.getSelectionModel().select(nodes.get(currentEntity.getSupperCategory()));
//		}else {
//			themesTree.getSelectionModel().clearSelection();
//		}
	}

	@Override
	protected TableView<IdString> getTable() {
		return null;
	}
	

    @FXML
    void answersClicked(MouseEvent event) {

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

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

@Component
@Scope(value = "prototype")
public class CategoryEditorController extends AbstractEditorController<IdString, Category> {

	@FXML
	private ListView<IdString> categoriesList;

	@FXML
	private TextField categoryName;

	@FXML
	private Button closeButton;

	@FXML
	private Button removeCategory;

	@FXML
	private Button saveAsNewCategory;

	@FXML
	private Button saveCategory;

	@FXML
	private Label statusMessage;

	@FXML
	private TreeView<Category> themesTree;

	@Autowired
	private CategoryService categoryService;

	Map<Category, TreeItem<Category>> nodes = new HashMap<>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		generalInit(resources);
		categoriesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		initView();
	}

	@Override
	protected void initView() {
		categoryService.loadCategories();
		ObservableList<IdString> categories = getSessionService().getCategories();
		categoriesList.setItems(categories);

		nodes = CategoryUtils.fillCategoryView(themesTree, getSessionService().getCategoryModels());

		statusMessage.setText(MessageFormat.format(getResources().getString("category.status"), categories.size()));
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
		categoryName.setText("");
		themesTree.getSelectionModel().clearSelection();
	}

	@Override
	protected EntityService<IdString, Category> getService() {
		return categoryService;
	}

	@Override
	protected ObservableList<IdString> getSelected() {
		return categoriesList.getSelectionModel().getSelectedItems();
	}

	@Override
	protected void populateEntity(Category entity) {
		String newTitle = categoryName.getText();
		entity.setName(newTitle);
		TreeItem<Category> selectedItem = themesTree.getSelectionModel().getSelectedItem();
		if (selectedItem != null) {
			entity.setSupperCategory(selectedItem.getValue());
		}
	}

	@Override
	protected Category createNew() {
		return new Category();
	}

	@Override
	protected String checkFilledFields() {
		TreeItem<Category> selectedItem = themesTree.getSelectionModel().getSelectedItem();
		if (selectedItem != null) {
			Category value = selectedItem.getValue();
			if (getCurrent() != null) {
				Optional<Category> current = getCategoryById(getCurrent().getId());
				if (current.isPresent()) {
					Category sup = value;
					do {
						if (current.get().equals(sup)) {
							return "cycle!";
						}
						sup = sup.getSupperCategory();
					} while (sup != null);
				}
			}
		}
		String newTitle = categoryName.getText();
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
	protected void populateFields(IdString current) {
		categoryName.setText(current.getValue());
		Category currentEntity = getCategoryById(current.getId()).get();
		if (currentEntity.getSupperCategory() != null) {
			themesTree.getSelectionModel().select(nodes.get(currentEntity.getSupperCategory()));
		}else {
			themesTree.getSelectionModel().clearSelection();
		}
	}

	@Override
	protected TableView<IdString> getTable() {
		return null;
	}
}

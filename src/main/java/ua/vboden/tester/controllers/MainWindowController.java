package ua.vboden.tester.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import ua.vboden.tester.components.ListChoiceDialog;
import ua.vboden.tester.dto.IdString;
import ua.vboden.tester.dto.TranslationRow;
import ua.vboden.tester.entities.Answer;
import ua.vboden.tester.entities.Category;
import ua.vboden.tester.entities.Question;
import ua.vboden.tester.entities.Type;
import ua.vboden.tester.fxservices.WordSpeakService;
import ua.vboden.tester.services.CategoryService;
import ua.vboden.tester.services.EntityService;
import ua.vboden.tester.services.QuestionService;

@Component
public class MainWindowController extends AbstractController {

	@FXML
	private MenuItem menuManageCategories;

	@FXML
	private MenuItem menuQuit;

	@FXML
	private TableView<TranslationRow> mainTable;

	@FXML
	private ListView<Question> questionsList;

	@FXML
	private ComboBox<String> catOrDictSelector;

	@FXML
	private ComboBox<String> conditionSelector;

	@FXML
	private ToggleButton findButton;

	@FXML
	private TextField findWordField;

	@FXML
	private CheckBox inTranslationsCheck;

	@FXML
	private Button filterButton;

	@FXML
	private Button resetButton;

	@FXML
	private Label statusMessage1;

	@FXML
	private Label statusMessage2;

	@FXML
	private Label statusMessage3;

	@FXML
	private TableColumn<TranslationRow, Integer> numberColumn;

	@FXML
	private TableColumn<TranslationRow, String> wordColumn;

	@FXML
	private TableColumn<TranslationRow, String> translationColumn;

	@FXML
	private TableColumn<TranslationRow, String> categoryColumn;

	@FXML
	private TableColumn<TranslationRow, String> transCategoryColumn;

	@FXML
	private TableColumn<TranslationRow, String> dictionaryColumn;

	@FXML
	private TableColumn<Answer, String> answerTextColumn;

	@FXML
	private TableView<Answer> answersTable;

	@FXML
	private TableColumn<Answer, Boolean> correctAnswerColumn;

	@FXML
	private TreeView<Category> chapters;

	@Autowired
	private ObjectFactory<CategoryEditorController> categoryEditorController;

//	@Autowired
//	private ObjectFactory<DictionaryEditorController> dictionaryEditorController;

	@Autowired
	private ObjectFactory<QuestionEditorController> questionEditorController;

//	@Autowired
//	private ObjectFactory<WordEditorController> wordEditorController;
//
//	@Autowired
//	private ObjectFactory<LanguageEditorController> languageEditorController;

	@Autowired
	private PrefsEditorController prefsEditorController;

//	@Autowired
//	private ImportDialogController importDialogController;

//	@Autowired
//	private EntryService entryService;

	@Autowired
	private QuestionService questionService;
//
//	@Autowired
//	private DictionaryService dictionaryService;

//	@Autowired
//	private LanguageService languageService;

	@Autowired
	private CategoryService categoryService;

//	@Autowired
//	private WordService wordService;
//
//	@Autowired
//	private IOService ioService;

//	@Autowired
//	private ImportService importService;

	private boolean filtered;

	private String selectedCatOrDictName;

	private int searchIndex;

	private ObservableList<Answer> answerItems;

	@Override
	String getFXML() {
		return "/fxml/testerMain.fxml";
	}

	@Override
	String getTitle() {
		return getSessionService().getCurrentDbFile();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		CategoryUtils.fillCategoryView(chapters, getSessionService().getCategoryModels());

		chapters.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Category>>() {

//		        @Override
//		        public void changed(ObservableValue<Category> observable, Category oldValue,
//		        		Category newValue) {
//
//		            TreeItem<Category> selectedItem = (TreeItem<Category>) newValue;
//		            System.out.println("Selected Text : " + selectedItem.getValue());
//		            // do what ever you want 
//		        }

			@Override
			public void changed(ObservableValue<? extends TreeItem<Category>> observable, TreeItem<Category> oldValue,
					TreeItem<Category> newValue) {
				TreeItem<Category> selectedItem = (TreeItem<Category>) newValue;
				Category selected = selectedItem.getValue();
				if (selected != null) {
					System.out.println("Selected Text : " + selected);
					answerItems.clear();
//		            updateQuestionsView();
					List<Integer> ids = new ArrayList<>();
					ids.add(selected.getId());
					addSubIds(selectedItem, ids);
					getSessionService().setQuestions(questionService.getAllByCategoryIds(ids));
//					getSessionService().setQuestions(questionService.getAllByCategoryId(selected.getId()));
//		    		updateTranslations();
					updateQuestionsView();
				}
			}

			private void addSubIds(TreeItem<Category> selectedItem, List<Integer> ids) {
				ids.add(selectedItem.getValue().getId());
				for (TreeItem<Category> child : selectedItem.getChildren()) {
					addSubIds(child, ids);
				}
			}

		});

		answerTextColumn.setCellValueFactory(new PropertyValueFactory<Answer, String>("value"));
		correctAnswerColumn.setCellFactory(col -> {
			CheckBoxTableCell<Answer, Boolean> cell = new CheckBoxTableCell<>(index -> {
				BooleanProperty active = new SimpleBooleanProperty(answersTable.getItems().get(index).isCorrect());
				active.addListener((obs, wasActive, isNowActive) -> {
					Answer item = answersTable.getItems().get(index);
					item.setCorrect(isNowActive);
				});
				return active;
			});
			return cell;
		});
		answerItems = FXCollections.observableArrayList(new ArrayList<>());
		answersTable.setItems(answerItems);
//		numberColumn.setCellValueFactory(new PropertyValueFactory<TranslationRow, Integer>("number"));
//		wordColumn.setCellValueFactory(new PropertyValueFactory<TranslationRow, String>("word"));
//		translationColumn.setCellValueFactory(new PropertyValueFactory<TranslationRow, String>("translation"));
//		categoryColumn.setCellValueFactory(new PropertyValueFactory<TranslationRow, String>("categories"));
//		transCategoryColumn.setCellValueFactory(new PropertyValueFactory<TranslationRow, String>("transCategories"));
//		dictionaryColumn.setCellValueFactory(new PropertyValueFactory<TranslationRow, String>("dictionaries"));
		updateQuestionsView();
//		catOrDictSelector
//				.setItems(FXCollections.observableArrayList(getResources().getString("filters.selection.categories"),
//						getResources().getString("filters.selection.dictionaries")));
//		conditionSelector.setItems(FXCollections.observableArrayList(getResources().getString("filters.selection.or"),
//				getResources().getString("filters.selection.and")));
//		catOrDictSelector.getSelectionModel().select(0);
//		conditionSelector.getSelectionModel().select(0);
////		loadCategories();
////		statusMessage3.setText(MessageFormat.format(resources.getString("dictionary.status"),
////				getSessionService().getDictionaries().size()));
//		mainTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

	}

	private void updateCategories() {
		categoryService.loadCategories();
		CategoryUtils.fillCategoryView(chapters, getSessionService().getCategoryModels());
	}

	private void updateQuestions() {
		questionService.loadQuestions(getSessionService().getQuestionIds());
	}

	private void updateQuestionsView() {
		ObservableList<Question> questions = getSessionService().getQuestions();
		updateTranslations(questions);
//		mainTable.getSelectionModel().select(translations.size() - 1);
//		mainTable.scrollTo(translations.size());
	}

	private void updateTranslations(ObservableList<Question> questions) {
//		if (getSessionService().isDisplayDefaultLanguagesOnly()) {
//			FilteredList<TranslationRow> filtered = new FilteredList<>(translations);
//			filtered.setPredicate(
//					row -> row.getWordLangCode().equals(getSessionService().getDefaultLanguageFrom().getCode()) && row
//							.getTranslationLangCode().equals(getSessionService().getDefaultLanguageTo().getCode()));
//			mainTable.setItems(filtered);
//		} else {
		questionsList.setItems(questions);
//		mainTable.setItems(translations);
//		}
//		statusMessage1
//				.setText(MessageFormat.format(getResources().getString("translations.status"), translations.size()));
	}

	private void loadCategories() {
//		ObservableList<IdString> categories = getSessionService().getCategoriesWithEmpty();
//		catDictsList.setItems(categories);
//		catDictsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//		statusMessage2.setText(MessageFormat.format(getResources().getString("category.status"), categories.size()));
	}

	private void loadDictionaries() {
//		ObservableList<IdString> dictionaries = getSessionService().getDictionariesWithEmpty();
//		catDictsList.setItems(dictionaries);
//		statusMessage3
//				.setText(MessageFormat.format(getResources().getString("dictionary.status"), dictionaries.size()));
	}

	@FXML
	void catOrDictChanged(ActionEvent event) {
		String selected = catOrDictSelector.getSelectionModel().getSelectedItem();
		if (getResources().getString("filters.selection.categories").equals(selected)) {
			loadCategories();
		} else {
			loadDictionaries();
		}
	}

	@FXML
	void doFiltering(ActionEvent event) {
//		ObservableList<IdString> selectedItems = catDictsList.getSelectionModel().getSelectedItems();
//		if (!selectedItems.isEmpty()) {
//
//			List<Integer> selectedIds = selectedItems.stream().map(IdString::getId).collect(Collectors.toList());
//			boolean condition = getResources().getString("filters.selection.and")
//					.equals(conditionSelector.getSelectionModel().getSelectedItem());
//			doFiltering(selectedIds, condition);
//		}
	}

	private void doFiltering(List<Integer> selectedIds, boolean condition) {
//		findButton.setSelected(false);
//		String selected = catOrDictSelector.getSelectionModel().getSelectedItem();
//		if (getResources().getString("filters.selection.categories").equals(selected)) {
//			entryService.loadTranslationsByCategories(selectedIds, condition);
//			if (selectedIds.get(0) != null)
//				selectedCatOrDictName = categoryService.findEntity(selectedIds.get(0)).getName();
//		} else {
//			entryService.loadTranslationsByDictionaries(selectedIds, condition);
//			if (selectedIds.get(0) != null)
//				selectedCatOrDictName = dictionaryService.findEntity(selectedIds.get(0)).getName();
//		}
//		updateTranslationsView();
//		filtered = true;
	}

	@FXML
	void resetFilters(ActionEvent event) {
//		entryService.loadTranslations();
//		updateTranslationsView();
//		filtered = false;
//		findButton.setSelected(false);
//		selectedCatOrDictName = null;
	}

	@FXML
	void editQuestion(MouseEvent event) throws BeansException, IOException {
		if (event.getClickCount() == 2) {
			Question selectedItem = questionsList.getSelectionModel().getSelectedItem();
			questionEditorController.getObject().showStage(selectedItem, this::updateQuestions);
//			doFiltering(Collections.singletonList(selectedItem.getId()), false);
		} else if (event.getClickCount() == 1) {
			Question selectedItem = questionsList.getSelectionModel().getSelectedItem();
			answerItems.clear();
			answerItems.addAll(selectedItem.getAnswers());
		}
	}

	@FXML
	void findWords(ActionEvent event) {
		filterWords();
	}

	private void filterWords() {
//		if (findButton.isSelected()) {
//			ObservableList<TranslationRow> translations = FXCollections.observableArrayList();
//			List<TranslationRow> searchResults;
//			String word = findWordField.getText();
//			if (inTranslationsCheck.isSelected()) {
//				if (filtered) {
//					searchResults = filterDisplayed(TranslationRow::getTranslation, word);
//				} else {
//					searchResults = entryService.getAllByTranslation(word);
//				}
//			} else {
//				if (filtered) {
//					searchResults = filterDisplayed(TranslationRow::getWord, word);
//				} else {
//					searchResults = entryService.getAllByWord(word);
//				}
//			}
//			translations.addAll(searchResults);
//			updateTranslations(translations);
//		} else {
//			ObservableList<TranslationRow> translations = getSessionService().getTranslations();
//			updateTranslations(translations);
//		}
	}

//	private List<TranslationRow> filterDisplayed(Function<TranslationRow, String> getter, String word) {
//		return getSessionService().getQuestions().stream()
//				.filter(row -> getter.apply(row).toLowerCase().contains(word.toLowerCase()))
//				.collect(Collectors.toList());
//	}

	@FXML
	void findWordsEnter(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER)) {
			goNext();
		} else {
			searchIndex = -1;
		}
	}

	@FXML
	void goNext(ActionEvent event) {
		goNext();
	}

	private void goNext() {
		searchIndex = findNextInDisplayed(findWordField.getText(), searchIndex + 1);
		if (searchIndex > -1) {
			mainTable.getSelectionModel().clearAndSelect(searchIndex);
			mainTable.scrollTo(searchIndex);
		}
	}

	@FXML
	void goPrevious(ActionEvent event) {
		searchIndex = findPreviousInDisplayed(findWordField.getText(), searchIndex - 1);
		if (searchIndex > -1) {
			mainTable.getSelectionModel().clearAndSelect(searchIndex);
			mainTable.scrollTo(searchIndex);
		}
	}

	private int findPreviousInDisplayed(String word, int index) {
//		Function<TranslationRow, String> getter = getSearchFiledGetter();
//		ObservableList<TranslationRow> translations = getSessionService().getQuestions();
//		if (index < 0) {
//			index = translations.size() - 1;
//		}
//		for (int i = index; i >= 0; i--) {
//			if (getter.apply(translations.get(i)).toLowerCase().contains(word.toLowerCase())) {
//				return i;
//			}
//		}
		return -1;
	}

	private int findNextInDisplayed(String word, int index) {
//		Function<TranslationRow, String> getter = getSearchFiledGetter();
//		ObservableList<TranslationRow> translations = getSessionService().getQuestions();
//		if (index > translations.size() - 1) {
//			index = 0;
//		}
//		for (int i = index; i < translations.size(); i++) {
//			if (getter.apply(translations.get(i)).toLowerCase().contains(word.toLowerCase())) {
//				return i;
//			}
//		}
		return -1;
	}

	private Function<TranslationRow, String> getSearchFiledGetter() {
		Function<TranslationRow, String> getter;
		if (inTranslationsCheck.isSelected()) {
			getter = TranslationRow::getTranslation;
		} else {
			getter = TranslationRow::getWord;
		}
		return getter;
	}

	@FXML
	void manageCategories(ActionEvent event) throws IOException {
		categoryEditorController.getObject().showStage(this::updateCategories);
	}

	@FXML
	void manageDictionaries(ActionEvent event) throws IOException {
//		dictionaryEditorController.getObject().showStage(null);
	}

	@FXML
	void manageDictionaryEntries(ActionEvent event) throws IOException {
		questionEditorController.getObject().showStage((Stage) null);
	}

	@FXML
	public void quit() {
		Platform.exit();
		System.exit(0);
	}

	@FXML
	void startEditing(MouseEvent event) throws IOException {
		if (event.getClickCount() == 2) {
			TranslationRow selected = mainTable.getSelectionModel().getSelectedItem();
//			questionEditorController.getObject().showStage(null, selected);
		}
	}

	@FXML
	void addToCategory(ActionEvent event) {
		String title = getResources().getString("menu.edit.addToCategory");
		String header = getResources().getString("category.select");
//		checkSelectedAndExecute(getSessionService()::getCategories, this::doAddToCategory, wordService, title, header);
	}

	@FXML
	void removeFromCategory(ActionEvent event) {
		String title = getResources().getString("menu.edit.removeFromCategory");
		String header = getResources().getString("category.select");
//		checkSelectedAndExecute(getSessionService()::getCategories, this::doRemoveFromCategory, wordService, title,
//				header);
	}

	@FXML
	void addToDictionary(ActionEvent event) {
		String title = getResources().getString("menu.edit.addToDictionary");
		String header = getResources().getString("dictionary.select");
//		checkSelectedAndExecute(getSessionService()::getDictionaries, this::doAddToDictionary, entryService, title,
//				header);
	}

	@FXML
	void removeFromDictionary(ActionEvent event) {
		String title = getResources().getString("menu.edit.removeFromDictionary");
		String header = getResources().getString("dictionary.select");
//		checkSelectedAndExecute(getSessionService()::getDictionaries, this::doRemoveFromDictionary, entryService, title,
//				header);
	}

	private ObservableList<TranslationRow> checkSelectedAndExecute(Supplier<ObservableList<IdString>> itemsGetter,
			BiFunction<TranslationRow, List<IdString>, List> translationAction, EntityService service, String title,
			String header) {
		ObservableList<TranslationRow> selectedEntries = mainTable.getSelectionModel().getSelectedItems();
		if (selectedEntries.size() == 0) {
			showInformationAlert(getResources().getString("editor.popup.items.not.selected"));
		} else {
			ListChoiceDialog<IdString> dialog = new ListChoiceDialog<>(itemsGetter.get(), title, header);
			Optional<List<IdString>> result = dialog.showAndWait();
			if (result.isPresent()) {
				List<?> entities = new ArrayList<>();
				for (TranslationRow translation : selectedEntries) {
					entities.addAll(translationAction.apply(translation, result.get()));
				}
				service.saveAll(entities);
//				entryService.loadTranslations(getSessionService().getTranslationIds());
				updateQuestionsView();
			}
		}
		return selectedEntries;
	}

	private <T> List<T> findNewOnly(List<T> collection1, List<T> collection2) {
		return collection1.stream().filter(el -> !collection2.contains(el)).collect(Collectors.toList());
	}

//	private List<Word> doAddToCategory(TranslationRow trnaslation, List<IdString> selectedCategories) {
//		List<Word> entities = new ArrayList<>();
//		DictionaryEntry entryEntity = entryService.findEntity(trnaslation);
//		List<Category> categoryEntities = categoryService.findEntities(selectedCategories);
//		Word wordEntity = entryEntity.getWord();
//		wordEntity.getCategory().addAll(findNewOnly(categoryEntities, wordEntity.getCategory()));
//		entities.add(wordEntity);
//		Word transEntity = entryEntity.getTranslation();
//		transEntity.getCategory().addAll(findNewOnly(categoryEntities, transEntity.getCategory()));
//		entities.add(transEntity);
//		return entities;
//	}

//	private List<Word> doRemoveFromCategory(TranslationRow trnaslation, List<IdString> selectedCategories) {
//		List<Word> entities = new ArrayList<>();
//		DictionaryEntry entryEntity = entryService.findEntity(trnaslation);
//		List<Category> categoryEntities = categoryService.findEntities(selectedCategories);
//		Word wordEntity = entryEntity.getWord();
//		wordEntity.getCategory().removeAll(categoryEntities);
//		entities.add(wordEntity);
//		Word transEntity = entryEntity.getTranslation();
//		transEntity.getCategory().removeAll(categoryEntities);
//		entities.add(transEntity);
//		return entities;
//	}

//	private List<DictionaryEntry> doAddToDictionary(TranslationRow trnaslation, List<IdString> selectedDictionaries) {
//		DictionaryEntry entryEntity = entryService.findEntity(trnaslation);
//		List<Type> dictionaryEntities = dictionaryService.findEntities(selectedDictionaries);
//		entryEntity.getDictionary().addAll(findNewOnly(dictionaryEntities, entryEntity.getDictionary()));
//		return List.of(entryEntity);
//	}
//
//	private List<DictionaryEntry> doRemoveFromDictionary(TranslationRow trnaslation,
//			List<IdString> selectedDictionaries) {
//		DictionaryEntry entryEntity = entryService.findEntity(trnaslation);
//		List<Type> dictionaryEntities = dictionaryService.findEntities(selectedDictionaries);
//		entryEntity.getDictionary().removeAll(dictionaryEntities);
//		return List.of(entryEntity);
//	}

	@FXML
	void removeSelected(ActionEvent event) {
//		ObservableList<TranslationRow> selectedEntries = mainTable.getSelectionModel().getSelectedItems();
//		if (selectedEntries.size() == 0) {
//			showInformationAlert(getResources().getString("editor.popup.items.not.selected"));
//		} else {
//			List<String> deleteNames = new ArrayList<>();
//			selectedEntries.stream().forEach(entry -> {
//				deleteNames.add(entry.toString());
//
//			});
//			Alert alert = new Alert(AlertType.CONFIRMATION,
//					getResources().getString("editor.popup.delete") + StringUtils.join(deleteNames, "\n"),
//					ButtonType.YES, ButtonType.NO);
//			alert.showAndWait();
//			if (alert.getResult() == ButtonType.YES) {
//				List<Integer> usedWords = new ArrayList<>();
//				entryService.getAllBySelected(selectedEntries).stream().forEach(entity -> {
//					usedWords.add(entity.getWord().getId());
//					usedWords.add(entity.getTranslation().getId());
//				});
//				entryService.deleteSelected(selectedEntries);
//
//				for (Word word : wordService.getAllByIds(usedWords)) {
//					if (entryService.getWordUsages(word) == 0) {
//						wordService.delete(word);
//					} else {
//						getSessionService().decreaseUsages(word.getId());
//					}
//				}
//
//				entryService.loadTranslations(getSessionService().getTranslationIds());
//				updateTranslationsView();
//			}
//		}
	}

	@FXML
	void exportEntries(ActionEvent event) {
//		ObservableList<TranslationRow> selectedEntries = mainTable.getSelectionModel().getSelectedItems();
//		if (selectedEntries.size() == 0) {
//			Alert alert = new Alert(AlertType.CONFIRMATION, getResources().getString("export.popup.items.not.selected"),
//					ButtonType.YES, ButtonType.NO);
//			alert.showAndWait();
//			if (alert.getResult() == ButtonType.YES) {
//				selectedEntries = mainTable.getItems();
//			} else {
//				return;
//			}
//		}
//		DictionaryEntry firstEntity = entryService.findEntity(selectedEntries.get(0));
//		String name = getLanguage(firstEntity.getWord()) + "-" + getLanguage(firstEntity.getTranslation()) + '_';
//		if (selectedCatOrDictName != null) {
//			name += selectedCatOrDictName;
//		}
//		name += "_" + getCurrentDate();
//		TextInputDialog dialog = new TextInputDialog(name);
//		dialog.setTitle(getResources().getString("export.dictionary.popup.title"));
//		dialog.setHeaderText(getResources().getString("export.dictionary.popup"));
//		dialog.setContentText(getResources().getString("export.dictionary.popup.file.name"));
//
//		Optional<String> result = dialog.showAndWait();
//		if (result.isPresent()) {
//			name = result.get() + ".vcb";
//			ioService.exportToFile(selectedEntries, name);
//		}
//		showInformationAlert(getResources().getString("export.finished"));
	}

//	private String getLanguage(Word word) {
//		return word.getLanguage().getCode();
//	}

	@FXML
	void exportEntriesAllCategories(ActionEvent event) {
//		ObservableList<IdString> categories = getSessionService().getCategories();
//		categories.add(0, new IdString(null, "No_Category"));
//		for (IdString category : categories) {
//			List<DictionaryEntry> entries = entryService.getAllByCategoryId(category.getId());
//			String dirName = "cagegories/";
//			ioService.exportEntries(category.getValue(), entries, dirName);
//		}
//		showInformationAlert(getResources().getString("export.finished"));
	}

	private String getCurrentDate() {
		return new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
	}

	@FXML
	void exportEntriesAllDictionaries(ActionEvent event) {
//		ObservableList<IdString> dictionaries = getSessionService().getDictionaries();
//		dictionaries.add(0, new IdString(null, "No_Dictionary"));
//		for (IdString dictionary : dictionaries) {
//			List<DictionaryEntry> entries = entryService.getAllByDictionaryId(dictionary.getId());
//			String dirName = "dictionaries/";
//			ioService.exportEntries(dictionary.getValue(), entries, dirName);
//		}
//		showInformationAlert(getResources().getString("export.finished"));
	}

	@FXML
	void importEntries(ActionEvent event) throws IOException {
//		importDialogController.showStage(null);
	}

	@FXML
	void exportEntriesToJson(ActionEvent event) {
//		ObservableList<TranslationRow> selectedEntries = mainTable.getSelectionModel().getSelectedItems();
//		if (selectedEntries.size() == 0) {
//			Alert alert = new Alert(AlertType.CONFIRMATION, getResources().getString("export.popup.items.not.selected"),
//					ButtonType.YES, ButtonType.NO);
//			alert.showAndWait();
//			if (alert.getResult() == ButtonType.YES) {
//				selectedEntries = mainTable.getItems();
//			} else {
//				return;
//			}
//		}
//		DictionaryEntry firstEntity = entryService.findEntity(selectedEntries.get(0));
//		String name = getLanguage(firstEntity.getWord()) + "-" + getLanguage(firstEntity.getTranslation()) + '_';
//		if (selectedCatOrDictName != null) {
//			name += selectedCatOrDictName;
//		}
//		name += "_" + getCurrentDate();
//		TextInputDialog dialog = new TextInputDialog(name);
//		dialog.setTitle(getResources().getString("export.json.popup.title"));
//		dialog.setHeaderText(getResources().getString("export.json.popup"));
//		dialog.setContentText(getResources().getString("export.dictionary.popup.file.name"));
//
//		Optional<String> result = dialog.showAndWait();
//		if (result.isPresent()) {
//			name = result.get() + ".json";
//			ioService.exportToJsonFile(selectedEntries, name);
//		}
//		showInformationAlert(getResources().getString("export.finished"));
	}

	@FXML
	void importEntriesFromJson(ActionEvent event) {
//		FileChooser fileChooser = new FileChooser();
//		fileChooser.setTitle(getResources().getString("import.from.json.title"));
//		fileChooser.getExtensionFilters()
//				.addAll(new ExtensionFilter(getResources().getString("import.from.json.filter"), "*.json"));
//		File file = fileChooser.showOpenDialog(getStage());
//		if (file != null) {
//			int count = importService.importFromJsonFile(file, "imported_" + getCurrentDate());
//			showInformationAlert(MessageFormat.format(getResources().getString("import.results"), count));
//			categoryService.loadCategories();
//			dictionaryService.loadData();
//			languageService.loadLanguages();
//			entryService.loadTranslations();
//		}
	}

	@FXML
	void openSettings(ActionEvent event) throws IOException {
		prefsEditorController.showStage(null);
	}

	@FXML
	void reloadQuestions(ActionEvent event) {
		questionService.loadQuestions(getSessionService().getQuestionIds());
	}

	@FXML
	void doEditTranslationWord(ActionEvent event) throws IOException {
		TranslationRow selected = mainTable.getSelectionModel().getSelectedItem();
//		wordEditorController.getObject().showStage(null, wordService.getDataById(selected.getTranslationId()));
	}

	@FXML
	void doEidtWord(ActionEvent event) throws IOException {
//		TranslationRow selected = mainTable.getSelectionModel().getSelectedItem();
//		wordEditorController.getObject().showStage(null, wordService.getDataById(selected.getWordId()));
	}

	@FXML
	void doSpeakWord(ActionEvent event) {
		TranslationRow selected = mainTable.getSelectionModel().getSelectedItem();
		WordSpeakService speakSevice = new WordSpeakService(selected.getWord(), selected.getWordLangCode());
		speakSevice.start();
	}

	@FXML
	void showAboutProgram(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION, getResources().getString("menu.help.about.content"),
				ButtonType.OK);
		alert.setTitle(getResources().getString("menu.help.about"));
		alert.setHeaderText(getResources().getString("menu.help.about.header"));
		alert.showAndWait();
	}
}

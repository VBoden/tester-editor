package ua.vboden.tester.services;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.stereotype.Service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ua.vboden.tester.dto.CodeString;
import ua.vboden.tester.dto.DictionaryData;
import ua.vboden.tester.dto.IdString;
import ua.vboden.tester.dto.TranslationRow;
import ua.vboden.tester.dto.WordData;
import ua.vboden.tester.entities.Category;
import ua.vboden.tester.entities.Question;

@Service
public class SessionService {

	private ResourceBundle resources = ResourceBundle.getBundle("bundles/localization");

	private ObservableList<Question> questions;

	private List<Integer> questionIds;

	private ObservableList<CodeString> languages;

	private ObservableList<WordData> words;

	private ObservableList<IdString> categories;

	private List<Category> categoryModels;

	private ObservableList<IdString> dictionaries;

	private ObservableList<DictionaryData> dictionaryData;

	private Map<Integer, Integer> wordUsages;

	private CodeString defaultLanguageFrom;

	private CodeString defaultLanguageTo;

	private boolean displayDefaultLanguagesOnly;

	private boolean autoLoadLastSelectedDb;

	private boolean fillDefaultLanguages;

	private boolean showTranscription;

	private IdString lastSelectedDictionary;

	private String currentDbFile;

	public void increaseUsages(int id) {
		if (wordUsages.containsKey(id)) {
			wordUsages.put(id, wordUsages.get(id) + 1);
		} else {
			wordUsages.put(id, 1);
		}
	}

	public void decreaseUsages(int id) {
		if (wordUsages.containsKey(id)) {
			wordUsages.put(id, wordUsages.get(id) - 1);
		} else {
			wordUsages.put(id, 0);
		}
	}

	public ObservableList<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
//		translations.sort(TranslationRow.lastFirstComparator());
		if (this.questions == null) {
			this.questions = FXCollections.observableArrayList(questions);
		} else {
			this.questions.clear();
			this.questions.addAll(questions);
		}
	}

	public List<Integer> getQuestionIds() {
		return questionIds;
	}

	public void setQuestionIds(List<Integer> questionIds) {
		this.questionIds = questionIds;
	}

	public ObservableList<IdString> getCategoriesWithEmpty() {
		return categories;
	}

	public ObservableList<IdString> getCategories() {
		if (categories.get(0).getId() == null) {
			ObservableList<IdString> newCategories = FXCollections.observableArrayList(categories);
			newCategories.remove(0);
			return newCategories;
		}
		return categories;
	}

	protected void setCategories(List<IdString> categoryModels) {
		if (this.categories == null) {
			this.categories = FXCollections.observableArrayList(categoryModels);
		} else {
			this.categories.clear();
			this.categories.addAll(categoryModels);
		}
		this.categories.add(0, new IdString(null, resources.getString("category.noCategory")));
	}

	public List<Category> getCategoryModels() {
		return categoryModels;
	}

	public void setCategoryModels(List<Category> categoryModels) {
		this.categoryModels = categoryModels;
	}

	public ObservableList<IdString> getDictionariesWithEmpty() {
		return dictionaries;
	}

	public ObservableList<IdString> getDictionaries() {
		if (dictionaries.get(0).getId() == null) {
			ObservableList<IdString> newDictionaries = FXCollections.observableArrayList(dictionaries);
			newDictionaries.remove(0);
			return newDictionaries;
		}
		return dictionaries;
	}

	public void setDictionaries(List<IdString> dictionaries) {
		if (this.dictionaries == null) {
			this.dictionaries = FXCollections.observableArrayList(dictionaries);
		} else {
			this.dictionaries.clear();
			this.dictionaries.addAll(dictionaries);
		}
		this.dictionaries.add(0, new IdString(null, resources.getString("dictionary.noDictionary")));
	}

	public ObservableList<DictionaryData> getDictionaryData() {
		return dictionaryData;
	}

	public void setDictionaryData(List<DictionaryData> dictionaryData) {
		this.dictionaryData = FXCollections.observableArrayList(dictionaryData);
	}

	public ObservableList<CodeString> getLanguages() {
		return languages;
	}

	public void setLanguages(List<CodeString> languages) {
		this.languages = FXCollections.observableArrayList(languages);
	}

	public ObservableList<WordData> getWords() {
		return words;
	}

	public void setWords(List<WordData> models) {
		this.words = FXCollections.observableArrayList(models);
	}

	public Map<Integer, Integer> getWordUsages() {
		return wordUsages;
	}

	public void setWordUsages(Map<Integer, Integer> wordUsages) {
		this.wordUsages = wordUsages;
	}

	public CodeString getDefaultLanguageFrom() {
		return defaultLanguageFrom;
	}

	public void setDefaultLanguageFrom(CodeString defaultLanguageFrom) {
		this.defaultLanguageFrom = defaultLanguageFrom;
	}

	public CodeString getDefaultLanguageTo() {
		return defaultLanguageTo;
	}

	public void setDefaultLanguageTo(CodeString defaultLanguageTo) {
		this.defaultLanguageTo = defaultLanguageTo;
	}

	public boolean isDisplayDefaultLanguagesOnly() {
		return displayDefaultLanguagesOnly;
	}

	public void setDisplayDefaultLanguagesOnly(boolean displayDefaultLanguagesOnly) {
		this.displayDefaultLanguagesOnly = displayDefaultLanguagesOnly;
	}

	public boolean isAutoLoadLastSelectedDb() {
		return autoLoadLastSelectedDb;
	}

	public void setAutoLoadLastSelectedDb(boolean autoLoadLastSelectedDb) {
		this.autoLoadLastSelectedDb = autoLoadLastSelectedDb;
	}

	public boolean isFillDefaultLanguages() {
		return fillDefaultLanguages;
	}

	public void setFillDefaultLanguages(boolean fillDefaultLanguages) {
		this.fillDefaultLanguages = fillDefaultLanguages;
	}

	public IdString getLastSelectedDictionary() {
		return lastSelectedDictionary;
	}

	public void setLastSelectedDictionary(IdString lastSelectedDictionary) {
		this.lastSelectedDictionary = lastSelectedDictionary;
	}

	public boolean isShowTranscription() {
		return showTranscription;
	}

	public void setShowTranscription(boolean showTranscription) {
		this.showTranscription = showTranscription;
	}

	public String getCurrentDbFile() {
		return currentDbFile;
	}

	public void setCurrentDbFile(String currentDbFile) {
		this.currentDbFile = currentDbFile;
	}

}

package ua.vboden.tester.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javafx.collections.ObservableList;
import ua.vboden.tester.dto.IdString;
import ua.vboden.tester.entities.Category;
import ua.vboden.tester.entities.Question;
import ua.vboden.tester.repositories.CategoryRepository;
import ua.vboden.tester.repositories.QuestionRepository;

@Service
public class QuestionService implements EntityService<Question, Question> {

	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private SessionService sessionService;

	@Override
	public void deleteSelected(ObservableList<? extends Question> selected) {
		questionRepository.deleteAllById(selected.stream().map(Question::getId).collect(Collectors.toList()));
	}

	@Override
	public Question findEntity(Question current) {
		return questionRepository.findById(current.getId()).get();
	}

	@Override
	public void save(Question entity) {
		questionRepository.save(entity);
	}

	@Override
	public void saveAll(List<Question> entities) {
		questionRepository.saveAll(entities);
	}

	public Question findEntity(int id) {
		return questionRepository.findById(id).get();
	}

//	public List<Question> findEntities(List<IdString> selected) {
//		List<Question> result = new ArrayList<>();
//		questionRepository.findAllById(selected.stream().map(IdString::getId).collect(Collectors.toList()))
//				.forEach(result::add);
//		return result;
//	}

	public Question findEntityByName(String name) {
		return questionRepository.findByText(name);
	}

	public void loadQuestions() {
		List<Question> allEntries = getAllEntries();
		sessionService.setQuestions(allEntries);
		sessionService.setQuestionIds(allEntries.stream().map(entry -> entry.getId()).collect(Collectors.toList()));
//		sessionService.setWordUsages(new HashMap<>());
//		for (DictionaryEntry entry : allEntries) {
//			sessionService.increaseUsages(entry.getWord().getId());
//			sessionService.increaseUsages(entry.getTranslation().getId());
//		}
	}

	public void loadQuestions(List<Integer> ids) {
		sessionService.setQuestions(getAllById(ids));
		sessionService.setQuestionIds(ids);
	}

	public List<Question> getAllById(List<Integer> ids) {
		List<Question> result = new ArrayList<>();
		questionRepository.findAllById(ids).forEach(result::add);
		return result;
	}
	private List<Question> getAllEntries() {
		return (List<Question>) questionRepository.findAll();
	}

}

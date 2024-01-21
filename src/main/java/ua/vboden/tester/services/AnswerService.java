package ua.vboden.tester.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javafx.collections.ObservableList;
import ua.vboden.tester.dto.IdString;
import ua.vboden.tester.entities.Answer;
import ua.vboden.tester.entities.Category;
import ua.vboden.tester.entities.Question;
import ua.vboden.tester.repositories.AnswerRepository;
import ua.vboden.tester.repositories.CategoryRepository;
import ua.vboden.tester.repositories.QuestionRepository;

@Service
public class AnswerService implements EntityService<Answer, Answer> {

	@Autowired
	private AnswerRepository answerRepository;
//	@Autowired
//	private SessionService sessionService;

	@Override
	public void deleteSelected(ObservableList<? extends Answer> selected) {
		answerRepository.deleteAllById(selected.stream().map(Answer::getId).collect(Collectors.toList()));
	}

	@Override
	public Answer findEntity(Answer current) {
		return answerRepository.findById(current.getId()).get();
	}

	@Override
	public void save(Answer entity) {
		answerRepository.save(entity);
	}

	@Override
	public void saveAll(List<Answer> entities) {
		answerRepository.saveAll(entities);
	}

//	public void loadCategories() {
//		List<IdString> categories = new ArrayList<>();
//		List<Category> entities = new ArrayList<>();
//		answerRepository.findAll().forEach(entry -> {
//			categories.add(new IdString(entry.getId(), entry.getName()));
//			entities.add(entry);
//		});
//		if(entities.isEmpty()) {
//			ResourceBundle resources = ResourceBundle.getBundle("bundles/localization");
//			Category entity = new Category();
//			entity.setName(resources.getString("theme.all"));
//			answerRepository.save(entity);
//			entities.add(entity);
//			categories.add(new IdString(entity.getId(), entity.getName()));
//		}
//		Collections.sort(categories);
//		sessionService.setCategories(categories);
//		sessionService.setCategoryModels(entities);
//	}

	public Answer findEntity(int id) {
		return answerRepository.findById(id).get();
	}

//	public List<Question> findEntities(List<IdString> selected) {
//		List<Question> result = new ArrayList<>();
//		answerRepository.findAllById(selected.stream().map(IdString::getId).collect(Collectors.toList()))
//				.forEach(result::add);
//		return result;
//	}

	public Answer findEntityByName(String name) {
		return answerRepository.findByValue(name);
	}


}

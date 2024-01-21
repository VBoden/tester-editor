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
import ua.vboden.tester.entities.Category;
import ua.vboden.tester.entities.Question;
import ua.vboden.tester.repositories.CategoryRepository;
import ua.vboden.tester.repositories.QuestionRepository;

@Service
public class QuestionService implements EntityService<Question, Question> {

	@Autowired
	private QuestionRepository questionRepository;
//	@Autowired
//	private SessionService sessionService;

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

//	public void loadCategories() {
//		List<IdString> categories = new ArrayList<>();
//		List<Category> entities = new ArrayList<>();
//		questionRepository.findAll().forEach(entry -> {
//			categories.add(new IdString(entry.getId(), entry.getName()));
//			entities.add(entry);
//		});
//		if(entities.isEmpty()) {
//			ResourceBundle resources = ResourceBundle.getBundle("bundles/localization");
//			Category entity = new Category();
//			entity.setName(resources.getString("theme.all"));
//			questionRepository.save(entity);
//			entities.add(entity);
//			categories.add(new IdString(entity.getId(), entity.getName()));
//		}
//		Collections.sort(categories);
//		sessionService.setCategories(categories);
//		sessionService.setCategoryModels(entities);
//	}

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


}

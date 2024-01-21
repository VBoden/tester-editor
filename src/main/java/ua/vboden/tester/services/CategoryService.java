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
import ua.vboden.tester.repositories.CategoryRepository;

@Service
public class CategoryService implements EntityService<IdString, Category> {

	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private SessionService sessionService;

	@Override
	public void deleteSelected(ObservableList<? extends IdString> selected) {
		categoryRepository.deleteAllById(selected.stream().map(IdString::getId).collect(Collectors.toList()));
	}

	@Override
	public Category findEntity(IdString current) {
		return categoryRepository.findById(current.getId()).get();
	}

	@Override
	public void save(Category entity) {
		categoryRepository.save(entity);
	}

	@Override
	public void saveAll(List<Category> entities) {
		categoryRepository.saveAll(entities);
	}

	public void loadCategories() {
		List<IdString> categories = new ArrayList<>();
		List<Category> entities = new ArrayList<>();
		categoryRepository.findAll().forEach(entry -> {
			categories.add(new IdString(entry.getId(), entry.getName()));
			entities.add(entry);
		});
		if(entities.isEmpty()) {
			ResourceBundle resources = ResourceBundle.getBundle("bundles/localization");
			Category entity = new Category();
			entity.setName(resources.getString("theme.all"));
			categoryRepository.save(entity);
			entities.add(entity);
			categories.add(new IdString(entity.getId(), entity.getName()));
		}
		Collections.sort(categories);
		sessionService.setCategories(categories);
		sessionService.setCategoryModels(entities);
	}

	public Category findEntity(int id) {
		return categoryRepository.findById(id).get();
	}

	public List<Category> findEntities(List<IdString> selected) {
		List<Category> result = new ArrayList<>();
		categoryRepository.findAllById(selected.stream().map(IdString::getId).collect(Collectors.toList()))
				.forEach(result::add);
		return result;
	}

	public Category findEntityByName(String name) {
		return categoryRepository.findByName(name);
	}


}

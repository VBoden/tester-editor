package ua.vboden.tester.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ua.vboden.tester.entities.Question;

public interface QuestionRepository extends CrudRepository<Question, Integer> {
	Question findByText(String text);

	List<Question> findByCategoriesId(Integer categoryId);

	List<Question> findByCategoriesIdIn(List<Integer> categoryIds);
}

package ua.vboden.tester.repositories;

import org.springframework.data.repository.CrudRepository;

import ua.vboden.tester.entities.Question;

public interface QuestionRepository extends CrudRepository<Question, Integer> {
	Question findByText(String text);
}

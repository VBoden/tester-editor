package ua.vboden.tester.repositories;

import org.springframework.data.repository.CrudRepository;

import ua.vboden.tester.entities.Answer;

public interface AnswerRepository extends CrudRepository<Answer, Integer> {
	Answer findByValue(String value);
}

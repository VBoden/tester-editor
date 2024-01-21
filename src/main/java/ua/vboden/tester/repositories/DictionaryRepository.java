package ua.vboden.tester.repositories;

import org.springframework.data.repository.CrudRepository;

import ua.vboden.tester.entities.Type;

public interface DictionaryRepository extends CrudRepository<Type, Integer> {

	Type findByName(String name);

}

package ua.vboden.tester.repositories;

import org.springframework.data.repository.CrudRepository;

import ua.vboden.tester.entities.Category;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
	Category findByName(String name);
}

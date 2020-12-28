package ca.benfarhat.simplecrud.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import ca.benfarhat.simplecrud.model.Tutorial;

/**
 * 
 * TutorialRepository: Repository pour les tutos 
 * 
 * @author Benfarhat Elyes
 * @since 2020-12-28
 * @version 1.0.0
 *
 */

public interface TutorialRepository extends CrudRepository<Tutorial, Long> {
	
	Optional<Tutorial> findById(Long id); 
	List<Tutorial> findByPublished(boolean published);
	List<Tutorial> findByTitleContaining(String title);
}

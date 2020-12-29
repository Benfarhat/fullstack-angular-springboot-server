package ca.benfarhat.simplecrud.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

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

public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
	
	
	List<Tutorial> findByPublished(boolean published);
	
	List<Tutorial> findByTitleContaining(String title);
}

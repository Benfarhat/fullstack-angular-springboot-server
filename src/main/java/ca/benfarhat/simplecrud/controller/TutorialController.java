package ca.benfarhat.simplecrud.controller;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ca.benfarhat.simplecrud.model.Tutorial;
import ca.benfarhat.simplecrud.repository.TutorialRepository;

@RestController
@RequestMapping("/tutorial")
public class TutorialController {

	@Autowired
	TutorialRepository tutorialRepository;

	@GetMapping(path = "", produces = "application/json;charset=UTF-8")
	public ResponseEntity<List<Tutorial>> getAllTutorials() {
		return ResponseEntity.ok(tutorialRepository.findAll());

	}

	@GetMapping(path = "/contains", produces = "application/json;charset=UTF-8")
	public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false) String title) {
		if (Objects.isNull(title)) {
			return ResponseEntity.ok(tutorialRepository.findAll());
		} else {
			return ResponseEntity.ok(tutorialRepository.findByTitleContaining(title));
		}

	}

	@GetMapping(path = "/{id}", produces = "application/json;charset=UTF-8")
	public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") long id) {
		Tutorial tutorial = tutorialRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tutoriel [" + id + "]non trouvé"));
		return ResponseEntity.ok(tutorial);
	}

	@PostMapping(path = "/add", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	public ResponseEntity<Tutorial> createTutorial(@Valid @RequestBody Tutorial tutorial) {
		Tutorial aAjouter = tutorialRepository.save(tutorial);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(aAjouter.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping(path = "/{id}", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") long id, @RequestBody Tutorial tutorial) {
		tutorialRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tutoriel [" + id + "]non trouvé"));
		tutorial.setId(id);
		return ResponseEntity.ok(tutorialRepository.save(tutorial));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTutorial(@PathVariable("id") long id) {
		tutorialRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/tutorials")
	public ResponseEntity<Void> deleteAllTutorials() {
		tutorialRepository.deleteAll();
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/published")
	public ResponseEntity<List<Tutorial>> findByPublished() {
		return ResponseEntity.ok(tutorialRepository.findByPublished(true));
	}

}

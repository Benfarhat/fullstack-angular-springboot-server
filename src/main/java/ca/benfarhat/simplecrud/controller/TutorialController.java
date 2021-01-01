package ca.benfarhat.simplecrud.controller;

import static ca.benfarhat.simplecrud.ApiConstant.CST_TUTORIAL_CTX;

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

import ca.benfarhat.simplecrud.dto.TutorialDto;
import ca.benfarhat.simplecrud.service.TutorialService;

/**
 * TutorialDtoController: Controller pour l'entité TutorialDto
 * 
 * @author Benfarhat Elyes
 * @since 2020-12-30
 * @version 1.0.0
 *
 */

@RestController
@RequestMapping(value = CST_TUTORIAL_CTX)
public class TutorialController {

	@Autowired
	TutorialService tutorialService;

	@GetMapping(path = "", produces = "application/json;charset=UTF-8")
	public ResponseEntity<List<TutorialDto>> getAllTutorialDtos() {
		return ResponseEntity.ok(tutorialService.findAll());
	}

	@GetMapping(path = "/contains", produces = "application/json;charset=UTF-8")
	public ResponseEntity<List<TutorialDto>> getAllTutorialDtos(@RequestParam(required = false) String search) {
		if (Objects.isNull(search)) {
			return ResponseEntity.ok(tutorialService.findAll());
		} else {
			return ResponseEntity.ok(tutorialService.findByTitleContaining(search));
		}
	}

	@GetMapping(path = "/{id}", produces = "application/json;charset=UTF-8")
	public ResponseEntity<TutorialDto> getTutorialDtoById(@PathVariable("id") long id) {
		return ResponseEntity.ok(tutorialService.getTutorialDtoById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tutoriel [" + id + "]non trouvé")));
	}

	@PostMapping(path = "", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	public ResponseEntity<TutorialDto> createTutorialDto(@Valid @RequestBody TutorialDto tutorialDto) {
		TutorialDto aAjouter = tutorialService.add(tutorialDto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(aAjouter.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping(path = "/{id}", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	public ResponseEntity<TutorialDto> updateTutorialDto(@PathVariable("id") long id, @RequestBody TutorialDto tutorialDto) {
		return ResponseEntity.ok(tutorialService.update(id, tutorialDto).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tutoriel [" + id + "]non trouvé")));	
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTutorialDto(@PathVariable("id") long id) {
		tutorialService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/TutorialDtos")
	public ResponseEntity<Void> deleteAllTutorialDtos() {
		tutorialService.deleteAll();
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/published")
	public ResponseEntity<List<TutorialDto>> findByPublished() {
		return ResponseEntity.ok(tutorialService.findByPublished(true));
	}

}

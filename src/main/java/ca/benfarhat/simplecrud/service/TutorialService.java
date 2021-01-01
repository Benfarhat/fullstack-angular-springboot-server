package ca.benfarhat.simplecrud.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.benfarhat.simplecrud.dto.TutorialDto;
import ca.benfarhat.simplecrud.entity.Tutorial;
import ca.benfarhat.simplecrud.mapper.TutorialMapper;
import ca.benfarhat.simplecrud.repository.TutorialRepository;

/**
 * TutorialService: Service pour les tutoriaux (regroupe notamment les appels au repository ou au mapper) 
 * 
 * @author Benfarhat Elyes
 * @since 2020-12-30
 * @version 1.0.0
 *
 */

@Service
public class TutorialService {

	@Autowired
	TutorialRepository tutorialRepository;

	@Autowired
	TutorialMapper tutorialMapper;
	
	/**
	 * Retourne tous les tutoriels
	 * @return
	 */
	public List<TutorialDto> findAll() {
		return tutorialRepository.findAll().stream().map(tutorialMapper::entityToDto).collect(Collectors.toList());
	}

	/**
	 * Permet une recherche d'une partie du titre
	 * @param title partie à rechercher
	 * @return
	 */
	public List<TutorialDto> findByTitleContaining(String title) {
		return tutorialRepository.findByTitleContaining(title).stream().map(tutorialMapper::entityToDto).collect(Collectors.toList());
	}

	/**
	 * Recherche par id
	 * @param id identifiant du dto/entité
	 * @return
	 */
	public Optional<TutorialDto> getTutorialDtoById(long id) {
		return tutorialRepository.findById(id).map(tutorialMapper::entityToDto);
	}

	/**
	 * Ajouter un tutoriel
	 * @param tutorialDto dto contenant les données a ajouter
	 * @return
	 */
	public TutorialDto add(TutorialDto tutorialDto) {
		Tutorial aAjouter = tutorialRepository.save(tutorialMapper.dtoToEntity(tutorialDto));
		return tutorialMapper.entityToDto(aAjouter);
	}

	/**
	 * Mise a jour d'un tutoriel
	 * @param id identifiant du tuto a modifier
	 * @param tutorialDto dto contenant les données a modifier
	 * @return
	 */
	public Optional<TutorialDto> update(long id, TutorialDto tutorialDto) {
		Optional<Tutorial> aModifier = tutorialRepository.findById(id);
		if (!aModifier.isPresent()) {
			return Optional.empty();
		}
		Tutorial aSauvegarder  = aModifier.get();
		tutorialDto.setId(id);
		tutorialMapper.updateEntityFromDto(tutorialDto, aSauvegarder);
		TutorialDto dtoapresSauvegarde =  tutorialMapper.entityToDto(tutorialRepository.save(aSauvegarder));
		return Optional.of(dtoapresSauvegarde);
	}

	/**
	 * Supprimer un tutoriel
	 * @param id identifiant du tuto
	 */
	public void delete(long id) {
		tutorialRepository.deleteById(id);
	}

	/**
	 * Supprimer tous les tutoriels
	 */
	public void deleteAll() {
		tutorialRepository.deleteAll();
	}

	/**
	 * Retourner les tutoriels selon le parametre de publication
	 * @param published true pour publié
	 * @return
	 */
	public List<TutorialDto> findByPublished(boolean published) {
		return tutorialRepository.findByPublished(published).stream().map(tutorialMapper::entityToDto).collect(Collectors.toList());
	}

}

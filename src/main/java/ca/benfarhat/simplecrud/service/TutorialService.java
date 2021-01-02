package ca.benfarhat.simplecrud.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import ca.benfarhat.simplecrud.CacheConstant;
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
@CacheDefaults(cacheName = CacheConstant.CST_CACHE_TUTORIAL)
public class TutorialService {

	@Autowired
	TutorialRepository tutorialRepository;

	@Autowired
	TutorialMapper tutorialMapper;
	
	/**
	 * Retourne tous les tutoriels
	 * @return
	 */
	@Cacheable(sync = true)
	public List<TutorialDto> findAll() {
		return tutorialRepository.findAll().stream().map(tutorialMapper::entityToDto).collect(Collectors.toList());
	}

	/**
	 * Permet une recherche d'une partie du titre
	 * @param title partie à rechercher
	 * @return
	 */
	@Cacheable(value = CacheConstant.CST_CACHE_TUTORIAL, sync = true)
	public List<TutorialDto> findByTitleContaining(String title) {
		return tutorialRepository.findByTitleContaining(title).stream().map(tutorialMapper::entityToDto).collect(Collectors.toList());
	}

	/**
	 * Recherche par id
	 * @param id identifiant du dto/entité
	 * @return
	 */
	@Cacheable(value = CacheConstant.CST_CACHE_TUTORIAL)
	public Optional<TutorialDto> getTutorialDtoById(@CacheKey long id) {
		return tutorialRepository.findById(id).map(tutorialMapper::entityToDto);
	}

	/**
	 * Ajouter un tutoriel
	 * @param tutorialDto dto contenant les données a ajouter
	 * @return
	 */
	@CachePut
	public TutorialDto add( @CacheValue TutorialDto tutorialDto) {
		Tutorial aAjouter = tutorialRepository.save(tutorialMapper.dtoToEntity(tutorialDto));
		return tutorialMapper.entityToDto(aAjouter);
	}

	/**
	 * Mise a jour d'un tutoriel
	 * @param id identifiant du tuto a modifier
	 * @param tutorialDto dto contenant les données a modifier
	 * @return
	 */
	@CachePut
	public Optional<TutorialDto> update(long id, @CacheValue TutorialDto tutorialDto) {
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
	@CacheEvict
	public void delete(@CacheKey long id) {
		tutorialRepository.deleteById(id);
	}

	/**
	 * Supprimer tous les tutoriels
	 */
	@CacheEvict(allEntries = true)
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

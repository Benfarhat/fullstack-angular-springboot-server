package ca.benfarhat.simplecrud.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import ca.benfarhat.simplecrud.dto.TutorialDto;
import ca.benfarhat.simplecrud.entity.Tutorial;

/**
 * 
 * TutorialMapper: Mapper pour les tutoriels 
 * 
 * @author Benfarhat Elyes
 * @since 2020-12-30
 * @version 1.0.0
 *
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TutorialMapper {
	
	TutorialDto entityToDto(Tutorial entity);
	Tutorial dtoToEntity(TutorialDto dto);

	void updateEntityFromDto(TutorialDto dto, @MappingTarget() Tutorial entity);
	void updateDtoFromEntity(Tutorial entity, @MappingTarget TutorialDto dto);
	
	default Tutorial updateFromDto(TutorialDto dto, Tutorial entity) {
		updateEntityFromDto(dto, entity);
		return entity;
	}	
	default TutorialDto updateFromEntity(Tutorial entity, TutorialDto dto) {
		updateDtoFromEntity(entity, dto);
		return dto;
	}

}

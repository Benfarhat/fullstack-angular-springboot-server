package ca.benfarhat.simplecrud.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.benfarhat.simplecrud.dto.TutorialDto;
import ca.benfarhat.simplecrud.entity.Tutorial;

/**
 * 
 * TutorialMapperTest: Test du mapper 
 * 
 * @author Benfarhat Elyes
 * @since 2020-12-30
 * @version 1.0.0
 *
 */

@SpringBootTest
class TutorialMapperTest {

	@Autowired
	TutorialMapper tutorialMapper;
	
	Tutorial tuto1, tuto2, tuto3;
	TutorialDto tutoDto1, tutoDto2, tutoDto3;
	
	@BeforeEach
	void init() {
		tuto1 = Tutorial.builder().id(0L).title("titre1").description("description1").published(true).build();
		tuto2 = Tutorial.builder().id(1L).title("titre2").build();
		tuto3 = Tutorial.builder().id(2L).title("titre3").published(true).build();
		tutoDto1 = TutorialDto.builder().id(0L).title("titreDto1").description("descriptionDto1").published(true).build();
		tutoDto2 = TutorialDto.builder().id(1L).title("titreDto2").description("descriptionDto2").build();
		tutoDto3 = TutorialDto.builder().id(2L).title("titreDto3").published(true).build();
	}
	
	
	@Test
	void testEntityToDto() {
		TutorialDto resultat1 = tutorialMapper.entityToDto(tuto1);
		TutorialDto resultat2 = tutorialMapper.entityToDto(tuto2);
		TutorialDto resultat3 = tutorialMapper.entityToDto(tuto3);
		
		assertThat(resultat1).isNotNull();
		assertThat(resultat2.getDescription()).isNull();
		assertThat(resultat2.getId()).isEqualTo(tuto2.getId());
		assertThat(resultat3.getTitle()).isEqualTo(tuto3.getTitle());
	}

	@Test
	void testDtoToEntity() {
		assertThat(tutorialMapper.dtoToEntity(tutoDto1).getTitle()).isEqualTo(tutoDto1.getTitle());
		assertThat(tutorialMapper.dtoToEntity(tutoDto3).getDescription()).isNull();
	}

	@Test
	void testUpdateEntityFromDto() {
		tutorialMapper.updateEntityFromDto(tutoDto2, tuto2);
		tutorialMapper.updateEntityFromDto(tutoDto3, tuto1);
		assertThat(tuto2.getDescription()).isEqualTo(tutoDto2.getDescription());
		assertThat(tuto2.getId()).isEqualTo(tutoDto2.getId());
		assertThat(tuto1.getDescription()).isEqualTo("description1");
	}

	@Test
	void testUpdateDtoFromEntity() {
		tutorialMapper.updateDtoFromEntity(tuto1, tutoDto1);
		tutorialMapper.updateDtoFromEntity(tuto2, tutoDto2);
		assertThat(tutoDto1.getDescription()).isEqualTo(tuto1.getDescription());
		assertThat(tutoDto2.getDescription()).isNotNull().isEqualTo("descriptionDto2");
	}

	@Test
	void testUpdateFromDto() {
		Tutorial resultat1 = tutorialMapper.updateFromDto(tutoDto2, tuto2);
		Tutorial resultat2 = tutorialMapper.updateFromDto(tutoDto3, tuto1);
		assertThat(resultat1.getDescription()).isEqualTo(tutoDto2.getDescription()).isEqualTo(tuto2.getDescription());
		assertThat(resultat1.getId()).isEqualTo(tutoDto2.getId()).isEqualTo(tuto2.getId());
		assertThat(resultat2.getDescription()).isNotEqualTo(tutoDto3.getDescription()).isEqualTo(tuto1.getDescription());
	}

	@Test
	void testUpdateFromEntity() {
		TutorialDto resultatDto1 = tutorialMapper.updateFromEntity(tuto1, tutoDto1);
		TutorialDto resultatDto2 = tutorialMapper.updateFromEntity(tuto2, tutoDto2);
		assertThat(resultatDto1.getDescription()).isEqualTo(tuto1.getDescription()).isEqualTo(tutoDto1.getDescription());
		assertThat(resultatDto2.getDescription()).isNotNull().isNotEqualTo(tuto2.getDescription()).isEqualTo(tutoDto2.getDescription());
	}


}

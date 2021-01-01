package ca.benfarhat.simplecrud.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.benfarhat.simplecrud.dto.TutorialDto;
import ca.benfarhat.simplecrud.dto.TutorialDto.TutorialDtoBuilder;
import ca.benfarhat.simplecrud.entity.Tutorial;
import ca.benfarhat.simplecrud.entity.Tutorial.TutorialBuilder;
import ca.benfarhat.simplecrud.mapper.TutorialMapper;
import ca.benfarhat.simplecrud.repository.TutorialRepository;

@ExtendWith(MockitoExtension.class)
class TutorialServiceTest {

	@Mock
	private TutorialRepository tutorialRepository;

	@Mock
	private TutorialMapper tutorialMapper;
	
	@InjectMocks
	private TutorialService tutorialService;

	private List<Tutorial> list = new ArrayList<>();
	
	@BeforeEach
	void setUp() throws Exception {

		list.add(Tutorial.builder().id(0L).title("title0").build());
		list.add(Tutorial.builder().id(1L).title("title1").published(true).build());
		list.add(Tutorial.builder().id(2L).title("titre2").build());
		list.add(Tutorial.builder().id(3L).title("titre3").published(true).build());
		list.add(Tutorial.builder().id(4L).title("titre4").published(true).build());
		list.add(Tutorial.builder().id(5L).title("titre5").build());
		
		// tutorialRepository stub
		lenient().doReturn(list).when(tutorialRepository).findAll();

		lenient().doAnswer(invocation -> {
			long id = (long) invocation.getArgument(0);
			return list.stream().filter(t -> t.getId() == id).findFirst();
		}).when(tutorialRepository).findById(any());
		
		lenient().doAnswer(invocation -> {
			Tutorial tuto = invocation.getArgument(0);
			if (Objects.isNull(tuto.getId())) {
				Long id = list.parallelStream().map(Tutorial::getId).max(Comparator.comparingLong(Long::valueOf)).orElse(-1L);
				tuto.setId(id + 1L);
				list.add(tuto);
			} else {
				List<Tutorial> listTemp = list.stream().filter(t -> t.getId() != tuto.getId()).collect(Collectors.toList());
				listTemp.add(tuto);
				list.clear();
				list.addAll(listTemp);
			}
			return tuto;
		}).when(tutorialRepository).save(any(Tutorial.class));	
		
		lenient().doAnswer(invocation -> {
			Long id = invocation.getArgument(0);
			List<Tutorial> listTemp = list.stream().filter(t -> t.getId() != id).collect(Collectors.toList());
			list.clear();
			list.addAll(listTemp);
			return null;
		}).when(tutorialRepository).deleteById(anyLong());
		
		lenient().doAnswer(invocation -> {
			list.clear();
			return null;
		}).when(tutorialRepository).deleteAll();		
		
		lenient().doAnswer(invocation -> {
			Boolean published = invocation.getArgument(0);
			return list.stream().filter(t -> published.equals(t.isPublished())).collect(Collectors.toList());
		}).when(tutorialRepository).findByPublished(anyBoolean());	
		
		lenient().doAnswer(invocation -> {
			String part = invocation.getArgument(0);
			return list.stream().filter(t -> t.getTitle().contains(part)).collect(Collectors.toList());
		}).when(tutorialRepository).findByTitleContaining(anyString());
		
		// tutorialMapper stub
		lenient().doAnswer(invocation -> {
			Tutorial entity = invocation.getArgument(0);
	        if ( entity == null ) {
	            return null;
	        }

	        TutorialDtoBuilder tutorialDto = TutorialDto.builder();

	        tutorialDto.description( entity.getDescription() );
	        tutorialDto.id( entity.getId() );
	        tutorialDto.published( entity.isPublished() );
	        tutorialDto.title( entity.getTitle() );

	        return tutorialDto.build();
		}).when(tutorialMapper).entityToDto(any(Tutorial.class));
		
		lenient().doAnswer(invocation -> {
			TutorialDto dto = invocation.getArgument(0);
	        if ( dto == null ) {
	            return null;
	        }

	        TutorialBuilder tutorial = Tutorial.builder();

	        tutorial.description( dto.getDescription() );
	        tutorial.id( dto.getId() );
	        tutorial.published( dto.isPublished() );
	        tutorial.title( dto.getTitle() );

	        return tutorial.build();
		}).when(tutorialMapper).dtoToEntity(any(TutorialDto.class));
		
		lenient().doAnswer(invocation -> {
			TutorialDto dto = invocation.getArgument(0);
			Tutorial entity = invocation.getArgument(1);
	        if ( dto == null ) {
	            return null;
	        }

	        if ( dto.getDescription() != null ) {
	            entity.setDescription( dto.getDescription() );
	        }
	        entity.setId( dto.getId() );
	        entity.setPublished( dto.isPublished() );
	        if ( dto.getTitle() != null ) {
	            entity.setTitle( dto.getTitle() );
	        }
			return null;
		}).when(tutorialMapper).updateEntityFromDto(any(TutorialDto.class), any(Tutorial.class));
		
		lenient().doAnswer(invocation -> {
			Tutorial entity = invocation.getArgument(0);
			TutorialDto dto = invocation.getArgument(1);
	        if ( entity == null ) {
	            return null;
	        }

	        if ( entity.getDescription() != null ) {
	            dto.setDescription( entity.getDescription() );
	        }
	        dto.setId( entity.getId() );
	        dto.setPublished( entity.isPublished() );
	        if ( entity.getTitle() != null ) {
	            dto.setTitle( entity.getTitle() );
	        }
	        return null;
		}).when(tutorialMapper).updateDtoFromEntity(any(Tutorial.class), any(TutorialDto.class));
		
		lenient().doAnswer(invocation -> {
			TutorialDto dto = invocation.getArgument(0);
			Tutorial entity = invocation.getArgument(1);
	        if ( dto == null ) {
	            return entity;
	        }

	        if ( dto.getDescription() != null ) {
	            entity.setDescription( dto.getDescription() );
	        }
	        entity.setId( dto.getId() );
	        entity.setPublished( dto.isPublished() );
	        if ( dto.getTitle() != null ) {
	            entity.setTitle( dto.getTitle() );
	        }
			return entity;
		}).when(tutorialMapper).updateFromDto(any(TutorialDto.class), any(Tutorial.class));
		
		lenient().doAnswer(invocation -> {
			Tutorial entity = invocation.getArgument(0);
			TutorialDto dto = invocation.getArgument(1);
	        if ( entity == null ) {
	            return dto;
	        }

	        if ( entity.getDescription() != null ) {
	            dto.setDescription( entity.getDescription() );
	        }
	        dto.setId( entity.getId() );
	        dto.setPublished( entity.isPublished() );
	        if ( entity.getTitle() != null ) {
	            dto.setTitle( entity.getTitle() );
	        }
	        return dto;
		}).when(tutorialMapper).updateFromEntity(any(Tutorial.class), any(TutorialDto.class));
		
	}

	@Test
	@Order(1) 
	void testFindAll() {
		assertThat(tutorialService.findAll()).isNotEmpty().hasSize(list.size());
	}

	@Test
	@Order(2) 
	void testFindByTitleContaining() {
		assertThat(tutorialService.findByTitleContaining("titre")).isNotEmpty().hasSize(4);
	}

	@Test
	@Order(3)
	void testGetTutorialDtoById() {
		assertThat(tutorialService.getTutorialDtoById(5L)).isNotEmpty();
		assertThat(tutorialService.getTutorialDtoById(15L)).isEmpty();
	}

	@Test
	@Order(4)
	void testAdd() {
		int taille = list.size();
		tutorialService.add(TutorialDto.builder().title("abc").published(true).build());
		assertThat(tutorialService.findAll()).hasSize(taille + 1);
		assertThat(tutorialService.findByTitleContaining("abc")).hasSize(1);
	}

	@Test
	@Order(5)
	void testUpdate() {
		assertThat(tutorialService.findByTitleContaining("titre")).hasSize(4);
		int taille = list.size();
		// Notons qu'ici l'id de mise a jour n'est pas celui du Dto
		tutorialService.update(2L, TutorialDto.builder().id(5L).title("abc").build());
		assertThat(tutorialService.findAll()).hasSize(taille);
		assertThat(tutorialService.findByTitleContaining("abc")).hasSize(1);
		assertThat(tutorialService.findByTitleContaining("titre")).hasSize(3);
		assertThat(tutorialService.getTutorialDtoById(2L).get().getTitle()).isEqualTo("abc");
	}

	@Test
	@Order(6)
	void testDelete() {
		int taille = list.size();
		tutorialService.delete(0L);
		tutorialService.delete(1L);
		assertThat(tutorialService.findAll()).hasSize(taille - 2);
		assertThat(tutorialService.findByTitleContaining("title")).isEmpty();
		assertThat(tutorialService.getTutorialDtoById(0L)).isEmpty();
		assertThat(tutorialService.getTutorialDtoById(2L)).isNotEmpty();
		assertThat(tutorialService.getTutorialDtoById(1L)).isEmpty();
	}

	@Test
	@Order(7)
	void testFindByPublished() {
		assertThat(tutorialService.findByPublished(true)).isNotEmpty().hasSize(3);
	}
	
	@Test
	@Order(8)
	void testDeleteAll() {
		int taille = list.size();
		if (taille == 0) {
			tutorialService.add(TutorialDto.builder().title("titre").published(true).build());
			tutorialService.add(TutorialDto.builder().title("titre").published(true).build());
		}
		assertThat(list).isNotEmpty();
		tutorialService.deleteAll();
		assertThat(list).isEmpty();
	}


}

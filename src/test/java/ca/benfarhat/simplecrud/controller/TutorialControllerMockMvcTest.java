package ca.benfarhat.simplecrud.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.benfarhat.simplecrud.ApiConstant;
import ca.benfarhat.simplecrud.dto.TutorialDto;
import ca.benfarhat.simplecrud.service.TutorialService;

@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class TutorialControllerMockMvcTest {

	private MockMvc mockMvc;
	
	@InjectMocks
	private TutorialController tutorialController;
	
	@Mock
	private TutorialService tutorialService;
	
	private List<TutorialDto> list = new ArrayList<>();
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@BeforeAll
	void init() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(tutorialController)
				.addPlaceholderValue("server.servlet.context-path", ApiConstant.CST_CONTEXT_PATH).build();
	} 
	
	@BeforeEach
	void setUp() throws Exception {
		list.clear();
		AtomicLong id = new AtomicLong(0L);
		for(int i=0; i<10; i++) {
			list.add(TutorialDto.builder().id(id.get()).title((i%2 == 0 ? "title" : "titre") + id.getAndIncrement()).published(i%3==0).build());
		}
		lenient().when(tutorialService.findAll()).thenReturn(list);
		lenient().doAnswer(invocation -> {list.clear();	return null;}).when(tutorialService).deleteAll();
		lenient().doAnswer(invocation -> {list = list.stream().filter(t -> !t.getId().equals(invocation.getArgument(0))).collect(Collectors.toList());return null;}).when(tutorialService).delete(anyLong());
		lenient().doAnswer(invocation -> list.stream().filter(t -> t.getTitle().contains(invocation.getArgument(0))).collect(Collectors.toList())).when(tutorialService).findByTitleContaining(anyString());
		lenient().doAnswer(invocation -> list.stream().filter(t -> ((Boolean)t.isPublished()).equals(invocation.getArgument(0))).collect(Collectors.toList())).when(tutorialService).findByPublished(anyBoolean());
		lenient().doAnswer(invocation -> list.stream().filter(t -> t.getId().equals(invocation.getArgument(0))).findFirst()).when(tutorialService).getTutorialDtoById(anyLong());
		lenient().doAnswer(invocation -> {list.add(invocation.getArgument(0)); return invocation.getArgument(0);}).when(tutorialService).add(any(TutorialDto.class));
//		lenient().doAnswer(invocation -> {
//			list = list.stream().filter(t -> !t.getId().equals(invocation.getArgument(0))).collect(Collectors.toList());
//			TutorialDto dto = invocation.getArgument(1);
//			dto.setId(invocation.getArgument(0));
//			list.add(dto);
//			return dto;
//			})
//		.when(tutorialService).update(anyLong(), any(TutorialDto.class));
		lenient().doAnswer(invocation -> {
			return invocation.getArgument(1);
			})
		.when(tutorialService).update(anyLong(), any(TutorialDto.class));
	} 

	@Test 
	void testListerTutorial() throws Exception{
		MvcResult resultat = mockMvc.perform(MockMvcRequestBuilders.get(ApiConstant.CST_TUTORIAL_CTX))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		String json = resultat.getResponse().getContentAsString();
		List<TutorialDto> listDto = mapper.readValue(json, new TypeReference<List<TutorialDto>>(){});
		assertThat(listDto).hasSameElementsAs(list);
	}

	@Test 
	void testContainTitleTutorial() throws Exception{
		MvcResult resultat = mockMvc.perform(MockMvcRequestBuilders.get(ApiConstant.CST_TUTORIAL_CTX + "/contains").param("search", "titre"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		String json = resultat.getResponse().getContentAsString();
		List<TutorialDto> listDto = mapper.readValue(json, new TypeReference<List<TutorialDto>>(){});
		assertThat(listDto).hasSize(5);

		resultat = mockMvc.perform(MockMvcRequestBuilders.get(ApiConstant.CST_TUTORIAL_CTX + "/contains"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		json = resultat.getResponse().getContentAsString();
		listDto = mapper.readValue(json, new TypeReference<List<TutorialDto>>(){});
		assertThat(listDto).hasSize(list.size());
	}
	@Test 
	void testAddTutorial() throws Exception{
		TutorialDto ajout = TutorialDto.builder().id(20L).title("title20").build();
		String content = mapper.writeValueAsString(ajout);
		int taille = list.size();
		mockMvc.perform(MockMvcRequestBuilders.post(ApiConstant.CST_TUTORIAL_CTX).contentType(MediaType.APPLICATION_JSON).content(content))
				.andDo(print())
				.andExpect(status().isCreated());
		assertThat(list.size()).isEqualTo(taille + 1);
		assertThat(list).contains(ajout);
		
	}

	@Test 
	@Disabled("")
	void testUpdateTutorial() throws Exception{
		TutorialDto ajout = TutorialDto.builder().id(20L).title("title20").build();
		String content = mapper.writeValueAsString(ajout);
		MvcResult resultat = mockMvc.perform(MockMvcRequestBuilders.put(ApiConstant.CST_TUTORIAL_CTX + "/{id}", 52L).contentType(MediaType.APPLICATION_JSON).content(content))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		String json = resultat.getResponse().getContentAsString();
		TutorialDto retourDto = mapper.readValue(json, TutorialDto.class);
		assertThat(retourDto.getId()).isEqualTo(2L);
	}
	
	@Test 
	void testDeleteAllTutorial() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.delete(ApiConstant.CST_TUTORIAL_CTX + "/TutorialDtos")).andDo(print()).andExpect(status().isNoContent());
		assertThat(list).isEmpty();
	}
	
	@Test 
	void testDeleteByIdTutorial() throws Exception{
		int taille = list.size();
		mockMvc.perform(MockMvcRequestBuilders.delete(ApiConstant.CST_TUTORIAL_CTX + "/{id}", 0L)).andDo(print()).andExpect(status().isNoContent());
		assertThat(list).hasSize(taille - 1);
	}
	

}

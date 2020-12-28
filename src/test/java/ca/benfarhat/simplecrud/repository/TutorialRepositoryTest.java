package ca.benfarhat.simplecrud.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlConfig.TransactionMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.benfarhat.simplecrud.model.Tutorial;
import ca.benfarhat.simplecrud.repository.TutorialRepository;

@ExtendWith(SpringExtension.class)
@Sql(
		scripts = {"/db/tutorial.sql"}, 
		config = @SqlConfig(encoding = "utf-8", transactionMode = TransactionMode.ISOLATED))
@DataJpaTest
public class TutorialRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	TutorialRepository repository;

	@Test
	public void should_find_no_tutorials_if_repository_is_empty() {
		Iterable<Tutorial> tutorials = repository.findAll();

		assertThat(tutorials).isEmpty();
	}

	@Test
	public void should_store_a_tutorial() {
		Tutorial tutorial = repository
				.save(Tutorial.builder().title("titre1").description("description1").published(true).build());
		assertThat(tutorial).hasFieldOrPropertyWithValue("title", "titre1");
		assertThat(tutorial).hasFieldOrPropertyWithValue("description", "description1");
		assertThat(tutorial).hasFieldOrPropertyWithValue("published", true);
	}

	@Test
	public void should_find_all_tutorials() {
		Tutorial tut1 = Tutorial.builder().title("titre1").description("description1").published(true).build();
		entityManager.persist(tut1);

		Tutorial tut2 = Tutorial.builder().title("titre2").description("description2").published(false).build();
		entityManager.persist(tut2);

		Tutorial tut3 = Tutorial.builder().title("titre3").description("description3").published(true).build();
		entityManager.persist(tut3);

		Iterable<Tutorial> tutorials = repository.findAll();

		assertThat(tutorials).hasSize(3).contains(tut1, tut2, tut3);
	}

	@Test
	public void should_find_tutorial_by_id() {
		Tutorial tut1 = Tutorial.builder().title("titre1").description("description1").published(true).build();
		entityManager.persist(tut1);

		Tutorial tut2 = Tutorial.builder().title("titre2").description("description2").published(false).build();
		entityManager.persist(tut2);

		Tutorial foundTutorial = repository.findById(tut2.getId()).get();

		assertThat(foundTutorial).isEqualTo(tut2);
	}

	@Test
	public void should_find_published_tutorials() {
		Tutorial tut1 = Tutorial.builder().title("titre1").description("description1").published(true).build();
		entityManager.persist(tut1);

		Tutorial tut2 = Tutorial.builder().title("titre2").description("description2").published(false).build();
		entityManager.persist(tut2);

		Tutorial tut3 = Tutorial.builder().title("titre3").description("description3").published(true).build();
		entityManager.persist(tut3);

		Iterable<Tutorial> tutorials = repository.findByPublished(true);

		assertThat(tutorials).hasSize(2).contains(tut1, tut3);
	}

	@Test
	public void should_find_tutorials_by_title_containing_string() {
		Tutorial tut1 = Tutorial.builder().title("Spring boot").description("description1").published(true).build();
		entityManager.persist(tut1);

		Tutorial tut2 = Tutorial.builder().title("Java").description("description2").published(false).build();
		entityManager.persist(tut2);

		Tutorial tut3 = Tutorial.builder().title("Spring Data").description("description3").published(true).build();
		entityManager.persist(tut3);
		
		List<Tutorial> tutorials = repository.findByTitleContaining("ring");

		assertThat(tutorials).hasSize(2).contains(tut1, tut3);
	}

	@Test
	public void should_update_tutorial_by_id() {
		Tutorial tut1 = Tutorial.builder().title("titre1").description("description1").published(true).build();
		entityManager.persist(tut1);

		Tutorial tut2 = Tutorial.builder().title("titre2").description("description2").published(false).build();
		entityManager.persist(tut2);

		Tutorial updatedTut = Tutorial.builder().title("updated titre2").description("description2").published(false).build();

		Tutorial tut = repository.findById(tut2.getId()).get();
		tut.setTitle(updatedTut.getTitle());
		tut.setDescription(updatedTut.getDescription());
		tut.setPublished(updatedTut.isPublished());
		repository.save(tut);

		Tutorial checkTut = repository.findById(tut2.getId()).get();

		assertThat(checkTut.getId()).isEqualTo(tut2.getId());
		assertThat(checkTut.getTitle()).isEqualTo(updatedTut.getTitle());
		assertThat(checkTut.getDescription()).isEqualTo(updatedTut.getDescription());
		assertThat(checkTut.isPublished()).isEqualTo(updatedTut.isPublished());
	}

	@Test
	public void should_delete_tutorial_by_id() {
		Tutorial tut1 = Tutorial.builder().title("titre1").description("description1").published(true).build();
		entityManager.persist(tut1);

		Tutorial tut2 = Tutorial.builder().title("titre2").description("description2").published(false).build();
		entityManager.persist(tut2);

		Tutorial tut3 = Tutorial.builder().title("titre3").description("description3").published(true).build();
		entityManager.persist(tut3);

		repository.deleteById(tut2.getId());

		Iterable<Tutorial> tutorials = repository.findAll();

		assertThat(tutorials).hasSize(2).contains(tut1, tut3);
	}

	@Test
	public void should_delete_all_tutorials() {
		Tutorial tut1 = Tutorial.builder().title("titre1").description("description1").published(true).build();
		entityManager.persist(tut1);

		Tutorial tut2 = Tutorial.builder().title("titre2").description("description2").published(false).build();
		entityManager.persist(tut2);

		Tutorial tut3 = Tutorial.builder().title("titre3").description("description3").published(true).build();
		entityManager.persist(tut3);

		repository.deleteAll();

		assertThat(repository.findAll()).isEmpty();
	}

}

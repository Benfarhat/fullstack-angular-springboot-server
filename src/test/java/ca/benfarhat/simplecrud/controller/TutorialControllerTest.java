package ca.benfarhat.simplecrud.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlConfig.TransactionMode;

import ca.benfarhat.simplecrud.ApiConstant;

/**
 * 
 * TutorialControllerTest: Test intégré du controller 
 * 
 * @author Benfarhat Elyes
 * @since 2020-12-31
 * @version 1.0.0
 *
 */

@Sql(
		scripts = {"/db/tutorial.sql","/db/insertTutorial.sql"}, 
		config = @SqlConfig(encoding = "utf-8", transactionMode = TransactionMode.ISOLATED))
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TutorialControllerTest {
	
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	TutorialController tutorialcontroller;
	
	private String url;
	
	@BeforeEach
	void setUp() throws Exception {
		String path = ApiConstant.CST_CONTEXT_PATH + ApiConstant.CST_TUTORIAL_CTX;
		this.url = "http://localhost:" + this.port + path;
	} 
	
	@Test
	void contextLoads() {
		assertThat(tutorialcontroller).isNotNull();
		assertThat(this.restTemplate.getForObject(url,
				String.class)).contains("[{\"id\":1,\"description\":\"description1\",\"titre\":\"titre1\",\"publie\":true},"
						+ "{\"id\":2,\"description\":\"description2\",\"titre\":\"titre2\",\"publie\":true},"
						+ "{\"id\":3,\"description\":null,\"titre\":\"titre2\",\"publie\":true},"
						+ "{\"id\":4,\"description\":\"description2\",\"titre\":\"titre2\",\"publie\":false},"
						+ "{\"id\":5,\"description\":\"description2\",\"titre\":\"titre3\",\"publie\":false}]");
	}

}

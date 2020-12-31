package ca.benfarhat.simplecrud;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

class ApiConstantTest {

	@Test
	void testValeurVersionApi() {
		assertThat(ApiConstant.CST_CONTEXT_PATH).startsWith("/").contains("v1");
		assertThat(ApiConstant.CST_TUTORIAL_CTX).startsWith("/").hasSizeGreaterThan(1);
	}

	@Test
	void testPrivateconstructor() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		Constructor<ApiConstant> constructor = ApiConstant.class.getDeclaredConstructor();
		assertThat(Modifier.isPrivate(constructor.getModifiers())).isTrue();
		assertThatCode(() -> {
			constructor.newInstance();
		}).isInstanceOf(IllegalAccessException.class).hasMessageContaining("can not access");
		// A ce niveau, meme en rendant le constructeur accessible il y a une autre exception qui sera déclenchée en interne
		constructor.setAccessible(true);
		try {
			constructor.newInstance();
		} catch (InvocationTargetException e) { 
			// on va tomber sur une exception qui est normale, il s'agit d'un wrapper au niveau de la reflexion
			assertThat(e.getTargetException()).isInstanceOf(IllegalStateException.class).hasMessageContaining("Utility class");
		}

	}

}

package ca.benfarhat.simplecrud;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

/**
 * 
 * CacheConstantTest: Test basic pour la classe de constantes 
 * 
 * @author Benfarhat Elyes
 * @since 2021-01-01
 * @version 1.0.0
 *
 */
class CacheConstantTest {


	@Test
	void testPrivateconstructor() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		Constructor<CacheConstant> constructor = CacheConstant.class.getDeclaredConstructor();
		assertThat(Modifier.isPrivate(constructor.getModifiers())).isTrue();
		assertThatCode(() -> {
			constructor.newInstance();
		}).isInstanceOf(IllegalAccessException.class).hasMessageContaining("can not access");
		constructor.setAccessible(true);
		try {
			constructor.newInstance();
		} catch (InvocationTargetException e) { 
			assertThat(e.getTargetException()).isInstanceOf(IllegalStateException.class).hasMessageContaining("Utility class");
		}

	}
}

package ca.benfarhat.simplecrud;

/**
 * 
 * CacheConstant: contient le nom des caches a utiliser 
 * 
 * @author Benfarhat Elyes
 * @since 2021-01-01
 * @version 1.0.0
 *
 */
public class CacheConstant {

	public static final String CST_CACHE_TUTORIAL = "cacheTutorials";
	public static final String CST_CACHE_TUTORIAL_PUBLISHED = "cacheExpiry1000";
	public static final String CST_CACHE_TUTORIAL_BY_TITLE = "cacheExpiry500Ttl3";
	
	private CacheConstant() {
		throw new IllegalStateException("Utility class");
	}
}

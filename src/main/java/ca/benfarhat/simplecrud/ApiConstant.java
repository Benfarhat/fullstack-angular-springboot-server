package ca.benfarhat.simplecrud;

/**
 * 
 * ApiConstants: Contient les constantes du projet 
 * 
 * @author Benfarhat Elyes
 * @since 2020-12-30
 * @version 1.0.0
 *
 */

public final class ApiConstant {

	public static final String CST_API_VERSION = "/v1";
	public static final String CST_CONTEXT_PATH = "/api" + CST_API_VERSION;
	public static final String CST_TUTORIAL_CTX = "/tutorial";
	public static final String CST_AUTHENTICATE_CTX = "/authenticate";
	
	private ApiConstant() {
		throw new IllegalStateException("Utility class");
	}

}

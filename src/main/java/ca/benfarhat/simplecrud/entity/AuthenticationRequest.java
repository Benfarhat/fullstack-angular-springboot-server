package ca.benfarhat.simplecrud.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * AuthenticationRequest: Permet de recevoir sur l'endpoint d.authentification 
 * 
 * @author Benfarhat Elyes
 * @since 2021-01-03
 * @version 1.0.0
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationRequest {
	
    private String username;
    private String password;

}

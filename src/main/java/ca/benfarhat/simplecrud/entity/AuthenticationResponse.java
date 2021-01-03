package ca.benfarhat.simplecrud.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 
 * AuthenticationResponse: permet suite a authentification de retourner le token 
 * 
 * @author Benfarhat Elyes
 * @since 2021-01-03
 * @version 1.0.0
 *
 */

@Getter
@AllArgsConstructor
@Builder
public class AuthenticationResponse {

    private final String jwt;

}
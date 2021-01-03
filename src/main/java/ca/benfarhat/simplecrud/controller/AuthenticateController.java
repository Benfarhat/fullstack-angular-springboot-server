package ca.benfarhat.simplecrud.controller;

import static ca.benfarhat.simplecrud.ApiConstant.CST_AUTHENTICATE_CTX;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.benfarhat.simplecrud.entity.AuthenticationRequest;
import ca.benfarhat.simplecrud.entity.AuthenticationResponse;
import ca.benfarhat.simplecrud.service.AppUserDetailsService;
import ca.benfarhat.simplecrud.util.JwtUtil;

/**
 * 
 * AuthenticateController: API Endpoint dédié a l.authentification 
 * 
 * @author Benfarhat Elyes
 * @since 2021-01-03
 * @version 1.0.0
 *
 */

@RestController
@RequestMapping(value = CST_AUTHENTICATE_CTX)
public class AuthenticateController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	AppUserDetailsService userDetailsService;
	
	@Autowired
	JwtUtil jwtUtil;

	@PostMapping(path = "", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	public ResponseEntity<AuthenticationResponse> requestAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
		
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("Vos informations de connexions sont erronées");
		}
		
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String jwt = jwtUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
		
	}
}

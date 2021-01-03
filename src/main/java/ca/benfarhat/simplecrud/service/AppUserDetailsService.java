package ca.benfarhat.simplecrud.service;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 
 * AppUserDetailsService: Service pour la récupération des infos des utilisateurs 
 * 
 * @author Benfarhat Elyes
 * @since 2021-01-02
 * @version 1.0.0
 *
 */

@Service
public class AppUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return User.builder().username("user").password("pass").authorities(new ArrayList<>()).build();
	}

}

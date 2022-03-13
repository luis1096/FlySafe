package com.flysafe.FlySafe.Security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.flysafe.FlySafe.Database.DataStore.User;
import com.flysafe.FlySafe.Database.DataStore.UserRepository;
import com.flysafe.FlySafe.Database.DataFacade;

/**
 * Implementation of UserDetailsService to enable Spring Security.
 */
public class FlySafeUserDetailsService implements UserDetailsService {

	/**
	 * A UserRepository instance.
	 */
	@Autowired
	private UserRepository userRepo;
	
	/**
	 * Implementation of loadByUsername(), returns a FlySafeUserDetails instance if the user is found in the database.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {		
		Optional<User> optUser = userRepo.findByEmail(username);
		
	    if (!optUser.isPresent())
	        throw new UsernameNotFoundException("Could not locate account for " + username);
	    final User user = optUser.get();
	    return new FlySafeUserDetails(user);
	}

}

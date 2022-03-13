package com.flysafe.FlySafe.Security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.flysafe.FlySafe.Database.DataStore.User;

/**
 * Implementation of UserDetails to enable Spring Security.
 */
public class FlySafeUserDetails implements UserDetails {
	
	/**
	 * The user logged into the system.
	 */
	private User user;
	
	/**
	 * Parameterized constructor.
	 * 
	 * @param user The user to be logged into the system
	 */
	public FlySafeUserDetails(User user) {
		this.user = user;
	}
	
	/**
	 * Implementation of getAuthorities() method for Spring Security. Gives all users a simple "User" authority.
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("User");
        return Arrays.asList(authority);
	}

	/**
	 * Implementation of getPassword(), returns the password of the user.
	 */
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	/**
	 * Implementation of getUsername(), returns the email address of the user. 
	 */
	@Override
	public String getUsername() {
		return user.getEmail();
	}
	
	/**
	 * Method to get the logged-in user of the system.
	 * 
	 * @return The user logged into the system
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Implementation of isAccountNonExpired(), returns true.
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * Implementation of isAccountNonLocked(), returns true.
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * Implementation of isCredentialsNonExpried(), returns true.
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * Implementation of isEnabled(), returns true.
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}
}

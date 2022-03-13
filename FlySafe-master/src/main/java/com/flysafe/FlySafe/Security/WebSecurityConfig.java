package com.flysafe.FlySafe.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.zaxxer.hikari.HikariDataSource;

/**
 * Implementation of WebSecurityConfigurerAdapter to enable Spring Security.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	/**
	 * Method to obtain a FlySafeUserDetails object.
	 */
	@Bean
	public UserDetailsService userDetailsService() {
		return new FlySafeUserDetailsService();
	}
	
	/**
	 * The HikariDataSource instance.
	 */
	@Autowired
	private HikariDataSource dataSource;
	
	/**
	 * Method to log in a user through Spring Security.
	 * 
	 * @param authBuilder The AuthenticationManagerBuilder used to log the user in
	 */
	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder authBuilder) throws Exception {
		authBuilder.jdbcAuthentication().dataSource(dataSource)
		.passwordEncoder(passwordEncoder())
		.usersByUsernameQuery("SELECT email, password, enabled FROM users where email=?")
		;
	}

	/**
	 * Method invoked to get BCryptPasswordEncoder.
	 * 
	 * @return A BCryptPasswordEncoder object
	 */
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/**
	 * Method invoked to obtain a DaoAuthenticationProvider to enable Spring Security login.
	 * 
	 * @return A DaoAuthenticationProvider object
	 */
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}
	
	/**
	 * Implementation of configure(), does some magical things.
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}
	
	/**
	 * Method to configure access settings for the various web pages of the application. Configured to allow 
	 * access to the Login page and the Create Account page for non-authenticated users, and no restrictions 
	 * for users logged into the system.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/", "/createaccount", "/submitCreateAccountForm", "/autherror", "/CSS/FlySafe.css").permitAll()
		.antMatchers("/FlightPage/**", "/UserPage/**").permitAll().anyRequest().authenticated()
		.and()
	      .formLogin()
	      .loginPage("/login")
	      .permitAll()
	      .defaultSuccessUrl("/submitLoginForm", true)
	      .failureUrl("/login?error=true")
	      .and()
	      .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
	      .logoutSuccessUrl("/login?logout=true")
	      .permitAll();
	}
}

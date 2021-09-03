package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.demo.JwtFilter.CustomAuthenticationFailureHandler;
import com.example.demo.JwtFilter.CustomAuthenticationSuccessHandler;
import com.example.demo.JwtFilter.CustomWebAuthenticationDetailsSource;
import com.example.demo.JwtFilter.SpringFilter;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserPrincipalDetailsService;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomWebAuthenticationDetailsSource authenticationDetailsSource;

	@Autowired
	private UserPrincipalDetailsService userPrincipalDetailsService;

	@Autowired
	private UserRepository userRepository;


	@Autowired
	private SpringFilter requestFilter;

	@Autowired
	@Qualifier("authenticationProvider")
	private AuthenticationProvider authenticationProvider;


	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers("/login").permitAll().antMatchers("/Login").permitAll()
				.antMatchers("/checkLogin").permitAll().anyRequest().authenticated();

		http.csrf().disable()
		.formLogin().authenticationDetailsSource(authenticationDetailsSource)
		.loginPage("/login").permitAll()
		.loginProcessingUrl("/login")
		.failureForwardUrl("/login")
		.successHandler(new CustomAuthenticationSuccessHandler(userPrincipalDetailsService, userRepository))
		.failureHandler(new CustomAuthenticationFailureHandler())
		.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
		
		http.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@SuppressWarnings("deprecation")
	@Bean
	public PasswordEncoder encodePassword() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Bean
	public AuthenticationManager authenticateManager() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public AuthenticationSuccessHandler successRedirectHandler() {
		return new CustomAuthenticationSuccessHandler(userPrincipalDetailsService, userRepository);
	}

	// Handler deciding where to redirect user after failed login
	@Bean
	public CustomAuthenticationFailureHandler authenticationFailureHandler() {
		return new CustomAuthenticationFailureHandler();
	}

}

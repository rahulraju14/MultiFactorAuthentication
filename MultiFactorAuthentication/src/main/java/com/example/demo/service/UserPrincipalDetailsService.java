package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.UserInfo;
import com.example.demo.repository.UserRepository;

@Service("userDetailsService")
public class UserPrincipalDetailsService implements UserDetailsService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserPrincipalDetailsService.class);
	
	private UserRepository userRepository;

	public UserPrincipalDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
		UserInfo user = userRepository.findByEmailAddress(emailAddress);
		if (user == null) {
			LOGGER.error("User not Found for given email Address: ");
			throw new UsernameNotFoundException("User not Found for given email Address: ");
		} else {
			UserPrincipal userPrincipal = new UserPrincipal(user);
			return userPrincipal;
		}
	}
}
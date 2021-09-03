package com.example.demo.JwtFilter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.model.UserInfo;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserPrincipal;
import com.example.demo.service.UserPrincipalDetailsService;
import com.example.demo.util.JwtProperties;
import com.example.demo.util.JwtUtil;

@Component
public class SpringFilter extends OncePerRequestFilter {
	
	@Autowired
	private UserRepository userRepository;

	/*
	 * public SpringFilter(AuthenticationManager authenticationManager,
	 * UserRepository userRepo) { super(authenticationManager); this.userRepository
	 * = userRepo; // TODO Auto-generated constructor stub }
	 */

	@Autowired
	private JwtUtil jwt;

//	@Autowired
//	private CustomUserDetails userService;

	@Autowired
	private UserPrincipalDetailsService userService;
//	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String url = request.getRequestURI();
		System.out.println("Url { } " + url);
	
		if(SecurityContextHolder.getContext().getAuthentication() != null) {
			if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserPrincipal) {
				UserPrincipal obj = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				System.out.println("User Successfully Authenticated !!!!!" + obj.getUsername());
			}
		}
		filterChain.doFilter(request, response);
	}

}

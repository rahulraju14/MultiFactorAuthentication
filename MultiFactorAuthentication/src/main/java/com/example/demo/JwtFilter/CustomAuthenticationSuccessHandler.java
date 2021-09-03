package com.example.demo.JwtFilter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.aerogear.security.otp.api.Base32;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo.model.UserInfo;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserPrincipal;
import com.example.demo.service.UserPrincipalDetailsService;
import com.example.demo.util.JwtProperties;

@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	private UserPrincipalDetailsService userService;

	private UserRepository userRepo;

	public CustomAuthenticationSuccessHandler(UserPrincipalDetailsService userService, UserRepository userRepository) {
		this.userService = userService;
		this.userRepo = userRepository;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		LOGGER.info("onAuthenticationSuccess user {} ", authentication);
		UserInfo user = ((UserPrincipal) (authentication.getPrincipal())).getUser();
		System.out.println(user);
//		String successUrl = "http://localhost:9090/welcome";
//		response.setHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		redirectStrategy.sendRedirect(request, response, "/homePage");

	}

	@Override
	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

	@Override
	protected RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}

}

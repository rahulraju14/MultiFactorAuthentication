package com.example.demo.JwtFilter;

import org.jboss.aerogear.security.otp.Totp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.model.UserInfo;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserPrincipalDetailsService;

@Component("authenticationProvider")
public class TTCOAuthenticationProvider extends DaoAuthenticationProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(TTCOAuthenticationProvider.class);

	private UserPrincipalDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	@Qualifier("userDetailsService")

	@Override
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		// This sets the userDetailsService and passwordEncoder for the Authentication
		// manager
		this.userDetailsService = (UserPrincipalDetailsService) userDetailsService;
		super.setUserDetailsService(userDetailsService);
		super.setPasswordEncoder(passwordEncoder);
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		try {
			String verificationCode = ((CustomWebAuthenticationDetails) authentication.getDetails()).getVerificationCode();
			System.out.println("Verfication Code: "+ verificationCode);
			System.out.println("Email Address : " + authentication.getName());
			System.out.println("Password : " + authentication.getCredentials());
			
			UserInfo currentUser = userRepo.findByEmailAddress(authentication.getName());
			if(currentUser == null) {
				throw new BadCredentialsException("Invalid username or password");
			} else {
				if(currentUser.isUsing2FA()) {
					if(currentUser.getSecretKey() != null) {
						Totp totp = new Totp(currentUser.getSecretKey());
						 if (!isValidLong(verificationCode) || !totp.verify(verificationCode)) {
				                throw new BadCredentialsException("Invalid verfication code");
				            }
					} 
				}
			}
			LOGGER.info("successfully logged in: {}", authentication.getName());
			return super.authenticate(authentication);

		} catch (BadCredentialsException exception) {
			LOGGER.error("BadCredentialsException occured: {}", exception.getMessage());
			throw exception;
		}
	}
	
	private boolean isValidLong(String code) {
        try {
            Long.parseLong(code);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}

package com.example.demo.JwtFilter;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {
	private static final long serialVersionUID = 398905717534279945L;

	private boolean verificationField;

	public CustomAuthenticationToken(Object principal, Object credentials,
			Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
		// TODO Auto-generated constructor stub
	}

	public boolean isVerificationField() {
		return verificationField;
	}

	public void setVerificationField(boolean verificationField) {
		this.verificationField = verificationField;
	}

}

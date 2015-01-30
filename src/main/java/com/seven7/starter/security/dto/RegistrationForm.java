package com.seven7.starter.security.dto;

import java.io.Serializable;

import com.seven7.starter.security.enumeration.SocialMediaService;

public class RegistrationForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1381154002897820142L;

	private String email;

	private String firstName;

	private String lastName;

	private SocialMediaService signInProvider;

	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public SocialMediaService getSignInProvider() {
		return this.signInProvider;
	}

	public boolean isNormalRegistration() {
		return signInProvider == null;
	}

	public String getPassword() {
		return this.password;
	}

	public void setSignInProvider(SocialMediaService signInProvider) {
		this.signInProvider = signInProvider;

	}

	public void setPassword(String password) {
		this.password = password;
	}

}

package com.seven7.starter.security.service.impl;

import com.seven7.starter.security.dto.RegistrationForm;
import com.seven7.starter.security.enumeration.SocialMediaService;

public class RegistrationFormBuilder {

	private final RegistrationForm registrationForm = new RegistrationForm();

	public RegistrationFormBuilder email(String registrationEmailAddress) {
		registrationForm.setEmail(registrationEmailAddress);
		return this;
	}

	public RegistrationFormBuilder firstName(String registrationFirstName) {
		registrationForm.setFirstName(registrationFirstName);
		return this;
	}

	public RegistrationFormBuilder lastName(String registrationLastName) {
		registrationForm.setLastName(registrationLastName);
		return this;
	}

	public RegistrationFormBuilder isSocialSignInViaSignInProvider(
			SocialMediaService socialSignInProvider) {
		registrationForm.setSignInProvider(socialSignInProvider);
		return this;
	}

	public RegistrationForm build() {
		return registrationForm;
	}

}

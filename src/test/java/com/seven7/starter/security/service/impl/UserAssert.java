package com.seven7.starter.security.service.impl;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import com.seven7.starter.security.entity.User;
import com.seven7.starter.security.enumeration.Role;
import com.seven7.starter.security.enumeration.SocialMediaService;

public class UserAssert extends AbstractAssert<UserAssert, User> {

	private UserAssert(User actual) {
		super(actual, UserAssert.class);
	}

	public static UserAssert assertThatUser(User actual) {
		return new UserAssert(actual);
	}

	public UserAssert hasEmail(String email) {
		isNotNull();
		Assertions
				.assertThat(actual.getEmail())
				.overridingErrorMessage(
						"Expected email to be <%s> but was <%s>", email,
						actual.getEmail()).isEqualTo(email);

		return this;
	}

	public UserAssert hasFirstName(String firstName) {
		isNotNull();

		Assertions
				.assertThat(actual.getFirstName())
				.overridingErrorMessage(
						"Expected first name to be <%s> but was <%s>",
						firstName, actual.getFirstName()).isEqualTo(firstName);

		return this;
	}

	public UserAssert hasLastName(String lastName) {
		isNotNull();

		Assertions
				.assertThat(actual.getLastName())
				.overridingErrorMessage(
						"Expected last name to be <%s> but was <%s>", lastName,
						actual.getLastName()).isEqualTo(lastName);

		return this;
	}

	public UserAssert isRegisteredByUsingSignInProvider(
			SocialMediaService signInProvider) {
		isNotNull();

		Assertions
				.assertThat(actual.getSignInProvider())
				.overridingErrorMessage(
						"Expected signInProvider to be <%s> but was <%s>",
						signInProvider, actual.getSignInProvider())
				.isEqualTo(signInProvider);

		hasNoPassword();

		return this;
	}

	private void hasNoPassword() {
		isNotNull();

		Assertions
				.assertThat(actual.getPasswordHash())
				.overridingErrorMessage(
						"Expected password to be <null> but was <%s>",
						actual.getPasswordHash()).isNull();
	}

	public UserAssert isRegisteredUser() {
		isNotNull();

		Assertions
				.assertThat(actual.getRole())
				.overridingErrorMessage(
						"Expected role to be <ROLE_USER> but was <%s>",
						actual.getRole()).isEqualTo(Role.ROLE_USER);

		return this;
	}
}

package com.seven7.starter.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seven7.starter.security.dto.RegistrationForm;
import com.seven7.starter.security.entity.User;
import com.seven7.starter.security.exception.DuplicateEmailException;
import com.seven7.starter.security.repository.UserRepository;
import com.seven7.starter.security.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private PasswordEncoder passwordEncoder;

	private UserRepository repository;

	@Autowired
	public UserServiceImpl(PasswordEncoder passwordEncoder,
			UserRepository repository) {
		this.passwordEncoder = passwordEncoder;
		this.repository = repository;
	}

	@Transactional
	@Override
	public User registerNewUserAccount(RegistrationForm registrationForm)
			throws DuplicateEmailException {
		if (emailExist(registrationForm.getEmail())) {
			throw new DuplicateEmailException("The email address: "
					+ registrationForm.getEmail() + " is already in use.");
		}

		String encodedPassword = encodePassword(registrationForm);

		User registered = User.getBuilder().email(registrationForm.getEmail())
				.firstName(registrationForm.getFirstName())
				.lastName(registrationForm.getLastName())
				.password(encodedPassword)
				.signInProvider(registrationForm.getSignInProvider()).build();

		return repository.save(registered);
	}

	private boolean emailExist(String email) {
		User user = repository.findByEmail(email);

		if (user != null) {
			return true;
		}

		return false;
	}

	private String encodePassword(RegistrationForm registrationForm) {
		if (registrationForm.isNormalRegistration()) {
			return passwordEncoder.encode(registrationForm.getPassword());
		}

		return null;
	}
}

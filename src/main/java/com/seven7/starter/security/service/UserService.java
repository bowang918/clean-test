package com.seven7.starter.security.service;

import com.seven7.starter.security.dto.RegistrationForm;
import com.seven7.starter.security.entity.User;
import com.seven7.starter.security.exception.DuplicateEmailException;

public interface UserService {

	User registerNewUserAccount(RegistrationForm userAccountData) throws DuplicateEmailException;

}

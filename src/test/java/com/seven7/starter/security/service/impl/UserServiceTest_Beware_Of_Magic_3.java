package com.seven7.starter.security.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.seven7.starter.security.dto.RegistrationForm;
import com.seven7.starter.security.entity.User;
import com.seven7.starter.security.enumeration.Role;
import com.seven7.starter.security.enumeration.SocialMediaService;
import com.seven7.starter.security.exception.DuplicateEmailException;
import com.seven7.starter.security.repository.UserRepository;
import com.seven7.starter.security.service.UserService;
 
 
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest_Beware_Of_Magic_3 {
 
    private static final String REGISTRATION_EMAIL_ADDRESS = "john.smith@gmail.com";
    private static final String REGISTRATION_FIRST_NAME = "John";
    private static final String REGISTRATION_LAST_NAME = "Smith";
    private static final Role ROLE_REGISTERED_USER = Role.ROLE_USER;
    private static final SocialMediaService SOCIAL_SIGN_IN_PROVIDER = SocialMediaService.TWITTER;
 
    private UserService registrationService;
 
    @Mock
    private PasswordEncoder passwordEncoder;
 
    @Mock
    private UserRepository repository;
 
    @Before
    public void setUp() {
        registrationService = new UserServiceImpl(passwordEncoder, repository);
    }
 
 
    @Test
    public void registerNewUserAccount_SocialSignInAndUniqueEmail_ShouldCreateNewUserAccountAndSetSignInProvider() throws DuplicateEmailException       {
        RegistrationForm registration = new RegistrationForm();
        registration.setEmail(REGISTRATION_EMAIL_ADDRESS);
        registration.setFirstName(REGISTRATION_FIRST_NAME);
        registration.setLastName(REGISTRATION_LAST_NAME);
        registration.setSignInProvider(SOCIAL_SIGN_IN_PROVIDER);
 
        when(repository.findByEmail(REGISTRATION_EMAIL_ADDRESS)).thenReturn(null);
 
        when(repository.save(isA(User.class))).thenAnswer(new Answer<User>() {
            @Override
            public User answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                return (User) arguments[0];
            }
        });
 
        User createdUserAccount = registrationService.registerNewUserAccount(registration);
 
        assertEquals(REGISTRATION_EMAIL_ADDRESS, createdUserAccount.getEmail());
        assertEquals(REGISTRATION_FIRST_NAME, createdUserAccount.getFirstName());
        assertEquals(REGISTRATION_LAST_NAME, createdUserAccount.getLastName());
        assertEquals(SOCIAL_SIGN_IN_PROVIDER, createdUserAccount.getSignInProvider());
        assertEquals(ROLE_REGISTERED_USER, createdUserAccount.getRole());
        assertNull(createdUserAccount.getPasswordHash());
 
        verify(repository, times(1)).findByEmail(REGISTRATION_EMAIL_ADDRESS);
        verify(repository, times(1)).save(createdUserAccount);
        verifyNoMoreInteractions(repository);
        verifyZeroInteractions(passwordEncoder);
    }
}

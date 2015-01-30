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
public class UserServiceTest_Naming_Matters_2 {
 
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
    public void registerNewUserAccount_SocialSignInAndUniqueEmail_ShouldCreateNewUserAccountAndSetSignInProvider() throws DuplicateEmailException {
        RegistrationForm registration = new RegistrationForm();
        registration.setEmail("john.smith@gmail.com");
        registration.setFirstName("John");
        registration.setLastName("Smith");
        registration.setSignInProvider(SocialMediaService.TWITTER);
 
        when(repository.findByEmail("john.smith@gmail.com")).thenReturn(null);
 
        when(repository.save(isA(User.class))).thenAnswer(new Answer<User>() {
            @Override
            public User answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                return (User) arguments[0];
            }
        });
 
        User createdUserAccount = registrationService.registerNewUserAccount(registration);
 
        assertEquals("john.smith@gmail.com", createdUserAccount.getEmail());
        assertEquals("John", createdUserAccount.getFirstName());
        assertEquals("Smith", createdUserAccount.getLastName());
        assertEquals(SocialMediaService.TWITTER, createdUserAccount.getSignInProvider());
        assertEquals(Role.ROLE_USER, createdUserAccount.getRole());
        assertNull(createdUserAccount.getPasswordHash());
 
        verify(repository, times(1)).findByEmail("john.smith@gmail.com");
        verify(repository, times(1)).save(createdUserAccount);
        verifyNoMoreInteractions(repository);
        verifyZeroInteractions(passwordEncoder);
    }
}

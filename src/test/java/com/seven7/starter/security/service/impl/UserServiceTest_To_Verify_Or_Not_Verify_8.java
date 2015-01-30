package com.seven7.starter.security.service.impl;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static com.seven7.starter.security.service.impl.UserAssert.assertThatUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.seven7.starter.security.dto.RegistrationForm;
import com.seven7.starter.security.entity.User;
import com.seven7.starter.security.enumeration.SocialMediaService;
import com.seven7.starter.security.exception.DuplicateEmailException;
import com.seven7.starter.security.repository.UserRepository;
import com.seven7.starter.security.service.UserService;
 
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest_To_Verify_Or_Not_Verify_8 {
 
    private static final String REGISTRATION_EMAIL_ADDRESS = "john.smith@gmail.com";
    private static final String REGISTRATION_FIRST_NAME = "John";
    private static final String REGISTRATION_LAST_NAME = "Smith";
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
    public void registerNewUserAccount_SocialSignInAndDuplicateEmail_ShouldThrowException() throws DuplicateEmailException {
        RegistrationForm registration = new RegistrationFormBuilder()
                .email(REGISTRATION_EMAIL_ADDRESS)
                .firstName(REGISTRATION_FIRST_NAME)
                .lastName(REGISTRATION_LAST_NAME)
                .isSocialSignInViaSignInProvider(SOCIAL_SIGN_IN_PROVIDER)
                .build();
 
        when(repository.findByEmail(REGISTRATION_EMAIL_ADDRESS)).thenReturn(new User());
 
        catchException(registrationService).registerNewUserAccount(registration);
 
        assertThat((Exception)caughtException()).isExactlyInstanceOf(DuplicateEmailException.class);
    }
 
    @Test
    public void registerNewUserAccount_SocialSignInAndDuplicateEmail_ShouldNotSaveNewUserAccount() throws DuplicateEmailException {
        RegistrationForm registration = new RegistrationFormBuilder()
                .email(REGISTRATION_EMAIL_ADDRESS)
                .firstName(REGISTRATION_FIRST_NAME)
                .lastName(REGISTRATION_LAST_NAME)
                .isSocialSignInViaSignInProvider(SOCIAL_SIGN_IN_PROVIDER)
                .build();
 
        when(repository.findByEmail(REGISTRATION_EMAIL_ADDRESS)).thenReturn(new User());
 
        catchException(registrationService).registerNewUserAccount(registration);
 
        verify(repository, never()).save(isA(User.class));
    }
 
    @Test
    public void registerNewUserAccount_SocialSignInAndUniqueEmail_ShouldSaveNewUserAccountAndSetSignInProvider() throws DuplicateEmailException {
        RegistrationForm registration = new RegistrationFormBuilder()
                .email(REGISTRATION_EMAIL_ADDRESS)
                .firstName(REGISTRATION_FIRST_NAME)
                .lastName(REGISTRATION_LAST_NAME)
                .isSocialSignInViaSignInProvider(SOCIAL_SIGN_IN_PROVIDER)
                .build();
 
        when(repository.findByEmail(REGISTRATION_EMAIL_ADDRESS)).thenReturn(null);
 
        registrationService.registerNewUserAccount(registration);
 
        ArgumentCaptor<User> userAccountArgument = ArgumentCaptor.forClass(User.class);
        verify(repository, times(1)).save(userAccountArgument.capture());
 
        User createdUserAccount = userAccountArgument.getValue();
 
        assertThatUser(createdUserAccount)
                .hasEmail(REGISTRATION_EMAIL_ADDRESS)
                .hasFirstName(REGISTRATION_FIRST_NAME)
                .hasLastName(REGISTRATION_LAST_NAME)
                .isRegisteredUser()
                .isRegisteredByUsingSignInProvider(SOCIAL_SIGN_IN_PROVIDER);
    }
 
 
    @Test
    public void registerNewUserAccount_SocialSignInAndUniqueEmail_ShouldReturnCreatedUserAccount() throws DuplicateEmailException {
        RegistrationForm registration = new RegistrationFormBuilder()
                .email(REGISTRATION_EMAIL_ADDRESS)
                .firstName(REGISTRATION_FIRST_NAME)
                .lastName(REGISTRATION_LAST_NAME)
                .isSocialSignInViaSignInProvider(SOCIAL_SIGN_IN_PROVIDER)
                .build();
 
        when(repository.findByEmail(REGISTRATION_EMAIL_ADDRESS)).thenReturn(null);
 
        when(repository.save(isA(User.class))).thenAnswer(new Answer<User>() {
            @Override
            public User answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                return (User) arguments[0];
            }
        });
 
        User createdUserAccount = registrationService.registerNewUserAccount(registration);
 
        assertThatUser(createdUserAccount)
                .hasEmail(REGISTRATION_EMAIL_ADDRESS)
                .hasFirstName(REGISTRATION_FIRST_NAME)
                .hasLastName(REGISTRATION_LAST_NAME)
                .isRegisteredUser()
                .isRegisteredByUsingSignInProvider(SOCIAL_SIGN_IN_PROVIDER);
    }
 
    @Test
    public void registerNewUserAccount_SocialSignInAnUniqueEmail_ShouldNotCreateEncodedPasswordForUser() throws DuplicateEmailException {
        RegistrationForm registration = new RegistrationFormBuilder()
                .email(REGISTRATION_EMAIL_ADDRESS)
                .firstName(REGISTRATION_FIRST_NAME)
                .lastName(REGISTRATION_LAST_NAME)
                .isSocialSignInViaSignInProvider(SOCIAL_SIGN_IN_PROVIDER)
                .build();
 
        when(repository.findByEmail(REGISTRATION_EMAIL_ADDRESS)).thenReturn(null);
 
        registrationService.registerNewUserAccount(registration);
 
        verifyZeroInteractions(passwordEncoder);
    }
}

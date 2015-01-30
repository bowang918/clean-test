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
public class UserServiceTest_Start_From_Do_Not_Use_Inheritance_1 {
 
    private UserService userService;
 
    @Mock
    private PasswordEncoder passwordEncoderMock;
 
    @Mock
    private UserRepository repositoryMock;
 
    @Before
    public void setUp() {
        userService = new UserServiceImpl(passwordEncoderMock, repositoryMock);
    }
 
 
    @Test
    public void registerNewUserAccountByUsingSocialSignIn() throws DuplicateEmailException {
        RegistrationForm form = new RegistrationForm();
        form.setEmail("john.smith@gmail.com");
        form.setFirstName("John");
        form.setLastName("Smith");
        form.setSignInProvider(SocialMediaService.TWITTER);
 
        when(repositoryMock.findByEmail("john.smith@gmail.com")).thenReturn(null);
         
        when(repositoryMock.save(isA(User.class))).thenAnswer(new Answer<User>() {
            @Override
            public User answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                return (User) arguments[0];
            }
        });
 
        User modelObject = userService.registerNewUserAccount(form);
 
        assertEquals("john.smith@gmail.com", modelObject.getEmail());
        assertEquals("John", modelObject.getFirstName());
        assertEquals("Smith", modelObject.getLastName());
        assertEquals(SocialMediaService.TWITTER, modelObject.getSignInProvider());
        assertEquals(Role.ROLE_USER, modelObject.getRole());
        assertNull(modelObject.getPasswordHash());
 
        verify(repositoryMock, times(1)).findByEmail("john.smith@gmail.com");
        verify(repositoryMock, times(1)).save(modelObject);
        verifyNoMoreInteractions(repositoryMock);
        verifyZeroInteractions(passwordEncoderMock);
    }
}

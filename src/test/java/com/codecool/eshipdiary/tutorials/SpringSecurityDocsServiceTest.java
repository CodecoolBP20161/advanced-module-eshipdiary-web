package com.codecool.eshipdiary.tutorials;
// source:
// http://docs.spring.io/spring-security/site/docs/4.0.x/reference/htmlsingle/#test-method

import com.codecool.eshipdiary.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
@WebAppConfiguration
public class SpringSecurityDocsServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void deleteUserUanauthenticated() {
        userRepository.delete(1L);

    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(authorities = { "USER" })
    public void deleteUserWithUnauthorizedUser() {
        userRepository.delete(1L);

    }

    @Test(expected = SpelEvaluationException.class)
    @WithMockUser
    public void findAllActiveUsersWithoutClubSet() {
        userRepository.findAllActives();

    }

    @Test
    @WithUserDetails
    public void findAllActiveUsersWithClubSet() {
        userRepository.findAllActives();

    }


}

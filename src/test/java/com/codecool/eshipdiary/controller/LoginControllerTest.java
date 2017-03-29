package com.codecool.eshipdiary.controller;

import com.codecool.AnonymousAuthMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(PowerMockRunner.class)
@PrepareForTest(SecurityContextHolder.class)
@ContextConfiguration
public class LoginControllerTest {
    private MockMvc mockMvc;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private LoginController loginController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
        PowerMockito.mockStatic(SecurityContextHolder.class);
        Authentication auth = AnonymousAuthMock.withDefaultAuth();

        BDDMockito.given(SecurityContextHolder.getContext()).willReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(auth);
    }

    @Test
    public void login() throws Exception {
        mockMvc.perform(get("/login")).andExpect(view().name("login_page"));
    }
}
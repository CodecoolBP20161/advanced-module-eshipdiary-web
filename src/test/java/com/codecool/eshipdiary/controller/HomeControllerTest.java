package com.codecool.eshipdiary.controller;

import com.codecool.AnonymousAuthMock;
import com.codecool.MockAuthentication;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(PowerMockRunner.class)
@PrepareForTest(SecurityContextHolder.class)
@ContextConfiguration
public class HomeControllerTest {

    private MockMvc mockMvc;
    private AnonymousAuthMock authentication;
    private HashSet<GrantedAuthority> authorities;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private HomeController homeController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();

        PowerMockito.mockStatic(SecurityContextHolder.class);
        BDDMockito.given(SecurityContextHolder.getContext()).willReturn(securityContext);
        authorities = new HashSet<>();
    }

    @Test
    public void indexWithAdmin() throws Exception {
        authorities.add(new SimpleGrantedAuthority("ADMIN"));
        authentication = AnonymousAuthMock.withAddedAuth(authorities);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        mockMvc.perform(get("/")).andExpect(redirectedUrl("/users"));
    }

    @Test
    public void indexWithNotAdmin() throws Exception {
        authentication = AnonymousAuthMock.withDefaultAuth();
        when(securityContext.getAuthentication()).thenReturn(authentication);
        mockMvc.perform(get("/")).andExpect(view().name("index"));
    }
}
package com.codecool.eshipdiary.controller;

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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

/**
 * Created by hamargyuri on 2017. 03. 19..
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SecurityContextHolder.class)
@ContextConfiguration
public class HomeControllerTest {
    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private HomeController homeController;

    private MockMvc mockMvc;

    private Authentication authentication;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();

        PowerMockito.mockStatic(SecurityContextHolder.class);
        BDDMockito.given(SecurityContextHolder.getContext()).willReturn(securityContext);

    }

    @Test
    public void indexWithAdmin() throws Exception {
        authentication = new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                Collection<GrantedAuthority> authorities = new HashSet<>();
                authorities.add(new SimpleGrantedAuthority("ADMIN"));
                return authorities;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean b) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return null;
            }
        };
        when(securityContext.getAuthentication()).thenReturn(authentication);
        assertEquals("redirect:/users", homeController.index());
    }

}
package com.codecool.eshipdiary.config;


import com.codecool.eshipdiary.filter.ApiAuthenticationFilter;
import com.codecool.eshipdiary.filter.IsActiveFilter;
import com.codecool.eshipdiary.security.AjaxAuthHandler;
import com.codecool.eshipdiary.security.AuthFailureHandler;
import com.codecool.eshipdiary.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ErrorAttributes errorAttributes;

    @Autowired
    private AuthFailureHandler authFailureHandler;

    @Autowired
    private ApiAuthenticationFilter apiAuthenticationFilter;

    @Autowired
    private IsActiveFilter isActiveFilter;

    @Autowired
    private AjaxAuthHandler ajaxAuthHandler;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsServiceImpl)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/api_login").permitAll()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()

                    .and()
                .formLogin()
                .failureHandler(authFailureHandler)
                .loginPage("/login")
                .permitAll()

                    .and()
                .logout()
                .deleteCookies("remember-me")
                .permitAll()

                    .and()
                .rememberMe()

                    .and()
                .addFilterBefore(apiAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(isActiveFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(ajaxAuthHandler)

                    .and()
                .csrf().disable();
    }
}

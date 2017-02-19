package com.codecool.eshipdiary.service;

import com.codecool.eshipdiary.model.Role;
import com.codecool.eshipdiary.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger LOG = LoggerFactory.getLogger(UserDetailsServiceImpl.class);


    @Autowired
    private UserRepositoryService userRepositoryService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepositoryService.getUserByUserName(username);
        LOG.info("User for authentication by name {} ", user.get().getUserName());

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : user.get().getRoles()){
            LOG.info("Role of the user for authentication {} ", role.getName());
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return new org.springframework.security.core.userdetails.User(user.get().getUserName(), user.get().getPasswordHash(), grantedAuthorities);
    }
}

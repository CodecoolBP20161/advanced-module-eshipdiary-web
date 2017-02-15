package com.codecool.eshipdiary.service;

import com.codecool.eshipdiary.model.User;
import com.codecool.eshipdiary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserRepositoryService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUserById(long id) {
        return Optional.ofNullable(userRepository.findOne(id));
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findOneByEmailAddress(email);
    }

    public Optional<User> getUserByUserName(String userName) {
        return userRepository.findOneByUserName(userName);
    }

    public Collection<User> getAllUsers() {
        return userRepository.findAll(new Sort("email"));
    }


}

package com.codecool.eshipdiary.service;

import com.codecool.eshipdiary.model.User;
import com.codecool.eshipdiary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Service
@Transactional
public class UserRepositoryService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUserById(long id) {
        return Optional.ofNullable(userRepository.findOne(id));
    }

    public Optional<User> getUserByUserName(String userName) {
        return userRepository.findOneByUserName(userName);
    }

    public Optional<User> getUserByAPIKey(String APIKey) {
        return userRepository.findOneByApiKey(APIKey);
    }

    public Optional<User> getUserByEmailAddress(String emailAddress) {
        return userRepository.findOneByEmailAddress(emailAddress);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void create(User user) { userRepository.save(user); }

    public boolean isEmailAddressTaken(String emailAddress, Long id){
        Optional<User> match = getUserByEmailAddress(emailAddress);
        return match.isPresent() && !match.get().getId().equals(id);
    }

    public boolean isUserNameTaken(String userName, Long id){
        Optional<User> match = getUserByUserName(userName);
        return match.isPresent() && !match.get().getId().equals(id);
    }
}

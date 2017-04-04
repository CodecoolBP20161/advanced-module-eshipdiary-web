package com.codecool.eshipdiary.service;

import com.codecool.eshipdiary.model.PasswordResetToken;
import com.codecool.eshipdiary.model.User;
import com.codecool.eshipdiary.repository.PasswordTokenRepository;
import com.codecool.eshipdiary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserRepositoryService {

    @Autowired
    private UserRepository userRepository;

    @Autowired PasswordTokenRepository passwordTokenRepository;

    public Optional<User> getUserById(long id) {
        return Optional.ofNullable(userRepository.findOne(id));
    }

    public Optional<User> getUserByUserName(String userName) {
        return userRepository.findOneByUserName(userName);
    }

    public Optional<User> getUserByApiToken(String APIKey) {
        return userRepository.findOneByApiToken(APIKey);
    }

    public Optional<User> getUserByEmailAddress(String emailAddress) {
        return userRepository.findOneByEmailAddress(emailAddress);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void create(User user) { userRepository.save(user); }

    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);
    }
}

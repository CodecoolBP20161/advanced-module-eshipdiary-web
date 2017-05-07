package com.codecool.eshipdiary.service;

import com.codecool.eshipdiary.model.Club;
import com.codecool.eshipdiary.model.PasswordResetToken;
import com.codecool.eshipdiary.model.User;
import com.codecool.eshipdiary.repository.PasswordTokenRepository;
import com.codecool.eshipdiary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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

    public Optional<User> getUserByUsername(String userName) {
        return userRepository.findOneByUsername(userName);
    }

    public Optional<User> getUserByApiToken(String token) {
        return userRepository.findOneByApiToken(token);
    }

    public Optional<User> getUserByEmailAddress(String emailAddress) {
        return userRepository.findOneByEmailAddress(emailAddress);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Iterable<User> getUsersEligibleForRental() {
        // - is active
        // - has no ongoing rental
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Club club = userRepository.findByUsername(userName).getClub();
        return userRepository.findByActiveTrueAndOnWaterFalseAndClub(club);
    }

    public void save(User user) { userRepository.save(user); }

    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);
    }

    public User getCurrentUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUserByUsername(userName).map(u -> u).orElse(new User());
    }

    public boolean userIsAvailable(User user) {
        if (user.isActive() && !user.isOnWater()) {
            return true;
        }
        return false;
    }

    public List<User> availableUsersFrom(List<User> users) {
        List<User> availables = new ArrayList<>();
        for (User user : users) {
            if (userIsAvailable(user)) {
                availables.add(user);
            }
        }
        return availables;
    }
}

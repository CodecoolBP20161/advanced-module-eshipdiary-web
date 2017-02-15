package com.codecool.eshipdiary.dao;

import com.codecool.eshipdiary.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserDao {

    Optional<User> getUserById(long id);

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserByUserName(String userName);

    Collection<User> getAllUsers();

// todo: create() method for saving logic;

}

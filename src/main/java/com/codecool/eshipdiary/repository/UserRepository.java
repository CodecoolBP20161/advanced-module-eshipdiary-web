package com.codecool.eshipdiary.repository;

import com.codecool.eshipdiary.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByEmailAddress(String email);
    Optional<User> findOneByUserName(String userName);
}

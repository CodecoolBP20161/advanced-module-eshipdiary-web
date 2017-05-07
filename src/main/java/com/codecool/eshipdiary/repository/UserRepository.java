package com.codecool.eshipdiary.repository;

import com.codecool.eshipdiary.model.Club;
import com.codecool.eshipdiary.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.Optional;

@Repository
@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserRepository extends CrudRepository<User, Long> {
    @Override
    @RestResource(exported = true)
    @Query("select user from User user where user.club = ?#{principal.club}")
    Iterable<User> findAll();
    User findByUsername(String userName);
    Optional<User> findOneByUsername(String userName);
    Optional<User> findOneByApiToken(String APIKey);
    Optional<User> findOneByEmailAddress(String emailAddress);

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    void delete(User user);

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    void delete(Long id);

    Iterable<User> findByActiveTrueAndOnWaterFalseAndClub(Club club);

    @Query("select user from User user where user.club = ?#{principal.club} and user.active = true")
    Iterable<User> findAllActives();
    @Query("select user from User user where user.club = ?#{principal.club} and user.active = false")
    Iterable<User> findAllInactives();
    @Query("select user from User user where user.club = ?#{principal.club} and user.member = false")
    Iterable<User> findAllNonMembers();
}

package com.codecool.eshipdiary.repository;

import com.codecool.eshipdiary.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserRepository extends CrudRepository<User, Long> {
    @Override
    @RestResource(exported = true)
    @Query("select user from User user where user.club = ?#{principal.club}")
    Iterable<User> findAll();
    User findByUserName(String userName);
    Optional<User> findOneByUserName(String userName);
    Optional<User> findOneByApiToken(String APIKey);
    Optional<User> findOneByEmailAddress(String emailAddress);

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    void delete(User user);

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    void delete(Long id);
}

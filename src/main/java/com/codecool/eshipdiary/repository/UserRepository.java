package com.codecool.eshipdiary.repository;

import com.codecool.eshipdiary.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findOneByEmailAddress(String email);
    Optional<User> findOneByUserName(String userName);
    Optional<User> findOneByApiKey(String APIKey);
}

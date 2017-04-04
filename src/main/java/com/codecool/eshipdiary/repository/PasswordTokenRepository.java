package com.codecool.eshipdiary.repository;

import com.codecool.eshipdiary.model.PasswordResetToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordTokenRepository extends CrudRepository<PasswordResetToken, Long>{
    Optional<PasswordResetToken> findOneByToken(String token);

}

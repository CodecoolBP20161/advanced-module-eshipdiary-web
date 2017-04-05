package com.codecool.eshipdiary.repository;

import com.codecool.eshipdiary.model.PasswordResetToken;
import com.codecool.eshipdiary.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface PasswordTokenRepository extends CrudRepository<PasswordResetToken, Long>{
    Optional<PasswordResetToken> findOneByToken(String token);

    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUser(User user);

    Stream<PasswordResetToken> findAllByExpiryDateLessThan(Date now);

    void deleteByExpiryDateLessThan(Date now);

    @Modifying
    @Query("delete from PasswordResetToken t where t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date now);

}

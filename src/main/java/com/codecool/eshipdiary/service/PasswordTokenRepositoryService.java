package com.codecool.eshipdiary.service;

import com.codecool.eshipdiary.model.PasswordResetToken;
import com.codecool.eshipdiary.repository.PasswordTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class PasswordTokenRepositoryService {

    @Autowired
    private PasswordTokenRepository passwordTokenRepository;

    public Optional<PasswordResetToken> getTokenByToken(String token) {
        return passwordTokenRepository.findOneByToken(token);
    }
}

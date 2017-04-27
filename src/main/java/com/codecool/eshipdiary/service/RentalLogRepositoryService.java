package com.codecool.eshipdiary.service;

import com.codecool.eshipdiary.model.Oar;
import com.codecool.eshipdiary.model.RentalLog;
import com.codecool.eshipdiary.model.Ship;
import com.codecool.eshipdiary.repository.RentalLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;


@Service
@Transactional
public class RentalLogRepositoryService {

    @Autowired
    private RentalLogRepository rentalLogRepository;

    public Optional<RentalLog> getRentalLogById(long id) {
        return Optional.ofNullable(rentalLogRepository.findOne(id));
    }

    public Iterable<RentalLog> getAllRentalLogs() {
        return rentalLogRepository.findAll();
    }

    @Transactional
    public void save(RentalLog rentalLog) { rentalLogRepository.save(rentalLog); }


}

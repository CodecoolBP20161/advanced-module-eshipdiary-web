package com.codecool.eshipdiary.service;

import com.codecool.eshipdiary.model.Oar;
import com.codecool.eshipdiary.model.RentalLog;
import com.codecool.eshipdiary.model.Ship;
import com.codecool.eshipdiary.repository.RentalLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class RentalLogRepositoryService {

    @Autowired
    private OarRepositoryService oarRepositoryService;

    @Autowired
    private ShipRepositoryService shipRepositoryService;

    @Autowired
    private RentalLogRepository rentalLogRepository;

    public Optional<RentalLog> getRentalLogById(long id) {
        return Optional.ofNullable(rentalLogRepository.findOne(id));
    }

    public Iterable<RentalLog> getAllRentalLogs() {
        return rentalLogRepository.findAll();
    }

    public void save(RentalLog rentalLog) { rentalLogRepository.save(rentalLog); }

    public void finalize(RentalLog original, RentalLog finalDetails) {
        original.setRentalEnd(new Date());

        if (finalDetails.getComment() != null) {
            original.setComment(finalDetails.getComment());
        }

        if (finalDetails.getInjuredShip() != null) {
            Ship injuredShip = finalDetails.getInjuredShip();
            injuredShip.setActive(false);
            shipRepositoryService.save(injuredShip);
            original.setInjuredShip(injuredShip);
        }

        if (finalDetails.getInjuredOars() != null) {
            for (Oar oar : finalDetails.getInjuredOars()) {
                oar.setActive(false);
                oarRepositoryService.save(oar);
            }
            original.setInjuredOars(finalDetails.getInjuredOars());
        }
        save(original);
    }
}

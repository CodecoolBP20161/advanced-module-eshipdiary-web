package com.codecool.eshipdiary.service;

import com.codecool.eshipdiary.model.Oar;
import com.codecool.eshipdiary.model.RentalLog;
import com.codecool.eshipdiary.model.Ship;
import com.codecool.eshipdiary.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RentalService {
    @Autowired
    RentalLogRepositoryService rentalLogRepositoryService;

    @Autowired
    ShipRepositoryService shipRepositoryService;

    @Autowired
    OarRepositoryService oarRepositoryService;

    @Autowired
    UserRepositoryService userRepositoryService;


    public void setOnWaterForInvolvedItemsIn(RentalLog log, Boolean bool){
        log.getChosenShip().setOnWater(bool);
        if (log.getCox() != null) {
            log.getCox().setOnWater(bool);
        }
        for (Oar oar : log.getOars()) {
            oar.setOnWater(bool);
        }
        for (User user : log.getCrew()) {
            user.setOnWater(bool);
        }
    }

    public void copyCurrentlyAvailableItems(RentalLog oldLog, RentalLog newLog) {
        newLog.setRentalPeriod(oldLog.getRentalPeriod());
        newLog.setItinerary(oldLog.getItinerary());
        newLog.setDistance(oldLog.getDistance());
        newLog.setCrew(userRepositoryService.availableUsersFrom(oldLog.getCrew()));
        newLog.setOars(oarRepositoryService.availableOarsFrom(oldLog.getOars()));

        if (shipRepositoryService.shipIsAvailable(oldLog.getChosenShip())) {
            newLog.setChosenShip(oldLog.getChosenShip());
        }

        if (oldLog.getCox() != null) {
            if (userRepositoryService.userIsAvailable(oldLog.getCox())) {
                newLog.setCox(oldLog.getCox());
            }
        }
    }

    @Transactional
    public void finalize(RentalLog original, RentalLog finalDetails) {
        original.setRentalEnd(new Date());

        if (finalDetails.getComment() != null) {
            original.setComment(finalDetails.getComment());
            original.setDistance(finalDetails.getDistance());
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
        rentalLogRepositoryService.save(original);
        setOnWaterForInvolvedItemsIn(original, false);
    }
}

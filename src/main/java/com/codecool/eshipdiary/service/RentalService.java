package com.codecool.eshipdiary.service;

import com.codecool.eshipdiary.controller.RentalController;
import com.codecool.eshipdiary.exception.RentalCannotBeSaved;
import com.codecool.eshipdiary.model.Oar;
import com.codecool.eshipdiary.model.RentalLog;
import com.codecool.eshipdiary.model.Ship;
import com.codecool.eshipdiary.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RentalService {
    private static final Logger LOG = LoggerFactory.getLogger(RentalService.class);

    @Autowired
    RentalLogRepositoryService rentalLogRepositoryService;

    @Autowired
    ShipRepositoryService shipRepositoryService;

    @Autowired
    OarRepositoryService oarRepositoryService;

    @Autowired
    UserRepositoryService userRepositoryService;

    public void saveIfItemsAreAvailable(RentalLog rentalLog) throws RentalCannotBeSaved {
        boolean everythingIsAvailable;
        everythingIsAvailable = !rentalLog.getChosenShip().isOnWater();
        for (Oar oar : rentalLog.getOars()) {
            if (oar.isOnWater()) everythingIsAvailable = false;
        }
        for (User user : rentalLog.getCrew()) {
            if (user.isOnWater()) everythingIsAvailable = false;
        }
        if (everythingIsAvailable) {
            rentalLogRepositoryService.save(rentalLog);
            setOnWaterForInvolvedItemsIn(rentalLog, true);
        }
        else throw new RentalCannotBeSaved("Some of the items to save are in another active rental.");
    }

    void setOnWaterForInvolvedItemsIn(RentalLog log, Boolean bool){
        log.getChosenShip().setOnWater(bool);
        shipRepositoryService.save(log.getChosenShip());
        if (log.getCox() != null) {
            log.getCox().setOnWater(bool);
            userRepositoryService.save(log.getCox());
        }
        for (Oar oar : log.getOars()) {
            oar.setOnWater(bool);
            oarRepositoryService.save(oar);
        }
        for (User user : log.getCrew()) {
            user.setOnWater(bool);
            userRepositoryService.save(user);
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
}

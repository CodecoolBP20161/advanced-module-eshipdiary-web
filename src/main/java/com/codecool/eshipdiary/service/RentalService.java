package com.codecool.eshipdiary.service;

import com.codecool.eshipdiary.exception.RentalCannotBeSaved;
import com.codecool.eshipdiary.model.Oar;
import com.codecool.eshipdiary.model.RentalLog;
import com.codecool.eshipdiary.model.Ship;
import com.codecool.eshipdiary.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;

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

    public void saveIfItemsAreAvailable(RentalLog rentalLog) throws RentalCannotBeSaved {
        boolean everythingIsAvailable;
        ArrayList<Ship> availableShips = (ArrayList<Ship>) shipRepositoryService.getAvailableShips();
        everythingIsAvailable =  availableShips.contains(rentalLog.getChosenShip());
        for (Oar oar : rentalLog.getOars()) {
            if (oar.isOnWater() || !oar.isActive()) everythingIsAvailable = false;
        }
        for (User user : rentalLog.getCrew()) {
            if (user.isOnWater() || !user.isActive()) everythingIsAvailable = false;
        }
        if (everythingIsAvailable) {
            rentalLogRepositoryService.save(rentalLog);
            setOnWaterForInvolvedItemsIn(rentalLog, true);
        }
        else throw new RentalCannotBeSaved();
    }

    private void setOnWaterForInvolvedItemsIn(RentalLog log, Boolean bool){
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

        if (shipRepositoryService.isShipAvailable(oldLog.getChosenShip())) {
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

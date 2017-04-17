package com.codecool.eshipdiary.service;

import com.codecool.eshipdiary.model.Oar;
import com.codecool.eshipdiary.model.RentalLog;
import com.codecool.eshipdiary.model.Ship;
import com.codecool.eshipdiary.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RentalService {


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
}

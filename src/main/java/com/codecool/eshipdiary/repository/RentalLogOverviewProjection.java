package com.codecool.eshipdiary.repository;

import com.codecool.eshipdiary.model.RentalLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name="rentalOverview", types={RentalLog.class})
public interface RentalLogOverviewProjection {
    String getId();
    @Value("#{target.captain.lastName+' '+target.captain.firstName}")
    String getCaptain();
    @Value("#{target.chosenShip != null ? target.chosenShip.name : 'nincs hajó'}")
    String getShip();
    String getRentalStart();
    String getRentalPeriod();
    @Value("#{target.cox != null ? target.cox.lastName+' '+target.cox.firstName : 'nincs kormányos'}")
    String getCox();
    String getDistance();
    boolean getFinalized();
}
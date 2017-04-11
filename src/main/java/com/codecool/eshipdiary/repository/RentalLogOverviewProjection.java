package com.codecool.eshipdiary.repository;

import com.codecool.eshipdiary.model.RentalLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;

@Projection(name="rentalOverview", types={RentalLog.class})
public interface RentalLogOverviewProjection {
    String getId();
    @Value("#{target.captain.lastName+' '+target.captain.firstName}")
    String getCaptain();
    @Value("#{target.chosenShip != null ? target.chosenShip.name : 'nincs hajó'}")
    String getShip();
    @Value("#{target.getFormattedDate(target.rentalStart)}")
    String getRentalStart();
    @Value("#{target.rentalEnd != null ? target.getFormattedDate(target.rentalEnd):''}")
    String getRentalEnd();
    @Value("#{target.cox != null ? target.cox.lastName+' '+target.cox.firstName : 'nincs kormányos'}")
    String getCox();
    String getItinerary();
    String getComment();
    String getCrewNames();
    @Value("#{(target.injuredShip != null) || (!target.injuredOars.isEmpty())}")
    Boolean isInjury();
}
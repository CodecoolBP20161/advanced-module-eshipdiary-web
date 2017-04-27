package com.codecool.eshipdiary.repository;

import com.codecool.eshipdiary.model.Ship;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name="shipOverview", types={Ship.class})
public interface ShipOverviewProjection {
    String getId();
    String getName();
    @Value("#{target.subType != null ? target.subType.code : 'Nincs típus'}")
    String getSubType();
    @Value("#{target.subType != null ? target.subType.maxSeat : 'Nincs adat'}")
    String getMaxSeat();
    boolean getActive();
    @Value("#{target.owner != null ? target.owner.lastName+' '+target.owner.firstName : target.club.name}")
    String getOwner();
    @Value("#{target.category.displayName}")
    String getCategory();
    int getRentalCount();
}

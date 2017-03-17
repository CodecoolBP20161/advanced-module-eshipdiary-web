package com.codecool.eshipdiary.repository;

import com.codecool.eshipdiary.model.Ship;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name="shipOverview", types={Ship.class})
public interface ShipOverviewProjection {
    String getId();
    String getName();
    @Value("#{target.type != null ? target.type.name : 'Nincs típus'}")
    String getShipType();
    boolean getActive();
    @Value("#{target.owner != null ? target.owner.lastName+' '+target.owner.firstName : 'Klub tulajdona'}")
    String getOwner();
    @Value("#{target.category.displayName}")
    String getCategory();
    String getMaxSeat();
}

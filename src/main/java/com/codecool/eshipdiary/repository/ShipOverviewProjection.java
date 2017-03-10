package com.codecool.eshipdiary.repository;

import com.codecool.eshipdiary.model.Ship;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name="shipOverview", types={Ship.class})
public interface ShipOverviewProjection {
    String getId();
    String getName();
    String getShipType();
    boolean getActive();
    @Value("#{target.owner != null ? target.owner.lastName+' '+target.owner.firstName : 'Klub tulajdona'}")
    String getOwner();
    String getCategory();
    String getMaxSeat();
}

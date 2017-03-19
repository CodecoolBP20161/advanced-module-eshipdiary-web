package com.codecool.eshipdiary.repository;

import com.codecool.eshipdiary.model.Oar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name="oarOverview", types={Oar.class})
public interface OarOverviewProjection {
    String getId();
    String getName();
    @Value("#{target.type != null ? target.type.name : 'Nincs típus'}")
    String getType();
    boolean getActive();
    @Value("#{target.owner != null ? target.owner.lastName+' '+target.owner.firstName : target.club.name}")
    String getOwner();
}

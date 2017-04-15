package com.codecool.eshipdiary.repository;

import com.codecool.eshipdiary.model.ShipType;
import com.codecool.eshipdiary.model.SubType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;

@Projection(name="shipTypeOverview", types={ShipType.class})
public interface ShipTypeOverviewProjection {
    String getId();
    String getName();
    @Value("#{target.subTypes}")
    List<SubType> getSubTypes();
}

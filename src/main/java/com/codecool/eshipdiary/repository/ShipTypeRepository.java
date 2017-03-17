package com.codecool.eshipdiary.repository;


import com.codecool.eshipdiary.model.ShipType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RepositoryRestResource(collectionResourceRel = "shipType", path = "shipType")
public interface ShipTypeRepository extends CrudRepository<ShipType, Long> {
    Optional<ShipType> findOneById(Long id);

    @Override
    @RestResource(exported = true)
    @Query("select shipType from ShipType shipType where shipType.club = ?#{principal.club}")
    Iterable<ShipType> findAll();
}

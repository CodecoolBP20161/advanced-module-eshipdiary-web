package com.codecool.eshipdiary.repository;


import com.codecool.eshipdiary.model.Ship;
import com.codecool.eshipdiary.model.ShipType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RepositoryRestResource(collectionResourceRel = "ship", path = "ship")
public interface ShipRepository extends CrudRepository<Ship, Long> {
    Optional<Ship> findOneById(Long id);
    @Override
    @RestResource(exported = true)
    @Query("select ship from Ship ship where ship.club = ?#{principal.club}")
    Iterable<Ship> findAll();
    Iterable<Ship> findAllByType(ShipType type);
}

package com.codecool.eshipdiary.repository;


import com.codecool.eshipdiary.model.ShipSize;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RepositoryRestResource(collectionResourceRel = "shipSize", path = "shipSize")
public interface ShipSizeRepository extends CrudRepository<ShipSize, Long> {
    Optional<ShipSize> findOneById(Long id);
    @Override
    @RestResource(exported = true)
    @Query("select shipSize from ShipSize shipSize where shipSize.club = ?#{principal.club}")
    Iterable<ShipSize> findAll();
}

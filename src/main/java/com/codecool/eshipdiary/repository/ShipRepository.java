package com.codecool.eshipdiary.repository;


import com.codecool.eshipdiary.model.Ship;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RepositoryRestResource(collectionResourceRel = "ship", path = "ship")
public interface ShipRepository extends CrudRepository<Ship, Long> {
    Optional<Ship> findOneById(Long id);
}

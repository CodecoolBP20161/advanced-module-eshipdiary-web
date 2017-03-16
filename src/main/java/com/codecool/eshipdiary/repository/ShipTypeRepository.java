package com.codecool.eshipdiary.repository;


import com.codecool.eshipdiary.model.ShipType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RepositoryRestResource(collectionResourceRel = "shipType", path = "shipType")
public interface ShipTypeRepository extends CrudRepository<ShipType, Long> {
    Optional<ShipType> findOneById(Long id);
}

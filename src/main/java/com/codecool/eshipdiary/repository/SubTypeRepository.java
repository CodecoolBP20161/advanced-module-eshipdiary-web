package com.codecool.eshipdiary.repository;

import com.codecool.eshipdiary.model.ShipType;
import com.codecool.eshipdiary.model.SubType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@RepositoryRestResource(collectionResourceRel = "subType", path = "subType")
public interface SubTypeRepository extends CrudRepository<SubType, Long> {
    Optional<SubType> findOneById(Long id);
    Iterable<SubType> findAll();
    Iterable<SubType> findAllByType(ShipType type);
}
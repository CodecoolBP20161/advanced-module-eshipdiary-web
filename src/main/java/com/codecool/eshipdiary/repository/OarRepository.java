package com.codecool.eshipdiary.repository;


import com.codecool.eshipdiary.model.Oar;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RepositoryRestResource(collectionResourceRel = "oar", path = "oar")
public interface OarRepository extends CrudRepository<Oar, Long> {
    Optional<Oar> findOneByName(String name);
    Optional<Oar> findOneByType(String type);
}

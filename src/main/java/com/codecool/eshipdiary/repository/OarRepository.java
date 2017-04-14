package com.codecool.eshipdiary.repository;


import com.codecool.eshipdiary.model.Oar;
import com.codecool.eshipdiary.model.ShipType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RepositoryRestResource(collectionResourceRel = "oar", path = "oar")
public interface OarRepository extends CrudRepository<Oar, Long> {
    Optional<Oar> findOneById(Long id);
    @Override
    @RestResource(exported = true)
    @Query("select oar from Oar oar where oar.club = ?#{principal.club}")
    Iterable<Oar> findAll();
    Iterable<Oar> findAllByType(ShipType type);

}

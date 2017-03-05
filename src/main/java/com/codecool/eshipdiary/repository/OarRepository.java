package com.codecool.eshipdiary.repository;


import com.codecool.eshipdiary.model.Oar;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(collectionResourceRel = "oar", path = "oar")
public interface OarRepository extends CrudRepository<Oar, Long> {
    Oar findOneById(Long id);
}

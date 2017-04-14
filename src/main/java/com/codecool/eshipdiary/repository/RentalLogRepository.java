package com.codecool.eshipdiary.repository;


import com.codecool.eshipdiary.model.RentalLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RepositoryRestResource(collectionResourceRel = "rental", path = "rental")
public interface RentalLogRepository extends CrudRepository<RentalLog, Long> {
    @Override
    @Query("select rentalLog from RentalLog rentalLog where rentalLog.club = ?#{principal.club}")
    Iterable<RentalLog> findAll();
}

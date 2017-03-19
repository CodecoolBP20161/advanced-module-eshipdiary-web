package com.codecool.eshipdiary.repository;


import com.codecool.eshipdiary.model.RentalLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(collectionResourceRel = "rental", path = "rental")
public interface RentalLogRepository extends JpaRepository<RentalLog, Long> {
}

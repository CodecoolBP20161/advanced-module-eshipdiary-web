package com.codecool.eshipdiary.repository;


import com.codecool.eshipdiary.model.RentalLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(collectionResourceRel = "rental", path = "rental")
public interface RentalLogRepository extends CrudRepository<RentalLog, Long> {
    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    @Query("select rentalLog from RentalLog rentalLog where rentalLog.club = ?#{principal.club}")
    Iterable<RentalLog> findAll();


//    TODO: query to be fixed, it ignores all rows where cox is null
//    @Query("select rentalLog " +
//            "from RentalLog rentalLog " +
//            "join rentalLog.crew crew " +
//            "where rentalLog.club = ?#{principal.club} " +
//            "and (crew.userName = ?#{principal.username} " +
//            "or (rentalLog.cox is null or rentalLog.cox.userName = ?#{principal.username}) " +
//            "or rentalLog.captain.userName = ?#{principal.username})")
    @Query("select rentalLog from RentalLog rentalLog where rentalLog.club = ?#{principal.club}")
    Iterable<RentalLog> findForPrincipal();
}

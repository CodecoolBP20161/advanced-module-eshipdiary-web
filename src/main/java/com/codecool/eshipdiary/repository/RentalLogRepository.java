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


    @Query(nativeQuery = true,
            value = "select rl.* " +
                    "from rental_log rl, club cl, users u, rental_log_crew cr " +
                    "where " +
                    "cr.rental_logs_id = rl.id AND " +
                    "rl.club_id = cl.id AND " +
                    "( u.id = rl.captain_id OR " +
                    "u.id = rl.cox_id OR " +
                    "cr.crew_id = u.id) AND " +
                    "u.user_name = ?#{principal.userName} " +
                    "group by rl.id")
    Iterable<RentalLog> findForPrincipal();
}

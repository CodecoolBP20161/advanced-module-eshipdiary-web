package com.codecool.eshipdiary.repository;

import com.codecool.eshipdiary.model.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    Club findByName(String name);
}

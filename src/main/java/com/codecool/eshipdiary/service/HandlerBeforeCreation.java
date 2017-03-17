package com.codecool.eshipdiary.service;

import com.codecool.eshipdiary.model.*;
import com.codecool.eshipdiary.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
@Slf4j
public class HandlerBeforeCreation {
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private Club club;

    @Autowired
    public HandlerBeforeCreation(UserRepository userRepository, ClubRepository clubRepository) {
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;
    }




    @HandleBeforeCreate(Ship.class)
    public void setShipClubUsingSecurityContext(Ship ship) {
        determineClubOfCurrentUser();
        log.debug("This ship belongs to club " + club.getName());
        ship.setClub(club);
    }

    @HandleBeforeCreate(User.class)
    public void setUserClubUsingSecurityContext(User user) {
        determineClubOfCurrentUser();
        log.debug("This user belongs to club " + club.getName());
        user.setClub(club);
    }

    @HandleBeforeCreate(Oar.class)
    public void setOarClubUsingSecurityContext(Oar oar) {
        determineClubOfCurrentUser();
        log.debug("This oar belongs to club " + club.getName());
        oar.setClub(club);
    }

    @HandleBeforeCreate(ShipSize.class)
    public void setShipSizeClubUsingSecurityContext(ShipSize shipSize) {
        determineClubOfCurrentUser();
        log.debug("This size of ship belongs to club " + club.getName());
        shipSize.setClub(club);
    }

    @HandleBeforeCreate(ShipType.class)
    public void setShipTypeClubUsingSecurityContext(ShipType shipType) {
        determineClubOfCurrentUser();
        log.debug("This size of ship belongs to club " + club.getName());
        shipType.setClub(club);
    }

    private void determineClubOfCurrentUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug("Found principal: " + userName);
        club = userRepository.findByUserName(userName).getClub();
        log.debug("Current principal's club is " + club.getName());
        if (club == null) {
            Club newClub = new Club();
            newClub.setName("unknown");
            clubRepository.save(newClub);
            club = newClub;
        }
    }
}

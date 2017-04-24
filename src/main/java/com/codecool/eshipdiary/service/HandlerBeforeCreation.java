package com.codecool.eshipdiary.service;

import com.codecool.eshipdiary.model.*;
import com.codecool.eshipdiary.repository.ClubRepository;
import com.codecool.eshipdiary.repository.RoleRepository;
import com.codecool.eshipdiary.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Component
@RepositoryEventHandler
@Slf4j
public class HandlerBeforeCreation {
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final UserRepositoryService userRepositoryService;
    private Club club;
    private HttpServletRequest context;

    @Autowired
    public HandlerBeforeCreation(UserRepository userRepository,
                                 ClubRepository clubRepository,
                                 RoleRepository roleRepository,
                                 EmailService emailService,
                                 UserRepositoryService userRepositoryService,
                                 HttpServletRequest httpServletRequest) {
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.userRepositoryService = userRepositoryService;
        this.context = httpServletRequest;
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

        Role userRole = roleRepository.findOneByName("USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }
        user.setRole(userRole);


    }

    @HandleAfterCreate(User.class)
    public void welcomeUser(User user) {
        String token = UUID.randomUUID().toString(); // generate token
        String link = getAppUrl(context) + "/reset-password/" + token;
        log.info("constructed password reset link: " + link);
        userRepositoryService.createPasswordResetTokenForUser(user, token); // save token to user in DB
        emailService.sendEmail(emailService.prepareRegistrationEmail(user, link));
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

    @HandleBeforeCreate(RentalLog.class)
    public void setOarClubUsingSecurityContext(RentalLog rentalLog) {
        determineClubOfCurrentUser();
        log.debug("This oar belongs to club " + club.getName());
        rentalLog.setClub(club);
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

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}

package com.codecool.eshipdiary;

import com.codecool.eshipdiary.model.*;
import com.codecool.eshipdiary.repository.ClubRepository;
import com.codecool.eshipdiary.repository.RoleRepository;
import com.codecool.eshipdiary.repository.ShipRepository;
import com.codecool.eshipdiary.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.sql.Date;

@SpringBootApplication
@EnableAsync
public class AdvancedModuleEshipdiaryWebApplication {
    private static final Logger LOG = LoggerFactory.getLogger(AdvancedModuleEshipdiaryWebApplication.class);


    public static void main(String[] args) {
        SpringApplication.run(AdvancedModuleEshipdiaryWebApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(UserRepository userRepository, ClubRepository clubRepository, ShipRepository shipRepository, RoleRepository roleRepository) {
        return (args) -> {

            Role admin = new Role();
            admin.setName("ADMIN");
            roleRepository.save(admin);

            Club bee = new Club();
            bee.setName("BEE");
            clubRepository.save(bee);

            Club dnhe = new Club();
            dnhe.setName("DNHE");
            clubRepository.save(dnhe);

            User ex = new User();
            ex.setFirstName("bee");
            ex.setLastName("admin");
            ex.setUserName("admin");
            ex.setBirthDate(Date.valueOf("1989-01-01"));
            ex.setActive(true);
            ex.setEmailAddress("admin@yahoo.com");
            ex.setPasswordHash("password");
            ex.setPhoneNumber("22021043");
            ex.setKnowledgeLevel(User.KnowledgeLevel.BEGINNER);
            ex.setWeightInKg(80);
            ex.setRole(admin);
            ex.setClub(bee);
            userRepository.save(ex);

            User dek = new User();
            dek.setFirstName("dnhe");
            dek.setLastName("admin");
            dek.setUserName("admin2");
            dek.setBirthDate(Date.valueOf("1989-01-01"));
            dek.setActive(true);
            dek.setEmailAddress("admin2@yahoo.com");
            dek.setPasswordHash("password");
            dek.setPhoneNumber("22021043");
            dek.setKnowledgeLevel(User.KnowledgeLevel.BEGINNER);
            dek.setWeightInKg(80);
            dek.setRole(admin);
            dek.setClub(dnhe);
            userRepository.save(dek);


            Role userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);

            User user = new User();
            user.setFirstName("first");
            user.setLastName("user");
            user.setUserName("user");
            user.setBirthDate(Date.valueOf("1900-01-01"));
            user.setActive(true);
            user.setEmailAddress("user@yahoo.com");
            user.setPasswordHash("");
            user.setPhoneNumber("62426180");
            user.setKnowledgeLevel(User.KnowledgeLevel.ADVANCED);
            user.setWeightInKg(90);
            user.setRole(userRole);
            user.setClub(bee);
            userRepository.save(user);

            Ship ship = new Ship();
            ship.setName("Atlanta");
            ship.setCategory(Ship.Category.TRAINING);
            ship.setCode("4x+");
            ship.setCoxed(true);
            ship.setMaxSeat(4);
            ship.setShipType(Type.SCULL);
            shipRepository.save(ship);


            LOG.info("Users found with findAll():");
            LOG.info("-------------------------------");
            for (User u : userRepository.findAll()) {
                LOG.info(u.toString());
            }
        };
    }
}

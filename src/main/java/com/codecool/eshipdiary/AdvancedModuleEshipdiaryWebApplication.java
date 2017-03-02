package com.codecool.eshipdiary;

import com.codecool.eshipdiary.model.Club;
import com.codecool.eshipdiary.model.Role;
import com.codecool.eshipdiary.model.User;
import com.codecool.eshipdiary.repository.ClubRepository;
import com.codecool.eshipdiary.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class AdvancedModuleEshipdiaryWebApplication {
    private static final Logger LOG = LoggerFactory.getLogger(AdvancedModuleEshipdiaryWebApplication.class);


    public static void main(String[] args) {
        SpringApplication.run(AdvancedModuleEshipdiaryWebApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(UserRepository userRepository, ClubRepository clubRepository) {
        return (args) -> {

            Role admin = new Role();
            admin.setName("ADMIN");
            Set<Role> roles = new HashSet<>();
            roles.add(admin);

            Club bee = new Club();
            bee.setName("BEE");
            clubRepository.save(bee);

            User ex = new User();
            ex.setFirstName("first");
            ex.setLastName("admin");
            ex.setUserName("admin");
            ex.setBirthDate("19890101");
            ex.setActive(true);
            ex.setEmailAddress("admin@yahoo.com");
            ex.setPasswordHash("password");
            ex.setPhoneNumber(22021043);
            ex.setKnowledgeLevel(User.KnowledgeLevel.BEGINNER);
            ex.setWeightInKg(80);
            ex.setRoles(roles);
            ex.setClub(bee);
            userRepository.save(ex);

            Role userRole = new Role();
            userRole.setName("USER");
            Set<Role> userRoles = new HashSet<>();
            userRoles.add(userRole);
            User user = new User();
            user.setFirstName("first");
            user.setLastName("user");
            user.setUserName("user");
            user.setBirthDate("19000101");
            user.setActive(true);
            user.setEmailAddress("user@yahoo.com");
            user.setPasswordHash("userpwd");
            user.setPhoneNumber(62426180);
            user.setKnowledgeLevel(User.KnowledgeLevel.ADVANCED);
            user.setWeightInKg(90);
            user.setRoles(userRoles);
            user.setClub(bee);
            userRepository.save(user);


            LOG.info("Users found with findAll():");
            LOG.info("-------------------------------");
            for (User u : userRepository.findAll()) {
                LOG.info(u.toString());
            }
        };
    }
}
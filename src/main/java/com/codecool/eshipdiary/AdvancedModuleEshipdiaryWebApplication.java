package com.codecool.eshipdiary;

import com.codecool.eshipdiary.model.User;
import com.codecool.eshipdiary.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AdvancedModuleEshipdiaryWebApplication {
    private static final Logger log = LoggerFactory.getLogger(AdvancedModuleEshipdiaryWebApplication.class);


    public static void main(String[] args) {
        SpringApplication.run(AdvancedModuleEshipdiaryWebApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner demo(UserRepository userRepository) {
//        return (args) -> {
//
//            User ex = new User();
//            ex.setFirstName("first");
//            ex.setLastName("rower");
//            ex.setUserName("rower_1");
//            ex.setBirthDate("19890101");
//            ex.setActive(true);
//            ex.setEmailAddress("firstuser@yahoo.com");
//            ex.setPasswordHash("jflsfd");
//            ex.setPhoneNumber(22021043);
//            ex.setKnowledgeLevel(User.KnowledgeLevel.BEGINNER);
//            ex.setWeightInKg(80);
//            userRepository.save(ex);
//
//            User user = new User();
//            user.setFirstName("second");
//            user.setLastName("rower");
//            user.setUserName("rower_2");
//            user.setBirthDate("19000101");
//            user.setActive(true);
//            user.setEmailAddress("seconduser@yahoo.com");
//            user.setPasswordHash("kdlslsla");
//            user.setPhoneNumber(62426180);
//            user.setKnowledgeLevel(User.KnowledgeLevel.ADVANCED);
//            user.setWeightInKg(90);
//            userRepository.save(user);
//
//
//            log.info("Users found with findAll():");
//            log.info("-------------------------------");
//            for (User u : userRepository.findAll()) {
//                log.info(u.toString());
//            }
//        };
//    }
}

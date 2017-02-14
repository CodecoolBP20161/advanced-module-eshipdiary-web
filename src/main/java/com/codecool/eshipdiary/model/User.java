package com.codecool.eshipdiary.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Table(name = "`USER`")
public class User {

    public enum KnowledgeLevel {
        BEGINNER, MEDIUM, ADVANCED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true)
    private String APIKey;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false, unique = true)
    private String emailAddress;

    @Column(nullable = false)
    private @JsonIgnore String passwordHash;

    @Column
    private Date birthDate;

    @Column(nullable = false)
    private int phoneNumber;

    @Column
    private KnowledgeLevel knowledgeLevel;

    @Column
    private int weightInKg;

    @Column(nullable = false)
    private boolean isActive;

    private enum role {
        ADMIN, USER
    } // separate class instead of enum based on spring security settings



    public void setBirthDate(String birthDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        this.birthDate = Date.valueOf(LocalDate.parse(birthDate, formatter));
    }



}

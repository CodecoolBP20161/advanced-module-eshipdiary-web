package com.codecool.eshipdiary.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude="roles")
@ToString(exclude = {"roles", "passwordHash"})
@Entity
@Table(name = "`user`")
public class User {

    public enum KnowledgeLevel {
        BEGINNER, MEDIUM, ADVANCED
    }

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public void setBirthDate(String birthDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        this.birthDate = Date.valueOf(LocalDate.parse(birthDate, formatter));
    }

    public void setPasswordHash(String rawPassword) {
        this.passwordHash = PASSWORD_ENCODER.encode(rawPassword);
    }

}

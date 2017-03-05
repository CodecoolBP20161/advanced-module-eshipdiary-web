package com.codecool.eshipdiary.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;
import java.util.UUID;


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
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String apiKey;

    @NotEmpty(message = "A mező nem lehet üres")
    @Column(nullable = false)
    private String firstName;

    @NotEmpty(message = "A mező nem lehet üres")
    @Column(nullable = false)
    private String lastName;

    @NotEmpty(message = "A mező nem lehet üres")
    @Size(min = 3, message = "A felhasználónév legalább 3 karakter")
    @Column(nullable = false, unique = true)
    private String userName;

    @NotEmpty(message = "A mező nem lehet üres")
    @Column(nullable = false, unique = true)
    private String emailAddress;

    @Column
    private @JsonIgnore String passwordHash;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Nem megfelelő dátum")
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @NotNull(message = "A mező nem lehet üres")
    @Column(nullable = false)
    private int phoneNumber;

    @Column
    private KnowledgeLevel knowledgeLevel;

    @Column
    private int weightInKg;

    @Column(nullable = false)
    private boolean active;

    @ManyToOne
    private Club club;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @PrePersist
    private void validate(){
        this.apiKey = UUID.randomUUID().toString();
        if(this.passwordHash == null){
            this.setPasswordHash(this.userName);
        }
    }

    public void setPasswordHash(String rawPassword) {
        if(rawPassword == ""){
            rawPassword = this.userName;
        }
        this.passwordHash = PASSWORD_ENCODER.encode(rawPassword);
    }

}

package com.codecool.eshipdiary.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.UUID;



@Data
@ToString(exclude = {"ships", "oars", "passwordHash", "rentalLogs"})
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String apiToken;

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
    private String phoneNumber;

    @Column
    private KnowledgeLevel knowledgeLevel;

    @Column
    private int weightInKg;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private boolean member;

    @JsonIgnore
    @ManyToOne
    private Club club;

    @JsonIgnore
    @ManyToOne
    private Role role;

    @OneToMany(mappedBy = "owner")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Ship> ships;

    @OneToMany(mappedBy = "owner")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Oar> oars;

    @ManyToMany(mappedBy = "crew")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<RentalLog> rentalLogs;

    @PrePersist
    private void validate(){
        this.apiToken = UUID.randomUUID().toString();
        if(this.passwordHash == null){
            this.setPasswordHash(this.userName);
        }
    }

    public void setPasswordHash(String rawPassword) {
        if(rawPassword.equals("")){
            rawPassword = this.userName;
        }
        this.passwordHash = PASSWORD_ENCODER.encode(rawPassword);
    }

    public User(){
        this.active = true;
        this.member = true;
    }

    public enum KnowledgeLevel {
        BEGINNER("kezdő"),
        MEDIUM("haladó"),
        ADVANCED("gyakorlott");

        private final String displayName;

        KnowledgeLevel(String displayName) {
            this.displayName = displayName;
        }
        public String getDisplayName() {
            return displayName;
        }
    }

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

}

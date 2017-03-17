package com.codecool.eshipdiary.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Data
@Entity
public class RentalLog {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Ship chosenShip;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date rentalStart;

    @Column
    private int rentalPeriod;

    @ManyToOne
    private User cox;

    @ManyToMany
    private List<User> crew;

    @ManyToMany
    private List<Oar> oars;

    @Column
    private String itinerary;

    @Column
    private int distance;

    @Column
    private String comment;

    @PrePersist
    public void setRentalStart() {
        this.rentalStart = new Date();
    }

}

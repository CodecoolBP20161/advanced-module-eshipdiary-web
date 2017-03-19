package com.codecool.eshipdiary.model;

import lombok.Data;

import javax.persistence.*;
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
    private int rentalPeriod;

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

}

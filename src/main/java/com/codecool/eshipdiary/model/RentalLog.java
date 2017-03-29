package com.codecool.eshipdiary.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    @ManyToOne
    private User captain;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date rentalStart;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date rentalEnd;

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
    private boolean finalized;

    @Column(columnDefinition="TEXT")
    private String comment;

    @Column(columnDefinition="TEXT")
    private String adminComment;

    public RentalLog() {
        this.rentalStart = new Date();
        this.rentalPeriod = 120;
    }

    public String  getFormattedDate(Date date) {
        return new SimpleDateFormat("yyyy. MM. dd. HH:mm").format(date);
    }

    public String getCrewNames() {
        String names = "";
        for (User crewMember : crew) {
            String name = crewMember.getLastName() + " " + crewMember.getFirstName() + ", ";
            names += name;
        }
        return names.substring(0, names.length()-2);
    }

}

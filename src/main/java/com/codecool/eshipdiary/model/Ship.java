package com.codecool.eshipdiary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.List;


@Entity
@Data
@ToString(exclude = {"enabledUsers"})
public class Ship {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotEmpty(message = "A mező nem lehet üres")
    private String name;

    @ManyToOne
    private ShipSize size;

    @Column
    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne(cascade = CascadeType.ALL)
    private SubType subType;

    @ManyToOne
    private User owner;

    @Column
    private String place;

    @Column
    private String notes;

    @Column(nullable = false)
    private boolean active;

    @Column
    private boolean onWater;

    @ManyToMany(mappedBy = "enabledShips")
    private List<User> enabledUsers;

    @JsonIgnore
    @ManyToOne
    private Club club;

    public enum Category {
        TRAINING("edzőhajó"),
        COMPETITION("versenyhajó"),
        TOP("TOP hajó"),
        TEACHING("oktatóhajó");

        private final String displayName;

        Category(String displayName) {
            this.displayName = displayName;
        }
        public String getDisplayName() {
            return displayName;
        }
    }

    @Formula("(select count(*) from rental_log where rental_log.chosen_ship_id = id)")
    int rentalCount;

    public Ship() {
        this.setActive(true);
    }

}

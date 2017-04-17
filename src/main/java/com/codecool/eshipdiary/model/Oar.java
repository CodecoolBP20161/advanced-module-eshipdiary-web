package com.codecool.eshipdiary.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

@Data
@Entity
public class Oar {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private ShipType type;

    @Column(nullable = false)
    @NotEmpty(message = "A mező nem lehet üres")
    private String name;

    @ManyToOne
    private User owner;

    @Column(nullable = false)
    private boolean active;

    @Column
    private boolean onWater = false;

    @JsonIgnore
    @ManyToOne
    private Club club;

    public Oar() {
        this.setActive(true);
    }
}

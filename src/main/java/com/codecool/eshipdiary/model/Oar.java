package com.codecool.eshipdiary.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Oar {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "A mező nem lehet üres")
    private Type type;

    @Column(nullable = false)
    @NotEmpty(message = "A mező nem lehet üres")
    private String name;

    @ManyToOne
    private User owner;

    @Column(nullable = false)
    private boolean active;

    @JsonIgnore
    @ManyToOne
    private Club club;

    public Oar() {
        this.setActive(true);
    }
}

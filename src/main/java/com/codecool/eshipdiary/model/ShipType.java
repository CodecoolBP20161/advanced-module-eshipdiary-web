package com.codecool.eshipdiary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class ShipType {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "A mező nem lehet üres")
    @Column(nullable = false)
    private String name;

    @JsonIgnore
    @ManyToOne
    private Club club;

    @Formula("(select count(*) from ship where ship.type_id = id)")
    int ships;

    @Formula("(select count(*) from oar where oar.type_id = id)")
    int oars;
}

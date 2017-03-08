package com.codecool.eshipdiary.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class ShipSize {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "A mező nem lehet üres")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "A mező nem lehet üres")
    @Column(nullable = false)
    private String minKg;

    @NotNull(message = "A mező nem lehet üres")
    @Column(nullable = false)
    private String maxKg;
}

package com.codecool.eshipdiary.model;


import lombok.Data;

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
    @NotNull(message = "A mező nem lehet üres")
    private String name;
}

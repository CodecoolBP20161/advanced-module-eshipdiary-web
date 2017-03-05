package com.codecool.eshipdiary.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Entity
@Data
public class Ship {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotEmpty(message = "A mező nem lehet üres")
    private String name;

    @Column(nullable = false)
    @NotEmpty(message = "A mező nem lehet üres")
    private String code;

    @Column
    @NotNull(message = "A mező nem lehet üres")
    @Min(message = "Nem lehet kisebb, mint egy", value = 1)
    private int maxSeat;

    @Column
    private boolean coxed;

    @Column
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column
    @NotNull(message = "A mező nem lehet üres")
    @Enumerated(EnumType.STRING)
    private Type shipType;

    @ManyToOne
    private User owner;

    @Column
    private String place;

    @Column
    private String notes;

    public enum Category {
        TRAINING, COMPETITION, TOP, TEACHING
    }

}

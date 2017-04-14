package com.codecool.eshipdiary.model;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class SubType {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column
    @NotNull(message = "A mező nem lehet üres")
    @Min(message = "Nem lehet kisebb, mint egy", value = 1)
    private int maxSeat;

    @Column
    private boolean coxed;

    @ManyToOne
    private ShipType type;

    public SubType() {
        this.setMaxSeat(1);
    }
}

package com.codecool.eshipdiary.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;

/**
 * Created by hamargyuri on 2017. 03. 03..
 */

@Entity
@Data
public class ShipType {

    private enum RecommendedOars {
        KAYAK, CANOE, SCULL, SWEEP, DRAGON
    }

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @NotEmpty(message = "A mező nem lehet üres")
    private String name;

    @Column(nullable = false)
    @NotEmpty(message = "A mező nem lehet üres")
    private String code;

    @Column
    @NotEmpty(message = "A mező nem lehet üres")
    @Min(message = "Nem lehet kisebb, mint egy", value = 1)
    private int maxSeat;

    @Column
    private boolean isCoxed;

    @Column
    private RecommendedOars recommendedOars;
}

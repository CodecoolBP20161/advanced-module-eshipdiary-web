package com.codecool.eshipdiary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

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
    @Min(message = "Nem lehet kisebb, mint egy", value = 1)
    @Column(nullable = false)
    private int minKg;

    @NotNull(message = "A mező nem lehet üres")
    @Min(message = "Nem lehet kisebb, mint egy", value = 1)
    @Column(nullable = false)
    private int maxKg;

    @JsonIgnore
    @ManyToOne
    private Club club;

    @Formula("(select count(*) from ship s where s.size_id = id)")
    int ships;
}

package com.codecool.eshipdiary.model;


import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@ToString(exclude = {"ships"})
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

    @OneToMany(mappedBy = "subType")
    @LazyCollection(LazyCollectionOption.FALSE)
    List<Ship> ships;

    @Formula("(select count(*) from ship where ship.sub_type_id = id)")
    int shipCount;

    public SubType() {
        this.setMaxSeat(1);
    }
}

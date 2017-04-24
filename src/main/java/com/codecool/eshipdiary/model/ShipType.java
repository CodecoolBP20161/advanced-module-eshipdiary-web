package com.codecool.eshipdiary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@ToString(exclude = {"subTypes", "oars"})
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

    @OneToMany(mappedBy = "type")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<SubType> subTypes;

    @OneToMany(mappedBy = "type")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Oar> oars;

    @Formula("(select count(*) from oar where oar.type_id = id)")
    int oarCount;
}

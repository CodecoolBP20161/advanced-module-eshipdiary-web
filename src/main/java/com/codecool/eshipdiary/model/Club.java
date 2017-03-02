package com.codecool.eshipdiary.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Club {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
}

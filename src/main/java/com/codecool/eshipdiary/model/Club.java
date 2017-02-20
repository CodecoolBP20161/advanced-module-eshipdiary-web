package com.codecool.eshipdiary.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Club {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    // not sure this is necessary, it makes the relation bidirectional
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<User> students;
}

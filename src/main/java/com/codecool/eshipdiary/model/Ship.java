package com.codecool.eshipdiary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;


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

//    @Column(nullable = false)
//    private String code;

    @ManyToOne
    private ShipSize size;

//    @Column
//    @NotNull(message = "A mező nem lehet üres")
//    @Min(message = "Nem lehet kisebb, mint egy", value = 1)
//    private int maxSeat;
//
//    @Column
//    private boolean coxed;

    @Column
    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne(cascade = CascadeType.ALL)
    private SubType subType;
//    @ManyToOne
//    private ShipType type;

    @ManyToOne
    private User owner;

    @Column
    private String place;

    @Column
    private String notes;

    @Column(nullable = false)
    private boolean active;

    @JsonIgnore
    @ManyToOne
    private Club club;

    public enum Category {
        TRAINING("edzőhajó"),
        COMPETITION("versenyhajó"),
        TOP("TOP hajó"),
        TEACHING("oktatóhajó");

        private final String displayName;

        Category(String displayName) {
            this.displayName = displayName;
        }
        public String getDisplayName() {
            return displayName;
        }
    }

    public Ship() {
//        this.setMaxSeat(1);
        this.setActive(true);
    }

}

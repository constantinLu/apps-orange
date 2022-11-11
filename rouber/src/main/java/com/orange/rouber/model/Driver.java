package com.orange.rouber.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

import static com.orange.rouber.model.Vehicle.State.ACTIVE;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private Long phoneNumber;

    private Float rating;

    @OneToMany(mappedBy = "driver", fetch = LAZY, cascade = ALL)
    private List<Vehicle> vehicles;

    @OneToMany(mappedBy = "assignedTo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Trip> trips;


    public Vehicle activeVehicle() {
        return vehicles.stream()
                .filter(v -> v.state.equals(ACTIVE))
                .findFirst()
                .orElseThrow();
    }
}

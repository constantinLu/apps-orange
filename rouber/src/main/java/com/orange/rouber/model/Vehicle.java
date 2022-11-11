package com.orange.rouber.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "vehicles")
public class Vehicle {

    public enum State {
        ACTIVE,
        INACTIVE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String brand;

    private String vin;

    private String licensePlate;

    private String color;

    private LocalDate registerDate;

    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    Driver driver;

    @Enumerated(EnumType.STRING)
    State state;
}

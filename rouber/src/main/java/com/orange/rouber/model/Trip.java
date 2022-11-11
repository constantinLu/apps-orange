package com.orange.rouber.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "trips")
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal price;

    private Float rating;

    private LocalDateTime startTrip;

    private LocalDateTime endTrip;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "start_lat")),
            @AttributeOverride(name = "y", column = @Column(name = "start_long"))
    })
    private Point startLocation;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "end_lat")),
            @AttributeOverride(name = "y", column = @Column(name = "end_long"))
    })
    private Point endLocation;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User requestedBy;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver assignedTo;

    @OneToOne
    private Payment payment;

}


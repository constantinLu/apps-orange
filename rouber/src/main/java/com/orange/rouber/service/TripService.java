package com.orange.rouber.service;

import com.orange.rouber.client.TripStatistics;
import com.orange.rouber.model.Trip;
import com.orange.rouber.repository.TripRepository;
import com.orange.rouber.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static java.time.temporal.ChronoUnit.*;

@Service
public class TripService {

    private final TripRepository tripRepository;

    private final UserRepository userRepository;

    private final DriverService driverService;

    private final PaymentService paymentService;

    public TripService(TripRepository tripRepository, UserRepository userRepository, DriverService driverService,
                       PaymentService paymentService) {
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
        this.driverService = driverService;
        this.paymentService = paymentService;
    }

    public void createTrip(Trip trip, Long userId) {
        final var user = userRepository.findById(userId).orElseThrow();
        trip.setRequestedBy(user);
        tripRepository.save(trip);
    }

    public List<Trip> readAvailableTrips() {
        return tripRepository.findByAssignedTo_IdIsNull();
    }

    public void startTrip(Long tripId, Long driverId) {
        final var assignedDriver = driverService.getDriver(driverId);
        final var currentTrip = tripRepository.findById(tripId);
        if (currentTrip.isPresent()) {
            final var trip = currentTrip.get();
            if (trip.getAssignedTo() == null) {
                trip.setAssignedTo(assignedDriver);
                trip.setStartTrip(LocalDateTime.now());
            } else {
                throw new ValidationException("Driver already assigned");
            }

            final var createdPayment = paymentService.createPayment(trip);
            trip.setPayment(createdPayment);
            tripRepository.save(trip);

        } else {
            throw new ValidationException("No trip available");
        }
    }

    public void endTrip(Long tripId, Long driverId) {
        final var currentTrip = tripRepository.findById(tripId).orElseThrow();
        Assert.isTrue(currentTrip.getAssignedTo().getId().equals(driverId), "Driver must be the same");
        paymentService.confirmPayment(currentTrip.getPayment().getId());

        currentTrip.setEndTrip(LocalDateTime.now());
        tripRepository.save(currentTrip);
    }

    public List<Trip> getDriverTrips(Long driverId) {
        return findByDriverId(driverId);
    }

    public Trip getDriverRatingsByTrip(Long tripId, Long driverId) {
        return findByTripAndDriverIds(tripId, driverId);
    }

    public void rateTrip(Long tripId, Long driverId, Float rating) {
        final var tripToRate = findByTripAndDriverIds(tripId, driverId);
        tripToRate.setRating(rating);

        tripRepository.save(tripToRate);

        driverService.processDriverRating(driverId);
    }

    public TripStatistics calculateStatistics(Long driverId, LocalDate localDate) {
        final var trips = tripRepository.findByAssignedTo_IdAndStartTripBetween(driverId, localDate.atStartOfDay(), endOfTheDay(localDate));

        return TripStatistics.builder()
                .totalTimePerDay(LocalTime.ofSecondOfDay(calculateTotalTimePerDay(trips)))
                .totalPricePerDay(BigDecimal.valueOf(calculateTotalPricePerDay(trips)))
                .avgPricePerDay(BigDecimal.valueOf(calculateAveragePrice(trips)))
                .build();
    }

    public BigDecimal calculateAverageTripPrice(Long driverId) {
        final var trips = findByDriverId(driverId);
        return BigDecimal.valueOf(calculateAveragePrice(trips));
    }

    private static long calculateTotalTimePerDay(List<Trip> trips) {
        return trips.stream()
                .mapToLong(trip -> {
                    final var difference = Duration.between(trip.getStartTrip(), trip.getEndTrip());
                    return difference.get(SECONDS);
                })
                .sum();
    }

    private double calculateTotalPricePerDay(List<Trip> trips) {
        return trips.stream()
                .mapToDouble(t -> t.getPrice().doubleValue())
                .sum();
    }

    private static double calculateAveragePrice(List<Trip> trips) {
        return trips.stream()
                .mapToDouble(t -> t.getPrice().doubleValue())
                .average()
                .orElse(Double.NaN);
    }

    private Trip findByTripAndDriverIds(Long tripId, Long driverId) {
        return tripRepository.findByIdAndAssignedTo_Id(tripId, driverId);
    }

    private List<Trip> findByDriverId(Long driverId) {
        return tripRepository.findByAssignedTo_Id(driverId);
    }

    private static LocalDateTime endOfTheDay(LocalDate localDate) {
        return localDate.atStartOfDay().plus(1, DAYS).minus(1, MILLIS);
    }
}

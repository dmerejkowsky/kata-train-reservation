package fr.arolla.trainreservation.ticket_office.domain;

import java.util.List;

public class SeatFinder {
  public static List<String> findAvailableSeats(Train train, String bookingReference, int numberOfSeats) {
    return List.of("1A");
  }
}

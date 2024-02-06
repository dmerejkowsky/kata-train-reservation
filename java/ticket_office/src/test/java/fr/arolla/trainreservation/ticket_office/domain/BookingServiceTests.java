package fr.arolla.trainreservation.ticket_office.domain;

import org.junit.jupiter.api.Test;

public class BookingServiceTests {

  @Test
  void booking_one_seat_from_empty_train() {
    BookingService bookingService = new BookingService();
    String trainId = "express_2000";
    int numberOfSeats = 1;
    bookingService.reserveSeats(trainId, numberOfSeats);
  }

  @Test
  void booking_one_seat_from_empty_train() {
    BookingService bookingService = new BookingService();
    String trainId = "express_2000";
    int numberOfSeats = 1;
    bookingService.reserveSeats(trainId, numberOfSeats);
  }

}

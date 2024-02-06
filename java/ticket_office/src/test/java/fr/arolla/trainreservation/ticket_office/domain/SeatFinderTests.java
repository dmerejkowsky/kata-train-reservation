package fr.arolla.trainreservation.ticket_office.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SeatFinderTests {

  @Test
  void find_available_seat_from_empty_train() {
    Train train = new Train("express_2000");
    String bookingReference = "1234";
    int numberOfSeats = 1;

    List<String> seatIds = SeatFinder.findAvailableSeats(train, bookingReference, numberOfSeats);

    assertThat(seatIds.size()).isEqualTo(1);
  }
}

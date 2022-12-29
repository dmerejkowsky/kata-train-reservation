package fr.arolla.trainreservation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TrainTests {
  @Test
  public void can_make_empty_train() {
    var train = Helpers.makeEmptyTrain();

  }

  @Test
  public void can_book_a_seat() {
    var train = new Train();
    var seat = Seat.empty("1", "A");
    train.add(seat);
    train.book("1A", "abc123");

    var returnedSeat = train.getSeat("1A");
    assert (returnedSeat.isBooked());
    assertEquals("abc123", returnedSeat.bookingReference());
  }

  @Test
  public void cannot_book_the_same_seat_twice_with_different_booking_references() {
    var train = new Train();
    var seat = Seat.booked("1", "A", "old-reference");
    train.add(seat);

    assertThrows(AlreadyBookedException.class,
      () -> train.book("1A", "new-reference"));
  }

}
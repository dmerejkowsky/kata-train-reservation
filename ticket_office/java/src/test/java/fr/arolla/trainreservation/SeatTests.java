package fr.arolla.trainreservation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SeatTests {
  @Test
  public void empty_seats_can_be_booked() {
    var seat = Seat.empty("1", "A");
    assertTrue(seat.isFree());
    assertFalse(seat.isBooked());
    assertNull(seat.bookingReference());

    seat.book("abcd123");

    assertFalse(seat.isFree());
    assertTrue(seat.isBooked());
    assertEquals("abcd123", seat.bookingReference());
  }

  @Test
  public void seats_cannot_be_booked_twice() {
    var seat = Seat.booked("1", "A", "old-reference");
    assertThrows(AlreadyBookedException.class, () ->
      seat.book("new-reference")
    );
  }

}
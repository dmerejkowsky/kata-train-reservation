package fr.arolla.trainreservation;

import org.junit.jupiter.api.Test;

class TrainTests {
  @Test
  public void can_book_a_seat() {
    var train = new Train();
    var seat = new Seat("1", "A", null);
    train.add(seat);
    train.book("1A");

    var returnedSeat = train.getSeat("1A");
    assert (returnedSeat.isBooked());
  }

}
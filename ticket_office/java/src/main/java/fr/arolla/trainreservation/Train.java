package fr.arolla.trainreservation;

import java.util.HashMap;
import java.util.Map;

public class Train {
  private final Map<String, Seat> seats;

  public Train() {
    seats = new HashMap<>();
  }

  public void book(String seatId) {
  }

  public Seat getSeat(String seatId) {
    return seats.get(seatId);
  }

  public void add(Seat seat) {
    seats.put(seat.id(), seat);
  }
}

package fr.arolla.trainreservation;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Train {
  private final Map<String, Seat> seats;

  public Train() {
    seats = new HashMap<>();
  }

  public Seat getSeat(String seatId) {
    return seats.get(seatId);
  }

  public List<Seat> getSeats() {
    return seats.values().stream().sorted(Comparator.comparing((Seat::id))).toList();
  }

  public void add(Seat seat) {
    seats.put(seat.id(), seat);
  }

  public void book(String seatId) {
  }
}

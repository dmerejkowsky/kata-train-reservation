package fr.arolla.trainreservation.train_data;

public class NoSuchSeat extends RuntimeException {
  public NoSuchSeat(String seatId) {
    super(String.format("No seat found for id: %s", seatId));
  }
}

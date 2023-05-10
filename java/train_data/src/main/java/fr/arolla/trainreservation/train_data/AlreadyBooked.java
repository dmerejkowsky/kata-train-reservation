package fr.arolla.trainreservation.train_data;

public class AlreadyBooked extends RuntimeException {
  public AlreadyBooked(String seatId, String bookingReference, String oldBookingReference) {
    super(String.format("Cannot book seat %s with %s - already booked with %s", seatId, bookingReference, oldBookingReference));
  }
}

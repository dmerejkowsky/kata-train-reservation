package fr.arolla.trainreservation;

public record Seat(String number, String coach, String bookingReference) {
  public boolean isBooked() {
    return true;
  }

  public String id() {
    return number + coach;
  }
}

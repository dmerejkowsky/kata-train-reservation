package fr.arolla.trainreservation;

import java.util.Objects;

public final class Seat {
  private final String number;
  private final String coach;
  private String bookingReference;

  private Seat(String number, String coach, String bookingReference) {
    this.number = number;
    this.coach = coach;
    this.bookingReference = bookingReference;
  }

  public static Seat empty(String number, String coach) {
    return new Seat(number, coach, null);
  }

  public static Seat booked(String number, String coach, String bookingReference) {
    return new Seat(number, coach, bookingReference);
  }

  public boolean isBooked() {
    return this.bookingReference != null;
  }

  public boolean isFree() {
    return !isBooked();
  }

  public String id() {
    return number + coach;
  }

  public void book(String bookingReference) {
    if (this.bookingReference != null && !this.bookingReference.equals(bookingReference)) {
      throw new AlreadyBookedException(id(), this.bookingReference, bookingReference);
    }
    this.bookingReference = bookingReference;
  }

  public String number() {
    return number;
  }

  public String coach() {
    return coach;
  }

  public String bookingReference() {
    return bookingReference;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (Seat) obj;
    return Objects.equals(this.number, that.number) &&
      Objects.equals(this.coach, that.coach) &&
      Objects.equals(this.bookingReference, that.bookingReference);
  }

  @Override
  public int hashCode() {
    return Objects.hash(number, coach, bookingReference);
  }

  @Override
  public String toString() {
    return "Seat[" +
      "number=" + number + ", " +
      "coach=" + coach + ", " +
      "bookingReference=" + bookingReference + ']';
  }
}

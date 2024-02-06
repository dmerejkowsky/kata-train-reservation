package fr.arolla.trainreservation.ticket_office.domain;

public class FakeBookingReferenceClient implements BookingReferenceClient {

  private String bookingReference;

  @Override
  public String getBookingReference() {
    return bookingReference;
  }

  public void setBookingReference(String bookingReference) {
    this.bookingReference = bookingReference;
  }
}

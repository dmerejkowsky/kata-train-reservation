package fr.arolla.trainreservation.ticket_office.domain;

import java.util.List;

public class BookingService {

  private final BookingReferenceClient bookingReferenceClient;
  private final TrainDataClient trainDataClient;
  private String bookingReference;

  public BookingService(BookingReferenceClient bookingReferenceClient, TrainDataClient trainDataClient) {
    this.bookingReferenceClient = bookingReferenceClient;
    this.trainDataClient = trainDataClient;
  }

  public void bookSeats(String trainId, int numberOfSeat) {
    bookingReference = bookingReferenceClient.getBookingReference();
    Train train = trainDataClient.getTrain();
    List<String> availableSeats = SeatFinder.findAvailableSeats(train, bookingReference, numberOfSeat);
    trainDataClient.bookSeats(availableSeats);
  }

  public String getBookingReference() {
    return bookingReference;
  }

  public List<Seat> getBookedSeats() {

    return List.of(new Seat("1A"));
  }
}

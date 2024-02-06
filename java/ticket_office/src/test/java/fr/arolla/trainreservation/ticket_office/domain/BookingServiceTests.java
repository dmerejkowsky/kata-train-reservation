package fr.arolla.trainreservation.ticket_office.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BookingServiceTests {

  private FakeBookingReferenceClient fakeBookingReferenceClient;
  private BookingService bookingService;
  private FakeTrainDataClient fakeTrainDataClient;
  private Train train;

  @BeforeEach
  void setup() {
    fakeBookingReferenceClient = new FakeBookingReferenceClient();
    fakeTrainDataClient = new FakeTrainDataClient();
    bookingService = new BookingService(fakeBookingReferenceClient, fakeTrainDataClient);
    train = new Train("express_2000");
    fakeTrainDataClient.setTrain(train);
  }

  @Test
  void get_booking_reference_from_client() {
    fakeBookingReferenceClient.setBookingReference("1234");

    bookingService.bookSeats("express_2000", 1);

    assertThat(bookingService.getBookingReference()).isEqualTo("1234");
  }

  @Test
  void booking_one_seat_from_empty_train() {
    fakeBookingReferenceClient.setBookingReference("1234");

    bookingService.bookSeats(train.getTrainId(), 1);

    assertThat(bookingService.getBookedSeats().size()).isEqualTo(1);
  }
}

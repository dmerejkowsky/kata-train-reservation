package fr.arolla.trainreservation.controllers;

import fr.arolla.trainreservation.FakeClient;
import fr.arolla.trainreservation.Train;
import org.junit.jupiter.api.Test;

class BookingControllerTests {

  @Test
  public void reserve_four_seats_from_empty_train() {
    var train = new Train();
    var fakeClient = new FakeClient(train);
    var controller = new BookingController(fakeClient);
    var bookingRequest = new BookingRequest("express_2000", 4);

    var response = controller.reserve(bookingRequest);

    System.out.println(response.seats());
  }
}
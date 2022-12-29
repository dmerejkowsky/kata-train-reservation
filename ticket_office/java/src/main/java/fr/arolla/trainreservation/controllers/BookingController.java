package fr.arolla.trainreservation.controllers;

import fr.arolla.trainreservation.ServiceClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class BookingController {

  private final ServiceClient serviceClient;

  BookingController(ServiceClient serviceClient) {
    this.serviceClient = serviceClient;
  }


  @RequestMapping("/reserve")
  BookingResponse reserve(@RequestBody BookingRequest bookingRequest) {
    var seatCount = bookingRequest.seat_count();
    var trainId = bookingRequest.train_id();

    // Step 1: Get a booking refenrence
    var bookingReference = serviceClient.getBookingReference();

    // Step 2: Retrieve train data for the given train ID
    var train = serviceClient.getTrain(trainId);
    var seats = train.getSeats();

    // Step 3: find available seats (hard-code coach 'A' for now)
    var inFirstCoach = seats.stream().filter(seat -> seat.coach().equals("A"));
    var availableSeats = inFirstCoach.filter(seat -> seat.bookingReference() == null);

    // Step 4: call the '/reserve' end point
    var toReserve = availableSeats.limit(seatCount);
    var ids = toReserve.map(seat -> seat.number() + seat.coach()).toList();
    serviceClient.makeReservation(trainId, bookingReference, ids);

    // Step 5: return reference and booked seats
    return new BookingResponse(bookingReference, ids);
  }
}

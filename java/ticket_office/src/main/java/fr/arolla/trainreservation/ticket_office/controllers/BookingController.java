package fr.arolla.trainreservation.ticket_office.controllers;

import fr.arolla.trainreservation.ticket_office.Seat;
import fr.arolla.trainreservation.ticket_office.repository.SeatsRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BookingController{
  private final SeatsRepository seatsRepository;

  public BookingController(SeatsRepository seatsRepository) {
    this.seatsRepository = seatsRepository;
  }

  @RequestMapping("/reserve")
  public BookingResponse reserve(@RequestBody BookingRequest bookingRequest) {
    var seatCount = bookingRequest.count();
    var trainId = bookingRequest.train_id();

    // Step 1: Get a booking reference
    String bookingReference = seatsRepository.getRef();

    // Step 2: Retrieve train data for the given train ID
    ArrayList<Seat> seats = seatsRepository.getSeats(trainId);

    // Step 4: call the '/reserve' end point
    List<String> ids = seatsRepository.reserveSeat(seats, seatCount, trainId, bookingReference);

    // Step 5: return reference and booked seats
    return new BookingResponse(trainId, bookingReference, ids);
  }
}

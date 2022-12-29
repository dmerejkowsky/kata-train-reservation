package fr.arolla.trainreservation.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.arolla.trainreservation.Seat;
import fr.arolla.trainreservation.ServiceClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;


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
    var json = serviceClient.getTrainData(trainId);
    ObjectMapper objectMapper = new ObjectMapper();
    ArrayList<Seat> seats = new ArrayList<>();
    try {
      var tree = objectMapper.readTree(json);
      var seatsNode = tree.get("seats");
      for (JsonNode node : seatsNode) {
        String coach = node.get("coach").asText();
        String seatNumber = node.get("seat_number").asText();
        var jsonBookingReference = node.get("booking_reference").asText();
        if (jsonBookingReference.isEmpty()) {
          var seat = new Seat(seatNumber, coach, null);
          seats.add(seat);
        } else {
          var seat = new Seat(seatNumber, coach, jsonBookingReference);
          seats.add(seat);
        }
      }
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

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
